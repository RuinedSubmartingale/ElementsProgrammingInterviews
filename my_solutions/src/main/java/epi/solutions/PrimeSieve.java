package epi.solutions;

import epi.solutions.helper.CloneableTestInputsMap;
import epi.solutions.helper.MiscHelperMethods;
import epi.solutions.helper.TimeTests;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 5/28/16.
 * Problem 6.12
 */
public class PrimeSieve {
  private static final int NUM_TESTS = (int) Math.pow(10, 6);

  private static abstract class AbstractPrimeSiever {
    abstract List<Integer> generatePrimes(int n);
  }

  /**
   * O(n/2 + n/3 + n/5 + n/7 + n/11 + ...) = O(n log log n)
   */
  private static class PrimeSiever extends AbstractPrimeSiever {

    @Override
    ArrayList<Integer> generatePrimes(int n) {
      ArrayList<Integer> primes = new ArrayList<>();
      if (n < 2) return primes;
      // isPrime.get(p) represents whether p is prime.
      // Initially, 0 and 1 are set false. 2..n are set true. Use sieving to eliminate non-primes.
      final int cntOdds = (int) Math.floor(0.5*(n-3)) + 1; // number of odds within range 3..n
      List<Boolean> isPrime = new ArrayList<>(Collections.nCopies(cntOdds, true));
      for (int i = 0; i < cntOdds; ++i) {
        if (isPrime.get(i)) {
          int p = (i*2) + 3;
          primes.add(p);

          // We begin sieving from p^2, because [2p, ..., (p-1)p] will have already been taken care of
          // by previous iterations [0..i-1]. The index of p^2 in isPrime is (2i^2 + 6i +3) because
          // isPrime.get(i) represents 2i + 3.
          // We only need to sieve [p^2 + kp] for even integers k, since [p^2 + kp] will be even for odd k.
          // So we can increment j by p each iteration, since (2j + 3) - [2(j+p)+3] = 2p.
          //
          // Note that we need to use long type for j because p^2 might overflow.
          // The for loop's condition avoids executing the loop for such overflow cases.
          for (long j = (i*i*2) + (i*6) + 3; (int) j >= 0 && j < cntOdds; j+=p) {
            isPrime.set((int) j, false);
          }
        }
      }
      return primes;
     }
  }

  /**
   * O(n^(3/2) / (log n)^2) .... pretty expensive
   */
  private static class PrimeSieverBasic extends AbstractPrimeSiever {

    @Override
    ArrayList<Integer> generatePrimes(int n) {
      ArrayList<Integer> primes = new ArrayList<>();
      if (n < 2) return primes;
      // isPrime.get(p) represents whether p is prime.
      // Initially, 0 and 1 are set false. 2..n are set true. Use sieving to eliminate non-primes.
      List<Boolean> isPrime = new ArrayList<>(Collections.nCopies(n + 1, true));
      isPrime.set(0, false);
      isPrime.set(1, false);
      for(int p = 2; p <= n; ++p) {
        if (isPrime.get(p)) {
          primes.add(p);
          for (int j=p; j <= n; j+=p) // sieve multiples of p
            isPrime.set(j, false);
        }
      }
      return primes;
    }
  }

  public static void main(String[] args) throws Exception {
    PrimeSieverBasic primeSieverBasic = new PrimeSieverBasic();
    PrimeSiever primeSiever = new PrimeSiever();
    Random rgen = new Random();
    int n = rgen.nextInt(10000) + 10000;
    Callable<CloneableTestInputsMap> formInput = () -> {
//      Random rgen = new Random();
      CloneableTestInputsMap inputs = new CloneableTestInputsMap();
//      inputs.addInteger("n", rgen.nextInt(99999) + 2); //
      inputs.addInteger("n", n);
      return inputs;
    };

    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg1 = (inputs) ->
            primeSieverBasic.generatePrimes(inputs.getInteger("n"));
    Function<CloneableTestInputsMap, ArrayList<Integer>> runAlg2 = (inputs) ->
            primeSiever.generatePrimes(inputs.getInteger("n"));
    Supplier<ArrayList<Integer>> emptyOutput = ArrayList::new;
    Function<ArrayList<Integer>, Boolean> checkResults = (observed) -> {
      for (Integer prime : observed) {
        for (int i = 2; i < prime; ++i) {
          if (prime % i == 0) return false;
        }
      }
      return true;
    };

    System.out.println("For finding primes up to " + n + "...");
    TimeTests<ArrayList<Integer>> algTimer1 = new TimeTests<>(formInput, runAlg1, emptyOutput, "Basic Prime Sieve");
    TimeTests<ArrayList<Integer>> algTimer2 = new TimeTests<>(formInput, runAlg2, emptyOutput, "Improved Prime Sieve");

    PrintStream originalStream = MiscHelperMethods.setSystemOutToDummyStream();
    algTimer1.timeAndCheck(100, checkResults); // checking is pretty expensive
    algTimer2.timeAndCheck(100, checkResults); // checking is pretty expensive

    System.setOut(originalStream);
    algTimer1.time(NUM_TESTS);
    algTimer2.time(NUM_TESTS);
  }

}

