package epi.solutions.helper;

/**
 * Created by psingh on 5/17/16.
 * A CloneableInput wrapper for Longs.
 */
public class CloneableLong extends CloneableInput<Long> {
  private Long data;
  CloneableLong(Long input) { data = input; }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  Long getInput() {
    return this.data;
  }
}
