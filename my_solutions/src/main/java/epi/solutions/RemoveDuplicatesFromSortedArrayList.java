package epi.solutions;

import epi.solutions.helper.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.runners.model.TestTimedOutException;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Created by psingh on 5/23/16.
 */
public class RemoveDuplicatesFromSortedArrayList {

  private static final int ARR_LEN = 1000;
  private static final int MAX_INT = 250;
  private static final int NUM_TESTS = (int) Math.pow(10, 2);

  // Assumes input is already sorted by key that determines equality
  private static <T> int removeDuplicates(ArrayList<T> A) {
    if (A.isEmpty()) return 0;
    int writeIdx = 0;
    for (int i=1; i < A.size(); ++i) {
      if (!A.get(writeIdx).equals(A.get(i))) {
        A.set(++writeIdx, A.get(i));
      } else {
//        System.out.println(String.format("Duplicate found at index %d: %s", i, A.get(i)));
      }

    }
    return writeIdx + 1;
  }

  private static void smallTest() {
    ArrayList<Student> students = new ArrayList<>();
    students.add(new Student("John", "Adams", 3.0));
    students.add(new Student("John", "Adams", 4.0)); // should flag as duplicate
    students.add(new Student("John", "Gold", 3.0));
    int studentsWithDiffNames = removeDuplicates(students);
    assert(studentsWithDiffNames == 2);

  }

  public static void main(String[] args) {
    smallTest();
    Callable<CloneableTestInputsMap> formInput = () -> {
      Random rgen = new Random();
      ArrayList<Integer> A = MiscHelperMethods.randArray(() -> rgen.nextInt(MAX_INT), ARR_LEN);
      Collections.sort(A);
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      inputs.put("A", new CloneableArrayList(A));
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg = (inputs) -> {
      int elemsLeft = removeDuplicates((ArrayList<Integer>) inputs.get("A"));
      inputs.put("elemsLeft", new CloneableInteger(elemsLeft));
      return (ArrayList<Integer>) inputs.get("A");
    };
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList<Integer>::new;
    Function<CloneableTestInputsMap, ArrayList<Integer>> getKnownOutput = (inputs) -> {
      Set<Integer> unique = new HashSet<>((ArrayList<Integer>) inputs.get("A"));
      return new ArrayList<>(unique);
    };
    Function<CloneableTestInputsMap, HashMap<String, Object>> saveExtraResults = (inputs) -> {
      HashMap<String, Object> algExtraResults = new HashMap<>();
      algExtraResults.put("elemsLeft", ((CloneableInteger) inputs.get("elemsLeft")).data);
      return algExtraResults;
    };
    TimeTests.TriFunction<ArrayList<Integer>, ArrayList<Integer>, HashMap, Boolean> checkResults =
            (observedResult, expectedResult, algExtraResults) -> {
              List<Integer> truncatedObservedResults = observedResult.subList(0, (int) algExtraResults.get("elemsLeft"));
              return truncatedObservedResults.equals(expectedResult);
            };
    TimeTests<ArrayList<Integer>> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "RemoveDuplicatesFromSortedArrayList");
    algTimer.testAndCheck(NUM_TESTS, checkResults, getKnownOutput, saveExtraResults);

  }


}

class Student implements Comparable<Student> {
  private final String fname;
  private final String lname;
  private final double gpa;
  private int cached_hash;

  Student(String _fname, String _lname, double _gpa) {
    fname = _fname;
    lname = _lname;
    gpa = _gpa;
  }

  @Override
  public int hashCode() {
    if (cached_hash == 0) {
      cached_hash = new HashCodeBuilder(41, 59) // 2 randomly chosen primes
              .append(fname).append(lname).toHashCode();
    }
    return cached_hash;
  }

  @Override
  public boolean equals(Object other) {
    if (! (other instanceof Student)) return false;
    Student that = (Student) other;
    return new EqualsBuilder().append(fname, that.fname).append(lname, that.lname).isEquals();
  }

  @Override
  public int compareTo(Student other) {
    return (lname + fname).compareTo(other.lname + other.fname);
  }
}