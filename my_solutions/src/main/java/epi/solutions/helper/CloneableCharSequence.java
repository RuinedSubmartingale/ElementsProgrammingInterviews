package epi.solutions.helper;

import com.sun.xml.fastinfoset.util.CharArray;

/**
 * Created by psingh on 5/28/16.
 * A CloneableInput wrapper for character arrays.
 */
class CloneableCharSequence extends CloneableInput<CharSequence> {
  CloneableCharSequence(String input) { super(new CharArray(input.toCharArray(), 0, input.length(), true)); }
  CloneableCharSequence(char[] input) { super(new CharArray(input, 0, input.length, true)); }
  CloneableCharSequence(CharArray input) {
    super(input);
  }

  @Override
  public Class<? extends CloneableInput> getType() {
    return this.getClass();
  }

}
