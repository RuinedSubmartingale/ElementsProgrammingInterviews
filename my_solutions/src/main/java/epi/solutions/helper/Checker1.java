package epi.solutions.helper;

import java.util.function.BiFunction;

public class Checker1<outputType> implements algResultsChecker {

  BiFunction<CloneableTestInputsMap, outputType, Boolean> _checker;
  CloneableTestInputsMap _inputs;
  outputType _output;

  public Checker1(BiFunction<CloneableTestInputsMap, outputType, Boolean> checker, CloneableTestInputsMap inputs, outputType output) {
    _checker = checker;
    _inputs = inputs;
    _output = output;
  }

  @Override
  public Boolean check() {
    return _checker.apply(_inputs, _output);
  }
}
