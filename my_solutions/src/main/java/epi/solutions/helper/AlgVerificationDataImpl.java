package epi.solutions.helper;

/**
 * Created by psingh on 7/17/16.
 */
public class AlgVerificationDataImpl<outputType> extends AlgVerificationData<outputType, CloneableInputsMap> {
  AlgVerificationDataImpl(outputType expectedResults, outputType observedResults
          , CloneableInputsMap expectedExtraResults, CloneableInputsMap observedExtraResults) {
    super(expectedResults, observedResults, expectedExtraResults, observedExtraResults);
  }
  AlgVerificationDataImpl(outputType expectedResults, outputType observedResults) {
    super(expectedResults, observedResults, null, null);
  }
  AlgVerificationDataImpl(CloneableInputsMap inputs, outputType expectedResults) {
    super(expectedResults, null, null, null);
  }
  AlgVerificationDataImpl() {super(null, null, null, null);}
}
