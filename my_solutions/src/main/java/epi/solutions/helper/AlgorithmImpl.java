package epi.solutions.helper;

import java.util.function.Function;

/**
 * Created by psingh on 7/17/16.
 */
class AlgorithmImpl<inputType, outputType> extends Algorithm<inputType, outputType> {
  private Function<inputType, outputType> _algRunner;
  AlgorithmImpl(inputType inputs, Function<inputType, outputType> algRunner) {
    super(inputs);
    _algRunner = algRunner;
  }

  @Override
  public void run() {
    this.setOutputs(_algRunner.apply(this.getInputs()));
  }
}