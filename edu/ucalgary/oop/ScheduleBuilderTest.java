package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Terminal Commands
 * the following commands can be used for...
 * code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
 * code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
 * test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar edu/ucalgary/oop/ScheduleBuilderTest.java
 * test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest
 */

public class ScheduleBuilderTest{ 

    private static Connection dbConnect;

    // TESTING TASK

    // Task Values
    int expectedTaskId = 2;
    String expectedTaskDescr = "Rebandage leg wound";
    int expectedDuration = 20;
    int expectedMaxWindow = 1;
    int expectedTotalTime = 20;

    int expectedTaskIdWP = 12; // an example TaskID that may not necessarly be reflective of current database
    String expectedTaskDescrWP = "Feeding - Coyote";
    int expectedDurationWP = 5;
    int expectedPrepTime = 10;
    int expectedMaxWindowWP = 1;
    int expectedTotalTimeWP = 15;

    Task sharedTaskobj = new Task(expectedTaskId, expectedTaskDescr, expectedDuration, expectedMaxWindow);
    Task sharedTaskobjWP = new Task(expectedTaskIdWP, expectedTaskDescrWP, expectedDurationWP, expectedPrepTime, expectedMaxWindowWP);

    @BeforeClass
    public static void setUp() throws SQLException {
        dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
    }

    // Testing constructor without PrepTime of Task class
    @Test 
    public void testTaskWOPrepTime() {
        int actualTaskId = sharedTaskobj.getTaskID();
        String actualTaskDescr = sharedTaskobj.getDescription();
        int actualDuration = sharedTaskobj.getDuration();
        int actualMaxWindow = sharedTaskobj.getMaxWindow();
        int actualTotalTime = sharedTaskobj.getTotalTime();

        assertEquals("Constructor or getter gave wrong value for Task ID.", expectedTaskId, actualTaskId);
        assertEquals("Constructor or getter gave wrong value for Task Description.", expectedTaskDescr, actualTaskDescr);
        assertEquals("Constructor or getter gave wrong value for Task Duration.", expectedDuration, actualDuration);
        assertEquals("Constructor or getter gave wrong value for Task Max Window.", expectedMaxWindow, actualMaxWindow);
        assertEquals("Constructor or getter gave wrong value for Task Total Time.", expectedTotalTime, actualTotalTime);
    }

    // Testing constructor with PrepTime of Task class
    @Test 
    public void testTaskWPrepTime() {
        int actualTaskIdWP = sharedTaskobjWP.getTaskID();
        String actualTaskDescrWP = sharedTaskobjWP.getDescription();
        int actualDurationWP = sharedTaskobjWP.getDuration();
        int actualPrepTime = sharedTaskobjWP.getPrepTime();
        int actualMaxWindowWP = sharedTaskobjWP.getMaxWindow();
        int actualTotalTimeWP = sharedTaskobjWP.getTotalTime();

        assertEquals("Constructor or getter gave wrong value for Task ID with Prep Time.", expectedTaskIdWP, actualTaskIdWP);
        assertEquals("Constructor or getter gave wrong value for Task Description with Prep Time.", expectedTaskDescrWP, actualTaskDescrWP);
        assertEquals("Constructor or getter gave wrong value for Task Duration with Prep Time.", expectedDurationWP, actualDurationWP);
        assertEquals("Constructor or getter gave wrong value for Task Prep Time.", expectedPrepTime, actualPrepTime);
        assertEquals("Constructor or getter gave wrong value for Task Max Window with Prep Time.", expectedMaxWindowWP, actualMaxWindowWP);
        assertEquals("Constructor or getter gave wrong value for Task Total Time with Prep Time.", expectedTotalTimeWP, actualTotalTimeWP);
    }

    // testing storeHashMap
    @Test 
    public void testTaskStoreHashMap() {
        try {
            // Create a dummy task in the database
            Statement insertStmt = dbConnect.createStatement();
            insertStmt.executeUpdate("INSERT INTO TASKS (TaskID, Description, Duration, MaxWindow) VALUES (21, 'Test task', 10, 2)");
            insertStmt.close();
            
            // Run the storeHashMap() method
            Task testpls = new Task();
            testpls.storeHashMap();
            HashMap<Integer, Task> tasks = Task.getTasks();
            
            // Check that the dummy task was loaded from the database
            Task testTask = tasks.get(21);
            assertNotNull(testTask);
            assertEquals("Test task", testTask.getDescription());
            assertEquals(10, testTask.getTotalTime());
            assertEquals(2, testTask.getMaxWindow());
            
            // Check that the cage cleaning and feeding tasks were added to the database and the HashMap
            Statement selectStmt = dbConnect.createStatement();
            ResultSet results = selectStmt.executeQuery("SELECT * FROM TASKS WHERE Description LIKE 'Cage cleaning%' OR Description LIKE 'Feeding%'");
            int rowCount = 0;
            while (results.next()) {
                rowCount++;
                int taskID = results.getInt("TaskID");
                String description = results.getString("Description");
                int duration = results.getInt("Duration");
                int maxWindow = results.getInt("MaxWindow");
                Task task = tasks.get(taskID);
                assertNotNull(task);
                assertEquals(description, task.getDescription());
                assertEquals(duration, task.getTotalTime());
                assertEquals(maxWindow, task.getMaxWindow());
            }
            assertEquals(6, rowCount); // There should be 3 feeding tasks and 3 cage cleaning tasks
            results.close();
            selectStmt.close();
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown: " + e.getMessage());
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    // Testing setter and getter functions of Task class
    @Test
    public void testTaskSettersGetters() {
        Task testSetGet = new Task(0, null, 0, 0, 0);
        testSetGet.setTaskID(14);;
        int actualTaskId = testSetGet.getTaskID();
        testSetGet.setDescription("Feeding - Fox");  
        String actualTaskDescr = testSetGet.getDescription();
        testSetGet.setDuration(5);
        int actualDuration = testSetGet.getDuration();
        testSetGet.setPrepTime(5);
        int actualPrepTime = testSetGet.getPrepTime();
        testSetGet.setMaxWindow(3);
        int actualMaxWindow = testSetGet.getMaxWindow();
        testSetGet.setTotalTime(10);
        int actualTotalTime = testSetGet.getTotalTime();
        

        assertEquals("Constructor or getter gave wrong value for Task ID.", 14, actualTaskId);
        assertEquals("Constructor or getter gave wrong value for Task Description.", "Feeding - Fox", actualTaskDescr);
        assertEquals("Constructor or getter gave wrong value for Task Duration.", 5, actualDuration);
        assertEquals("Constructor or getter gave wrong value for Task Prep Time.", 5, actualPrepTime);
        assertEquals("Constructor or getter gave wrong value for Task Max Window.", 3, actualMaxWindow);
        assertEquals("Constructor or getter gave wrong value for Task Total Time with Prep Time.", 10, actualTotalTime);
    }

    // TESTING SCHEDULE

    // testing default constructor

    // testing constructor with only task, animal, startTime, and timeSpent arguments
    @Test 
    public void testScheduleTAST() {
        Schedule schedule = new Schedule(1, "Grooming", "Belly", 10, 30);
        assertEquals(1, schedule.getTreatmentIndices().size());
        assertEquals("Grooming", schedule.getTask());
        assertEquals(1, schedule.getAnimalList().size());
        assertTrue(schedule.getAnimalList().contains("Belly"));
        assertEquals(10, schedule.getStartTime());
        assertEquals(30, schedule.getTimeSpent());
    }

    // testing valid input parameters when backup is not required for Schedule class
    @Test 
    public void testScheduleConstructorNOBackup() {
        Schedule schedule = new Schedule(1, "Rebandage Leg Wound", "Foxy", 13, 1, 20, 40, false);
        assertEquals(1, schedule.getTreatmentIndices().size());
        assertEquals("Rebandage Leg Wound", schedule.getTask());
        assertEquals(1, schedule.getAnimalList().size());
        assertTrue(schedule.getAnimalList().contains("Foxy"));
        assertEquals(13, schedule.getStartTime());
        assertEquals(1, schedule.getQuantity());
        assertEquals(20, schedule.getTimeSpent());
        assertEquals(40, schedule.getTimeRemaining());
        assertFalse(schedule.getBackupRequired());
    }

    // testing valid input parameters when backup is required
    @Test 
    public void testScheduleConstructorBackup() {
        Schedule schedule = new Schedule(1, "Medical Treatments", "Coy", 15, 2, 70, 50, true);
        assertEquals(1, schedule.getTreatmentIndices().size());
        assertEquals("Medical Treatments", schedule.getTask());
        assertEquals(1, schedule.getAnimalList().size());
        assertTrue(schedule.getAnimalList().contains("Coy"));
        assertEquals(15, schedule.getStartTime());
        assertEquals(2, schedule.getQuantity());
        assertEquals(70, schedule.getTimeSpent());
        assertEquals(50, schedule.getTimeRemaining());
        assertTrue(schedule.getBackupRequired());
    }

    // testing setters and getters for Schedule class
    

    // // testing createTaskString() for Schedule class
    @Test 
    public void testScheduleCreateTaskString() {
        Schedule testScheduleCreateTaskString = new Schedule(1, "Rebandage Leg Wound", "Bubble", 13, 1, 20, 40, false);
        testScheduleCreateTaskString.createTaskString();
        assertEquals("* Rebandage Leg Wound (1: Bubble)", testScheduleCreateTaskString.getTaskString());

        // adding another animal to the list
        testScheduleCreateTaskString.setAnimalList("Bubs");
        testScheduleCreateTaskString.createTaskString();
        assertEquals("* Rebandage Leg Wound (2: Bubble, Bubs)", testScheduleCreateTaskString.getTaskString());
    }

    // TESTING SCHEDULEBUILDER

    // testing constructor for ScheduleBuilder class


    // testing createSchedule() in ScheduleBuilder class
    
    //////Start of treatment.java testing:

    // @Test //if first constructor  passes valid values for all parameters
    // public void testAllTreatmentGetters (){
    //     Treatment t = new Treatment(1, 2, 3, 10);
    //     assertEquals(1, t.getTreatementID());
    //     assertEquals(2, t.getAnimalID());
    //     assertEquals(3, t.getTaskID());
    //     assertEquals(10, t.getStartHour());
        
    // }
    // @Test //if the second constructor passes valid values for treatmentID, animalID, and taskID
    // public void testSecondTreatmentGetters (){
    //     Treatment t = new Treatment(1, 2, 3);
    //     assertEquals(1, t.getTreatementID());
    //     assertEquals(2, t.getAnimalID());
    //     assertEquals(3, t.getTaskID());
        
    // } 
    // @Test // if the setter methods is setting new values for each property
    // public void testTreatmentSetters (){
    //     Treatment t = new Treatment(1, 2, 3);
    //     t.setTreatementID(4);
    //     t.setAnimalID(5);
    //     t.setTaskID(6);
    //     t.setStartHour(12);
    //     assertEquals(4, t.getTreatementID());
    //     assertEquals(5, t.getAnimalID());
    //     assertEquals(6, t.getTaskID());
    //     assertEquals(12, t.getStartHour());        
        
    // } 
    // @Test //if the first constructor is passing an invalid value for the startHour parameter (less than 0)
    // public void testConstructorWithInvalidStartHour(){
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         Treatment t = new Treatment(1, 2, 3, -1);
    //     });
        
    // }
    // @Test //if the first constructor is passing an invalid value for the startHour parameter (greater than or equal to 24)
    // public void testConstructorWithInvalidStartHourOutOfRange(){
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         Treatment t = new Treatment(1, 2, 3, 24);
    //     });
        
    // }
    // @Test //if the second constructor is passing an invalid value for the treatmentID parameter (less than or equal to 0)
    // public void testConstructorWithInvalidTreatmentID(){
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         Treatment t = new Treatment(0, 2, 3);
    //     });
        
    // }
    // @Test //if the second constructor is passing an invalid value for the animalID parameter (less than or equal to 0)
    // public void testConstructorWithInvalidAnimalID(){
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         Treatment t = new Treatment(1, 0, 3);
    //     });
        
    // }
    // @Test //if the second constructor is passing an invalid value for the taskID parameter (less than or equal to 0)
    // public void testConstructorWithInvalidTaskID(){
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         Treatment t = new Treatment(1, 2, 0);
    //     });
        
    // }
    @AfterClass
    public static void tearDown() throws SQLException {
        dbConnect.close();
    }

}

