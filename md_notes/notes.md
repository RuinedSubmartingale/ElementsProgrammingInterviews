# Notes on Elements of Programming Interviews
##### Task: Setup `java` and `javac`
```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
sudo update-alternatives --list java
sudo update-alternatives --list javac
```

##### Task: Setup JUnit
```bash
sudo mv /usr/lib/junit/hamcrest-core-1.3.jar /usr/lib/junit/junit-4.12.jar /usr/share/java
sudo ln -s /usr/share/java/hamcrest-core-1.3.jar /usr/share/java/hamcrest.jar
sudo ln -s /usr/share/java/junit-4.12.jar /usr/share/java/junit.jar
sudo ln -s /usr/share/java/junit.jar /usr/share/java/junit-4.12.jar
```

##### Problem: assertions not working in JUnit tests/Java apps
Solution: http://stackoverflow.com/a/18168305/1446892 - need to set the JVM option to enable assertions
