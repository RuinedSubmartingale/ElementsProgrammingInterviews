package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Created by psingh on 5/23/16.
 * Problem 6.6
 */
public class RemoveDuplicatesFromSortedArrayList {

  private static final int ARR_LEN = 1000;
  private static final int MAX_INT = 250;
  private static final int NUM_TESTS = (int) Math.pow(10, 3);

  // Assumes input is already sorted by key that determines equality
  private static <T> int removeDuplicates(ArrayList<T> A) {
    if (A.isEmpty()) return 0;
    int writeIdx = 0;
    for (int i=1; i < A.size(); ++i) {
      if (!A.get(writeIdx).equals(A.get(i)))
        A.set(++writeIdx, A.get(i));
//      else
//        System.out.println(String.format("Duplicate found at index %d: %s", i, A.get(i)));
    }
    return writeIdx + 1;
  }

  private static void smallTest() {
    ArrayList<Student> students = new ArrayList<>();
    students.add(new Student("John", "Adams"));
    students.add(new Student("John", "Adams")); // should flag as duplicate
    students.add(new Student("John", "Gold"));
    int studentsWithDiffNames = removeDuplicates(students);
    assert(studentsWithDiffNames == 2);

  }

  private static <T extends Comparable<? super T>> void runTests(Supplier<T> randSupplier, String testDesc) throws Exception {
    Callable<CloneableTestInputsMap> formInput = () -> {
      ArrayList<T> A = MiscHelperMethods.randArray(randSupplier, ARR_LEN);
      Collections.sort(A);
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      inputs.addArrayList("A", A);
      return inputs;
    };
    Function<CloneableTestInputsMap, ArrayList<T>> runAlg = (inputs) -> {
      int elemsLeft = removeDuplicates(inputs.getArrayList("A"));
      inputs.addInteger("elemsLeft", elemsLeft);
      return inputs.getArrayList("A");
    };
    Supplier<ArrayList<T>> emptyOutput = ArrayList::new;
    Function<CloneableTestInputsMap, ArrayList<T>> getKnownOutput = (inputs) -> {
      ArrayList<T> unique = new ArrayList<>(new HashSet<>(inputs.getArrayList("A")));
      Collections.sort(unique);
      return unique;
    };
    Function<CloneableTestInputsMap, CloneableTestInputsMap> saveExtraResults = (inputs) -> {
      CloneableTestInputsMap algExtraResults = new CloneableTestInputsMap();
      algExtraResults.addInteger("elemsLeft", inputs.getInteger("elemsLeft"));
      return algExtraResults;
    };
    TimeTests.TriFunction<ArrayList<T>, ArrayList<T>, CloneableTestInputsMap, Boolean> checkResults =
            (observedResult, expectedResult, algExtraResults) -> {
              List<T> truncatedObservedResults = observedResult.subList(0, algExtraResults.getInteger("elemsLeft"));
              return truncatedObservedResults.equals(expectedResult);
            };

    TimeTests<ArrayList<T>> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, testDesc);
    algTimer.testAndCheck(NUM_TESTS, checkResults, getKnownOutput, saveExtraResults);
  }


  public static void main(String[] args) throws Exception {
    smallTest();
    Random rgen = new Random();
    runTests(() -> rgen.nextInt(MAX_INT), "RemoveDuplicatesFromSortedArrayList - Integer array");
    runTests(() -> new Student(
                        Student.FNAME.values()[rgen.nextInt(Student.FNAME.values().length)].toString()
                        , Student.LNAME.values()[rgen.nextInt(Student.LNAME.values().length)].toString()
                      )
              , "RemoveDuplicatesFromSortedArrayList - Students array");
  }
}

class Student implements Comparable<Student> {
  enum FNAME {
    ADAM("ADAM"), BOB("BOB"), CHARLIE("CHARLIE"), DAVID("DAVID");
    private String fname;
    FNAME(String name) {
      this.fname = name;
    }
    @Override
    public String toString() {
      return fname;
    }
  }
  enum LNAME {
    JOHNSON("JOHNSON"), FIELDS("FIELDS"), ROSE("ROSE"), SMITH("SMITH");
    private String lname;
    LNAME(String name) {
      this.lname = name;
    }
    @Override
    public String toString() {
      return lname;
    }
  }

  private final String fname;
  private final String lname;
  private int cached_hash;

  Student(String _fname, String _lname) {
    fname = _fname;
    lname = _lname;
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
    if (other == null || !(other instanceof Student)) return false;
    if (this == other) return true;
    Student that = (Student) other;
    return new EqualsBuilder().append(fname, that.fname).append(lname, that.lname).isEquals();
  }

  @Override
  public int compareTo(Student other) {
    return (lname + fname).compareTo(other.lname + other.fname);
  }
}