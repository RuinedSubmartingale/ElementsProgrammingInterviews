package epi.solutions.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by psingh on 5/12/16.
 * A HashMap of (name, input) that is fed to the algorithm. The inputs are required to be cloneable.
 */

public class CloneableTestInputsMap extends HashMap<String, CloneableTestInput> {
  public CloneableTestInputsMap() {
  }

  public <T extends CloneableTestInput> T get(String name, Class c) throws IllegalArgumentException {

    if (!c.isInstance(this.get(name)))
      throw new IllegalArgumentException(name + " is not an instance of " + c.toString(), new Throwable(this.toString()));
    else {
      T result = (T) this.get(name); // This unchecked type cast should be caught by the condition above.
      Objects.requireNonNull(result, "No input called \"" + name + "\"");
      return result;
    }
  }

  public <T> void addArrayList(String name, List<T> A) {
    this.put(name, new CloneableArrayList<>(A));
  }
  public <T> ArrayList<T> getArrayList(String name) throws IllegalArgumentException {
    CloneableArrayList<T> result = get(name, new CloneableArrayList<T>().getType());
    return result.getInput();
  }

  public void addInteger(String name, Integer x) {
    this.put(name, new CloneableInteger(x));
  }
  public Integer getInteger(String name) {
    CloneableInteger result = get(name, CloneableInteger.class);
    return result.getInput();
  }

  public void addLong(String name, Long x) {
    this.put(name, new CloneableLong(x));
  }
  public Long getLong(String name) {
    CloneableLong result = get(name, CloneableLong.class);
    return result.getInput();
  }

  public void addDouble(String name, Double x) {
    this.put(name, new CloneableDouble(x));
  }
  public Double getDouble(String name) {
    CloneableDouble result = get(name, CloneableDouble.class);
    return result.getInput();
  }

  public void addString(String name, String s) {
    this.put(name, new CloneableString(s));
  }
  public String getString(String name) {
    CloneableString result = get(name, CloneableString.class);
    return result.getInput();
  }

  public void addCharArray(String name, char[] s) {
    this.put(name, new CloneableCharArray(s));
  }
  public char[] getCharArray(String name) {
    CloneableCharArray result = get(name, CloneableCharArray.class);
    return result.getInput();
  }

  public String printInputs() {
    StringBuilder sb = new StringBuilder();
    this.forEach((name, inputType) -> sb.append("\n" + name + ": " + inputType.getInput()));
    return sb.toString();
  }
}
