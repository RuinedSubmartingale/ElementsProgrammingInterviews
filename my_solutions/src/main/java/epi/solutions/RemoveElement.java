package epi.solutions;

import epi.solutions.helper.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/20/16.
 * Problem 6.5
 * @input key - element of which all occurrences are to be removed
 * @input A - array of elements
 */
public class RemoveElement {
  private static final int ARRAY_LENGTH = 100;
  private static final int ELEMS_BUCKET_SIZE = 20;
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static <T extends Comparable> int removeElement(T key, ArrayList<T> A) {
    int writeIdx= 0;
    for(int i=0; i < A.size(); ++i)
      if (!A.get(i).equals(key))
        A.set(writeIdx++, A.get(i));
    return writeIdx;
  }

  private static <T extends Comparable> void checkResults(T key, ArrayList<T> A) {
    for(int i=0; i < A.size(); ++i)
      assert(!A.get(i).equals(key));
  }

  public static void main(String[] args) {
    Callable<CloneableTestInputsMap> formInput = () -> {
      Random rgen = new Random();
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      CloneableArrayList A = new CloneableArrayList(
              MiscHelperMethods.randArray(() -> rgen.nextInt(ELEMS_BUCKET_SIZE), ARRAY_LENGTH)
      );
      CloneableInteger key = new CloneableInteger(rgen.nextInt(ELEMS_BUCKET_SIZE));
      inputs.put("A", A);
      inputs.put("key", key);
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg = (inputs) -> {
      int elemsLeft = removeElement(((CloneableInteger) inputs.get("key")).data, (ArrayList<Integer>) inputs.get("A"));
      inputs.put("elemsLeft", new CloneableInteger(elemsLeft));
      return (ArrayList<Integer>) inputs.get("A");
    };
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList<Integer>::new;
    Function<CloneableTestInputsMap, ArrayList<Integer>> getKnownOutput = (inputs) -> {
      // TODO: Try .... while(A.remove(key)) {} .... or ... A.removeAll(Collections.singleton(key))... instead of removeIf?
      ((ArrayList<Integer>) inputs.get("A")).removeIf(((CloneableInteger) inputs.get("key")).data::equals);
      return (ArrayList<Integer>) inputs.get("A");
    };
    Function<CloneableTestInputsMap, HashMap<String, Object>> saveExtraResults = (inputs) -> {
      HashMap<String, Object> algExtraResults = new HashMap<>();
      algExtraResults.put("elemsLeft", inputs.get("elemsLeft"));
      return algExtraResults;
    };
    TimeTests.TriFunction<ArrayList<Integer>, ArrayList<Integer>, HashMap, Boolean> checkResults =
            (observedResults, expectedResults, algExtraResults) -> {
              List<Integer> truncatedObservedResults = observedResults.subList(0, ((CloneableInteger) algExtraResults.get("elemsLeft")).data);
              // TODO: Implement equality comparison for two generic lists
              return truncatedObservedResults.equals(expectedResults);
            };

    TimeTests<ArrayList<Integer>> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "RemoveElement");
    algTimer.testAndCheck(NUM_TESTS, checkResults, getKnownOutput, saveExtraResults);

//    ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1,2,1,5,3,4,1,8));
//    ArrayList<Integer> copyA = new ArrayList<>(A);
//    int sizeLeft = removeElement(1, A);
//    copyA.removeAll(Arrays.asList(1));
//    System.out.println(String.format("%10s: %s", "A: ", A.subList(0, sizeLeft)));
//    System.out.print(String.format("%10s: %s", "copyA: ", copyA));
  }
}
