package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/13/16.
 * Problem 6.1
 */
public class DutchFlagPartition {

  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static final int FLAG_LENGTH = 20;

  @SuppressWarnings("unused")
  private enum Color {RED, WHITE, BLUE}
  private enum MyBoolean {FALSE, TRUE}

  private static void partition(Color pivot, ArrayList<Color> A) {
    /**
     * Keep the following invariants during partitioning:
     * bottom group: A.subList(0, smaller).
     * middle group: A.subList(smaller, equal).
     * unclassified group: A.subList(equal, larger).
     * top group: A.subList(larger, A.size()).
     */
    int smaller = 0, equal = 0, larger = A.size();
    while (equal < larger) {
      if (A.get(equal).ordinal() < pivot.ordinal())
        Collections.swap(A, smaller++, equal++);
      else if (A.get(equal).ordinal() == pivot.ordinal())
        ++equal;
      else
        Collections.swap(A, equal, --larger);
    }
  }

  private static void partitionBooleans(MyBoolean pivot, ArrayList<MyBoolean> A) {
    int false_idx = 0, true_idx = A.size();
    while (false_idx < true_idx) {
      if (A.get(false_idx).ordinal() < pivot.ordinal())
        ++false_idx;
      else
        Collections.swap(A, false_idx, --true_idx);
    }
  }

  public static void main(String[] args) throws Exception {
    runTest(Color.values(), Color.WHITE, DutchFlagPartition::partition, "DutchFlagPartition (colors - inplace)");
    runTest(MyBoolean.values(), MyBoolean.TRUE, DutchFlagPartition::partitionBooleans, "DutchFlagPartition (Booleans - inplace and stable)");
  }

  private static <T extends Enum<T>> void runTest(T[] enumVals, T pivot, BiConsumer<T, ArrayList<T>> partitionMethod, String testDesc) throws Exception {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      inputs.addArrayList("A", MiscHelperMethods.randArray(() -> enumVals[rgen.nextInt(enumVals.length)], FLAG_LENGTH));
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<T>> runAlgorithm = (input) -> {
      partitionMethod.accept(pivot, input.getArrayList("A"));
//      System.out.println(String.format("%-20s %s", "Observed output: ", (ArrayList<Color>) input));
      return input.getArrayList("A");
    };
    Function<CloneableTestInputsMap, ArrayList<T>> getKnownOutput = (orig_input) -> {
      ArrayList<T> result;
      result = orig_input.getArrayList("A");
      result.sort((T t1, T t2) -> t1.ordinal() - t2.ordinal());
//      System.out.println(String.format("%-20s %s", "Expected output: ", (ArrayList<Color>) orig_input));
      return result;
    };
    BiFunction<ArrayList<T>, ArrayList<T>, Boolean> checkResults = ArrayList::equals;
    Supplier<ArrayList<T>> emptyOutput = ArrayList::new;
    TimeTests<ArrayList<T>> algTimer = new TimeTests<>(formInput, runAlgorithm, emptyOutput, testDesc);
    algTimer.timeAndCheck(NUM_TESTS, checkResults, getKnownOutput);
  }
}
