Group:
Isha Haider: 30140419
Saba Sadoughi Yarandi: 30139710
Zahwa Fatima: 30159309
Nessma Mohdy: 30147402

Instructions (Terminal Commands ) on running the code and tests:
- code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
- code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
- test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilderTest.java
- test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest


Comment for compiling and executing code:
please ensure to refresh the SQL database after executing ScheduleBuilderTest file, before executing any other file. This is because through one of the test methods we add values to the SQL database through the Java application.