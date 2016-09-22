package epi.solutions;

import com.google.common.collect.EvictingQueue;
import epi.solutions.helper.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 9/22/16.
 */
public class Tail {

  private static final int FILE_LINE_LENGTH = (int) Math.pow(10, 2);
  private static final int N = (int) Math.pow(10, 1);
  private static final int NUM_TESTS = (int) Math.pow(10, 4);

  public static String tail(String fileName, int N) {
    StringBuilder tailingLines = new StringBuilder();
    try {
      RandomAccessFile filePtr = new RandomAccessFile(fileName, "r");
      filePtr.seek(filePtr.length() - 1);
      long fileSize = filePtr.length(), newLineCnt = 0;

      // Read file in reverse, looking for '\n'
      for (long i = fileSize - 1; i != -1; --i) {
        filePtr.seek(i);
        char ch = (char) filePtr.readByte();
        if (ch == '\n') {
          if (++newLineCnt > N) break;
        }
        tailingLines.append(ch);
      }
      filePtr.close();
      Files.delete(Paths.get(fileName));

      tailingLines.reverse();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tailingLines.toString();
  }

  public static String tailQueued(String fileName, int N) {
    StringBuilder tailingLines = new StringBuilder();
    try {

      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String currLine;
      Queue<String> linesQueue = EvictingQueue.create(N);
      while ((currLine = br.readLine()) != null) {
        linesQueue.add(currLine);
      }
      br.close();

      int numLines = linesQueue.size();
      while (numLines-- > 0) tailingLines.append(linesQueue.remove() + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tailingLines.toString();
  }

  public static void main(String[] args) throws Exception {
    Supplier<CloneableInputsMap> formInputs = () -> {
    CloneableInputsMap inputs = new CloneableInputsMap();
    try {
      File testFile = File.createTempFile("TailTest", ".txt");
      PrintWriter writer = new PrintWriter(testFile);
      Random rgen = new Random();
      for (int i = 0; i <= FILE_LINE_LENGTH; ++i) {
        writer.println(MiscHelperMethods.randString(() -> (char) (rgen.nextInt(127 - 32) + 32), rgen.nextInt(80)));
        // range of printable ASCII characters: 32 = SPACE ; 126 = ~)
      }
      writer.close();
      inputs.addString("fileName", testFile.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return inputs;
    };

    Function<CloneableInputsMap, String> runAlg = (inputs) -> tail(inputs.getString("fileName"), N);
    Function<CloneableInputsMap, String> knownSolution = (inputs) -> tailQueued(inputs.getString("fileName"), N);
    AlgVerifierInterfaces<String, CloneableInputsMap> algVerifier = new OutputComparisonVerifier<>(String::equals);
    AlgorithmFactory algorithmFactory = new AlgorithmRunnerAndVerifier<>("UNIX tail command", NUM_TESTS, formInputs, runAlg, knownSolution, algVerifier);
    algorithmFactory.run();
  }
}
