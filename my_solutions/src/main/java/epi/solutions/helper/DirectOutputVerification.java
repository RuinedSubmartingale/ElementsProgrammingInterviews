package epi.solutions.helper;

import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public class DirectOutputVerification<inputType, outputType, extraType> extends AlgVerifierInterfaces<outputType, extraType> {
  private Function<outputType, Boolean> _fn;

  public DirectOutputVerification(Function<outputType, Boolean> fn) {
    _fn = fn;
  }

  @Override
  Class getFnType() {
    return _fn.getClass();
  }

  @Override
  Function<AlgVerificationData<outputType, extraType>, Boolean> verify() {
    return algVerificationData -> _fn.apply(algVerificationData.getObservedOutput());
  }
}
