package epi.solutions.helper;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class TimeTests<outputType> {
  private Callable<CloneableTestInputsMap> _formInput;
  private Function<CloneableTestInputsMap, outputType> _runAlgorithm;
  private Function<CloneableTestInputsMap, outputType> _getKnownOutput;
  private int _numTests;
  private String _algDescription;
  private Function<AlgCompleteData<outputType>, Boolean> _algChecker;
  private Function<CloneableTestInputsMap, CloneableTestInputsMap> _saveExtraAlgResults;

  public TimeTests(Callable<CloneableTestInputsMap> formInput
                  , Function<CloneableTestInputsMap, outputType> runAlgorithm
                  , Supplier<outputType> emptyOutput
                  , String algName) {
    _formInput = formInput;
    _runAlgorithm = runAlgorithm;
    _algDescription = algName;

    _getKnownOutput = (inputs) -> emptyOutput.get();
    _saveExtraAlgResults = (inputs) -> new CloneableTestInputsMap();
    // TODO: prob not a good practice to use Object as value type in HashMap here and use unchecked type casting below. Try to figure out a better approach...
  }

  // See https://stackoverflow.com/questions/1998544/method-has-the-same-erasure-as-another-method-in-type
  // for why classes aren't allowed to have methods that are override-equivalent,
  // i.e. methods that have the same parameter types after erasure
  // It took a bit of brainstorming to overload testAndCheck method in a valid way as follows
  public void testAndCheck(final int numTests, BiFunction<outputType, outputType, Boolean> checkResults
          , Function<CloneableTestInputsMap, outputType> getKnownOutput) throws Exception {
    _numTests = numTests;
    _getKnownOutput = getKnownOutput;
    _algChecker = algCompleteData ->
            checkResults.apply(algCompleteData._observedResults, algCompleteData._expectedResults);
    _numTests = numTests;
    testAndCheck();
  }

  // TODO: Clearer variable names and better documentation/explanation of use cases for TriFunction.
  @FunctionalInterface
  public interface TriFunction<A, B, C, D> {
    D apply(A a, B b, C c);
  }

  public void testAndCheck(final int numTests, TriFunction<outputType, outputType, CloneableTestInputsMap, Boolean> checkResults
          , Function<CloneableTestInputsMap, outputType> getKnownOutput
          , Function<CloneableTestInputsMap, CloneableTestInputsMap> saveExtraAlgResults) throws Exception {
    _numTests = numTests;
    _getKnownOutput = getKnownOutput;
    _algChecker = algCompleteData ->
            checkResults.apply(algCompleteData._observedResults, algCompleteData._expectedResults, algCompleteData._algExtraResults);
    _numTests = numTests;
    _saveExtraAlgResults = saveExtraAlgResults;
    testAndCheck();
  }

  public void testAndCheck(final int numTests, BiFunction<CloneableTestInputsMap, outputType, Boolean> checkResults) throws Exception {
    _numTests = numTests;
    _algChecker = algCompleteData ->
            checkResults.apply(algCompleteData._orig_inputs, algCompleteData._observedResults);
    testAndCheck();
  }

  public void test(final int numTests) throws Exception {
    _numTests = numTests;
    _algChecker = (completeData) -> true;
    testAndCheck();
  }

  private void testAndCheck() throws Exception {
    DecimalFormat df = new DecimalFormat("#.####");
    Runtime javaApp = Runtime.getRuntime();
    int nProcs = Math.max(javaApp.availableProcessors() - 1, 1);
    ExecutorService execService = Executors.newFixedThreadPool(nProcs);
    int numTestPerThread = _numTests / nProcs;
    Callable<Long> task  = () -> {
      long total = 0, start;
      for (int i = 0; i < numTestPerThread; ++i) {
        CloneableTestInputsMap input = _formInput.call();
        CloneableTestInputsMap orig_input = new CloneableTestInputsMap();
        input.forEach((name, inputType) -> orig_input.put(name, inputType.cloneInput()));
        start = System.nanoTime();
        outputType algorithmResult = _runAlgorithm.apply(input);
        total += System.nanoTime() - start;
        CloneableTestInputsMap algExtraResults = _saveExtraAlgResults.apply(input);
        assert (check(orig_input, algorithmResult, algExtraResults));
      }
      return total;
    };
    List<Callable<Long>> tasks = Collections.nCopies(nProcs, task);

    List<Future<Long>> futures = execService.invokeAll(tasks);
    Long totalExecTime = futures
            .stream()
            .map((future) -> {
              try {
                return future.get();
              } catch (Exception e){
                throw new IllegalStateException(e);
              }
            })
            .reduce((a,b) -> a + b).orElse(0L);
    System.out.println(String.format("%s %50s %s", "DEBUG: ", _algDescription, " took "
            + df.format(totalExecTime * 1.0 / _numTests)
            + " nanoseconds on average for " + _numTests * 1.0 / Math.pow(10, 6) + " million tests"));

  }

  private boolean check(CloneableTestInputsMap orig_input, outputType algorithmResult, CloneableTestInputsMap algExtraResults) {
    outputType expectedResult = _getKnownOutput.apply(orig_input);
    AlgCompleteData<outputType> completeData = new AlgCompleteData<>(orig_input, expectedResult, algorithmResult, algExtraResults);
    return _algChecker.apply(completeData);
  }
}
