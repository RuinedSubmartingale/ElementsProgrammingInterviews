package epi.solutions.helper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by psingh on 5/14/16.
 * An interface for different input types for an algorithm. The inputs need to be able to be cloned
 * so that TimeTests can verify correctness of algorithm by verifying the results of the algorithm
 * with the results of a known solution on a cloned version of the inputs.
 * See TimeTests for usage details.
 */
abstract class CloneableInput<T> implements Cloneable {
  private T data;

  CloneableInput(T data) {
    this.data = data;
  }
  abstract Class<? extends CloneableInput> getType();
  T getInput() { return this.data; }

  @SuppressWarnings("unchecked")
  CloneableInput<T> cloneInput() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    try {
      return (CloneableInput<T>) getType().getDeclaredConstructor(getInput().getClass()).newInstance(getInput());
    } catch (InstantiationException | SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
      throw e;
    }
  }
}
