package epi.solutions.helper;

import com.google.common.base.Preconditions;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
}
