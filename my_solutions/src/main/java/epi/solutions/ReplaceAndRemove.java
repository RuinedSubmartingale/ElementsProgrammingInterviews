package epi.solutions;

import epi.solutions.helper.*;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/28/16.
 * Problem 7.2
 *
 * @summary Delete each "b" and replace each "a" with a "dd" in a given character array.
 *
 * @strategy
 * 1) On the first pass, count the number of non-"b" characters and the number of "a", using the variables
 * writeIdx and aCnt, respectively. For each non-"b" character, write it to writeIdx on the first pass.
 *
 * The desired string will be writeIdx + aCnt characters long. Assume the character array is big enough.
 *
 * 2) Start writing final string backwards, from right to left, while iterating from writeIdx down to 0,
 * taking care to write "a" as "dd".
 */
public class ReplaceAndRemove {
  private static final int ARR_LEN = (int) Math.pow(10, 2);
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  /**
   *
   * @param s character array
   * @return length of string in character array after algorithm.
   */
  private static int replaceAndRemove(char[] s, int len) {
    int writeIdx = 0, aCnt = 0;
    // First pass: ignore "b"s, write everything else. Count the number of "a"s
    for (int i = 0; i < len; ++i) {
      if (s[i] != 'b') {
        s[writeIdx++] = s[i];
      }
      if (s[i] == 'a') {
        aCnt += 1;
      }
    }

    // Second pass: iterate backwars (right to left), while replacing "a"s with "dd"s
    int finalSize = writeIdx + aCnt;
    int currIdx = writeIdx - 1; // iterate from right to left over the characters we just wrote
    writeIdx = finalSize - 1; // start writing right to left from where final string will end

    while(currIdx >= 0) {
      if (s[currIdx] == 'a') {
        s[writeIdx--] = 'd';
        s[writeIdx--] = 'd';
      } else {
        s[writeIdx--] = s[currIdx];
      }
      currIdx -= 1;

    }

    return finalSize;
  }

  private static String knownOutput(char[] s, int len) {
    String str = new String(s, 0, len);
    str = str.replace("b", "");
    str = str.replace("a", "dd");
    return str;
  }

  public static void main(String[] args) throws Exception {
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      char[] s = new char[(ARR_LEN << 1) + 1];
      MiscHelperMethods.randCharArray(() -> (char) ('a' + rgen.nextInt(26)), ARR_LEN, s);
      inputs.addCharArray("s", s);
      inputs.addInteger("len", ARR_LEN);
      return inputs;
    };
    Function<CloneableInputsMap, Integer> runAlg = (inputs) ->
            replaceAndRemove(inputs.getCharArray("s"), inputs.getInteger("len"));

    Function<CloneableInputsMap, Integer> knownOutput = (inputs) -> {
      String expected = knownOutput(inputs.getCharArray("s"), inputs.getInteger("len"));
      inputs.addCharArray("s", expected.toCharArray());
      return expected.length();
    };

    Function<CloneableInputsMap, CloneableInputsMap> saveExtraResults = (inputs) -> {
      CloneableInputsMap extraResults = new CloneableInputsMap();
      extraResults.addCharArray("s", inputs.getCharArray("s"));
      return extraResults;
    };
    AlgVerifierInterfaces.QuadFunction<Integer, Integer, CloneableInputsMap, CloneableInputsMap, Boolean> checkAns =
            (observed, expected, algExtraResults, expExtraResults) ->
                    observed.equals(expected)
                            && algExtraResults.getCharArray("s").length >= observed
                            && expExtraResults.getCharArray("s").length >= observed
                            && String.valueOf(algExtraResults.getCharArray("s")).substring(0, observed).equals(String.valueOf(expExtraResults.getCharArray("s")).substring(0, observed));

    AlgVerifierInterfaces< Integer, CloneableInputsMap> algVerifier = new OutputOutputExtraExtraVerifier<>(checkAns);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Replace and Remove", NUM_TESTS, formInputs, runAlg, knownOutput, saveExtraResults, algVerifier);

    System.out.println(String.format("Running algorithms on character arrays of length %d...", ARR_LEN));
//    algorithmFactory.setSequential();
    algorithmFactory.run();
  }
}
