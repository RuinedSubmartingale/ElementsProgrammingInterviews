package epi.solutions.helper;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/20/16.
 *
 */
public class MiscHelperMethods {

  public static <T> ArrayList<T> randArray(Supplier<T> randSupplier, int len) {
    ArrayList<T> result = new ArrayList<>(len);
    for (int i = 0; i < len; ++i) {
      result.add(randSupplier.get());
    }
    return result;
  }

  public static PrintStream setSystemOutToDummyStream() {
    PrintStream originalStream = System.out;
    PrintStream dummyStream  = new PrintStream(new OutputStream(){
      @Override
      public void write(int b) {
        //NO-OP
      }
    });
    System.setOut(dummyStream);
    return originalStream;
  }

  // TODO: random character array (w. options for range of char values) / random string
  public static char[] randCharArray(Supplier<Character> randSupplier, int len) {
    char[] result = new char[len];
    for (int i = 0; i < len; ++i) {
      result[i] = randSupplier.get();
    }
    return result;
  }

  public static void randCharArray(Supplier<Character> randSupplier, int len, char[] result) {
    Preconditions.checkArgument(result.length >= len);
    for (int i = 0; i < len; ++i) {
      result[i] = randSupplier.get();
    }
  }

  public static String randString(Supplier<Character> randSupplier, int len) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; ++i) {
      sb.append(randSupplier.get());
    }
    return sb.toString();
  }


  public enum CharTypes { PRINTABLE, LOWERCASE_ALPHABETIC, UPPERCASE_ALPHABETIC, NUMERIC };
  private static final Map<CharTypes, Pair<Integer, Integer>> mapping = new HashMap<>();
  static {
    mapping.put(CharTypes.PRINTABLE, new ImmutablePair<>(32, 127)); // range of printable ASCII characters: [ 32 = SPACE ; 127 = DELETE ]
    mapping.put(CharTypes.LOWERCASE_ALPHABETIC, new ImmutablePair<>(97, 122));
    mapping.put(CharTypes.UPPERCASE_ALPHABETIC, new ImmutablePair<>(65, 90));
    mapping.put(CharTypes.NUMERIC, new ImmutablePair<>(48, 57));
  }

  // Returns random string within range of printable ASCII characters
  public static String randString(int len) {
    return randString(CharTypes.PRINTABLE, len);
  }

  public static String randString(CharTypes type, int len) {
    Pair<Integer, Integer> asciiRange = mapping.get(type);
    Random rgen = new Random();
    return randString(() -> (char) (rgen.nextInt(asciiRange.getRight() - asciiRange.getLeft() + 1) + asciiRange.getLeft()), len);
  }

}
