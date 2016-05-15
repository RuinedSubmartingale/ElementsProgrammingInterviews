package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 */
public class CloneableInteger implements CloneableTestInput<CloneableInteger> {
  public Integer data;
  public CloneableInteger() {}
  public CloneableInteger(Integer input) { data = input; }

  public CloneableInteger cloneInput() { return new CloneableInteger(Integer.valueOf(data)); }
}
