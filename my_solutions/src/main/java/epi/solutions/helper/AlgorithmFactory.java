package epi.solutions.helper;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 7/17/16.
 */
public abstract class AlgorithmFactory<inputType, outputType> {
  // TODO: Decide whether to use a Supplier or Callable for _formInputs
  // Note that Callable can throw checked exceptions whereas Supplier cannot
  // see https://programmers.stackexchange.com/questions/254311/what-is-the-difference-between-callablet-and-java-8s-suppliert/322517#322517
  private Supplier<inputType> _formInputs;
  Function<inputType, outputType> _algRunner;
  private inputType _inputs;
  inputType _observedInputs;
  private final String _description;
  private boolean _parallel;
  private int _numTests;
  boolean _shouldVerify;        // try to move to concrete subclasses
  boolean _tmpSkipVerification;

  AlgorithmFactory(String desc, int numTests, Supplier<inputType> formInputs) {
    this._description = desc;
    this._numTests = numTests;
    this._formInputs = formInputs;
    this._parallel = true;
    this._shouldVerify = true;
//    setSequential();
  }
  String getDescription() { return this._description; }
  abstract Algorithm<inputType, outputType> createAlgInstance(inputType inputs, Function<inputType, outputType> runner);

//  abstract Algorithm<inputType, outputType> createKnownSolution(inputType inputs);

  abstract void beforeRun(Algorithm<inputType, outputType> algorithm);
  abstract void afterRun(Algorithm<inputType, outputType> algorithm);

  public void run(int numTests) throws Exception {
    setNumTests(numTests);
    run();
  }

  public void runSkipVerif() throws Exception {
    skipVerification();
    run();
  }

  public void run() throws Exception {
    logAlgorithmTitle();
    Long totalExecTime;
    if (_parallel) {
      totalExecTime = runParallel();
    } else {
      totalExecTime = runSequential();
    }
    logAlgorithmTime(totalExecTime);
  }

  private Long runSequential() throws Exception {
    Long totalExecTime = runCallable(_formInputs, _numTests).call();
    return totalExecTime;
  }

//  // TODO: consider removing this requirement. Only add to subclasses that need it in their afterRun() calls
//  abstract Boolean testCorrectness(AlgVerificationData algVerificationData);

  private Long runParallel() throws Exception {
    Runtime javaApp = Runtime.getRuntime();
    int nProcs = Math.max(javaApp.availableProcessors(), 1);
    ExecutorService execService = Executors.newFixedThreadPool(nProcs);
//    System.out.println("Using " + nProcs + " cores...");

    /* Following comments don't really apply anymore after having `synchronized` the majority of runCallable()
    * // Twice as many tasks as processors. Before adding `synchronized` requirements to some methods,
    * // I'd get full 100% CPU utilization with just nProcs tasks. But after `synchranized` requirements were added
    * // the algorithms would only utilize 75% cores at a time, and the cores would very frequently swap activity.
    * // Setting number of tasks to (nProcs * 2) seems to have taken care of this issue and got back to 100% CPU util
    * */
    int numTasks = nProcs;
    int numTestPerThread = _numTests / numTasks;
    Callable<Long> task;
    task = () -> runCallable(_formInputs, numTestPerThread).call();

    List<Callable<Long>> tasks = Collections.nCopies(numTasks, task);

    List<Future<Long>> futures = execService.invokeAll(tasks);
    Long totalExecTime = futures.stream()
            .map((future) -> {
              try {
                return future.get();
              } catch (Exception e){
                throw new IllegalStateException(e);
              }
            })
            .reduce((a,b) -> a + b).orElse(0L);
    execService.shutdown();
    return totalExecTime;
  }

  private void logAlgorithmTitle() {
    StringBuilder sb = new StringBuilder();
    sb.append("Running ").append(_description);
    if (_shouldVerify) {
      sb.append(" with verification");
    }
    sb.append(" - ").append(_numTests * 1.0 / Math.pow(10, 6)).append(" million tests");
    if (_parallel) {
      sb.append(" in parallel");
    }
    sb.append("...");
    System.out.println(sb.toString());
  }

  private void logAlgorithmTime(Long totalExecTime) {
    DecimalFormat df = new DecimalFormat("#.####");
    System.out.println(String.format("%s %50s %s", "DEBUG: ", _description, " took "
                + df.format(totalExecTime * 1.0 / _numTests)
                + " nanoseconds on average for " + _numTests * 1.0 / Math.pow(10, 6) + " million tests"));
  }

  private Callable<Long> runCallable(Supplier<inputType> formInputs, int numTestsPerThread) {
    return () -> {
      long total = 0, start;
      for (int i = 0; i < numTestsPerThread; ++i) {
        Algorithm<inputType, outputType> algorithm;

        /* I don't think the following _indented_ comments apply anymore...but honestly I'm not too sure about this at all
                 Adding syncrhonized here results in CPU cores swapping threads very frequrently...
                 ...if you have some good reason to synchronize this, then try setting numTasks = nProcs * 2
                 ...so that the CPU is more likely to have an available thread?? does that make sense? or would it
                 ...just result in more thread swapping?

            Parallel algorithm execution fails for more than half the algorithms if I take out this synchronization :/
          */
        synchronized(this) {
          _inputs = formInputs.get();
          algorithm = createAlgInstance(_inputs, _algRunner);
          beforeRun(algorithm);
          start = System.nanoTime();
          algorithm.run();
          total += System.nanoTime() - start;
          afterRun(algorithm);
        }
      }
      _tmpSkipVerification = false;
      return total;
    };
  }
  public void setSequential() {
    this._parallel = false;
  }

  private void skipVerification() { this._tmpSkipVerification = true; }

  public void setNumTests(int numTests) { this._numTests = numTests; }
}
