package epi.solutions.helper;

/**
 * Created by psingh on 5/20/16.
 */
class AlgCompleteData<outputType> {
  CloneableTestInputsMap _orig_inputs;
  outputType _expectedResults;
  outputType _observedResults;

  AlgCompleteData(CloneableTestInputsMap orig_inputs, outputType expectedResults, outputType observedResults) {
    _orig_inputs = orig_inputs; _expectedResults = expectedResults; _observedResults = observedResults;
  }
}
