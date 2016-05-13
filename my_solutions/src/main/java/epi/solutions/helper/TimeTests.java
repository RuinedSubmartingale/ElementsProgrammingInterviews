package epi.solutions.helper;

import java.text.DecimalFormat;

/**
 * Created by psingh on 5/12/16.
 */

public class TimeTests {
  public static void test(TestInterface ti, final int NUM_TESTS, String testName) {
    try {
      DecimalFormat df = new DecimalFormat("#.####");
      long start = System.nanoTime();
      ti.test(NUM_TESTS);
      System.out.println("DEBUG: " + testName + " took "
              + df.format((System.nanoTime() - start) * 1.0 / NUM_TESTS)
              + " nanoseconds on average for " + NUM_TESTS + " tests");
    } catch (AssertionError e) {
      System.out.println(e.toString() + " - " + e.getMessage() + " - ");
      e.printStackTrace();
    }

  }

  public void main(String[] args) {

  }
}
