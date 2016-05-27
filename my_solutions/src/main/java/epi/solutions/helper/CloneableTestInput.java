package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 * An interface for different input types for an algorithm. The inputs need to be able to be cloned
 * so that TimeTests can verify correctness of algorithm by verifying the results of the algorithm
 * with the results of a known solution on a cloned version of the inputs.
 * See TimeTests for usage details.
 */
public interface CloneableTestInput<T extends CloneableTestInput<T>> extends Cloneable {
  Class getType();
  Object getInput();
  T cloneInput();
}
