package epi.solutions;

import epi.solutions.helper.CloneableList;
import epi.solutions.helper.CloneableTestInput;
import epi.solutions.helper.TimeTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import static com.google.common.base.Preconditions.*;

/**
 * Created by psingh on 5/13/16.
 * Problem 6.1
 */
public class DutchFlagPartition {

  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static final int FLAG_LENGTH = 20;

  private static enum Color {RED, WHITE, BLUE}
  private static enum MyBoolean {FALSE, TRUE}

  private static void partition(Color pivot, List<Color> A) {
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

  private static void partitionBooleans(MyBoolean pivot, List<MyBoolean> A) {
    int false_idx = 0, true_idx = A.size();
    while (false_idx < true_idx) {
      if (A.get(false_idx).ordinal() < pivot.ordinal())
        ++false_idx;
      else
        Collections.swap(A, false_idx, --true_idx);
    }
  }

  private static <T> List<T> randFlagArray(Supplier<T> randSupplier, int len) {
    List<T> result = new ArrayList<>(len);
    for (int i = 0; i < len; ++i) {
      result.add(randSupplier.get());
    }
    return result;
  }

//  @FunctionalInterface
//  private interface PartitionFunction<T> {
//    void apply(T pivot, List<T> A);
//  }
//  private static <T extends Enum<T>> void unitTestFuncInterface(T pivot, List<T> A, PartitionFunction<T> partitionMethod) {
//    List<T> Adup = new ArrayList<>(A);
//    partitionMethod.apply(pivot, A);
//    assert(check(pivot, A, Adup));
//  }

  public static void main(String[] args) {
    runTest(Color.values(), Color.WHITE, DutchFlagPartition::partition);
    runTest(MyBoolean.values(), MyBoolean.TRUE, DutchFlagPartition::partitionBooleans);
  }

  public static <T extends Enum<T>> void runTest(T[] enumVals, T pivot, BiConsumer<T, List<T>> partitionMethod) {
    Callable<CloneableTestInput> formInput = () -> {
      Random rgen = new Random();
      return new CloneableList(randFlagArray(() ->
              enumVals[rgen.nextInt(enumVals.length)], FLAG_LENGTH));
    };
    Function<CloneableTestInput, List<T>> runAlgorithm = (input) -> {
      partitionMethod.accept(pivot, (List<T>) input);
//      System.out.println(String.format("%-20s %s", "Observed output: ", (List<Color>) input));
      return (List<T>) input;
    };
    Function<CloneableTestInput, List<T>> getKnownOutput = (orig_input) -> {
      ((List<T>) orig_input).sort((T t1, T t2) -> t1.ordinal() - t2.ordinal());
//      System.out.println(String.format("%-20s %s", "Expected output: ", (List<Color>) orig_input));
      return (List<T>) orig_input;
    };
    BiFunction<List<T>, List<T>, Boolean> checkResults = List::equals;
    TimeTests<List<T>> algTimer = new TimeTests<>();
    algTimer.test(formInput, runAlgorithm, getKnownOutput, checkResults
            , NUM_TESTS, "DutchFlagPartition (colors - inplace)");
  }
}
