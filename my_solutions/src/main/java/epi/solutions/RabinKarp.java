package epi.solutions;

import epi.solutions.helper.*;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/22/16.
 * Problem 7.14
 */
public class RabinKarp {
  private static final int NUM_TESTS = (int) Math.pow(10, 5);
  private static final int M = 4;                         // length of string s
  private static final int N = (int) Math.pow(10, 3);     // length of string t


  /**
   * Rabin-Karp algorithm for finding the first occurrence of a substring in a given text string.
   *
   * O(n^2) Brute-force strategy:
   *    2 nested loops (for each possible starting point of substring,
   *    check if matched by iterating over characters of substring)
   *
   * O(m + n) Rabin-Karp strategy:
   *    Use a "good" rolling hash (i.e. one whose hash of a string is an additive function
   *    of the individual characters). The idea is to create a "fingerprint" of the search string `s`
   *    and match against rolling fingerprints of susbtrings in the text string `t`
   *
   *    A "good" rolling hash function minimizes the likelihood of collisions
   *    Our rolling hash function is: f(s) = ( SUM[c_i * kBase^i] ) % kMod
   *    where   s = c_n c_n-1 ... c_2 c_1   is the string we're searching for
   *    and     kBase = 26      (why is this a good choice? must obviously be related to # chars in alphabet)
   *    and     kMod = 997      (997 is a prime. trade-off between large primes and computational complexity?)
   *
   * @param t text string
   * @param s string to look for
   * @return index `i` of string `t` such that the first occurrence of `s` in `t` begins at index `i`.
   *         or -1 if no such occurrence exists
   */
  private static int rabinKarp(String t, String s) {
    if (s.length() > t.length())
      return -1; // s is longer than t, so the former can't be a substring of the latter.
    final int kBase = 26;
    final int kMod = 997;
    int sHash = 0, tHash = 0; // hash codes for s and the substring of t
    int powerS = 1; // the modulo result of kBase ^ |s|
    for (int i = 0; i < s.length(); ++i) {
      powerS = i > 0 ? powerS  * kBase : 1;
      sHash = ( sHash * kBase + s.charAt(i) ) % kMod;
      tHash = ( tHash * kBase + t.charAt(i) ) % kMod;
    }

    for (int i = s.length(); i < t.length(); ++i) {
      // Must verify even if hashes are equal to eliminate false positives that may occur from hash collisions.
      if (tHash == sHash && t.substring(i - s.length(), i).equals(s)) {
        return i - s.length(); // found a match.
      }

      // Uses a rolling hash to compute the new hash code
      tHash -= ( t.charAt(i - s.length()) * powerS ) % kMod;
      if (tHash < 0) tHash += kMod;
      tHash = ( tHash * kBase + t.charAt(i) ) % kMod;
    }

    // Tries to match s at the end of t
    if (tHash == sHash && t.substring(t.length() - s.length()).equals(s)) {
      return t.length() - s.length();
    }

    return -1; // s is not a substring of t
  }

  public static void main(String[] args) throws Exception {
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      inputs.addString("s", MiscHelperMethods.randString(MiscHelperMethods.CharTypes.LOWERCASE_ALPHABETIC, M));
      inputs.addString("t", MiscHelperMethods.randString(MiscHelperMethods.CharTypes.LOWERCASE_ALPHABETIC, N));
      return inputs;
    };

    Function<CloneableInputsMap, Integer> runAlg = (inputs) -> rabinKarp(inputs.getString("t"), inputs.getString("s"));
    Function<CloneableInputsMap, Integer> knownSolution = (inputs) ->
            inputs.getString("t").indexOf(inputs.getString("s"));
    AlgVerifierInterfaces<Integer, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(Integer::equals);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("RabinKarp Substring Search", NUM_TESTS, formInputs, runAlg, knownSolution, algVerifier);
    algorithmFactory.run();
  }
}
