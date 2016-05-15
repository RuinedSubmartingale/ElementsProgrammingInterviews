package epi.solutions;

import epi.solutions.helper.CloneableDouble;
import epi.solutions.helper.CloneableInteger;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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

  public static void main(String[] args) {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      inputs.put("x", new CloneableDouble(rgen.nextDouble() * 10));
      inputs.put("y", new CloneableInteger(rgen.nextInt(257) - 128));
      return inputs;
    };
    Function<CloneableTestInputsMap, Double> runAlgorithm = (inputs) ->
            power(((CloneableDouble) inputs.get("x")).data
                    , ((CloneableInteger) inputs.get("y")).data);
    Function<CloneableTestInputsMap, Double> getKnownOutput = (inputs) ->
            Math.pow(((CloneableDouble) inputs.get("x")).data
                    , ((CloneableInteger) inputs.get("y")).data);
    BiFunction<Double, Double, Boolean> checkResults = (observed, expected) -> {
      final Double diff = (observed - expected) / expected;
      return ((diff < -1.0E-9) ? 1 : (diff > 1.0e-9) ? 1 : 0) == 0;
    };

    TimeTests<Double> algTimer =
            new TimeTests<>(formInput, runAlgorithm, getKnownOutput
                            , checkResults, NUM_TESTS, "PowerXY");
    algTimer.testAndCheck();
  }

}
