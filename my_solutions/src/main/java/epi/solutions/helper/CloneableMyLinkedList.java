package epi.solutions.helper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for ArrayLists.
 */
class CloneableMyLinkedList<T extends Comparable<? super T>> extends CloneableInput<LinkedList<T>> {
  CloneableMyLinkedList() { super(new LinkedList<>()); }
  CloneableMyLinkedList(List<T> input) {
    super(new LinkedList<>(input));
  }
  CloneableMyLinkedList(MyLinkedList<T> input) {
    super(new MyLinkedList<>(input));
  }
//  CloneableMyLinkedList(LinkedList<T> input) {
//    super(new MyLinkedList<>(input));
//  }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }
}
