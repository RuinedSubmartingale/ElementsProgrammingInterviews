package epi.solutions;


import epi.solutions.helper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    Supplier<CloneableInputsMap> formInputs = () -> {
      Random rgen = new Random();
      CloneableInputsMap inputs = new CloneableInputsMap();
      inputs.addArrayList("A", MiscHelperMethods.randArray(() -> rgen.nextInt(ELEMS_BUCKET_SIZE), ARRAY_LENGTH));
      inputs.addInteger("key", rgen.nextInt(ELEMS_BUCKET_SIZE));
      return inputs;
    };
    Function<CloneableInputsMap, ArrayList<Integer>> runAlg = (inputs) -> {
      int elemsLeft = removeElement(inputs.getInteger("key"), inputs.getArrayList("A", Integer.class));
      inputs.addInteger("elemsLeft", elemsLeft);
      return inputs.getArrayList("A", Integer.class);
    };
    Function<CloneableInputsMap, ArrayList<Integer>> getKnownOutput = (inputs) -> {
      // TODO: Try .... while(A.remove(key)) {} .... or ... A.removeAll(Collections.singleton(key))... instead of removeIf?
      inputs.getArrayList("A", Integer.class).removeIf(inputs.getInteger("key")::equals);
      return inputs.getArrayList("A", Integer.class);
    };
    AlgVerifierInterfaces.TriFunction<ArrayList<Integer>, ArrayList<Integer>, CloneableInputsMap, Boolean> checkResults =
            (observedResults, expectedResults, algExtraResults) -> {
              List<Integer> truncatedObservedResults = observedResults.subList(0, algExtraResults.getInteger("elemsLeft"));
              // TODO: Implement equality comparison for two generic lists
              return truncatedObservedResults.equals(expectedResults);
            };

    AlgVerifierInterfaces< ArrayList<Integer>, CloneableInputsMap> algVerifier = new OutputOutputExtraVerifier<>(checkResults);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("RemoveElement", NUM_TESTS, formInputs, runAlg, getKnownOutput, algVerifier);
//    algorithmFactory.setSequential();
    algorithmFactory.run();

//    ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1,2,1,5,3,4,1,8));
//    ArrayList<Integer> copyA = new ArrayList<>(A);
//    int sizeLeft = removeElement(1, A);
//    copyA.removeAll(Arrays.asList(1));
//    System.out.println(String.format("%10s: %s", "A: ", A.subList(0, sizeLeft)));
//    System.out.print(String.format("%10s: %s", "copyA: ", copyA));
  }
}
