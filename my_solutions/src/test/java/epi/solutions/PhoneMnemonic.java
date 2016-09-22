package epi.solutions;

import epi.solutions.helper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/21/16.
 */
public class PhoneMnemonic {

  private static final int INPUT_LEN = (int) Math.pow(10, 1);
  private static final int NUM_TESTS = (int) Math.pow(10, 4);

  // The mapping from digit to corresponding characters on a typical push-button telephone.
  private static final String[] MAPPINGS = {"0", "1", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"};

  private static List<String> phoneMnemonic(String phoneNumber) {
    char[] partialMnemonic = new char[phoneNumber.length()];
    List<String> mnemonics = new ArrayList<>();
    phoneMnemonicHelper(phoneNumber, 0, partialMnemonic, mnemonics);
    return mnemonics;
  }

  private static void phoneMnemonicHelper(String phoneNumber, int digit
                                          , char[] partialMnemonic, List<String> mnemonics) {
    if (digit == phoneNumber.length()) {
      // All digits have been processed. Add a copy since subsequent calls modify partialMnemonic.
      mnemonics.add(new String(partialMnemonic));
    } else {
      // Try all possible characters for this digit.
      for (int i = 0; i < MAPPINGS[phoneNumber.charAt(digit) - '0'].length(); ++i) {
        char c = MAPPINGS[phoneNumber.charAt(digit) - '0'].charAt(i);
        partialMnemonic[digit] = c;
        phoneMnemonicHelper(phoneNumber, digit + 1, partialMnemonic, mnemonics);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Supplier<CloneableInputsMap> formInputs = () -> {
      CloneableInputsMap inputs = new CloneableInputsMap();
      Random rgen = new Random();
      inputs.addString("phoneNumber", MiscHelperMethods.randString(() -> Character.forDigit(rgen.nextInt(10), 10), INPUT_LEN));
      return inputs;
    };

    Function<CloneableInputsMap, List<String>> runAlg = (inputs) -> phoneMnemonic(inputs.getString("phoneNumber"));
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("Compute all Mnemonics of Phone #", NUM_TESTS, formInputs, runAlg);
    algorithmFactory.run();
  }
}
