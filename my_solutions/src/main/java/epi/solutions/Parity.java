package epi.solutions;

import epi.solutions.helper.CloneableInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/*
* Problem 5.1 from EPI
*/
@SuppressWarnings("unused")
public class Parity {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);
  private static final int WORD_SIZE = 16;
  private static short[] preComputedParity;
  private static long[] testValues;

  private static short defaultCompute(long value) {
    short result = 0;
    while (value != 0) {
      result ^= (value & 1);
      value >>>= 1;
    }
    return result;
  }

  static {
    preComputedParity = new short[1 << WORD_SIZE];
    for (int i = 0; i < (1 << WORD_SIZE); ++i) {
      preComputedParity[i] = defaultCompute(i);
    }

    testValues = new long[NUM_TESTS];
    Random r = new Random();
    for (int i = 0; i < NUM_TESTS; ++i) {
      testValues[i] = r.nextInt(Integer.MAX_VALUE);
    }
  }

  private static abstract class AbstractParity {
    protected short[] compute() {
      short[] testResults = new short[NUM_TESTS];
      for (int i = 0; i < NUM_TESTS; ++i) {
        testResults[i] = compute(testValues[i]);
      }
      return testResults;
    }

    abstract short compute(long value);
  }

  private static class Parity1 extends AbstractParity {
    @Override
    short compute(long x) {
      return defaultCompute(x);
    }
  }

  private static class Parity2 extends AbstractParity {
    @Override
    short compute(long x) {
      short result = 0;
      while (x != 0) {
        x = x & (x - 1);
        result ^= 1;
      }
      return result;
    }
  }

  private static class Parity3 extends AbstractParity {
    private static final int BIT_MASK = 0xFFFF;

    @Override
    short compute(long x) {
      return (short) (
              preComputedParity[(int) ((x >>> (3 * WORD_SIZE)) & BIT_MASK)] ^
                      preComputedParity[(int) ((x >>> (2 * WORD_SIZE)) & BIT_MASK)] ^
                      preComputedParity[(int) ((x >>> WORD_SIZE) & BIT_MASK)] ^
                      preComputedParity[(int) (x & BIT_MASK)]
      );
    }
  }

  private static class Parity4 extends AbstractParity {
    private static final int FOUR_BIT_PARITY = 0b0110100110010110;

    @Override
    short compute(long x) {
      x ^= x >>> 32;
      x ^= x >>> 16;
      x ^= x >>> 8;
      x ^= x >>> 4;
      x &= 0xf;
      return (short) ((FOUR_BIT_PARITY >> x) & 1);
    }
  }

  public static void main(String[] args) throws Exception {
    Parity1 p1 = new Parity1();
    Parity2 p2 = new Parity2();
    Parity3 p3 = new Parity3();
    Parity4 p4 = new Parity4();

    if (args.length == 1) {
      long x = Long.parseLong(args[0]);
      assert (p1.compute(x) == p2.compute(x));
      assert (p1.compute(x) == p3.compute(x));
      assert (p1.compute(x) == p4.compute(x));
      System.out.println("x = " + x + ", parity = " + p1.compute(x));
    } else {
      Callable<CloneableInputsMap> formInput = () -> {
        CloneableInputsMap inputs = new CloneableInputsMap();
        Random rgen = new Random();
        inputs.addLong("x", rgen.nextLong());
        return inputs;
      };
      Function<CloneableInputsMap, Short> runParity1 = (inputs) ->
              p1.compute(inputs.getLong("x"));
      Function<CloneableInputsMap, Short> runParity2 = (inputs) ->
              p2.compute(inputs.getLong("x"));
      Function<CloneableInputsMap, Short> runParity3 = (inputs) ->
              p3.compute(inputs.getLong("x"));
      Function<CloneableInputsMap, Short> runParity4 = (inputs) ->
              p4.compute(inputs.getLong("x"));
      Supplier<Short> emptyOutput = () -> (short) 0;
      TimeTests<Short> algTimer1 = new TimeTests<>(formInput, runParity1, emptyOutput, "Parity1");
      TimeTests<Short> algTimer2 = new TimeTests<>(formInput, runParity2, emptyOutput, "Parity2");
      TimeTests<Short> algTimer3 = new TimeTests<>(formInput, runParity3, emptyOutput, "Parity3");
      TimeTests<Short> algTimer4 = new TimeTests<>(formInput, runParity4, emptyOutput, "Parity4");
//      algTimer1.setSequential();
      algTimer1.time(NUM_TESTS);
//      algTimer2.setSequential();
      algTimer2.time(NUM_TESTS);
//      algTimer3.setSequential();
      algTimer3.time(NUM_TESTS);
//      algTimer4.setSequential();
      algTimer4.time(NUM_TESTS);
    }
  }
}