package epi.solutions.helper;

import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public class OutputOutputExtraExtraVerifier<outputType, extraType> extends AlgVerifierInterfaces<outputType, extraType> {
  QuadFunction<outputType, outputType, extraType, extraType, Boolean> _fn;

  public OutputOutputExtraExtraVerifier(QuadFunction<outputType, outputType, extraType, extraType, Boolean> fn) {
    this._fn = fn;
  }

  @Override
  Class getFnType() { return _fn.getClass(); }

  @Override
  Function<AlgVerificationData<outputType, extraType>, Boolean> verify() {
    return algVerificationData -> _fn.apply(algVerificationData.getObservedOutput(), algVerificationData.getExpectedOutput()
            , algVerificationData.getObservedExtraResults(), algVerificationData.getExpectedExtraResults());
  }
}
