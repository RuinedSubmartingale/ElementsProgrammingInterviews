package epi.solutions.helper;

/**
 * Created by psingh on 5/28/16.
 * A CloneableInput wrapper for character arrays.
 */
class CloneableCharArray extends CloneableInput<char[]> {
  private char[] data;

  CloneableCharArray(String input) { data = input.toCharArray(); }
  CloneableCharArray(char[] input) { data = input.clone(); }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  public char[] getInput() {
    return this.data;
  }
}
