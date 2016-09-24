package epi.solutions;

import epi.solutions.helper.*;

import java.util.List;
import java.util.Random;
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

    MyLinkedList<T> dummyHead = new MyLinkedList<>();
    MyLinkedList.Node<T> tail = dummyHead.head;
    MyLinkedList.Node<T> p1 = L1.head, p2 = L2.head;
    while ( p1.next != null && p2.next != null) {
      if ( p1.next.data.compareTo(p2.next.data) < 0 ) {
        tail.next = p1.next;
        p1.next = p1.next.next;
      } else {
        tail.next = p2.next;
        p2.next = p2.next.next;
      }
      tail = tail.next;
    }
    tail.next = p1.next != null ? p1.next : p2.next;

//    dummyHead.head = dummyHead.head.next;
    return dummyHead;
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
    merged.print();
    MyLinkedList.Node<Integer> cursor = merged.head.next;
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
    merged.print();
    cursor = merged.head.next;
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
      Random rgen = new Random();
      List<Integer> t1 = MiscHelperMethods.randArray(rgen::nextInt, LIST_LEN);
      t1.sort(Integer::compareTo);
      List<Integer> t2 = MiscHelperMethods.randArray(rgen::nextInt, LIST_LEN);
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
    AlgVerifierInterfaces<MyLinkedList<Integer>, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(MyLinkedList<Integer>::equals);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Merge two sorted lists", NUM_TESTS, formInputs, runAlg, knownOutput, algVerifier);
//    algorithmFactory.setSequential();
    algorithmFactory.run();
  }
}
