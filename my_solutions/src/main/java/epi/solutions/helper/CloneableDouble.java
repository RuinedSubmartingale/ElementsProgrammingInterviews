package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Doubles.
 */
class CloneableDouble extends CloneableInput<Double> {
  private Double data;
  CloneableDouble(Double input) { data = input; }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  Double getInput() {
    return this.data;
  }
}
