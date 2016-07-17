package epi.solutions.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for ArrayLists.
 */
class CloneableArrayList<T> extends CloneableInput<ArrayList<T>> {
  private ArrayList<T> data;
  CloneableArrayList() {}
  CloneableArrayList(List<T> input) {
    data = new ArrayList<>(input);
  }
  CloneableArrayList(ArrayList<T> input) {
    data = new ArrayList<>(input);
  }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  ArrayList<T> getInput() {
    return this.data;
  }
}
