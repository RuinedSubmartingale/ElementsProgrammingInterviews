package epi.solutions;

import epi.solutions.helper.CloneableArrayList;
import epi.solutions.helper.CloneableTestInput;
import epi.solutions.helper.CloneableTestInputsMap;
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

  private static enum Color {RED, WHITE, BLUE}
  private static enum MyBoolean {FALSE, TRUE}

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

  private static <T> ArrayList<T> randFlagArray(Supplier<T> randSupplier, int len) {
    ArrayList<T> result = new ArrayList<>(len);
    for (int i = 0; i < len; ++i) {
      result.add(randSupplier.get());
    }
    return result;
  }

//  @FunctionalInterface
//  private interface PartitionFunction<T> {
//    void apply(T pivot, ArrayList<T> A);
//  }
//  private static <T extends Enum<T>> void unitTestFuncInterface(T pivot, ArrayList<T> A, PartitionFunction<T> partitionMethod) {
//    ArrayList<T> Adup = new ArrayArrayList<>(A);
//    partitionMethod.apply(pivot, A);
//    assert(check(pivot, A, Adup));
//  }

  public static void main(String[] args) {
    runTest(Color.values(), Color.WHITE, DutchFlagPartition::partition, "DutchFlagPartition (colors - inplace)");
    runTest(MyBoolean.values(), MyBoolean.TRUE, DutchFlagPartition::partitionBooleans, "DutchFlagPartition (Booleans - inplace and stable)");
  }

  public static <T extends Enum<T>> void runTest(T[] enumVals, T pivot, BiConsumer<T, ArrayList<T>> partitionMethod, String testDesc) {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      CloneableArrayList A = new CloneableArrayList(
              randFlagArray(() -> enumVals[rgen.nextInt(enumVals.length)], FLAG_LENGTH)
      );
      inputs.put("A", A);
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<T>> runAlgorithm = (input) -> {
      partitionMethod.accept(pivot, (ArrayList<T>) input.get("A"));
//      System.out.println(String.format("%-20s %s", "Observed output: ", (ArrayList<Color>) input));
      return (ArrayList<T>) input.get("A");
    };
    Function<CloneableTestInputsMap, ArrayList<T>> getKnownOutput = (orig_input) -> {
      ((ArrayList<T>) orig_input.get("A")).sort((T t1, T t2) -> t1.ordinal() - t2.ordinal());
//      System.out.println(String.format("%-20s %s", "Expected output: ", (ArrayList<Color>) orig_input));
      return (ArrayList<T>) orig_input.get("A");
    };
    BiFunction<ArrayList<T>, ArrayList<T>, Boolean> checkResults = ArrayList::equals;
    TimeTests<ArrayList<T>> algTimer = new TimeTests<>(formInput, runAlgorithm, getKnownOutput, checkResults, NUM_TESTS, testDesc);
    algTimer.testAndCheck();
  }
}
