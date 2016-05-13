package epi.solutions.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by psingh on 5/12/16.
 */
public class ReferenceToInstanceMethodExample {

  /**
   * @param: the command line arguments
   */
  private static class Person {

    private final String name;
    private final int age;


    public Person(String name, int age) {
      this.name = name;
      this.age = age;

    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }


  }

  public static void main(String[] args) {
    // TODO code application logic here
    List persons = new ArrayList();
    persons.add(new Person("Albert", 80));
    persons.add(new Person("Ben", 15));
    persons.add(new Person("Charlote", 20));
    persons.add(new Person("Dean", 6));
    persons.add(new Person("Elaine", 17));


    List allAges = ReferenceToInstanceMethodExample.listAllAges(persons, Person::getAge);
    System.out.println("Printing out all ages \n"+allAges);
  }


  private static List listAllAges(List<Person> person, Function < Person, Integer > f){
    List result = new ArrayList();
    person.forEach(x -> result.add(f.apply(x)));
    return result;
  }
}