package epi.solutions.helper;

import java.util.ArrayList;

public class CloneableArrayList extends ArrayList implements CloneableTestInput<CloneableArrayList> {

  public CloneableArrayList() {  }
  public CloneableArrayList(ArrayList input) {
    this.addAll(input);
  }

  @Override
  public CloneableArrayList cloneInput() {
    return new CloneableArrayList(this);
  }

  public static void main(String[] args) {
    CloneableArrayList testInput = new CloneableArrayList();
    testInput.add(1);
    CloneableArrayList clonedInput = testInput.cloneInput();
    clonedInput.add(2);
    System.out.println("testInput: " + testInput);
    System.out.println("clonedInput: " + clonedInput);
  }
}