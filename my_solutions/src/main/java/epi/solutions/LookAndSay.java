package epi.solutions;

import epi.solutions.helper.AlgorithmFactory;
import epi.solutions.helper.AlgorithmRunnerAndVerifier;
import epi.solutions.helper.CloneableInputsMap;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/22/16.
 * Problem 7.6
 */
public class LookAndSay {
  private static final int NUM_TESTS = (int) Math.pow(10, 5);

  private static String lookAndSay(int n) {
    String s = "1";
    for (int i = 1; i < n; ++i)
      s = nextNumber(s);
    return s;
  }

  private static String nextNumber(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); ++i) {
      int count = 1;
      while (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
        ++i; ++count;
      }
      sb.append(count).append(s.charAt(i));
    }
    return sb.toString();
  }

  public static void main(String[] args) throws Exception {
    assert(lookAndSay(1).equals("1"));
    assert(lookAndSay(2).equals("11"));
    assert(lookAndSay(3).equals("21"));
    assert(lookAndSay(4).equals("1211"));
    assert(lookAndSay(5).equals("111221"));
    assert(lookAndSay(6).equals("312211"));
    assert(lookAndSay(7).equals("13112221"));
    assert(lookAndSay(8).equals("1113213211"));

    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      inputs.addInteger("n", 20 + rgen.nextInt(5));
      return inputs;
    };

    Function<CloneableInputsMap, String> runAlg = (inputs) -> lookAndSay(inputs.getInteger("n"));
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Look & Say Numbers", NUM_TESTS, formInputs, runAlg);
    algorithmFactory.run();
  }
}
