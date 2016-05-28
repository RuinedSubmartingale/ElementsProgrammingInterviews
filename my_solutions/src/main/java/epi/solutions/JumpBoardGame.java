package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/20/16.
 * Problem 6.4
 */
public class JumpBoardGame {
  private static final int BOARD_LENGTH = 20;
  private static final int MAX_ADVANCE_PER_STEP = 5;
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /**
   * @param A Array A of nonnegative integers, where A[i] represents the maximum
   *        you can advance forward from that position
   * @return Boolean: true if you can advance from start to end of array. false otherwise.
   */
  private static boolean isWinnable(final ArrayList<Integer> A) {
    int furthest_reach = 0;
    for (int i = 0; i <= furthest_reach && furthest_reach < A.size() - 1; ++i) {
      furthest_reach = Math.max(furthest_reach, i  + A.get(i));
    }
    return furthest_reach >= A.size() - 1;
  }

  private static void smallTest() {
    assert(isWinnable(new ArrayList<>(Arrays.asList(2, 3, 1, 1, 4))));
    assert(!isWinnable(new ArrayList<>(Arrays.asList(3, 2, 1, 0, 4))));
    assert(!isWinnable(new ArrayList<>(Arrays.asList(3, 2, 1, -10, 4))));
    assert(isWinnable(new ArrayList<>(Arrays.asList(2, 3, -1, -1, 4))));
    assert(!isWinnable(new ArrayList<>(Arrays.asList(2, 2, -1, -1, 100))));

  }
  public static void main(String[] args) throws Exception {
    smallTest();
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      Random rgen = new Random();
      inputs.addArrayList("A", MiscHelperMethods.randArray(() -> rgen.nextInt(MAX_ADVANCE_PER_STEP), BOARD_LENGTH));
      return inputs;
    };
    Function<CloneableTestInputsMap, Boolean> runAlg = (inputs) ->
            isWinnable(inputs.getArrayList("A"));
    Supplier<Boolean> emptyOutput = () -> false;
    TimeTests<Boolean> algTimer = new TimeTests<>(formInput, runAlg, emptyOutput, "JumpBoardGame");
    algTimer.time(NUM_TESTS);

  }
}
