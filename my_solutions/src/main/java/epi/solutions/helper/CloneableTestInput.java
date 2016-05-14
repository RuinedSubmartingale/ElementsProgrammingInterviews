package epi.solutions.helper;

/**
 * Created by psingh on 5/14/16.
 */
public interface CloneableTestInput<T extends CloneableTestInput<T>> extends Cloneable {
  T clone();
}
