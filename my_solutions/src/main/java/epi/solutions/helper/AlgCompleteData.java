package epi.solutions.helper;

/**
 * Created by psingh on 5/20/16.
 * Used by TimeTests. Has sufficient information about the algorithm to check its correctness.
 */
class AlgCompleteData<outputType> {
  CloneableInputsMap _orig_inputs;
  outputType _expectedResults;
  outputType _observedResults;
  CloneableInputsMap _algExtraResults;
  CloneableInputsMap _expectedExtraResults;

  AlgCompleteData(CloneableInputsMap orig_inputs, outputType expectedResults, outputType observedResults
          , CloneableInputsMap algExtraResults, CloneableInputsMap expectedExtraResults) {
    _orig_inputs = orig_inputs;
    _expectedResults = expectedResults;
    _observedResults = observedResults;
    _algExtraResults = algExtraResults;
    _expectedExtraResults = expectedExtraResults;
  }
}
