package epi.solutions;

import epi.solutions.helper.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/22/16.
 * Problem 8.1
 */
public class MergeSortedLists {
  private static final int LIST_LEN = (int) Math.pow(10, 1);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static <T extends Comparable<? super T>> MyLinkedList<T> mergeTwoSortedLists(MyLinkedList<T> L1, MyLinkedList<T> L2) {
    if (L1 == null) return L2;
    else if (L2 == null) return L1;

    MyLinkedList<T> merged = new MyLinkedList<>();
    MyLinkedList.Node<T> p1 = L1.head, p2 = L2.head;
    while ( p1 != null && p2 != null) {
      if ( p1.data.compareTo(p2.data) < 0 ) {
        merged.add(p1);
        p1 = p1.next;
      } else {
        merged.add(p2);
        p2 = p2.next;
      }
    }
    merged.add(p1 != null ? p1 : p2);

//    newList.head = newList.head.next;
    return merged;
  }

  private static <T extends Comparable<? super T>> void appendNode(MyLinkedList.Node<T> node, MyLinkedList.Node<T> tail) {

  }

  private static void simpleTest() {
    // Test for interlacing lists
    MyLinkedList<Integer> L1 = new MyLinkedList<>();
    L1.add(-1); L1.add(1);
    MyLinkedList<Integer> L2 = new MyLinkedList<>();
    L2.add(0); L2.add(2);
    MyLinkedList<Integer> merged = mergeTwoSortedLists(L1, L2);
    System.out.println(merged);
    MyLinkedList.Node<Integer> cursor = merged.head;
    assert(cursor.data == -1);
    cursor = cursor.next;
    assert(cursor.data == 0);
    cursor = cursor.next;
    assert(cursor.data == 1);
    cursor = cursor.next;
    assert(cursor.data == 2);
    cursor = cursor.next;
    assert(cursor == null);


    // Test for when one list is empty
    L1 = new MyLinkedList<>();
    L1.add(-1); L1.add(1);
    merged = mergeTwoSortedLists(L1, null);
    System.out.println(merged);
    cursor = merged.head;
    assert(cursor.data == -1);
    cursor = cursor.next;
    assert(cursor.data == 1);
    cursor = cursor.next;
    assert(cursor == null);
  }

  public static void main(String[] args) throws Exception {
    simpleTest();
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      List<Integer> t1 = MiscHelperMethods.randNumberArray(Integer.class, LIST_LEN);
      t1.sort(Integer::compareTo);
      List<Integer> t2 = MiscHelperMethods.randNumberArray(Integer.class, LIST_LEN);
      t2.sort(Integer::compareTo);

      MyLinkedList<Integer> L1 = new MyLinkedList<>();
      MyLinkedList<Integer> L2 = new MyLinkedList<>();
      L1.addAll(t1);
      L2.addAll(t2);

      inputs.addMyLinkedList("L1", L1); // why is this yielding an "Unchecked method invocation" warning :( !?!?
      inputs.addMyLinkedList("L2", L2);

      return inputs;
    };


    Function<CloneableInputsMap, MyLinkedList<Integer>> runAlg = (inputs) ->
            mergeTwoSortedLists(inputs.getMyLinkedList("L1", Integer.class)
                              , inputs.getMyLinkedList("L2", Integer.class));

    Function<CloneableInputsMap, MyLinkedList<Integer>> knownOutput = (inputs) -> {
      MyLinkedList<Integer> merged = inputs.getMyLinkedList("L1", Integer.class);
      merged.addAll(inputs.getMyLinkedList("L2", Integer.class));
      merged.sort(Integer::compareTo);
      return merged;
    };
    AlgVerifierInterfaces<MyLinkedList<Integer>, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(MyLinkedList::equals);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Merge two sorted lists", NUM_TESTS, formInputs, runAlg, knownOutput, algVerifier);
//    algorithmFactory.setSequential();
    algorithmFactory.run();
  }
}
