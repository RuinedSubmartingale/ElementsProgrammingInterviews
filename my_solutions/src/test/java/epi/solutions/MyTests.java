package epi.solutions;

import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.Timeout;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Created by psingh on 3/14/16.
 * JUnit tests for EPI solutions
 */
public class MyTests {

  private static final String[] NO_ARGS = new String[0];
//  static final String[] SMALL_ARGS = {"10", "10"};
  private static final String[] ARGS = NO_ARGS;
  private static long maxTimePerMethod = 1000000;

  private static final Logger logger = Logger.getLogger("");
  private static void logInfo(Description desc, String status, long nanos) {
    String displayName = desc.getDisplayName();
//    String methodName = desc.getMethodName();
//    Class testClass = desc.getTestClass();
    logger.info(String.format("%s : status %s : %d milliseconds"
            , displayName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
  }


  @Rule
  public Timeout globalTimeout = new Timeout(maxTimePerMethod, TimeUnit.MILLISECONDS);

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Rule
  public Stopwatch stopwatch = new Stopwatch() {
    @Override
    public long runtime(TimeUnit unit) {
      return super.runtime(unit);
    }

    @Override
    protected void succeeded(long nanos, Description description) {
      logInfo(description, "succeeded", nanos);
      super.succeeded(nanos, description);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
      logInfo(description, "failed", nanos);
      super.failed(nanos, e, description);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
      logInfo(description, "skipped", nanos);
      super.skipped(nanos, e, description);
    }

    @Override
    protected void finished(long nanos, Description description) {
//      logInfo(description, "finished", nanos);
//      super.finished(nanos, description);
    }
  };

  @Before
  public void setup() {
//    Logger logger = Logger.getLogger("MyLog");
//    FileHandler fh;
    String timeoutString = System.getProperty("timeout");
    if (timeoutString != null) {
      globalTimeout = new Timeout(new Long(timeoutString), TimeUnit.MILLISECONDS);
      System.out.println("timeout (in msec) = " + timeoutString);
    } else System.out.println("timeout (in msec) = " + maxTimePerMethod);
  }

  @Test // 5.1
  public void Parity() throws Exception { Parity.main(ARGS); }

  @Test // 5.2
  public void SwapBits() throws Exception { SwapBits.main(ARGS); }

  @Test // 5.4
  public void ClosestIntSameBits() throws Exception { ClosestIntSameBits.main(ARGS); }

  @Test // 5.5
  public void MultiplyShiftAdd() throws Exception { MultiplyShiftAdd.main(ARGS); }

  @Test // 5.7
  public void PowerXY() throws Exception { PowerXY.main(ARGS); }

  @Test // 5.11
  public void isPalindrome() throws Exception { IsPalindrome.main(ARGS); }

  @Test // 5.12
  public void UnifRandomNumberGenerator() throws Exception { UnifRandomNumberGenerator.main(ARGS); }

  @Test // 6.1
  public void DutchFlagPartition() throws Exception { DutchFlagPartition.main(ARGS); }

  @Test // 6.2
  public void PlusOne() throws Exception { PlusOne.main(ARGS); }

  @Test // 6.4
  public void JumpBoardGame() throws Exception { JumpBoardGame.main(ARGS); }

  @Test // 6.5
  public void RemoveElement() throws Exception { RemoveElement.main(ARGS); }

  @Test // 6.6
  public void RemoveDuplicatesFromSortedArrayList() throws Exception { RemoveDuplicatesFromSortedArrayList.main(ARGS); }

  @Test // 6.9
  public void MaxDifferenceKPairs() throws Exception { MaxDifferenceKPairs.main(ARGS); }

  @Test // 6.10
  public void BiggestNMinus1Product() throws Exception { BiggestNMinus1Product.main(ARGS); }

  @Test // 6.12
  public void PrimeSieve() throws Exception { PrimeSieve.main(ARGS); }

  @Test // 6.13
  public void PermuteArray() throws Exception { PermuteArray.main(ARGS); }

  @Test // 6.16
  public void SampleOffline() throws Exception { SampleOffline.main(ARGS); }

  @Test // 7.1
  public void InterconvertingStringInteger() throws Exception { InterconvertingStringInteger.main(ARGS); }

  @Test // 7.2
  public void ReplaceAndRemove() throws Exception { ReplaceAndRemove.main(ARGS); }

  @Test // 13.2
  public void CanStringBePalindrome() throws Exception { CanStringBePalindrome.main(ARGS); }
}
