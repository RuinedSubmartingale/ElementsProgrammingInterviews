package epi.solutions.helper;

import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
public abstract class AlgVerifierInterfaces<outputType, extraType>  {
  abstract Class getFnType();
  abstract Function<AlgVerificationData<outputType, extraType>, Boolean> verify();

  @FunctionalInterface
  public interface QuadFunction<A, B, C, D, E> {
    E apply(A a, B b, C c, D d);
  }
  @FunctionalInterface
  public interface TriFunction<A, B, C, D> {
    D apply(A a, B b, C c);
  }
}
