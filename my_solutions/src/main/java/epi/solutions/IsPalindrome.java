package epi.solutions;

import epi.solutions.helper.*;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/12/16.
 * Problem 5.11 and 7.3 from EPI
 @summary
 A palindromic string is one which reads the same forwards and backwards, e.g., "redivider".
 */

public class IsPalindrome {
  private static final int NUM_TESTS = (int) Math.pow(10, 7);
  private static final int STRING_LEN = 10;

  /**
   * @problem 5.11
   * Write a program that takes an integer and determines if that integer's
   * representation as a decimal string is a palindrome.
   */
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

  /**
   * @problem 7.3
   * Write a program that takes determines if a given string is a palindrome.
   * Ignore non-alphanumeric characters and case. e.g. "Race! Car!" should return true.
   */
  private static boolean isPalindrome(String s, Predicate<Character> countChar) {
    s = s.toLowerCase();
    int i = 0;
    int j = s.length() - 1;
    while (i < j) {
      while(!countChar.test(s.charAt(i)) && i < j) ++i;
      while(!countChar.test(s.charAt(j)) && i < j) --j;
      if(s.charAt(i++) != s.charAt(j--)) return false;
    }
    return true;
  }

  private static boolean slowSolution(int x) {
    String s = String.valueOf(x);
    return slowSolution(s, (ch) -> true);

  }
  private static boolean slowSolution(String s, Predicate<Character> countChar) {
    s = s.toLowerCase();
    for (int i = 0, j = s.length() - 1; i < j; ++i, --j) {
      while(!countChar.test(s.charAt(i)) && i < j) ++i;
      while(!countChar.test(s.charAt(j)) && i < j) --j;
      if (s.charAt(i) != s.charAt(j))
        return false;
    }
    return true;
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      int x = Integer.parseInt(args[0]);
      System.out.println(x + " " + isPalindrome(x));
      assert (slowSolution(x) == isPalindrome(x));
    } else {
      Supplier<CloneableInputsMap> formIntegerInputs = () -> {
        Random rgen = new Random();
        CloneableInputsMap inputs = new CloneableInputsMap();
        inputs.addInteger("x", rgen.nextInt(99999 * 2 + 1) - 99999);
        return inputs;
      };
      Function<CloneableInputsMap, Boolean> runIntegerAlg =
              (input) -> isPalindrome(input.getInteger("x"));
      Function<CloneableInputsMap, Boolean> integerKnownOutput =
              (orig_input) -> slowSolution(orig_input.getInteger("x"));
      AlgVerifierInterfaces< Boolean, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(Boolean::equals);
      AlgorithmFactory intAlgorithmFactory = new AlgorithmRunnerAndVerifier<>("Plus One", NUM_TESTS, formIntegerInputs, runIntegerAlg, integerKnownOutput, algVerifier);
      intAlgorithmFactory.run();

      Supplier<CloneableInputsMap> formStringInputs = () -> {
        Random rgen = new Random();
        CloneableInputsMap inputs = new CloneableInputsMap();
        inputs.addString("s", MiscHelperMethods.randString(() ->
                (char) (rgen.nextInt(127 - 32) + 32), STRING_LEN));
                // range of printable ASCII characters: 32 = SPACE ; 126 = ~
        return inputs;
      };
      Predicate<Character> isAlphaNum = Character::isLetterOrDigit;
      Function<CloneableInputsMap, Boolean> runStringAlg =
              (input) -> isPalindrome(input.getString("s"), isAlphaNum);
      Function<CloneableInputsMap, Boolean> stringKnownOutput = (orig_inputs) ->
              slowSolution(orig_inputs.getString("s"), isAlphaNum);
      AlgorithmFactory stringAlgorithmFactory = new AlgorithmRunnerAndVerifier<>("Plus One", NUM_TESTS, formStringInputs, runStringAlg, stringKnownOutput, algVerifier);
      stringAlgorithmFactory.run();
    }
  }
}
