package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Strings.
 */
class CloneableString extends CloneableInput<String> {
  CloneableString(String input) { super(input); }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }
}
