package epi.solutions;

import epi.solutions.helper.CloneableInteger;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.11 from EPI
 @summary
 A palindromic string is one which reads the same forwards and backwards,
 e.g., "redivider".  In this problem, you are to write a program which determines if the decimal representation
 of an integer is a palindromic string.  For example, your program should return true for the inputs
 $0, 1, 7, 11, 121, 333$, and $2147447412$,
 and false for the inputs $-1, 12, 100$, and $2147483647$.

 @problem
 Write a program that takes an integer and determines if that integer's
 representation as a decimal string is a palindrome.
 */

public class IsPalindrome {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static boolean isPalindrome(int x) {
    if (x < 0) return false;
    else if (x == 0) return true;

    final int kNumDigits = (int) (Math.floor(Math.log10(x)) + 1);
    int x_remaining = x, msd_shift = (int) Math.pow(10, kNumDigits - 1);
    for (int i = 0; i < (kNumDigits / 2); i++) {
      if (x / msd_shift != x_remaining % 10)
        return false;
      x %= msd_shift;
      msd_shift /= 10;
      x_remaining /= 10;
    }
    return true;
  }

  private static boolean slowSolution(int x) {
    String s = String.valueOf(x);
    for (int i = 0, j = s.length() - 1; i < j; ++i, --j) {
      if (s.charAt(i) != s.charAt(j))
        return false;
    }
    return true;
  }

  public static void main(String[] args) {
    if (args.length == 1) {
      int x = Integer.parseInt(args[0]);
      System.out.println(x + " " + isPalindrome(x));
      assert (slowSolution(x) == isPalindrome(x));
    } else {
      Callable<CloneableTestInputsMap> formInput = () -> {
        Random rgen = new Random();
        CloneableTestInputsMap inputs = new CloneableTestInputsMap();
        inputs.put("x", new CloneableInteger(rgen.nextInt(99999 * 2 + 1) - 99999));
        return inputs;
      };
      Function<CloneableTestInputsMap, Boolean> runAlgorithm =
              (input) -> isPalindrome(((CloneableInteger) input.get("x")).data);
      Function<CloneableTestInputsMap, Boolean> getKnownOutput =
              (orig_input) -> slowSolution(((CloneableInteger) orig_input.get("x")).data);
      BiFunction<Boolean, Boolean, Boolean> checkResults = Boolean::equals;
      Supplier<Boolean> emptyOutput = () -> true;
      TimeTests<Boolean> algTimer =
              new TimeTests<>(formInput, runAlgorithm, emptyOutput, "IsPalindrome");
      algTimer.testAndCheck(NUM_TESTS, checkResults, getKnownOutput);
    }
  }
}
