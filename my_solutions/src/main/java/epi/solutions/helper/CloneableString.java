package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Strings.
 */
class CloneableString extends CloneableInput<String> {
  private String data;

  CloneableString(String input) { data = input; }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  String getInput() {
    return this.data;
  }
}
