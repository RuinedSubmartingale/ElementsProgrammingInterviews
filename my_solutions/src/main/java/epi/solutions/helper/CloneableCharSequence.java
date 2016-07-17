package epi.solutions.helper;

import com.sun.xml.internal.fastinfoset.util.CharArray;

/**
 * Created by psingh on 5/28/16.
 * A CloneableInput wrapper for character arrays.
 */
class CloneableCharSequence extends CloneableInput<CharSequence> {
  private CharSequence data;

  CloneableCharSequence(String input) { data = new CharArray(input.toCharArray(), 0, input.length(), true); }
  CloneableCharSequence(char[] input) { data = new CharArray(input, 0, input.length, true); }
  CloneableCharSequence(CharArray input) {
    this(input.toString());
  }



  @Override
  public Class getType() {
    return this.getClass();
  }

  @Override
  CharSequence getInput() {
    return this.data;
  }
}
