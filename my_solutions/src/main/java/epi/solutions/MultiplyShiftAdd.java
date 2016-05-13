package epi.solutions;

//import epi.solutions.helper.TimeTests;

import epi.solutions.helper.TimeTests;
import java.util.Random;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.5
 */
public class MultiplyShiftAdd {
  private static long multiply(long x, long y) {
    long sum = 0;
    while (x != 0) {
      // Examine each bit of x
      if ((x & 1) != 0)
        sum = add(sum, y);
      x >>>= 1;
      y <<= 1;
    }
    return sum;
  }

  private static long add(long a, long b) {
    long sum = 0, carryin = 0, carryout = 0, k=1, tempA = a, tempB = b;
    while (tempA != 0 || tempB != 0) {
      long ak = a & k, bk = b & k;
      carryout = ((ak & bk) | (ak & carryin) | (bk & carryin)) << 1;
      sum |= (ak ^ bk ^ carryin); // ^
      carryin = carryout;
      k <<= 1;
      tempA >>>= 1;
      tempB >>>= 1;
    }
    return sum | carryin;
  }

  public static void main(String[] args) {
    if (args.length == 2) {
      int x = Integer.parseInt(args[0]), y = Integer.parseInt(args[1]);
      long res = multiply(x,y);
      assert(res == (long)x*y);
    } else {
      final int NUM_TESTS = (int) Math.pow(10, 6);
      TimeTests.test(MultiplyShiftAdd::runTests, NUM_TESTS, "MultiplyShiftAdd");
    }
  }

  public static void runTests(final int NUM_TESTS) {
    Random rgen = new Random();
    int x, y;
    final int MAX_XY = (1 << 16) - 1;
    // MAX_XY needed because random test only works if product
    // is not greater than 2^32 - 1
    for (int i = 0; i < NUM_TESTS; ++i) {
      x = rgen.nextInt(MAX_XY);
      y = rgen.nextInt(MAX_XY);
      long prod = (long) multiply(x,y);
      assert(prod == (long)x*y);
      // System.out.println("PASS: x = " + x + ", y = " + y + "; prod = " + prod);
    }
  }
}
