**Pet Scheduling Program**
Ever had to manage a centre of pets that require medicine, certain care, and certain foods given a limited amount of staff? This java program uses a clean GUI and an algorithm to sort through a database to create the perfect schedule to account for all pet needs.

Included: all required code, UML, and JUnitTesting for all functions.  

Group: Isha Haider, Saba Sadoughi Yarandi, Zahwa Fatima, Nessma Mohdy
Recieved Grade: 100%

Instructions (Terminal Commands ) on running the code and tests:
- code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
- code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
- test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilderTest.java
- test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest

Comment for compiling and executing code:
please ensure to refresh the SQL database after executing ScheduleBuilderTest file, before executing any other file. This is because through one of the test methods we add values to the SQL database through the Java application.
