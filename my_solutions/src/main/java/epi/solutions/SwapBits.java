package epi.solutions;

import epi.solutions.helper.CloneableInteger;
import epi.solutions.helper.CloneableLong;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.2
 */
public class SwapBits {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  public static long swapBits(long x, int i, int j) {
    // Check if i-th and j-th bits differ
    if (((x >> i) & 1) != ((x >> j) & 1)) {
      // They differ. So swap them by flipping their values.
      // Select the bits with the bitmask and then XOR to flip.
      long bitMask = (1L << i) | (1L << j);
      x ^= bitMask;
    }
    return x;
  }

  private static void simpleTest() {
    assert(swapBits(47, 1, 4) == 61);
    assert(swapBits(28, 0, 2) == 25);
  }

  public static void main(String[] args) {
    simpleTest();
    if (args.length == 3) {
      long x = Long.parseLong(args[0]);
      int i = Integer.parseInt(args[1]);
      int j = Integer.parseInt(args[2]);
      System.out.println("x = " + x + ", i = " + i
              + ", j = " + j
              + ".\nswapped_x = " + swapBits(x,i,j));
    } else {
      Callable<CloneableTestInputsMap> formInput = () -> {
        Random rgen = new Random();
        long x = rgen.nextLong();
        int x_num_bits = 0;
        while (x != 0) {
          x_num_bits += 1;
          x >>>= 1;
        }
        int i = rgen.nextInt(x_num_bits);
        int j = rgen.nextInt(x_num_bits);
        CloneableTestInputsMap inputs = new CloneableTestInputsMap();
        inputs.put("x", new CloneableLong(x));
        inputs.put("i", new CloneableInteger(i));
        inputs.put("j", new CloneableInteger(j));
        return inputs;
      };
      Function<CloneableTestInputsMap, Long> runAlg = (inputs) -> swapBits( ((CloneableLong) inputs.get("x")).data
                , ((CloneableInteger) inputs.get("i")).data
                , ((CloneableInteger) inputs.get("j")).data);
      Supplier<Long> emptyOutput = () -> 0L;
      TimeTests<Long> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "SwapBits");
      algTimer.test(NUM_TESTS);
    }
  }
}
