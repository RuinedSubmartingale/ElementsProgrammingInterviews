package epi.solutions;

import epi.solutions.helper.TimeTests;

import java.text.DecimalFormat;
import java.util.*;

/*
* Problem 5.1 from EPI
*/
public abstract class Parity {

  private static final int NUM_TESTS = (int) Math.pow(10, 8);
  private static final int WORD_SIZE = 16;
  protected static short[] preComputedParity;
  protected static long[] testValues;
  protected short[] testResults;

  protected static short defaultCompute(long value) {
    short result = 0;
    while (value != 0) {
      result ^= (value & 1);
      value >>>= 1;
    }
    return result;
  }

  static {
    preComputedParity = new short[1 << WORD_SIZE];
    for (int i=0; i < (1 << WORD_SIZE); ++i) {
      preComputedParity[i] = defaultCompute(i);
    }

    testValues = new long[NUM_TESTS];
    Random r = new Random();
    for (int i = 0; i < NUM_TESTS; ++i) {
      testValues[i] = r.nextInt(Integer.MAX_VALUE);
    }
  }

  protected short[] compute() {
    testResults = new short[NUM_TESTS];
    for (int i = 0; i < NUM_TESTS; ++i) {
      testResults[i] = compute(testValues[i]);
    }
    return testResults;
  }

  abstract short compute(long value);

  private static class Parity1 extends Parity {
    @Override
    short compute(long x) {
      return defaultCompute(x);
    }
  }

  private static class Parity2 extends Parity {
    @Override
    short compute(long x) {
      short result = 0;
      while (x != 0) {
        x = x & (x-1);
        result ^= 1;
      }
      return result;
    }
  }

  private static class Parity3 extends Parity {
    private static final int BIT_MASK = 0xFFFF;

    @Override
    short compute(long x) {
      return (short) (
              preComputedParity[(int)  ((x >>> (3*WORD_SIZE)) & BIT_MASK)] ^
                      preComputedParity[(int)  ((x >>> (2*WORD_SIZE)) & BIT_MASK)] ^
                      preComputedParity[(int)  ((x >>> WORD_SIZE) & BIT_MASK)] ^
                      preComputedParity[(int)  (x  & BIT_MASK)]
      );
    }
  }

  private static class Parity4 extends Parity {
    private static final int FOUR_BIT_PARITY = 0b0110100110010110;

    @Override
    short compute(long x) {
      x ^= x >>> 32;
      x ^= x >>> 16;
      x ^= x >>> 8;
      x ^= x >>> 4;
      x &= 0xf;
      return (short)((FOUR_BIT_PARITY >> x) & 1);
    }
  }

  public static void main(String[] args) {
    Parity1 p1 = new Parity1();
    Parity2 p2 = new Parity2();
    Parity3 p3 = new Parity3();
    Parity4 p4 = new Parity4();

    if (args.length == 1) {
      long x = Long.parseLong(args[0]);
      assert(p1.compute(x) == p2.compute(x));
      assert(p1.compute(x) == p3.compute(x));
      assert(p1.compute(x) == p4.compute(x));
      System.out.println("x = " + x + ", parity = " + p1.compute(x));
    } else {
      TimeTests.test((NUM_TESTS) -> p1.compute(), NUM_TESTS, "Parity1.compute");
      TimeTests.test((NUM_TESTS) -> p2.compute(), NUM_TESTS, "Parity2.compute");
      TimeTests.test((NUM_TESTS) -> p3.compute(), NUM_TESTS, "Parity3.compute");
      TimeTests.test((NUM_TESTS) -> p4.compute(), NUM_TESTS, "Parity4.compute");

      for (int i = 0; i < NUM_TESTS; i++) {
        try {
          assert(p1.testResults[i] == p2.testResults[i]);
          assert(p1.testResults[i] == p3.testResults[i]);
          assert(p1.testResults[i] == p4.testResults[i]);
//                    if (testResults1[i] != testResults4[i]) {
//                        System.out.println("Error at (" + i + ", " + testValues[i] + ") - expected: " + testResults1[i] + " - output: " + testResults4[i]);
//                    }
        } catch (AssertionError e) {
//                    System.out.println(e.toString() + " - " + e.getMessage() + " - ");
//                    e.printStackTrace();
        }

      }
//          System.out.println("x = " + x + ", parity = " + Parity3(x));
    }
  }
}