package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableTestInput wrapper for Doubles.
 */
class CloneableDouble implements CloneableTestInput<CloneableDouble> {
  private Double data;
  CloneableDouble(Double input) { data = input; }

  @Override
  public Class getType() {
    return CloneableDouble.class;
  }

  @Override
  public Double getInput() {
    return data;
  }

  @Override
  public CloneableDouble cloneInput() { return new CloneableDouble(data); }
}
