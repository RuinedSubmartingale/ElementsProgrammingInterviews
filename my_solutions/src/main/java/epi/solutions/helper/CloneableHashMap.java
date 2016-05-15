package epi.solutions.helper;

import java.util.HashMap;

/**
 * Created by psingh on 5/14/16.
 */
public class CloneableHashMap extends HashMap implements CloneableTestInput<CloneableHashMap> {
  public CloneableHashMap() {}
  public CloneableHashMap(HashMap input) {this.putAll(input);}

  @Override
  public CloneableHashMap cloneInput() { return new CloneableHashMap(this); }
}
