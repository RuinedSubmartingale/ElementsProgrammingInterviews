package epi.solutions.helper;

import java.util.HashMap;

/**
 * Created by psingh on 5/14/16.
 * A CloneableInput wrapper for HashMaps.
 */
@SuppressWarnings("ALL")
public class CloneableHashMap<K, V> extends CloneableInput<HashMap<K,V>> {
  private HashMap<K, V> data;

  CloneableHashMap() {}
  CloneableHashMap(HashMap<K, V> input) {
    data = new HashMap<>(input);
  }

  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  HashMap<K, V> getInput() {
    return this.data;
  }
}
