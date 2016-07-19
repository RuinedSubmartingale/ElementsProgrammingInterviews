package epi.solutions.helper;

/**
 * Created by psingh on 5/20/16.
 * Used by TimeTests. Has sufficient information about the algorithm to check its correctness.
 */
abstract class AlgVerificationData<outputType, extraType> {
  private outputType _expectedOutput;
  private outputType _observedOutput;
  private extraType _expectedExtraResults;
  private extraType _observedExtraResults;

  AlgVerificationData(outputType expectedResults, outputType observedResults
          , extraType expectedExtraResults, extraType observedExtraResults) {
    _expectedOutput = expectedResults;
    _observedOutput = observedResults;
    _expectedExtraResults = expectedExtraResults;
    _observedExtraResults = observedExtraResults;
  }

  outputType getExpectedOutput() { return this._expectedOutput; }
  outputType getObservedOutput() { return this._observedOutput; }
  extraType getExpectedExtraResults() { return this._expectedExtraResults; }
  extraType getObservedExtraResults() { return this._observedExtraResults; }

  public void setExpectedOutput(outputType expectedOutput) {
    this._expectedOutput = expectedOutput;
  }

  public void setObservedOutput(outputType observedOutput) {
    this._observedOutput = observedOutput;
  }

  public void setExpectedExtraResults(extraType expectedExtraResults) {
    this._expectedExtraResults = expectedExtraResults;
  }

  public void setObservedExtraResults(extraType observedExtraResults) {
    this._observedExtraResults = observedExtraResults;
  }
}
