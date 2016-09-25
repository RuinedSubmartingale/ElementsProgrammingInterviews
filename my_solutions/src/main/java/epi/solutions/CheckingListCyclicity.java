package epi.solutions;

import epi.solutions.helper.*;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/24/16.
 * Problem 8.5
 */
public class CheckingListCyclicity {


  private static final int LIST_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /**
   * O(n) time + O(1) space
   * Uses a pair of iterators to traverse list at different speeds.
   * More succinct solution than hasCycle2() that doesn't explicitly count the length of the cycle.
   *
   * @Strategy
   * Let F = # nodes from the head of the list till the start of the cycle.
   * Let C = # nodes in the cycle.
   * Suppose `slow` and `fast` meet at some position 0 <= i < C in the cycle.
   * Then we have the following equation (1) for some j >= 0, k >= 0:
   *     (1)        2*(F + i + jC) = F + i + jC + kC
   * where the LHS represents `slow` having completed j cycles before the meet,
   * and the RHS represents `fast` having completed k more cycles than `slow` before the meet.
   * If `slow` then restarts at `head`, it'll take F steps to reach position 0 of the cycle.
   * If `fast` moves F steps from their original meeting point, then it'll be at some position P
   *                P = [ F + (i + jC + kC)] % C
   * in the cycle. Well equation (1) also tells us that
   *    (2)         kC = 2*(F + i + jC) - (F + i + jC) = F + i
   * So C divides (F + i), and so C divides P. Hence, `fast` will meet `slow` at position `0` in the cycle
   * after the F steps. Since they couldn't possibly meet before `slow` takes F steps and reaches the cycle,
   * the first node of the cycle will be the first time they meet again. So we don't need to know the value
   * of F beforehand, instead we can just wait for them to meet again.
   *
   * @param list SLL whose cyclicity is to be determined
   * @return first node in the cycle, or nullptr if no cycle exists
   */
  // consider changing parameter from (MLL) list to (MLL.Node) head
  private static <T extends Comparable<? super T>> MyLinkedList.Node<T> hasCycle1(MyLinkedList<T> list) {
    MyLinkedList.Node<T> slow = list.head;
    MyLinkedList.Node<T> fast = slow;
    while (fast != null && fast.next != null && fast.next.next != null) {
      slow = slow.next;
      fast = fast.next.next;
      if (slow == fast) {
        slow = list.head;
        while (slow != fast) {
          slow = slow.next;
          fast = fast.next;
        }
        return slow;
      }
    }
    return null;
  }


  /**
   * O(n) time + O(1) space
   * Uses a pair of iterators to traverse list at different speeds.
   * Explicily counts length of the cycle.
   * @param list SLL whose cyclicity is to be determined
   * @return first node in the cycle, or nullptr if no cycle exists
   */
  // consider changing parameter from (MLL) list to (MLL.Node) head
  private static <T extends Comparable<? super T>> MyLinkedList.Node<T> hasCycle2(MyLinkedList<T> list) {
    MyLinkedList.Node<T> slow = list.head;
    MyLinkedList.Node<T> fast = slow;
    while(fast != null && fast.next != null && fast.next.next != null) {
      slow = slow.next;
      fast = fast.next.next;
      if (slow == fast) {
        int cycleLen = 0;
        do {
          ++cycleLen;
          fast = fast.next;
        } while (slow != fast); // measures cycle length
        MyLinkedList.Node<T> iter1 = list.head;
        while(cycleLen-- > 0) {
          iter1 = iter1.next;
        }
        MyLinkedList.Node<T> iter2 = list.head;
        // iter1 and iter2 are exactly cycleLen nodes apart. so they will meet exactly when iter2 enters the cycle.
        while (iter1 != iter2) {
          iter1 = iter1.next;
          iter2 = iter2.next;
        }
        return iter2;
      }
    }
    return null;
  }

  /**
   * O(n) time + O(n) space
   * Straight-forward approach of caching the nodes in a hash as we traverse the list. If we re-encounter a hashed node,
   * then it's the first node of the cycle. If we run out of nodes before such an event, then there's no cycle.
   * @param list SLL whose cyclicity is to be determined
   * @return first node in the cycle, or nullptr if no cycle exists
   */
  private static <T extends Comparable<? super T>> MyLinkedList.Node<T> hasCycleSimple(MyLinkedList<T> list) {
    LinkedHashSet<MyLinkedList.Node<T>> visitedNodes = new LinkedHashSet<>();
    MyLinkedList.Node<T> cursor = list.head;
    while (cursor != null && !visitedNodes.contains(cursor)) { // TODO: Ask codereview.stackexchange if this is a good (or even valid) approach
      visitedNodes.add(cursor);
      cursor = cursor.next;
    }
    if (cursor == null) return null;
    else return cursor;
  }

  private static void simpleTest() {
    MyLinkedList<Integer> L = new MyLinkedList<>();
    L.add(1); L.add(2); L.add(3); L.add(4);
    assert(hasCycle1(L) == null);
    assert(hasCycle2(L) == null);

    MyLinkedList.Node<Integer> node;
    L.tail.next = L.head.next;
    node = hasCycle1(L);
    assert(node != null && node.data == 2);
    node = hasCycle2(L);
    assert(node != null && node.data == 2);
    L.tail.next = L.head;
    node = hasCycle1(L);
    assert(node != null && node.data == 1);
    node = hasCycle2(L);
    assert(node != null && node.data == 1);
  }

  public static void main(String[] args) throws Exception {
//    simpleTest();
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      MyLinkedList<Integer> L = new MyLinkedList<Integer>(MiscHelperMethods.randArray(rgen::nextInt, LIST_LEN));
      if(rgen.nextInt(2) > 0) {
        int F = rgen.nextInt(LIST_LEN);
        MyLinkedList.Node<Integer> cursor = L.head;
        for (int i = 0; i < F; ++i) { cursor = cursor.next; }
        L.tail.next = cursor;
      }
      inputs.addMyLinkedList("L", L);
      return inputs;
    };
    Function<CloneableInputsMap, MyLinkedList.Node<Integer>> runAlg1 = (inputs) ->
            hasCycle1(inputs.getMyLinkedList("L", Integer.class));
    Function<CloneableInputsMap, MyLinkedList.Node<Integer>> runAlg2 = (inputs) ->
            hasCycle2(inputs.getMyLinkedList("L", Integer.class));
    Function<CloneableInputsMap, MyLinkedList.Node<Integer>> knownSolution = (inputs) ->
      hasCycleSimple(inputs.getMyLinkedList("L", Integer.class));
    AlgVerifierInterfaces<MyLinkedList.Node<Integer>, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(
            (expected, observed) ->
                    (expected == null && observed == null) ||
                    (expected != null && observed != null && expected.equals(observed))
    );
    AlgorithmFactory algorithmFactory1 = new AlgorithmRunnerAndVerifier<>("CheckingListCyclicity (explicitly counting cycle length) of SLLs (N = " + LIST_LEN + ")"
            , NUM_TESTS, formInputs, runAlg1, knownSolution, algVerifier);
    AlgorithmFactory algorithmFactory2 = new AlgorithmRunnerAndVerifier<>("CheckingListCyclicity (succinctly) of SLLs (N = " + LIST_LEN + ")"
            , NUM_TESTS, formInputs, runAlg2, knownSolution, algVerifier);
    // TODO: It would be much much much better (for comparison) if we ran both algorithms on the same set of random inputs.
//    algorithmFactory1.setSequential();
    algorithmFactory1.run();
//    algorithmFactory2.setSequential();
    algorithmFactory2.run();

  }
}
