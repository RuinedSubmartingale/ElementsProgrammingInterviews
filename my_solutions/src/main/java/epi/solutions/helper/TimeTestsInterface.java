package epi.solutions.helper;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by psingh on 5/13/16.
 */
@FunctionalInterface
public interface TimeTestsInterface<outputType> {
  void test(Callable<CloneableTestInput> formInput
          , Function<CloneableTestInput, outputType> runAlgorithm
          , Function<CloneableTestInput, outputType> getKnownOutput
          , BiFunction<outputType, outputType, Boolean> checkResults
          , final int NUM_TESTS, String testName);
}
