package epi.solutions;

import epi.solutions.helper.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/24/16.
 * Problem 8.3
 */
public class ReverseSublist {
  private static final int LIST_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /**
   * @Problem
   * Given a SLL and two integers s and f, reverse the order of the nodes from the s-th node to the f-th node, both inclusive.
   * The nodes in the list are numbered starting with 1, i.e. the head node is the first node.
   * Perform the reversal in a single pass. Do not allocate additional nodes.
   * Assume that the list has at least f nodes.
   *
   * @Strategy
   * We can use solution 8.2 ReverseLinkedList; however, it requires 2 passes over the sublist.
   * We can solve it in one pass by using an iteration to get node `s` and its predecessor.
   * Once we reach `s`, we start reversing until we reach node `f`, and then we link the
   * reverted portion with the unreverted portions of the list.
   */
  private static <T extends Comparable<? super T>> void reverseSublist(MyLinkedList<T> list, int s, int f) {
    if (s == f) return; // no need to reverse in this case
    MyLinkedList.Node<T> preHead = new MyLinkedList.Node<>(null, list.head); // new node before head of list
    MyLinkedList.Node<T> sublistPred = preHead; // gonna make sublistPred point to the (s-1)-th node of the list
    int k = 0;
    while (++k < s) { sublistPred = sublistPred.next; }
    // sublistIter always points to the original s-th node, but each iteration of the while loop increments its new index by 1
    // i.e. after j iterations, the original s-th node is now the (s+j)-th node
    MyLinkedList.Node<T> sublistIter = sublistPred.next;
    while (k++ < f) {
      MyLinkedList.Node<T> tmp = sublistIter.next;
      sublistIter.next = tmp.next;
      if (sublistIter.next == null) list.tail = sublistIter; // k = f - 1, and the f-th node used to be the lists' tail
      tmp.next = sublistPred.next;
      sublistPred.next = tmp;
    }
    list.head = preHead.next; // draw out a few iterations to see this is right.
  }

  private static void simpleTest() {
    MyLinkedList<Integer> L = new MyLinkedList<>();
    L.add(1); L.add(2); L.add(3); L.add(4);
    System.out.println(L);
    reverseSublist(L, 2, 3);
    System.out.println(L);
    reverseSublist(L, 2, 3);
    System.out.println(L);
    reverseSublist(L, 2, 4);
    System.out.println(L);
    reverseSublist(L, 2, 4);
    System.out.println(L);
    reverseSublist(L, 1, 3);
    System.out.println(L);
    reverseSublist(L, 1, 3);
    System.out.println(L);
    reverseSublist(L, 1, 4);
    System.out.println(L);
  }
  public static void main(String[] args) throws Exception {
//    simpleTest();
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      MyLinkedList<Integer> L = new MyLinkedList<Integer>(MiscHelperMethods.randArray(rgen::nextInt, LIST_LEN));
      inputs.addMyLinkedList("L", L);
      int f = rgen.nextInt(LIST_LEN) + 1;
      inputs.addInteger("f", f);
      inputs.addInteger("s", rgen.nextInt(f) + 1);
      return inputs;
    };
    Function<CloneableInputsMap, MyLinkedList<Integer>> runAlg = (inputs) -> {
      MyLinkedList<Integer> L = inputs.getMyLinkedList("L", Integer.class);
      reverseSublist(L, inputs.getInteger("s"), inputs.getInteger("f"));
      return L;
    };
    Function<CloneableInputsMap, MyLinkedList<Integer>> knownSolution = (inputs) -> {
      MyLinkedList<Integer> L = inputs.getMyLinkedList("L", Integer.class);
      List<Integer> A = L.toList();
      int s = inputs.getInteger("s");
      int f = inputs.getInteger("f");
      Collections.reverse(A.subList(s-1, f));
      L.replace(A);
      return L;
    };
    AlgVerifierInterfaces<MyLinkedList<Integer>, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(MyLinkedList::equals);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Reverse sublist (s,f) of SLL of length " + String.valueOf(LIST_LEN) + " in O(f) time"
            , NUM_TESTS, formInputs, runAlg, knownSolution, algVerifier);
    algorithmFactory.run();
  }
}
