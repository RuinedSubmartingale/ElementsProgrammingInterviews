package epi.solutions.helper;

import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public class OutputOutputExtraVerifier<outputType, extraType> extends AlgVerifierInterfaces<outputType, extraType> {
  private TriFunction<outputType, outputType, extraType, Boolean> _fn;

  public OutputOutputExtraVerifier(TriFunction<outputType, outputType, extraType, Boolean> fn) {
    this._fn = fn;
  }

  @Override
  Class getFnType() {
    return _fn.getClass();
  }

  @Override
  Function<AlgVerificationData<outputType, extraType>, Boolean> verify() {
    return algVerificationData -> _fn.apply(algVerificationData.getObservedOutput(), algVerificationData.getExpectedOutput()
            , algVerificationData.getObservedExtraResults());
  }
}
