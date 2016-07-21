package epi.solutions.helper;

/**
 * Created by psingh on 7/17/16.
 */
abstract class Algorithm<inputType, outputType> implements Runnable {
  private final inputType _inputs;
  private outputType _outputs;
  Algorithm(inputType inputs) { _inputs = inputs; }
  inputType getInputs() { return _inputs; }
  outputType getOutputs() { return _outputs; }

  void setOutputs(outputType outputs) {
    this._outputs = outputs;
  }
}

