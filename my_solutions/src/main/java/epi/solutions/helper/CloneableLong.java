package epi.solutions.helper;

/**
 * Created by psingh on 5/17/16.
 */
public class CloneableLong implements CloneableTestInput<CloneableLong> {
  public long data;
  public CloneableLong() {}
  public CloneableLong(long input) { data = input; }

  public CloneableLong cloneInput() { return new CloneableLong(Long.valueOf(data)); }
}
