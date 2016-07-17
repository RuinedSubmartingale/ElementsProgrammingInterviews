package epi.solutions;

import epi.solutions.helper.CloneableInputsMap;
import epi.solutions.helper.TimeTests;

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
      Callable<CloneableInputsMap> formInput = () -> {
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
      BiFunction<CloneableInputsMap, Integer, Boolean> checkResults = (orig_input, observed) ->
              orig_input.getInteger("a") <= observed && observed <= orig_input.getInteger("b");
      Supplier<Integer> emptyOutput = () -> 0;
      TimeTests<Integer> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "UnifRandomNumberGenerator");
      algTimer.timeAndCheck(NUM_TESTS, checkResults);
    }

  }
}
