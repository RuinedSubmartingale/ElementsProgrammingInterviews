package epi.solutions;

import epi.solutions.helper.*;

import java.util.Random;
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
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      inputs.addDouble("x", rgen.nextDouble() * 10);
      inputs.addInteger("y", rgen.nextInt(257) - 128);
      return inputs;
    };
    Function<CloneableInputsMap, Double> runAlgorithm = (inputs) ->
            power(inputs.getDouble("x"), inputs.getInteger("y"));
    Function<CloneableInputsMap, Double> getKnownOutput = (inputs) ->
            Math.pow(inputs.getDouble("x"), inputs.getInteger("y"));
    BiFunction<Double, Double, Boolean> checkResults = (observed, expected) -> {
      final Double diff = (observed - expected) / expected;
      return ((diff < -1.0E-9) ? 1 : (diff > 1.0e-9) ? 1 : 0) == 0;
    };

    AlgVerifierInterfaces< Double, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(checkResults);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("PowerXY", NUM_TESTS, formInputs, runAlgorithm, getKnownOutput, algVerifier);
//    algorithmFactory.setSequential();
    algorithmFactory.run();
  }

}
