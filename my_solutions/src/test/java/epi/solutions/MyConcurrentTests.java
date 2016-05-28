package epi.solutions;

import net.jodah.concurrentunit.ConcurrentTestCase;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.Timeout;
import org.junit.runner.Description;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psingh on 5/27/16.
 */
public class MyConcurrentTests extends ConcurrentTestCase {
  private static final String[] NO_ARGS = new String[0];
  //  static final String[] SMALL_ARGS = {"10", "10"};
  private static final String[] ARGS = NO_ARGS;
  private static long maxTimePerMethod = 100000;

  private static final Logger logger = Logger.getLogger("");
  private static void logInfo(Description desc, String status, long nanos) {
    String displayName = desc.getDisplayName();
//    String methodName = desc.getMethodName();
//    Class testClass = desc.getTestClass();
    logger.info(String.format("%s : status %s : %d milliseconds"
            , displayName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
  }

    @ClassRule
  public static ExpectedException thrown = ExpectedException.none();

  @ClassRule
  public static Stopwatch stopwatch = new Stopwatch() {
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

  @BeforeClass
  public static void setup() {
//    Logger logger = Logger.getLogger("MyLog");
//    FileHandler fh;
    String timeoutString = System.getProperty("timeout");
    if (timeoutString != null) {
      System.out.println("timeout (in msec) = " + timeoutString);
    } else System.out.println("timeout (in msec) = " + maxTimePerMethod);
  }

  @Test
  public void shouldFailIn1() throws Throwable {
    new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
//        e.printStackTrace();
      }
      threadAssertTrue(false);
      resume();
    }).start();
    new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
//        e.printStackTrace();
      }
      threadAssertTrue(true);
      resume();
    }).start();
    await(5, TimeUnit.SECONDS, 2);
  }

  @Test
  public void shouldFailIn3() throws Throwable {
    new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
//        e.printStackTrace();
      }
      threadAssertTrue(true);
      resume();
    }).start();
    new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
//        e.printStackTrace();
      }
      threadAssertTrue(false);
      resume();
    }).start();
    await(5, TimeUnit.SECONDS, 2);
  }

}
