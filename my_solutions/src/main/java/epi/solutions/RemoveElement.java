package epi.solutions;


import epi.solutions.helper.CloneableInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/20/16.
 * Problem 6.5
 */
public class RemoveElement {
  private static final int ARRAY_LENGTH = 100;
  private static final int ELEMS_BUCKET_SIZE = 20;
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /*
  *@param key - element of which all occurrences are to be removed
 * @param A - array of elements
  */
  private static <T extends Comparable> int removeElement(T key, ArrayList<T> A) {
    int writeIdx= 0;
    for(int i=0; i < A.size(); ++i)
      if (!A.get(i).equals(key))
        A.set(writeIdx++, A.get(i));
    return writeIdx;
  }

  public static void main(String[] args) throws Exception {
    Callable<CloneableInputsMap> formInput = () -> {
      Random rgen = new Random();
      CloneableInputsMap inputs = new CloneableInputsMap();
      inputs.addArrayList("A", MiscHelperMethods.randArray(() -> rgen.nextInt(ELEMS_BUCKET_SIZE), ARRAY_LENGTH));
      inputs.addInteger("key", rgen.nextInt(ELEMS_BUCKET_SIZE));
      return inputs;
    };
    Function<CloneableInputsMap, ArrayList<Integer>> runAlg = (inputs) -> {
      int elemsLeft = removeElement(inputs.getInteger("key"), inputs.getArrayList("A"));
      inputs.addInteger("elemsLeft", elemsLeft);
      return inputs.getArrayList("A");
    };
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList<Integer>::new;
    Function<CloneableInputsMap, ArrayList<Integer>> getKnownOutput = (inputs) -> {
      // TODO: Try .... while(A.remove(key)) {} .... or ... A.removeAll(Collections.singleton(key))... instead of removeIf?
      inputs.getArrayList("A").removeIf(inputs.getInteger("key")::equals);
      return inputs.getArrayList("A");
    };
    Function<CloneableInputsMap, CloneableInputsMap> saveExtraResults = (inputs) -> {
      CloneableInputsMap algExtraResults = new CloneableInputsMap();
      algExtraResults.addInteger("elemsLeft", inputs.getInteger("elemsLeft"));
      return algExtraResults;
    };
    TimeTests.TriFunction<ArrayList<Integer>, ArrayList<Integer>, CloneableInputsMap, Boolean> checkResults =
            (observedResults, expectedResults, algExtraResults) -> {
              List<Integer> truncatedObservedResults = observedResults.subList(0, algExtraResults.getInteger("elemsLeft"));
              // TODO: Implement equality comparison for two generic lists
              return truncatedObservedResults.equals(expectedResults);
            };

    TimeTests<ArrayList<Integer>> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "RemoveElement");
    algTimer.setKnownOutput(getKnownOutput);
    algTimer.saveExtraAlgResults(saveExtraResults);
    algTimer.timeAndCheck(NUM_TESTS, checkResults);

//    ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1,2,1,5,3,4,1,8));
//    ArrayList<Integer> copyA = new ArrayList<>(A);
//    int sizeLeft = removeElement(1, A);
//    copyA.removeAll(Arrays.asList(1));
//    System.out.println(String.format("%10s: %s", "A: ", A.subList(0, sizeLeft)));
//    System.out.print(String.format("%10s: %s", "copyA: ", copyA));
  }
}
