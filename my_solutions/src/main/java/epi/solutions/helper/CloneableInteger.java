package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Integers.
 */
class CloneableInteger extends CloneableInput<Integer> {
  private Integer data;
  CloneableInteger(Integer input) { data = input; }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  Integer getInput() {
    return this.data;
  }
}
