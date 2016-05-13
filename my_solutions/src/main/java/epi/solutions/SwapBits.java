package epi.solutions;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.2
 */
public class SwapBits {
  private static long swapBits(long x, int i, int j) {
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
    long x;
    int i, j;
    if (args.length == 3) {
      x = Long.parseLong(args[0]);
      i = Integer.parseInt(args[1]);
      j = Integer.parseInt(args[2]);
      System.out.println("x = " + x + ", i = " + i
              + ", j = " + j
              + ".\nswapped_x = " + swapBits(x,i,j));
    }
  }
}
