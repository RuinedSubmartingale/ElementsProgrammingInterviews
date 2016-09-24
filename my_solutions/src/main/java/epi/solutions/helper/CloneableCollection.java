package epi.solutions.helper;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * Created by psingh on 9/23/16.
 */
public class CloneableCollection<T extends Comparable<? super T>> extends CloneableInput<Collection<T>> {
  CloneableCollection(Collection<T> input, Constructor<? extends Collection<T>> constructor) {
    super(input, constructor);
  }

  CloneableCollection(Collection<T> input) {
    super(input);
  }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }
}
