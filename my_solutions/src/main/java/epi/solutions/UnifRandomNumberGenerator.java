package epi.solutions;

import epi.solutions.helper.*;

import java.awt.image.CropImageFilter;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.12 from EPI
 */
public class UnifRandomNumberGenerator {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static int zeroOneRandom() {
    Random rgen = new Random();
    return rgen.nextInt(2);
  }
  private static int UnifRandom(int lowerBound, int upperBound) {
    final int numOutcomes = upperBound - lowerBound + 1;
    int result;
    do {
      result = 0;
      for(int i=0; (1 << i) < numOutcomes; ++i)
        result = (result << 1) | zeroOneRandom();
    } while (result >= numOutcomes);
    return lowerBound + result;

  }
  public static void main(String[] args) throws Exception {
    if (args.length == 2) {
      int a, b;
      a = Integer.parseInt(args[0]);
      b = Integer.parseInt(args[1]);
      int x = UnifRandom(a, b);
      assert(a <= x && x <= b);
    } else {
      Supplier<CloneableInputsMap> formInputs = () -> {
        Random rgen = new Random();
        int a = rgen.nextInt(100);
        int b = rgen.nextInt(100) + a + 1;
        CloneableInputsMap inputs = new CloneableInputsMap();
        inputs.addInteger("a", a);
        inputs.addInteger("b", b);
        return inputs;
      };
      Function<CloneableInputsMap, Integer> runAlg = (inputs) ->
        UnifRandom(inputs.getInteger("a"), inputs.getInteger("b"));
      BiFunction<Integer, CloneableInputsMap, Boolean> checkResults = (observed, extra) ->
              extra.getInteger("a") <= observed && observed <= extra.getInteger("b");

      AlgVerifierInterfaces<Integer, CloneableInputsMap> algVerifier = new InputOutputVerifier<>(checkResults);
      AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("UnifRandomNumberGenerator", NUM_TESTS, formInputs, runAlg, algVerifier);
      algorithmFactory.run();
    }

  }
}
