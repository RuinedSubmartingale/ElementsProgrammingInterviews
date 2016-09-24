package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for ArrayLists.
 */
class CloneableMyLinkedList<T extends Comparable<? super T>> extends CloneableInput<MyLinkedList<T>> {
  CloneableMyLinkedList() { super(new MyLinkedList<>()); }
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
