package epi.solutions.helper;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/20/16.
 *
 */
class MiscHelperMethods {

  static <T> ArrayList<T> randArray(Supplier<T> randSupplier, int len) {
    ArrayList<T> result = new ArrayList<>(len);
    for (int i = 0; i < len; ++i) {
      result.add(randSupplier.get());
    }
    return result;
  }
}
