package epi.solutions.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by psingh on 9/22/16.
 */
public class MyLinkedList<T extends Comparable<? super T>> {
  public Node<T> head;
  public Node<T> tail;

  public MyLinkedList() { head = null; tail = null; }
  public MyLinkedList(MyLinkedList<T> list) { this(); this.addAll(list); }
  public MyLinkedList(List<T> list) { this(); this.addAll(list); }


  public boolean add(T data) {
    final Node<T> node = new Node<>(data, null);
    return this.add(node);
  }

  public boolean add(Node<T> node) {
    if (this.head == null) {
      this.head = node;
      this.tail = node;
    } else {
      this.tail.next = node;
      this.tail = node;
    }
    return true;
  }

  public boolean addAll(List<? extends T> L) {
    L.forEach(this::add);
    return true;
  }

  // Note this creates a clone of the entire input MyLinkedList. This is necessary for this function to be properly used
  // by CloneableMyLinkedList(MyLinkedList<T> input) constructor, which is in turn called by CloneableInput.cloneInput() method
  // TODO: either rename to cloneAll() or figure out a way around cloning here. The cloning should really only happen in/for said constructor
  public boolean addAll(MyLinkedList<T> L) {
    Node<T> cursor = L.head;
    while (cursor != null) {
      this.add(new Node<>(cursor));   // specifically, input is cloned here.
      cursor = cursor.next;
    }
    return true;
  }

  public void replace(List<T> newList) {
    this.head = null;
    this.tail = null;
    this.addAll(newList);
  }

  public List<T> toList() {
    List<T> result = new ArrayList<T>();
    Node<T> cursor = this.head;
    while (cursor != null) {
      result.add(cursor.data);
      cursor = cursor.next;
    }
    return result;
  }

  public void sort(Comparator<? super T> c) {
    List<T> l = this.toList();
    l.sort(c);
    this.replace(l);
  }

  public boolean equals(MyLinkedList<T> other) {
    return this.toList().equals(other.toList());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Node<T> cursor = new Node<>(this.head);
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
