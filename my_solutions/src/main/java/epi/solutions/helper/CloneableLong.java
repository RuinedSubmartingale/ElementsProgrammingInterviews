package epi.solutions.helper;

/**
 * Created by psingh on 5/17/16.
 * A CloneableTestInput wrapper for Longs.
 */
public class CloneableLong implements CloneableTestInput<CloneableLong> {
  private Long data;
  CloneableLong(long input) { data = input; }

  @Override
  public Class getType() {
    return CloneableLong.class;
  }

  @Override
  public Long getInput() {
    return data;
  }

  @Override
  public CloneableLong cloneInput() { return new CloneableLong(data); }
}
