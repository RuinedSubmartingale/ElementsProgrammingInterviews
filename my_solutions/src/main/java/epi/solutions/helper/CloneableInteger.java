package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Integers.
 */
class CloneableInteger extends CloneableInput<Integer> {
  CloneableInteger(Integer input) { super(input); }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }

}
