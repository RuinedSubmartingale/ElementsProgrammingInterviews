package epi.solutions.helper;

/**
 * Created by psingh on 5/28/16.
 * A CloneableInput wrapper for character arrays.
 */
class CloneableCharArray extends CloneableInput<char[]> {
  CloneableCharArray(String input) { super(input.toCharArray()); }
  CloneableCharArray(char[] input) { super(input.clone()); }

  @Override
  public Class getType() {
    return this.getClass();
  }
}
