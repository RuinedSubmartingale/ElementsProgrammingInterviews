package epi.solutions;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by psingh on 5/28/16.
 * Problem 6.13
 */

public class PermuteArray {
  private static final int ARR_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static abstract class AbstractArrayPermutation {
    abstract <T> void applyPermutation(List<Integer> perm, List<T> A);
  }

  private static class ArrayPermutation1 extends AbstractArrayPermutation {

    /**   O(n) time / O(n) implicit storage (assumes we can overwrite P)
     *
     * @param perm Permutation array
     * @param A Array of elements to permute
     */
    @Override
    <T> void applyPermutation(@NotNull List<Integer> perm, @NotNull List<T> A) {
      Preconditions.checkArgument(perm.size() == A.size(), "The sizes of A and P (the permutation array) differ.");
      int n = perm.size();
      for (int i = 0; i < n; ++i) {
        // Checks if the element at index i has not been moved already
        // by checking if perm.get(i) is nonnegative
        int currPos = i;
        while(perm.get(currPos) >= 0) {
          Collections.swap(A, i, perm.get(currPos));
          int next = perm.get(currPos);
          // Subtracts perm.size() from an entry in perm to make it negative,
          // which indicates that the corresponding move has been performed.
          perm.set(currPos, perm.get(currPos) - n);
          currPos = next;
        }
      }
    }
  }

  private static class ArrayPermutation2 extends AbstractArrayPermutation {

    /**   O(n^2) time / O(1) space
     *
     * @param perm  Permutation array.
     * @param A     Array of elements to permute
     * @param <T>   I'm pretty sure this can be any type that can be contained in a list.
     */
    @Override
    <T> void applyPermutation(List<Integer> perm, List<T> A) {
      Preconditions.checkArgument(perm.size() == A.size(), "The sizes of A and P (the permutation array) differ.");
      int n = A.size();
      for (int i = 0; i < n; ++i) {
        boolean isMin = true;
        int nextPos = perm.get(i);
        // Check if A.get(i) is the leftmost element for some some cyclic permutation in perm
        while(nextPos != i) {
          if(nextPos < i) {
            isMin = false; break;
          }
          nextPos = perm.get(nextPos);
        }
        if (isMin) {
          cyclicPermutation(A, perm, i);
        }
      }
    }

    private <T> void cyclicPermutation(List<T> A, List<Integer> perm, int start) {
      int currPos = start;
      T currElem = A.get(currPos);
      do {
        int nextPos = perm.get(currPos);
        T nextElem = A.get(nextPos);
        A.set(nextPos, currElem);
        currPos = nextPos;
        currElem = nextElem;
      } while (currPos != start);
    }
  }

  public static void main(String[] args) throws Exception {
    ArrayPermutation1 ap1 = new ArrayPermutation1();
    ArrayPermutation2 ap2 = new ArrayPermutation2();
//      System.out.println("A: " + A + "\nP: " + P);
//    System.out.println("A: " + A + "\nP: " + P);

    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      List<Integer> A = MiscHelperMethods.randArray(rgen::nextInt, ARR_LEN);
      List<Integer> P = IntStream.range(0, A.size()).boxed().collect(Collectors.toList());
      Collections.shuffle(A);
      Collections.shuffle(P);
//      List<Integer> A = Arrays.asList(9,0,4,7,1,6,3,5,8,2);
//      List<Integer> P = Arrays.asList(8,0,4,2,3,1,7,5,9,6);
//      Expecteed                      [0,6,7,1,4,5,2,3,9,8]
      inputs.addArrayList("A", A);
      inputs.addArrayList("P", P);
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg1 = (inputs) -> {
      ap1.applyPermutation(inputs.getArrayList("P"), inputs.getArrayList("A"));
      return inputs.getArrayList("A");
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg2 = (inputs) -> {
      ap2.applyPermutation(inputs.getArrayList("P"), inputs.getArrayList("A"));
      return inputs.getArrayList("A");
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> getKnownOutput = (inputs) -> {
      ArrayList<Integer> perm = inputs.getArrayList("P");
      ArrayList<Integer> A = inputs.getArrayList("A");
      // The following line took me longer to get right than I'd care to admit...
      // This is also probably very slow since it has n calls to perm.indexOf(), each of which is O(n)
      return IntStream.range(0, A.size()).boxed().map(i -> A.get(perm.indexOf(i))).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    };
    BiFunction<ArrayList<Integer>, ArrayList<Integer>, Boolean> checkAns = ArrayList::equals;
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList::new;

    System.out.println(String.format("Running algorithms on arrays of length %d...", ARR_LEN));
    TimeTests<ArrayList<Integer>> algTimer1 = new TimeTests<>(formInput, runAlg1, emptyOutput, "Permute Array: O(n) time / O(n) space");
    TimeTests<ArrayList<Integer>> algTimer2 = new TimeTests<>(formInput, runAlg2, emptyOutput, "Permute Array: O(n^2) time / O(1) space");


    CloneableTestInputsMap inputs = formInput.call();
    CloneableTestInputsMap orig_input = new CloneableTestInputsMap();
    inputs.forEach((name, inputType) -> orig_input.put(name, inputType.cloneInput()));
    ArrayList<Integer> observed = runAlg1.apply(inputs);
    ArrayList<Integer> expected = getKnownOutput.apply(orig_input);
    boolean correct = checkAns.apply(expected, observed);
    assert(correct);
    algTimer1.timeAndCheck(NUM_TESTS, checkAns, getKnownOutput);
    algTimer2.timeAndCheck(NUM_TESTS, checkAns, getKnownOutput);
  }
}