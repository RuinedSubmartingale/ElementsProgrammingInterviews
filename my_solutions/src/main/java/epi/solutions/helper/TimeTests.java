package epi.solutions.helper;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class TimeTests<outputType> implements TimeTestsInterface {
  private Callable<CloneableTestInputsMap> _formInput;
  private Function<CloneableTestInputsMap, outputType> _runAlgorithm;
  private Function<CloneableTestInputsMap, outputType> _getKnownOutput;
  private BiFunction<outputType, outputType, Boolean> _checkResults;
  private int _numTests;
  private String _testDescription;


  public TimeTests(Callable<CloneableTestInputsMap> formInput
                  , Function<CloneableTestInputsMap, outputType> runAlgorithm
                  , Function<CloneableTestInputsMap, outputType> getKnownOutput
                  , BiFunction<outputType, outputType, Boolean> checkResults
                  , final int NUM_TESTS, String testName) {
    _formInput = formInput;
    _runAlgorithm = runAlgorithm;
    _getKnownOutput = getKnownOutput;
    _checkResults = checkResults;
    _numTests = NUM_TESTS;
    _testDescription = testName;
  }

  public void testAndCheck() {
    try {
      DecimalFormat df = new DecimalFormat("#.####");
      long total = 0, start;
      for (int i=0; i < _numTests; ++i) {
        CloneableTestInputsMap input = _formInput.call();
        CloneableTestInputsMap orig_input = new CloneableTestInputsMap();
        input.forEach((name, inputType) -> orig_input.put(name, inputType.cloneInput()));
        start = System.nanoTime();
        outputType algorithmResult = _runAlgorithm.apply(input);
        total += System.nanoTime() - start;
        outputType expectedResult = _getKnownOutput.apply(orig_input);
        assert(_checkResults.apply(algorithmResult, expectedResult));
      }
      System.out.println(String.format("%s %50s %s", "DEBUG: ", _testDescription, " took "
              + df.format(total * 1.0 / _numTests)
              + " nanoseconds on average for " + _numTests + " tests"));
    } catch (Exception|AssertionError e) {
      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
      e.printStackTrace();
    }
  }

  // TODO: Abstract out algorithm checking code. use intfc that takes in all availble data inputs, and chooses which method to use to check results.
  public void testAndCheck(BiFunction<CloneableTestInputsMap, outputType, Boolean> checkResults) {
    try {
      DecimalFormat df = new DecimalFormat("#.####");
      long total = 0, start;
      for (int i=0; i < _numTests; ++i) {
        CloneableTestInputsMap input = _formInput.call();
        CloneableTestInputsMap orig_input = new CloneableTestInputsMap();
        input.forEach((name, inputType) -> orig_input.put(name, inputType.cloneInput()));
        start = System.nanoTime();
        outputType algorithmResult = _runAlgorithm.apply(input);
        total += System.nanoTime() - start;
        Checker1<outputType> resultChecker = new Checker1<>(checkResults, orig_input, algorithmResult);
        assert(resultChecker.check());
      }
      System.out.println(String.format("%s %50s %s", "DEBUG: ", _testDescription, " took "
              + df.format(total * 1.0 / _numTests)
              + " nanoseconds on average for " + _numTests + " tests"));
    } catch (Exception|AssertionError e) {
      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
      e.printStackTrace();
    }
  }

  public void test() {
//    try {
//      DecimalFormat df = new DecimalFormat("#.####");
//      long total = 0, start;
//      for (int i=0; i < _numTests; ++i) {
//        CloneableTestInputsMap input = _formInput.call();
//        CloneableTestInputsMap orig_input = (CloneableTestInputsMap) input.cloneInput();
//        start = System.nanoTime();
//        outputType algorithmResult = _runAlgorithm.apply(input);
//        total += System.nanoTime() - start;
//        assert(_checkResults.apply(algorithmResult, algorithmResult));
//      }
//      System.out.println(String.format("%s %50s %s", "DEBUG: ", _testDescription, " took "
//              + df.format(total * 1.0 / _numTests)
//              + " nanoseconds on average for " + _numTests + " tests"));
//    } catch (Exception|AssertionError e) {
//      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
//      e.printStackTrace();
//    }
  }

  public void main(String[] args) {

  }
}

