package epi.solutions.helper;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public class OutputComparisonVerifier<outputType, extraType> extends AlgVerifierInterfaces<outputType, extraType> {
  private BiFunction<outputType, outputType, Boolean> _fn;

  public OutputComparisonVerifier(BiFunction<outputType, outputType, Boolean>  fn) {
    _fn = fn;
  }

  @Override
  Class getFnType() {
    return _fn.getClass();
  }

  @Override
  Function<AlgVerificationData<outputType, extraType>, Boolean> verify() {
    return algVerificationData -> _fn.apply(algVerificationData.getObservedOutput(), algVerificationData.getExpectedOutput());
  }
}
