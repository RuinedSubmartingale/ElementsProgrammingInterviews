package epi.solutions.helper;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by psingh on 5/20/16.
 *
 */
public class MiscHelperMethods {

  private static final Map<Class<? extends Number>, Stream<? extends Number>> map = new HashMap<>();
  static {
    Random rgen = new Random();
    map.put(Integer.class, rgen.ints().boxed());
    map.put(Double.class, rgen.doubles().boxed());
    map.put(Long.class, rgen.longs().boxed());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Number> Stream<T> randNumberStream(Class<T> numberClass, int len, T... range) {
    try {
      T minInclusive = range.length > 0 ? range[0] : (T) numberClass.getDeclaredField("MIN_VALUE").get(null);
      T maxExclusive = range.length > 1 ? range[1] : (T) numberClass.getDeclaredField("MAX_VALUE").get(null);
      Random rgen = new Random();
      if (numberClass == Integer.class) {
        return (Stream<T>) rgen.ints(len, (Integer) minInclusive, (Integer) maxExclusive).boxed();
      } else if (numberClass == Double.class) {
        return (Stream<T>) rgen.doubles(len, (Double) minInclusive, (Double) maxExclusive).boxed();
      } else if (numberClass == Long.class) {
        return (Stream<T>) rgen.longs(len, (Long) minInclusive, (Long) maxExclusive).boxed();
      } else {
        throw new IllegalArgumentException();
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  @SafeVarargs
  public static <T extends Number> ArrayList<T> randNumberArray(Class<T> numberClass, int len, T... range) {
    Stream<T> stream = randNumberStream(numberClass, len, range);
    assert(stream != null);
    return stream.collect(Collectors.toCollection(ArrayList::new));
  }

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


  public enum CharTypes { PRINTABLE, LOWERCASE_ALPHABETIC, UPPERCASE_ALPHABETIC, NUMERIC }
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
