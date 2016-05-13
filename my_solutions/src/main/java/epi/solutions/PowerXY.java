package epi.solutions;

import epi.solutions.helper.TimeTests;

import java.util.Random;
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
    TimeTests.test(PowerXY::runTests, NUM_TESTS, "PowerXY");
  }

  private static void runTests(final int NUM_TESTS) {
    Random rgen = new Random();
    double x; int y;
    for (int times = 0; times < NUM_TESTS; ++times) {
      x = rgen.nextDouble() * 10;
      y = rgen.nextInt(257) - 128;
      final Double answer = Math.pow(x,y);
      Predicate<Double> approxCorrect = (Double result) -> {
        final Double diff = (result - answer) / answer;
        return ((diff < -1.0E-9) ? 1 : (diff > 1.0e-9) ? 1 : 0) == 0;
      };
      assert(approxCorrect.test(power(x,y)));
    }
  }
}
