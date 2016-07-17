package epi.solutions.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for ArrayLists.
 */
class CloneableArrayList<T> extends CloneableInput<ArrayList<T>> {
  CloneableArrayList() { super(new ArrayList<>()); }
  CloneableArrayList(List<T> input) {
    super(new ArrayList<>(input));
  }
  CloneableArrayList(ArrayList<T> input) {
    super(new ArrayList<>(input));
  }

  @Override
  public Class getType() {
    return this.getClass();
  }
}
