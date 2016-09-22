package epi.solutions;

import epi.solutions.helper.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/21/16.
 */
public class ReverseWords {


  private static final int INPUT_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static void reverseWords(String input) {
    // TODO: implement reverseWords for String-type inputs. Either use built-in String/CharArray/etc. methods, implement your own, or abstract reverse() and find() methods below to work for Strings.
  }

  private static void reverseWords(char[] input) {
    reverse(input, 0, input.length);
    int startIdx = 0, endIdx;
    while( (endIdx = find(input, ' ', startIdx)) != -1) {
      reverse(input, startIdx, endIdx);
      startIdx = endIdx + 1;
    }

    reverse(input, startIdx, input.length);
  }

  private static int find(char[] input, char c, int startIdx) {
    for (int i = startIdx; i < input.length; ++i) {
      if (input[i] == c) return i;
    }
    return -1;
  }

  private static void reverse(char[] input, int startIdx, int endIdx) {
    if (startIdx >= endIdx) return;

    int lastIdx = endIdx - 1;
    for (int i = startIdx; i <= startIdx + (lastIdx - startIdx) / 2; ++i) {
      char tmp = input[i];
      input[i] = input[startIdx + (lastIdx - i)];
      input[startIdx + (lastIdx - i)] = tmp;
    }
  }

  public static void main(String[] args) throws Exception {
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      char[] s = new char[INPUT_LEN];
      MiscHelperMethods.randCharArray(() -> (char) (' ' + rgen.nextInt(95)), INPUT_LEN, s); // TODO: abstract out PrintableCharSupplier
//      s = "hello my name is bob".toCharArray();
//      s = " OneWordLast".toCharArray();
//      s = "OneWordFirst ".toCharArray();
      inputs.addCharArray("s", s);
      inputs.addInteger("len", INPUT_LEN);
      return inputs;
    };

    Function<CloneableInputsMap, String> runAlg = (inputs) -> {
//      System.out.println(String.format("%12s %s", "Input: ", String.valueOf(inputs.getCharArray("s"))));
      reverseWords(inputs.getCharArray("s"));
//      System.out.println(String.format("%12s %s", "Observed: ", String.valueOf(inputs.getCharArray("s"))));
      return String.valueOf(inputs.getCharArray("s"));
    };

    Function<CloneableInputsMap, String> knownOutput = (inputs) -> {
      String s = String.valueOf(inputs.getCharArray("s"));
      List<String> split = Arrays.asList(s.split(" ", -1)); // The limit param is needed for empty string words
      Collections.reverse(split);
      String expected = String.join(" ", split);
//      System.out.println("\n\n" + String.format("%12s %s", "Expected: ", expected));
      return expected;
    };

    BiFunction<String, String, Boolean> checkAns = String::equals;

    AlgVerifierInterfaces<String, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(checkAns);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Reverse Words in String", NUM_TESTS, formInputs, runAlg, knownOutput, algVerifier);

    System.out.println(String.format("Running algorithms on character arrays of length %d...", INPUT_LEN));
//    algorithmFactory.setSequential();
    algorithmFactory.run();
  }
}
