package epi.solutions;

import epi.solutions.helper.TimeTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/13/16.
 * Problem 6.1
 */
public class DutchFlagPartition {

  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static final int FLAG_LENGTH = 20;

  private static enum Color { RED, WHITE, BLUE }
  private static enum MyBoolean {FALSE, TRUE};

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
    for(int i = 0; i < len; ++i) {
      result.add(randSupplier.get());
    }
    return result;
  }

  private static <T extends Enum<T>> void unitTestFuncInterface(T pivot, List<T> A, PartitionFunction<T> partitionMethod) {
    List<T> Adup = new ArrayList<>(A);
    partitionMethod.apply(pivot, A);
    assert(check(pivot, A, Adup));
  }

  private static <T extends Enum<T>> void unitTestBiConsumer(T pivot, List<T> A, BiConsumer<T, List<T>> partitionMethod) {
    List<T> Adup = new ArrayList<>(A);
    partitionMethod.accept(pivot, A);
    assert(check(pivot, A, Adup));
  }

  private static <T extends Enum<T>> boolean check(T pivot, List<T> A, List<T> Adup) {
    int n = A.size();
    int i = 0;
    while (i < n && A.get(i).ordinal() < pivot.ordinal())
      ++i;
    while (i < n && A.get(i) == pivot)
      ++i;
    while (i < n && A.get(i).ordinal() > pivot.ordinal())
      ++i;
    if (i != n)
      System.out.println("Failed tests on input " + Adup
              + "\nYour code updated to " + A);
    return i == n;
  }

  public static void main(String[] args) {
    Random rgen = new Random();
    TimeTests.test((NUM_TESTS1) -> {
      for (int times = 0; times < NUM_TESTS1; ++times) {
        List<Color> A = randFlagArray(() -> Color.values()[rgen.nextInt(3)], FLAG_LENGTH);
        // PartitionFunction<Color> partitionColors = DutchFlagPartition::partition;
        // unitTestFuncInterface(Color.WHITE, A, DutchFlagPartition::partition);
        unitTestBiConsumer(Color.WHITE, A, DutchFlagPartition::partition);
      }
      }, NUM_TESTS, "DutchFlagPartition");
    TimeTests.test((NUM_TESTS1) -> {
      for (int times = 0; times < NUM_TESTS1; ++times) {
        List<MyBoolean> A = randFlagArray(() -> MyBoolean.values()[rgen.nextInt(2)], FLAG_LENGTH);
        // PartitionFunction<MyBoolean> partitionBooleans = DutchFlagPartition::partitionBooleans;
        // unitTestFuncInterface(MyBoolean.TRUE, A, DutchFlagPartition::partitionBooleans);
        unitTestBiConsumer(MyBoolean.TRUE, A, DutchFlagPartition::partitionBooleans);
      }
    }, NUM_TESTS, "DutchFlagPartition variant (inplace stable sort of Booleans)");
  }

//  @FunctionalInterface
//  private interface PartitionFunction<T> {
//    void apply(T pivot, List<T> A);
//  }
}
