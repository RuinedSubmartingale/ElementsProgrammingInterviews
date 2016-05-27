package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableTestInput wrapper for Integers.
 */
class CloneableInteger implements CloneableTestInput<CloneableInteger> {
  private Integer data;
  CloneableInteger(Integer input) { data = input; }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  public Integer getInput() {
    return data;
  }

  @Override
  public CloneableInteger cloneInput() { return new CloneableInteger(data); }
}
