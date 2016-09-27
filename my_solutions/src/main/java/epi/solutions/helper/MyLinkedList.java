package epi.solutions.helper;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.*;

/**
 * Created by psingh on 9/22/16.
 */
public class MyLinkedList<T extends Comparable<? super T>> implements Iterable<T> {
  public Node<T> head;
  public Node<T> tail;

  public MyLinkedList() { head = null; tail = null; }
  public MyLinkedList(MyLinkedList<T> list) { this(); this.addAll(list); } // called by CloneableInput.cloneInput()
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
    L.forEach(this::add); // input is cloned by this.add() here.
    return true;
  }

  // Note this creates a clone of the entire input MyLinkedList. This is necessary for this function to be properly used
  // by CloneableMyLinkedList(MyLinkedList<T> input) constructor, which is in turn called by CloneableInput.cloneInput() method
  // TODO: either rename to cloneAll() or figure out a way around cloning here. The cloning should really only happen in/for said constructor
  // Note that this gets stuck in the loop if the list has a cycle.
  public boolean addAll(MyLinkedList<T> L) {
    L.forEach(this::add); // input is cloned by this.add() here.
    return true;
  }

  public void replace(List<T> newList) {
    this.head = null;
    this.tail = null;
    this.addAll(newList);
  }

  // Note that this gets stuck in the loop if the list has a cycle.
  public List<T> toList() {
    List<T> result = new ArrayList<>();
    this.forEach(result::add);
    return result;
  }

  public void sort(Comparator<? super T> c) {
    List<T> l = this.toList();
    l.sort(c);
    this.replace(l);
  }

  public int length() {
    int length = 0;
    for (T elem : this) { length += 1; }
    return length;
  }

  // TODO: think about using Node's hashCode/equals relations to determine whether the entire lists are equivalent
  public boolean equals(MyLinkedList<T> other) {
    return this.toList().equals(other.toList());
  }


  @Override
  // Note that this gets stuck in the loop if the list has a cycle.
  public String toString() {
    StringBuilder sb = new StringBuilder();
    this.forEach((elem) -> sb.append(String.valueOf(elem)).append(" -> "));
    sb.append("null");
    return sb.toString();
  }

  @Override
  public Iterator<T> iterator() {
    return new Itr<>(this.head);
  }


  // TODO: Iterable interface says that the default implementation should usually be overridden. Figure out if and how you should.
  //  @Override
  //  public Spliterator<T> spliterator() {
  //    return null;
  //  }

  public static class Node<E extends Comparable<? super E>> {
    public E data;
    public Node<E> next;

    Node(E data) { this.data = data; this.next = null; }
    Node(Node<E> node) { this.data = node.data; this.next = node.next; }
    public Node(E data, Node<E> next) { this.data = data; this.next = next; }

//    @Override
//    public int hashCode() {
//      HashCodeBuilder hb = new HashCodeBuilder(41, 59); // 2 randomly chosen primes
//      return hb.append(data).toHashCode();
//      // can't add on .append(next), since that will result in infinite loop for cyclic lists
//    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof Node)) return false;
      if (this == obj) return true;
      Node that = (Node) obj;
      return new EqualsBuilder().append(this.data, that.data).append(this.next, that.next).isEquals();
    }

//    void set(Node<E> node) {
//      this.data = node.data;
//      this.next = node.next;
//    }
  }

  private static class Itr<E extends Comparable<? super E>> implements Iterator<E> {
    private Node<E> nextNode;
    private LinkedHashSet<Node<E>> visitedNodes;
    // TODO: Ask codereview.stackexchange if the above hash is a good (or even valid) approach to deal with cyclic lists.
    // Also consider the trade-offs: hasNext() returns false once cycle has been all covered. do we always want this?

    Itr(Node<E> head) {
      this.nextNode = head;
      this.visitedNodes = new LinkedHashSet<>();
    }

    @Override
    public boolean hasNext() {
      return (nextNode != null && !visitedNodes.contains(nextNode));
    }

    @Override
    public E next() {
      if (!this.hasNext()) throw new NoSuchElementException();
      E result = nextNode.data;
      visitedNodes.add(nextNode);
      nextNode = nextNode.next;
      return result;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
