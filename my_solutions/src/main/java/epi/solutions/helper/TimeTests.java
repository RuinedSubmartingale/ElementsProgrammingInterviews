package epi.solutions.helper;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TimeTests<outputType> {
  private Callable<CloneableTestInputsMap> _formInput;
  private Function<CloneableTestInputsMap, outputType> _runAlgorithm;
  private Function<CloneableTestInputsMap, outputType> _getKnownOutput;
  private int _numTests;
  private String _algDescription;
  private Function<AlgCompleteData<outputType>, Boolean> _algChecker;
  private Function<CloneableTestInputsMap, HashMap<String, Object>> _saveExtraAlgResults;

  public TimeTests(Callable<CloneableTestInputsMap> formInput
                  , Function<CloneableTestInputsMap, outputType> runAlgorithm
                  , Supplier<outputType> emptyOutput
                  , String algName) {
    _formInput = formInput;
    _runAlgorithm = runAlgorithm;
    _algDescription = algName;

    _getKnownOutput = (inputs) -> emptyOutput.get();
    _saveExtraAlgResults = (inputs) -> new HashMap<String, Object>();
    // TODO: prob not a good practice to use Object as value type in HashMap here and use unchecked type casting below. Try to figure out a better approach...
  }

  // See https://stackoverflow.com/questions/1998544/method-has-the-same-erasure-as-another-method-in-type
  // for why classes aren't allowed to have methods that are override-equivalent,
  // i.e. methods that have the same parameter types after erasure
  // It took a bit of brainstorming to overload testAndCheck method in a valid way as follows
  public void testAndCheck(final int numTests, BiFunction<outputType, outputType, Boolean> checkResults
          , Function<CloneableTestInputsMap, outputType> getKnownOutput) {
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

  public void testAndCheck(final int numTests, TriFunction<outputType, outputType, HashMap, Boolean> checkResults
          , Function<CloneableTestInputsMap, outputType> getKnownOutput
          , Function<CloneableTestInputsMap, HashMap<String, Object>> saveExtraAlgResults) {
    _numTests = numTests;
    _getKnownOutput = getKnownOutput;
    _algChecker = algCompleteData ->
            checkResults.apply(algCompleteData._observedResults, algCompleteData._expectedResults, algCompleteData._algExtraResults);
    _numTests = numTests;
    _saveExtraAlgResults = saveExtraAlgResults;
    testAndCheck();
  }

  public void testAndCheck(final int numTests, BiFunction<CloneableTestInputsMap, outputType, Boolean> checkResults) {
    _numTests = numTests;
    _algChecker = algCompleteData ->
            checkResults.apply(algCompleteData._orig_inputs, algCompleteData._observedResults);
    testAndCheck();
  }

  public void test(final int numTests) {
    _numTests = numTests;
    _algChecker = (completeData) -> true;
    testAndCheck();
  }

  private void testAndCheck() {
    DecimalFormat df = new DecimalFormat("#.####");
    long total = 0, start;
    try {
      for (int i=0; i < _numTests; ++i) {
        CloneableTestInputsMap input = _formInput.call();
        CloneableTestInputsMap orig_input = new CloneableTestInputsMap();
        input.forEach((name, inputType) -> orig_input.put(name, inputType.cloneInput()));
        start = System.nanoTime();
        outputType algorithmResult = _runAlgorithm.apply(input);
        total += System.nanoTime() - start;
        HashMap<String, Object> algExtraResults = _saveExtraAlgResults.apply(input);
        assert(check(orig_input, algorithmResult, algExtraResults));
      }
      System.out.println(String.format("%s %50s %s", "DEBUG: ", _algDescription, " took "
              + df.format(total * 1.0 / _numTests)
              + " nanoseconds on average for " + _numTests * 1.0 / Math.pow(10, 6) + " million tests"));
    } catch (Exception|AssertionError e) {
      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
      e.printStackTrace();
    }
  }

  private boolean check(CloneableTestInputsMap orig_input, outputType algorithmResult, HashMap<String, Object> algExtraResults) {
    outputType expectedResult = _getKnownOutput.apply(orig_input);
    AlgCompleteData<outputType> completeData = new AlgCompleteData<>(orig_input, expectedResult, algorithmResult, algExtraResults);
    return _algChecker.apply(completeData);
  }

  public void main(String[] args) {

  }
}

