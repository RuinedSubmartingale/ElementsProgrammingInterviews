package epi.solutions.helper;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public class InputOutputVerifier<outputType, extraType> extends AlgVerifierInterfaces<outputType, extraType> {
  BiFunction<outputType, extraType, Boolean> _fn;

  public InputOutputVerifier(BiFunction<outputType, extraType, Boolean>  fn) {
    _fn = fn;
  }

  @Override
  Class getFnType() {
    return _fn.getClass();
  }

  @Override
  Function<AlgVerificationData<outputType, extraType>, Boolean> verify() {
    return algVerificationData -> _fn.apply(algVerificationData.getObservedOutput(), algVerificationData.getObservedExtraResults());
  }
}
