package epi.solutions.helper;

import java.util.*;

/**
 * Created by psingh on 5/12/16.
 * A HashMap of (name, input) that is fed to the algorithm. The inputs are required to be cloneable.
 */

public class CloneableInputsMap extends HashMap<String, CloneableInput> {
//  public CloneableInputsMap() {
//  }

  @SuppressWarnings("unchecked")
  public <DT> DT get(String name, Class<? extends CloneableInput> c, Class<? extends DT> dt) {
    CloneableInput inputContainer = this.get(name);
    try {
      Objects.requireNonNull(inputContainer, "No input called \"" + name + "\"");
      if (!c.isInstance(inputContainer))
        throw new IllegalArgumentException(name + " is not an instance of " + c.toString(), new Throwable(this.toString()));
      else {
//      return input; // This unchecked type cast should be caught by the condition above.
        if (!dt.isInstance(inputContainer.getInput()))
          throw new IllegalArgumentException(name + " is a CloneableArrayList container, but its data type is not of type " + dt.toString(), new Throwable(this.toString()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (DT) inputContainer.getInput();
  }

  public <T, E extends List<T>> void addArrayList(String name, E A) {
    this.put(name, new CloneableArrayList<>(A));
  }
  // TODO: figure out if/when the following warning suppression is needed
  @SuppressWarnings("unchecked")
  public <T> ArrayList<T> getArrayList(String name, Class<T> dataType) {
    ArrayList<T> result = get(name, CloneableArrayList.class, (new ArrayList<T>()).getClass());
    try {
      // // The following would throw a ClassCastException if result wasn't actually of type ArrayList<T>
      // result.toArray((T[]) Array.newInstance(dataType, 0));
      if(!result.get(0).getClass().isAssignableFrom(dataType)) {
        // See JumpBoardGame.main for a commented-out example of when this error would be thrown
        throw new IllegalArgumentException(name + " is a CloneableArrayList container, but its data type is "
                + result.get(0).getClass() + "...we expected" + dataType.toString(), new Throwable(this.toString()));
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    return result;
  }

  public <T extends Comparable<? super T>, V extends epi.solutions.helper.MyLinkedList<T>> void addMyLinkedList(String name, V A) {
    this.put(name, new CloneableMyLinkedList<>(A));
  }
  // TODO: figure out if/when the following warning suppression is needed
  @SuppressWarnings("unchecked")
  public <T extends Comparable<? super T>> epi.solutions.helper.MyLinkedList<T> getMyLinkedList(String name, Class<T> dataType) {
    epi.solutions.helper.MyLinkedList<T> result = get(name, CloneableMyLinkedList.class, (new epi.solutions.helper.MyLinkedList<T>()).getClass());
    try {
      // // The following would throw a ClassCastException if result wasn't actually of type MyLinkedList<T>
      // result.toArray((T[]) Array.newInstance(dataType, 0));
      if(!result.head.next.data.getClass().isAssignableFrom(dataType)) {
        // See JumpBoardGame.main for a commented-out example of when this error would be thrown
        throw new IllegalArgumentException(name + " is a CloneableMyLinkedList container, but its data type is "
                + result.head.next.data.getClass() + "...we expected" + dataType.toString(), new Throwable(this.toString()));
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    return result;
  }

//  public <T extends Comparable<? super T>, V extends Collection<T>> void addCollection(String name, Collection<T> A, Constructor<V> constructor) {
//    this.put(name, new CloneableCollection<>(A, constructor));
//  }
//  // TODO: figure out if/when the following warning suppression is needed
//  @SuppressWarnings("unchecked")
//  public <T extends Comparable<? super T>, V extends Collection<T>> Collection<T> getCollection(String name, Class<T> dataType, Constructor<V> constructor)
//          throws IllegalAccessException, InvocationTargetException, InstantiationException {
//    Collection<T> result = get(name, CloneableCollection.class, constructor.newInstance().getClass());
//    try {
//      // // The following would throw a ClassCastException if result wasn't actually of type MyLinkedList<T>
//      // result.toArray((T[]) Array.newInstance(dataType, 0));
//
//      if(!result.iterator().next().getClass().isAssignableFrom(dataType)) {
//        // See JumpBoardGame.main for a commented-out example of when this error would be thrown
//        throw new IllegalArgumentException(name + " is a CloneableMyLinkedList container, but its data type is "
//                + result.iterator().next().getClass() + "...we expected" + dataType.toString(), new Throwable(this.toString()));
//      }
//    } catch (Exception e){
//      e.printStackTrace();
//    }
//    return result;
//  }

  public void addInteger(String name, Integer x) {
    this.put(name, new CloneableInteger(x));
  }
  public Integer getInteger(String name) {
    return get(name, CloneableInteger.class, Integer.class);
  }

  public void addLong(String name, Long x) {
    this.put(name, new CloneableLong(x));
  }
  public Long getLong(String name) {
    return get(name, CloneableLong.class, Long.class);
  }

  public void addDouble(String name, Double x) {
    this.put(name, new CloneableDouble(x));
  }
  public Double getDouble(String name) {
    return get(name, CloneableDouble.class, Double.class);
  }

  public void addString(String name, String s) {
    this.put(name, new CloneableString(s));
  }
  public String getString(String name) {
    return get(name, CloneableString.class, String.class);
  }

  public void addCharSequence(String name, char[] s) {
    this.put(name, new CloneableCharSequence(s));
  }
  public CharSequence getCharSequence(String name) {
    return get(name, CloneableCharSequence.class, CharSequence.class);
  }

  public void addCharArray(String name, char[] s) {
    this.put(name, new CloneableCharArray(s));
  }
  public char[] getCharArray(String name) {
    return get(name, CloneableCharArray.class, char[].class);
  }

  public String printInputs() {
    StringBuilder sb = new StringBuilder();
    this.forEach((name, inputType) -> sb.append("\n").append(name).append(": ").append(inputType.getInput()));
    return sb.toString();
  }
}
