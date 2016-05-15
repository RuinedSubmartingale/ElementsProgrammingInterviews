package epi.solutions;

import epi.solutions.helper.CloneableString;
import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.TimeTests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by psingh on 5/14/16.
 * Problem 13.2 from EPI
 */
public class CanStringBePalindrome {
  private static final int INPUT_STRING_LENGTH = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static boolean canFormPalindromeHash(final String s) {
    Map<Character, Integer> charFreqs = new HashMap<>();
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      if (!charFreqs.containsKey(c))
        charFreqs.put(c, 1);
      else
        charFreqs.put(c, charFreqs.get(c) + 1);
    }

    int oddFreqCount = 0;
    for (Map.Entry<Character, Integer> entry : charFreqs.entrySet()) {
      if ((entry.getValue() % 2) != 0 && (++oddFreqCount > 1))
        return false;
    }
    return true;
  }


  private static boolean canFormPalindromeSorting(final String s) {
    // TODO : find faster/simpler way of getting frequency counts
    char[] a = s.toCharArray();
    Arrays.sort(a);
    int oddFreqCount = 0;
    int numCurrChar =1;

    for (int i = 1; i < a.length && oddFreqCount <= 1; ++i) {
      if(a[i] != a[i-1]) {
        if ((numCurrChar & 1) != 0)
          ++oddFreqCount;
        numCurrChar = 1;
      } else
        ++numCurrChar;
    }
    if ((numCurrChar & 1) != 0)
      ++oddFreqCount;
    return oddFreqCount <= 1;
  }

  private static String randString(int len) {
    StringBuilder sb = new StringBuilder();
    Random rgen = new Random();
    while (len-- > 0)
      sb.append((char)(rgen.nextInt(26) + 'A'));
    return sb.toString();
  }

  public static void main(String[] args) {
    Callable<CloneableTestInputsMap> formInput = () -> {
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
      inputs.put("s", new CloneableString(randString(INPUT_STRING_LENGTH)));
      return inputs;
    };
    Function<CloneableTestInputsMap, Boolean > runAlgorithm =
            (inputs) -> canFormPalindromeHash(((CloneableString) inputs.get("s")).data);
    Function<CloneableTestInputsMap, Boolean> getKnownOutput =
            (orig_inputs) -> canFormPalindromeSorting(((CloneableString) orig_inputs.get("s")).data);
    BiFunction<Boolean, Boolean, Boolean> checkResults = Boolean::equals;
    TimeTests<Boolean> algTimer =
            new TimeTests<>(formInput, runAlgorithm, getKnownOutput, checkResults, NUM_TESTS, "CanStringBePalindrome");
    algTimer.testAndCheck();
  }
}
