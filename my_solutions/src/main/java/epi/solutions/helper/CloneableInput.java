package epi.solutions.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by psingh on 5/14/16.
 * An interface for different input types for an algorithm. The inputs need to be able to be cloned
 * so that TimeTests can verify correctness of algorithm by verifying the results of the algorithm
 * with the results of a known solution on a cloned version of the inputs.
 * See TimeTests for usage details.
 */
abstract class CloneableInput<T> {
  private T data;

  CloneableInput(T data) {
    this.data = data;
  }
  CloneableInput(T data, Constructor<? extends T> constructor) {
    try {
      this.data = constructor.newInstance(data);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }
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
