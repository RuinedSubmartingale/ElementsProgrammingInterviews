package epi.solutions.helper;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by psingh on 7/17/16.
 */
public class AlgorithmRunnerAndVerifier<outputType> extends AlgorithmFactory<CloneableInputsMap, outputType> {
  private Function<CloneableInputsMap, outputType> _knownSolution;
  private Function<CloneableInputsMap, CloneableInputsMap> _saveExtraResults;
  private CloneableInputsMap _expectedInputs;
  private CloneableInputsMap _observedInputs;
  private outputType _expectedOutput;
  private outputType _observedOutput;
  private CloneableInputsMap _expectedExtraData;
  private CloneableInputsMap _observedExtraData;
  private Function<AlgVerificationData<outputType, CloneableInputsMap>, Boolean> _algVerifier;
  private Algorithm<CloneableInputsMap, outputType> _knownAlgInstance;
  private boolean _verifyAgainstKnownOutput;


  private AlgorithmRunnerAndVerifier(String desc, int numTests
          , Supplier<CloneableInputsMap> formInputs
          , Function<CloneableInputsMap, outputType> algRunner
          , Function<CloneableInputsMap, outputType> knownSolution
          , Function<CloneableInputsMap, CloneableInputsMap> saveExtraResults
          , Function<AlgVerificationData<outputType, CloneableInputsMap>, Boolean> algVerifier) {
    super(desc, numTests, formInputs);
    this._algRunner = algRunner;
    this._knownSolution = knownSolution;
    this._saveExtraResults = Optional.ofNullable(saveExtraResults).orElseGet(() -> (input) -> input);
    this._verifyAgainstKnownOutput = true;
    _algVerifier = algVerifier;
  }

  public AlgorithmRunnerAndVerifier(String desc, int numTests
          , Supplier<CloneableInputsMap> formInputs
          , Function<CloneableInputsMap, outputType> algRunner
          , Function<CloneableInputsMap, outputType> knownSolution
          , Function<CloneableInputsMap, CloneableInputsMap> saveExtraResults
          , AlgVerifierInterfaces<outputType, CloneableInputsMap> checkResults) {
    this(desc, numTests, formInputs, algRunner, knownSolution, saveExtraResults, checkResults.verify());
  }

  public AlgorithmRunnerAndVerifier(String desc, int numTests
          , Supplier<CloneableInputsMap> formInputs
          , Function<CloneableInputsMap, outputType> algRunner
          , Function<CloneableInputsMap, outputType> knownSolution
          , AlgVerifierInterfaces<outputType, CloneableInputsMap> checkResults) {
    this(desc, numTests, formInputs, algRunner, knownSolution, null, checkResults.verify());
  }

  public AlgorithmRunnerAndVerifier(String desc, int numTests
          , Supplier<CloneableInputsMap> formInputs
          , Function<CloneableInputsMap, outputType> algRunner
          , AlgVerifierInterfaces<outputType, CloneableInputsMap> checkResults) {
    this(desc, numTests, formInputs, algRunner, null, null, checkResults.verify());
    shouldVerifyAgainstKnownOutput(false);
  }

  public AlgorithmRunnerAndVerifier(String desc, int numTests
          , Supplier<CloneableInputsMap> formInputs
          , Function<CloneableInputsMap, outputType> algRunner) {
    // the explicit cast on last parameter below is needed to avoid an ambiguous constructor call with null parameters
    this(desc, numTests, formInputs, algRunner, null, null, (Function<AlgVerificationData<outputType, CloneableInputsMap>, Boolean>) null);
    shouldVerify(false);
  }

  @Override
  Algorithm<CloneableInputsMap, outputType> createAlgInstance(CloneableInputsMap inputs, Function<CloneableInputsMap, outputType> runner) {
    return new AlgorithmImpl<>(inputs, runner);
  }


  @Override
  void beforeRun(Algorithm<CloneableInputsMap, outputType> algorithm) {
    if (_shouldVerify && !_tmpSkipVerification) {
      if(_verifyAgainstKnownOutput) {
        _expectedInputs = cloneInputs(algorithm.getInputs());
        _knownAlgInstance = createAlgInstance(_expectedInputs, _knownSolution);

        _expectedOutput = _knownSolution.apply(_expectedInputs);
        _expectedExtraData = _saveExtraResults.apply(_expectedInputs);
      }
    }
  }

  @Override
  void afterRun(Algorithm<CloneableInputsMap, outputType> algorithm) {
    if (_shouldVerify && !_tmpSkipVerification) {
      _observedOutput = algorithm.getOutputs();
      _observedInputs = algorithm.getInputs();
      _observedExtraData = _saveExtraResults.apply(_observedInputs);
      AlgVerificationData<outputType, CloneableInputsMap> algData = new AlgVerificationDataImpl<>(_expectedOutput, _observedOutput, _expectedExtraData, _observedExtraData);
      assert(testCorrectness(algData));
    }
  }

  private Boolean testCorrectness(AlgVerificationData<outputType, CloneableInputsMap> algVerificationData) {
    return _algVerifier.apply(algVerificationData);
  }

  private CloneableInputsMap cloneInputs(CloneableInputsMap inputs) {
    CloneableInputsMap orig_inputs = new CloneableInputsMap();
    inputs.forEach((name, inputType) -> {
      try {
        orig_inputs.put(name, inputType.cloneInput());
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    return orig_inputs;
  }

  private void shouldVerify(boolean b) { this._shouldVerify = b; }

  private void shouldVerifyAgainstKnownOutput(boolean b) {
    this._verifyAgainstKnownOutput = b;
  }
}
