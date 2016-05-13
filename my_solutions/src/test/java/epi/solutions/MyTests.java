package epi.solutions;

import static org.junit.Assert.fail;

import javafx.scene.paint.Stop;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.Timeout;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Created by psingh on 3/14/16.
 */
public class MyTests {

  static final String[] NO_ARGS = new String[0];
  static final String[] SMALL_ARGS = {"10", "10"};
  static final String[] ARGS = NO_ARGS;
  static long maxTimePerMethod = 100000;

  private static final Logger logger = Logger.getLogger("");
  private static void logInfo(Description desc, String status, long nanos) {
    String displayName = desc.getDisplayName();
//        String methodName = desc.getMethodName();
    Class testClass = desc.getTestClass();
    logger.info(String.format("%s : status %s : %d milliseconds"
            , displayName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
  }


  @Rule
  public Timeout globalTimeout = new Timeout(maxTimePerMethod, TimeUnit.MILLISECONDS);

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
    String timeoutString = System.getProperty("timeout");
    if (timeoutString != null) {
      globalTimeout = new Timeout(new Long(timeoutString), TimeUnit.MILLISECONDS);
      System.out.println("timeout (in msec) = " + timeoutString);
    } else System.out.println("timeout (in msec) = " + maxTimePerMethod);
  }

  @Test // 5.1
  public void Parity() { Parity.main(ARGS); }

  @Test // 5.2
  public void SwapBits() { SwapBits.main(ARGS); }

  @Test // 5.5
  public void MultiplyShiftAdd() { MultiplyShiftAdd.main(ARGS); }

  @Test // 5.7
  public void PowerXY() { PowerXY.main(ARGS); }

  @Test // 5.11
  public void isPalindrome() { isPalindrome.main(ARGS); }

  @Test // 5.12
  public void UnifRandomNumberGenerator() { UnifRandomNumberGenerator.main(ARGS); }


}
