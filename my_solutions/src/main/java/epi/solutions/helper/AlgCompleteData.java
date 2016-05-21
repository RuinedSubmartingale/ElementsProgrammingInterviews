package epi.solutions.helper;

import java.util.HashMap;

/**
 * Created by psingh on 5/20/16.
 */
class AlgCompleteData<outputType> {
  CloneableTestInputsMap _orig_inputs;
  outputType _expectedResults;
  outputType _observedResults;
  HashMap<String, Object> _algExtraResults;

  AlgCompleteData(CloneableTestInputsMap orig_inputs, outputType expectedResults, outputType observedResults
          , HashMap<String, Object> algExtraResults) {
    _orig_inputs = orig_inputs;
    _expectedResults = expectedResults;
    _observedResults = observedResults;
    _algExtraResults = algExtraResults;
  }
}
