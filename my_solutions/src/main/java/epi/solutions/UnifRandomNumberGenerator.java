package epi.solutions;

import epi.solutions.helper.TimeTests;

import java.util.Random;

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
  public static void main(String[] args) {
    if (args.length == 2) {
      int a, b;
      a = Integer.parseInt(args[0]);
      b = Integer.parseInt(args[1]);
    } else {
      TimeTests.test(UnifRandomNumberGenerator::runTests, NUM_TESTS, "UniformRandomNumberGenerator");
    }

  }

  private static void runTests(final int NUM_TESTS) {
    int a, b;
    Random rgen = new Random();
    for (int times = 0; times < NUM_TESTS; ++times) {
      a = rgen.nextInt(100);
      b = rgen.nextInt(100) + a + 1;
      int x = UnifRandom(a,b);
      assert (x >= a && x <= b);
    }
  }
}
