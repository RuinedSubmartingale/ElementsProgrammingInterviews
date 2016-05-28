package epi.solutions.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psingh on 5/14/16.
 * A CloneableTestInput wrapper for ArrayLists.
 */
class CloneableArrayList<T> implements CloneableTestInput<CloneableArrayList<T>> {
  private ArrayList<T> data;
  CloneableArrayList() {}
  CloneableArrayList(List<T> input) {
    data = new ArrayList<>(input);
  }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  public ArrayList<T> getInput() {
    return data;
  }

  @Override
  public CloneableArrayList<T> cloneInput() {
    return new CloneableArrayList<>(data);
  }
}
