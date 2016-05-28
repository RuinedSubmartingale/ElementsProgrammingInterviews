package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/13/16.
 * Problem 5.7
 */
public class PowerXY {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static double power(double x, int y) {
    double result = 1.0;
    long power = y;
    if (y < 0) {
      power = -power;
      x = 1.0 / x;
    }
    while(power != 0) {
      if ((power & 1) != 0)
        result *= x;
      x *= x;
      power >>>= 1;
    }
    return result;
  }

  public static void main(String[] args) throws Exception {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      inputs.addDouble("x", rgen.nextDouble() * 10);
      inputs.addInteger("y", rgen.nextInt(257) - 128);
      return inputs;
    };
    Function<CloneableTestInputsMap, Double> runAlgorithm = (inputs) ->
            power(inputs.getDouble("x"), inputs.getInteger("y"));
    Function<CloneableTestInputsMap, Double> getKnownOutput = (inputs) ->
            Math.pow(inputs.getDouble("x"), inputs.getInteger("y"));
    BiFunction<Double, Double, Boolean> checkResults = (observed, expected) -> {
      final Double diff = (observed - expected) / expected;
      return ((diff < -1.0E-9) ? 1 : (diff > 1.0e-9) ? 1 : 0) == 0;
    };
    Supplier<Double> emptyOutput = () -> 0.0;

    TimeTests<Double> algTimer =
            new TimeTests<>(formInput, runAlgorithm, emptyOutput, "PowerXY");
    algTimer.timeAndCheck(NUM_TESTS, checkResults, getKnownOutput);
  }

}
