package epi.solutions.helper;

/**
 * Created by psingh on 5/20/16.
 */
public class AlgCompleteData<outputType> {
  public CloneableTestInputsMap _orig_inputs;
  public outputType _expectedResults;
  public outputType _observedResults;

  public AlgCompleteData(CloneableTestInputsMap orig_inputs, outputType expectedResults, outputType observedResults) {
    _orig_inputs = orig_inputs; _expectedResults = expectedResults; _observedResults = observedResults;
  }
}
