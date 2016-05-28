package epi.solutions;

import com.google.common.base.Joiner;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/13/16.
 * Problem 6.2
 * Given an array A of digits encoding a decimal number D,
 * with MSD at A[0]. Update A to hold D + 1.
 */
public class PlusOne {
  private static final int NUM_TESTS = (int) Math.pow(10, 5);
  private static final int ARR_LENGTH = (int) Math.pow(10, 2);

  @SuppressWarnings("Duplicates")
  private static ArrayList<Integer> plusOne(ArrayList<Integer> A) {
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

  private static ArrayList<Integer> randArray(int len) {
    ArrayList<Integer> A = new ArrayList<>();
    if (len == 0) return A;
    Random rgen = new Random();
    A.add(rgen.nextInt(9) + 1);
    --len;
    while (len != 0) {
      A.add(rgen.nextInt(10));
      --len;
    }
    return A;
  }

  public static void main(String[] args) throws Exception {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      inputs.addArrayList("A", randArray(ARR_LENGTH));
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlgorithm =
            (input) -> plusOne(input.getArrayList("A"));
    Function<CloneableTestInputsMap, ArrayList<Integer>> getKnownOutput = (orig_input) -> {
      BigInteger B = new BigInteger(Joiner.on("").join(orig_input.getArrayList("A")));
      B = B.add(BigInteger.valueOf(1));
      ArrayList<Integer> expectedOutput = new ArrayList<>();
      while (B.compareTo(BigInteger.valueOf(0)) > 0) {
        expectedOutput.add(0, B.mod(BigInteger.valueOf(10)).intValue());
        B = B.divide(BigInteger.valueOf(10));
      }
      return expectedOutput;
    };
    BiFunction<ArrayList<Integer>, ArrayList<Integer>, Boolean> checkResults = List::equals;
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList<Integer>::new;
    TimeTests<ArrayList<Integer>> algTimer =
            new TimeTests<>(formInput, runAlgorithm, emptyOutput, "PlusOne");
    algTimer.testAndCheck(NUM_TESTS, checkResults, getKnownOutput);
  }
}
