package epi.solutions;

import epi.solutions.helper.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/24/16.
 * Problem 8.2
 */
public class ReverseLinkedList {

  private static final int LIST_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /**
   * O(n) time + O(n) space
   * Recursive solution to reversing a singly linked list
   * Note that this overwrites the list (hence why its return type is void and not MyLinkedList)
   * @param list singly-linked list to be reversed
   */
  private static <T extends Comparable<? super T>> void reverseRecursively(MyLinkedList<T> list) {
    list.tail = list.head;
    list.head = reverseRecursivelyHelper(list.head);
  }

  // In contrast, this helper method returns the newHead of the list after reversing
  private static <T extends Comparable<? super T>> MyLinkedList.Node<T> reverseRecursivelyHelper(MyLinkedList.Node<T> head) {
    if (head == null || head.next == null)
      return head;
    MyLinkedList.Node<T> newHead = reverseRecursivelyHelper(head.next);
    head.next.next = head;  //
    head.next = null;       // old head becomes new tail
    return newHead;
  }

  /**
   * O(n) time + O(1) space
   * Iterative solution to reversing a singly linked list
   * @param list singly-linked list to be reversed
   */
  private static <T extends Comparable<? super T>> void reverseIteratively(MyLinkedList<T> list) {
    list.tail = list.head;
    MyLinkedList.Node<T> prev = null, curr = list.head, next = null;
    while (curr != null) {
      next = curr.next;
      curr.next = prev;
      prev = curr;
      curr = next;
    }
    list.head = prev;
  }

  private static void simpleTest() {
    MyLinkedList<Integer> L = new MyLinkedList<>();
    L.add(0); L.add(1); L.add(2);
    reverseRecursively(L);
    MyLinkedList.Node<Integer> cursor = L.head;
    assert(cursor.data == 2);
    cursor = cursor.next;
    assert(cursor.data == 1);
    cursor = cursor.next;
    assert(cursor.data == 0);
    assert(cursor.next == null);

    reverseIteratively(L);
    cursor = L.head;
    assert(cursor.data == 0);
    cursor = cursor.next;
    assert(cursor.data == 1);
    cursor = cursor.next;
    assert(cursor.data == 2);
    assert(cursor.next == null);
  }

  public static void main(String[] args) throws Exception {
    simpleTest();

    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      // TODO: find a way to abstract out rgen into MiscHelperMethods so as to not have to repeat this code for each algorithm
      Random rgen = new Random();
      MyLinkedList<Integer> L = new MyLinkedList<Integer>(MiscHelperMethods.randArray(rgen::nextInt, LIST_LEN));
      inputs.addMyLinkedList("l", L);
      return inputs;
    };
    Function<CloneableInputsMap, MyLinkedList<Integer>> runRecursiveAlg= (inputs) -> {
      MyLinkedList<Integer> L = inputs.getMyLinkedList("l", Integer.class);
      reverseRecursively(L);
      return L;
    };
    Function<CloneableInputsMap, MyLinkedList<Integer>> runIterativeAlg = (inputs) -> {
      MyLinkedList<Integer> L = inputs.getMyLinkedList("l", Integer.class);
      reverseIteratively(L);
      return L;
    };
    Function<CloneableInputsMap, MyLinkedList<Integer>> knownSolution = (inputs) -> {
      MyLinkedList<Integer> L = inputs.getMyLinkedList("l", Integer.class);
      List<Integer> tmp = L.toList();
      Collections.reverse(tmp);
      L.replace(tmp);
      return L;
    };
    AlgVerifierInterfaces<MyLinkedList<Integer>, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(MyLinkedList::equals);
    AlgorithmFactory recursiveAlgFactory = new AlgorithmRunnerAndVerifier<>("Reverse SLL of length " + String.valueOf(LIST_LEN) + " recursively in O(n) time + O(n) space"
            , NUM_TESTS, formInputs, runRecursiveAlg, knownSolution, algVerifier);
    AlgorithmFactory iterativeAlgFactory = new AlgorithmRunnerAndVerifier<>("Reverse SLL of length " + String.valueOf(LIST_LEN) + " iteratively in O(n) time + O(1) space"
            , NUM_TESTS, formInputs, runIterativeAlg, knownSolution, algVerifier);

    recursiveAlgFactory.run();
    iterativeAlgFactory.run();
  }
}
