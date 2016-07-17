package epi.solutions.helper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by psingh on 5/14/16.
 * An interface for different input types for an algorithm. The inputs need to be able to be cloned
 * so that TimeTests can verify correctness of algorithm by verifying the results of the algorithm
 * with the results of a known solution on a cloned version of the inputs.
 * See TimeTests for usage details.
 */
public abstract class CloneableInput<T> implements Cloneable {
  private T data;

  public CloneableInput(T data) {
    this.data = data;
  }
  abstract Class getType();
  T getInput() { return this.data; }
  CloneableInput<T> cloneInput() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    return (CloneableInput<T>) getType().getDeclaredConstructor(getInput().getClass()).newInstance(getInput());
  };
}
