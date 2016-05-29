package epi.solutions.helper;

/**
 * Created by psingh on 5/28/16.
 * A CloneableTestInput wrapper for character arrays.
 */
class CloneableCharArray implements CloneableTestInput<CloneableCharArray> {
  private char[] data;

  CloneableCharArray(String input) { data = input.toCharArray(); }
  CloneableCharArray(char[] input) { data = input.clone(); }

  @Override
  public Class getType() {
    return CloneableCharArray.class;
  }

  @Override
  public char[] getInput() {
    return data;
  }

  @Override
  public CloneableCharArray cloneInput() { return new CloneableCharArray(data); }
}
