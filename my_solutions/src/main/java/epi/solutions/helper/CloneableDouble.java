package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for Doubles.
 */
class CloneableDouble extends CloneableInput<Double> {
  CloneableDouble(Double input) { super(input); }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }

}
