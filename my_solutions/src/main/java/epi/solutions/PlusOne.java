package epi.solutions;

import com.google.common.base.Joiner;
import epi.solutions.helper.CloneableList;
import epi.solutions.helper.CloneableTestInput;
import epi.solutions.helper.TimeTests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by psingh on 5/13/16.
 * Problem 6.2
 * Given an array A of digits encodiing a decimal number D,
 * with MSD at A[0]. Update A to hold D + 1.
 */
public class PlusOne {
  private static final int NUM_TESTS = (int) Math.pow(10, 5);
  private static final int ARR_LENGTH = (int) Math.pow(10, 2);

  @SuppressWarnings("Duplicates")
  private static List<Integer> plusOne(List<Integer> A) {
    int n = A.size() - 1;
    A.set(n, A.get(n) + 1);
    for (int i = n; i > 0 && A.get(i) == 10; --i) {
      A.set(i, 0);
      A.set(i-1, A.get(i-1) + 1);
    }
    if (A.get(0) == 10) {
      // Need additional digit up front as MSD
      A.set(0,0);
      A.add(0,1);
    }
    return A;
  }

  private static List<Integer> randArray(int len) {
    if (len == 0)
      return Arrays.asList(0);
    Random rgen = new Random();
    List<Integer> A = new ArrayList<>();
    A.add(rgen.nextInt(9) + 1);
    --len;
    while (len != 0) {
      A.add(rgen.nextInt(10));
      --len;
    }
    return A;
  }

  public static void main(String[] args) {
    Callable<CloneableTestInput> formInput = () -> new CloneableList(randArray(ARR_LENGTH));
    Function<CloneableTestInput, List<Integer>> runAlgorithm = (input) -> plusOne((List<Integer>) input);
    Function<CloneableTestInput, List<Integer>> getKnownOutput = (orig_input) -> {
      BigInteger B = new BigInteger(Joiner.on("").join((List<Integer>) orig_input));
      B = B.add(BigInteger.valueOf(1));
      List<Integer> expectedOutput = new ArrayList<>();
      while (B.compareTo(BigInteger.valueOf(0)) > 0) {
        expectedOutput.add(0, B.mod(BigInteger.valueOf(10)).intValue());
        B = B.divide(BigInteger.valueOf(10));
      }
      return expectedOutput;
    };
    BiFunction<List<Integer>, List<Integer>, Boolean> checkResults = List::equals;
    TimeTests<List<Integer>> algTimer = new TimeTests<>();
    algTimer.test(formInput, runAlgorithm, getKnownOutput, checkResults, NUM_TESTS, "PlusOne");
  }
}
