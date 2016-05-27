package epi.solutions.helper;

import java.util.HashMap;

/**
 * Created by psingh on 5/14/16.
 * A CloneableTestInput wrapper for HashMaps.
 */
@SuppressWarnings("ALL")
public class CloneableHashMap<K, V> extends HashMap implements CloneableTestInput<CloneableHashMap<K,V>> {
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
  public HashMap<K,V> getInput() {
    return data;
  }

  @Override
  public CloneableHashMap<K,V> cloneInput() {
    return new CloneableHashMap<>(data);
  }
}
