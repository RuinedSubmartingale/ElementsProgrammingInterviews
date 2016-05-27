package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableTestInput wrapper for Strings.
 */
class CloneableString implements CloneableTestInput<CloneableString> {
  private String data;

  CloneableString(String input) { data = input; }

  @Override
  public Class getType() {
    return CloneableString.class;
  }

  @Override
  public String getInput() {
    return data;
  }

  @Override
  public CloneableString cloneInput() { return new CloneableString(String.valueOf(data)); }
}
