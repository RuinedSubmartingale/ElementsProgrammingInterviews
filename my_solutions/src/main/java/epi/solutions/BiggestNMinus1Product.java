package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/27/16.
 * Problem 6.10
 */
public class BiggestNMinus1Product {
  private static final int N = (int) Math.pow(10, 3);
  private static final int NUM_TESTS = (int) Math.pow(10, 4);

  private static abstract class AbstractBiggestNMinus1Product {
    /**
     * @param A array of n integers
     * @return max product of n-1 elements
     */
    @SuppressWarnings("unused")
    public abstract long findBiggestNMinusOneProduct(List<Integer> A);
  }

  /**
   * O(n) time / O(n) space
   */
  private static class Algorithm1 extends AbstractBiggestNMinus1Product {
    @Override
    public long findBiggestNMinusOneProduct(List<Integer> A) {
      List<Long> leftPartialProducts = new ArrayList<>(Collections.nCopies(A.size(), 0L));
      List<Long> rightPartialProducts = new ArrayList<>(Collections.nCopies(A.size(), 0L));
      long product = 1;
      for (int i=0; i < A.size(); ++i) {
        product *= A.get(i);
        leftPartialProducts.set(i, product);
      }
      product = 1;
      for (int i=A.size() - 1; i >= 0; --i) {
        product *= A.get(i);
        rightPartialProducts.set(i, product);
      }

      long maxProduct = Long.MIN_VALUE;
      for (int i = 0; i < A.size(); ++i) {
        long forward = i > 0 ? leftPartialProducts.get(i - 1) : 1;
        long backward = i < A.size() - 1 ? rightPartialProducts.get(i + 1) : 1;
        maxProduct = Math.max(maxProduct, forward * backward);
      }
      return maxProduct;
    }
  }

  /**
   * O(n) time / O(1) space
   */
  private static class Algorithm2 extends AbstractBiggestNMinus1Product {
    @SuppressWarnings("ConstantConditions")
    @Override
    public long findBiggestNMinusOneProduct(List<Integer> A) {
      int zeroCnt = 0, zeroIdx = -1;
      int posCnt = 0, leastPosIdx = -1; // least positive meaning closest to 0.
      int negCnt = 0, mostNegIdx = -1, leastNegIdx = -1; // least negative meaning closest to 0.

      for (int i = 0; i < A.size(); ++i) {
        if (A.get(i) < 0) {
          negCnt += 1;
          if (mostNegIdx > 0 && A.get(i) < A.get(mostNegIdx)) {
            mostNegIdx = i;
          } else if (leastNegIdx > 0 && A.get(i) > A.get(leastNegIdx)) {
            leastNegIdx = i;
          }
        } else if (A.get(i) == 0) {
          zeroCnt += 1;
          if (zeroCnt > 1) return 0;
          zeroIdx = i;
        } else { // A.get(i) > 0
          posCnt += 1;
          if (leastPosIdx > 0 && A.get(i) < A.get(leastPosIdx)) {
            leastPosIdx = i;
          }
        }
      }

      int j; // Identifies the index to skip when computing product.
      if (zeroCnt == 0) {
        if ((negCnt & 1) == 1) { // odd number of negatives
          j = leastNegIdx;
        } else { // even number of negative numbers
          if (posCnt > 0) {
            j = leastPosIdx;
          } else {
            j = mostNegIdx; // input consists solely of negative integers. exclude the most negative one.
          }
        }
      } else { // exactly 1 zero
        if ((negCnt & 1) == 1) { // odd number of negatives
          return 0; // excluding a negative yields a 0 product. excluding the zero yields a negative product.
        } else { // even number of negatives
          j = zeroIdx; // excluding the zero yields a positive product.
        }
      }

      long maxProduct = 1;
      for (int i = 0; i < A.size(); ++i) {
        if (i != j) maxProduct *= A.get(i);
      }

      return maxProduct;
    }
  }

  /**
   * naive solution - O(n^2) time / O(1) space
   * @param A array of n integers
   * @return max product of n-1 elements
   */
  private static long naiveSolution(List<Integer> A) {
    long maxProduct = Long.MIN_VALUE;
    for (int i = 0; i < A.size(); ++ i) {
      long product = 1;
      for (int j = 0; j < i; ++j) {
        product *= A.get(j);
      }
      for (int j = A.size() - 1; j > i; --j) {
        product *= A.get(j);
      }
      maxProduct = Math.max(maxProduct, product);
    }
    return maxProduct;
  }

  public static void main(String[] args) throws Exception {
    Algorithm1 alg1 = new Algorithm1();
    Algorithm2 alg2 = new Algorithm2();

    Callable<CloneableTestInputsMap> formInput = () -> {
      Random rgen = new Random();
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      ArrayList<Integer> A = MiscHelperMethods.randArray(rgen::nextInt, N);
      inputs.addArrayList("A", A);
      return inputs;
    };
    Function<CloneableTestInputsMap, Long> runAlg1 = (inputs) -> alg1.findBiggestNMinusOneProduct(inputs.getArrayList("A"));
    Function<CloneableTestInputsMap, Long> runAlg2 = (inputs) -> alg2.findBiggestNMinusOneProduct(inputs.getArrayList("A"));
    Supplier<Long> emptyOutput = () -> 0L;
    Function<CloneableTestInputsMap, Long> getKnownOutput = (inputs) -> naiveSolution(inputs.getArrayList("A"));
    BiFunction<Long, Long, Boolean> checkAns = Long::equals;

    TimeTests<Long> alg1Timer = new TimeTests<>(formInput, runAlg1, emptyOutput, "BiggestProductNMinus1 [ O(n) time / O(n) space ]");
    TimeTests<Long> alg2Timer = new TimeTests<>(formInput, runAlg2, emptyOutput, "BiggestProductNMinus1 [ O(n) time / O(1) space ]");

    PrintStream originalStream = MiscHelperMethods.setSystemOutToDummyStream();
    alg1Timer.timeAndCheck(1000, checkAns, getKnownOutput); // checking is O(n^2) expensive
    alg2Timer.timeAndCheck(1000, checkAns, getKnownOutput); // checking is O(n^2) expensive

    System.setOut(originalStream);
    alg1Timer.time(NUM_TESTS);
    alg2Timer.time(NUM_TESTS);
  }
}
