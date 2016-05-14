package epi.solutions.helper;

import java.util.ArrayList;
import java.util.List;

public class CloneableList extends ArrayList implements CloneableTestInput<CloneableList> {

  public CloneableList() {  }
//
//  public CloneableArrayList(CloneableArrayList input) {
//    this.addAll(input);
//  }

  public CloneableList(List input) {
    this.addAll(input);
  }

  @Override
  public CloneableList clone() {
    return new CloneableList(this);
  }

  public static void main(String[] args) {
    CloneableList testInput = new CloneableList();
    testInput.add(1);
    testInput.add(2);
    testInput.add(3);
    CloneableList clonedInput = testInput.clone();
    clonedInput.add(4);
    System.out.println(testInput + ": ");
    System.out.println(clonedInput + ": ");
  }
}