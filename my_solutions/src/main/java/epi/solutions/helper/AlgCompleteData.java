package epi.solutions.helper;

/**
 * Created by psingh on 5/20/16.
 * Used by TimeTests. Has sufficient information about the algorithm to check its correctness.
 */
class AlgCompleteData<outputType> {
  CloneableTestInputsMap _orig_inputs;
  outputType _expectedResults;
  outputType _observedResults;
  CloneableTestInputsMap _algExtraResults;

  AlgCompleteData(CloneableTestInputsMap orig_inputs, outputType expectedResults, outputType observedResults
          , CloneableTestInputsMap algExtraResults) {
    _orig_inputs = orig_inputs;
    _expectedResults = expectedResults;
    _observedResults = observedResults;
    _algExtraResults = algExtraResults;
  }
}
