package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 */
public class CloneableDouble implements CloneableTestInput<CloneableDouble> {
  public Double data;
  public CloneableDouble() {}
  public CloneableDouble(Double input) { data = input; }

  public CloneableDouble cloneInput() { return new CloneableDouble(Double.valueOf(data)); }
}
