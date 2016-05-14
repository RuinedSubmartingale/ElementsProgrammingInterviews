package epi.solutions.helper;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by psingh on 5/12/16.
 */

public class TimeTests<outputType> {
  public void test(Callable<CloneableTestInput> formInput
          , Function<CloneableTestInput, outputType> runAlgorithm
          , Function<CloneableTestInput, outputType> getKnownOutput
          , BiFunction<outputType, outputType, Boolean> checkResults, final int NUM_TESTS, String testName) {
    try {
      DecimalFormat df = new DecimalFormat("#.####");
      long total = 0, start;
      for (int i=0; i < NUM_TESTS; ++i) {
        CloneableTestInput input = formInput.call();

        CloneableTestInput orig_input = input.clone();
        start = System.nanoTime();
        outputType algorithmResult = runAlgorithm.apply(input);
        total += System.nanoTime() - start;
        outputType expectedResult = getKnownOutput.apply(orig_input);
        assert(checkResults.apply(algorithmResult, expectedResult));
      }
      System.out.println("DEBUG: " + testName + " took "
              + df.format(total * 1.0 / NUM_TESTS)
              + " nanoseconds on average for " + NUM_TESTS + " tests");
    } catch (Exception|AssertionError e) {
      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
      e.printStackTrace();
    }

  }

  public void main(String[] args) {

  }
}
