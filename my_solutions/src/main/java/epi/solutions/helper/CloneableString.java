package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 */
public class CloneableString implements CloneableTestInput<CloneableString> {
  public String data;
  public CloneableString() {}
  public CloneableString(String input) { data = input; }

  public CloneableString clone() { return new CloneableString(String.valueOf(data)); }
}
