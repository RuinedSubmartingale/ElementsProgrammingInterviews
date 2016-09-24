package epi.solutions.helper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by psingh on 9/22/16.
 */
public class MyLinkedList<T extends Comparable<? super T>> extends LinkedList<T> {
  public Node<T> head;
//  public NodePtr<T> tail;

  public MyLinkedList() { head = new Node<>(null, null); }
//  public MyLinkedList(Iterable<? extends T> listNode) { this(); this.addAll(listNode); } // what happens if you remove "this();" ?
  public MyLinkedList(MyLinkedList<T> list) { this(); this.addAll(list); }
  public MyLinkedList(LinkedList<T> list) { this(); this.addAll(list); }


//  @Override
  public boolean add(T data) {
    final Node<T> node = new Node<>(data, null);
    Node<T> cursor = this.head;
    while(cursor.next != null) { cursor = cursor.next; }
    cursor.next = node;
    return true;
  }

  public boolean addAll(List<? extends T> L) {
    L.forEach(this::add);
    return true;
  }

  // Note this creates a clone of the entire input MyLinkedList. This is necessary for this function to be properly used
  // by CloneableMyLinkedList(MyLinkedList<T> input) constructor, which is in turn called by CloneableInput.cloneInput() method
  public boolean addAll(MyLinkedList<T> L) {
    Node<T> thisCursor = this.head;
    while (thisCursor.next != null) thisCursor = thisCursor.next;

    Node<T> otherCursor = L.head;
    while (otherCursor.next != null) {
      thisCursor.next = new Node<>(otherCursor.next);   // specifically, input is cloned here.
      thisCursor = thisCursor.next;
      otherCursor = otherCursor.next;
    }

    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Node<T> cursor = new Node<>(head.next);
    while (cursor != null) {
      sb.append(String.valueOf(cursor.data)).append(" -> ");
      cursor = cursor.next;
    }
    sb.append("null");
    return sb.toString();
  }

  public static class Node<E extends Comparable<? super E>> {
    public E data;
    public Node<E> next;

    Node(E data) { this.data = data; this.next = null; }
    Node(Node<E> node) { this.data = node.data; this.next = node.next; }
    public Node(E data, Node<E> next) { this.data = data; this.next = next; }

//    void set(Node<E> node) {
//      this.data = node.data;
//      this.next = node.next;
//    }
  }

}
