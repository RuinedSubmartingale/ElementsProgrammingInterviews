package epi.solutions.helper;

import java.util.HashMap;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for HashMaps.
 */
@SuppressWarnings("ALL")
public class CloneableHashMap<K, V> extends CloneableInput<HashMap<K,V>> {
  CloneableHashMap() { super(new HashMap<>()); }
  CloneableHashMap(HashMap<K, V> input) {
    super(new HashMap<>(input));
  }

  @Override
  public Class getType() {
    return this.getClass();
  }
}
