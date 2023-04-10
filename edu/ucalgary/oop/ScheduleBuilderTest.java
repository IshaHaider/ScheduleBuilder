/** 
 * @author ENSF380 Group 20: Zahwa Fatima, Saba Yarandi, Nessma Mohdy, Isha Haider
 * ScheduleBuilderTest is the java class containing all of the tests
 * @version     2.1
 * @since       1.0
*/
package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;
import java.beans.Transient;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Terminal Commands
 * the following commands can be used for...
 * code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
 * code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
 * test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilderTest.java
 * test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/mysql-connector-java-8.0.23.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest
 */

public class ScheduleBuilderTest{ 

    private static Connection dbConnect;

    @BeforeClass
    public static void setUp() throws SQLException {
        dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
    }

    // TESTING TASK CLASS
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

    // Testing default constructor
    public void testTaskDefaultConstructor() {
        Task taskDefault = new Task();
        assertEquals(0, taskDefault.getTaskID());
        assertNull(taskDefault.getDescription());
        assertEquals(0, taskDefault.getDuration());
        assertEquals(0, taskDefault.getPrepTime());
        assertEquals(0, taskDefault.getTotalTime());
        assertEquals(0, taskDefault.getMaxWindow());
        assertNull(taskDefault.getTasks());
    }

    // Testing constructor without PrepTime of Task class
    @Test 
    public void testTaskConstructorNoPrepTime() {
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
    public void testTaskConstructorPrepTime() {
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
                assertEquals(duration, task.getDuration());
                assertEquals(maxWindow, task.getMaxWindow());
            }
            assertEquals(10, rowCount); // There should be 5 feeding tasks and 5 cage cleaning tasks
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

    
    // TESTING ANIMAL CLASS
    
    @Test //Does the default constructor creates an object with null or default values for all data members
    public void testAnimalDefaultConstructor() {
        Animal animal = new Animal();
        assertNull(animal.getNickname());
        assertNull(animal.getSpecies());
        assertNull(animal.getMostActive());
        assertEquals(0, animal.getAnimalID());
    }
    
    @Test  // will the animal constructor create an Animal object without an exception and therefore are the getters working?
    public void testAnimalConstructor() {
        int expectedAnimalID = 1;
        String expectedAnimalNickname = "Olivia";
        String expectedAnimalSpecies = Species.COYOTE.toString();
        String expectedMostActive = "Crepuscular";
        
        Animal animal1 = new Animal(expectedAnimalID, expectedAnimalNickname, expectedAnimalSpecies, expectedMostActive);
        assertEquals("Constructor or getter gave wrong value for animalID", expectedAnimalID, animal1.getAnimalID());
        assertEquals("Constructor or getter gave wrong value for Animal Nickname", expectedAnimalNickname, animal1.getNickname());
        assertEquals("Constructor or getter gave wrong value for Animal species", expectedAnimalSpecies, animal1.getSpecies());
        assertEquals("Constructor or getter gave wrong value for MostActive", expectedMostActive, animal1.getMostActive());

        }

    @Test //test case checks if the storeHashMap() method can successfully retrieve animal 
          //records from the database and store them in a HashMap, as well as if the HashMap contains the expected values
          //also accounts for the exception
    public void testAnimalStoreHashMap() throws Exception {
        Connection dbConnect = null;
        Statement myStmt = null;
        ResultSet results = null;
    
        try {
            // Connect to the database
            myStmt = dbConnect.createStatement();
    
            // Insert test data into the animals table
            myStmt.executeUpdate("INSERT INTO animals (AnimalID, AnimalNickname, AnimalSpecies) VALUES (50, 'Fido', 'coyote')");
            myStmt.executeUpdate("INSERT INTO animals (AnimalID, AnimalNickname, AnimalSpecies) VALUES (51, 'Mittens', 'beaver')");
    
            // Get the number of rows in the animals table
            results = myStmt.executeQuery("SELECT COUNT(*) FROM animals");
            results.next();
            int expectedSize = results.getInt(1);
    
            // Create an Animal object and call storeHashMap() method
            Animal animal = new Animal();
            animal.storeHashMap();
    
            // Get the HashMap of animals and verify the size and contents
            HashMap<Integer, Animal> animalsMap = Animal.getAnimals();
            assertNotNull(animalsMap);
            assertEquals(expectedSize, animalsMap.size());
    
            Animal retrievedAnimal1 = animalsMap.get(50);
            assertNotNull(retrievedAnimal1);
            assertEquals(50, retrievedAnimal1.getAnimalID());
            assertEquals("Fido", retrievedAnimal1.getNickname());
            assertEquals("coyote", retrievedAnimal1.getSpecies());
            assertNotNull(retrievedAnimal1.getMostActive());
    
            Animal retrievedAnimal2 = animalsMap.get(51);
            assertNotNull(retrievedAnimal2);
            assertEquals(51, retrievedAnimal2.getAnimalID());
            assertEquals("Mittens", retrievedAnimal2.getNickname());
            assertEquals("beaver", retrievedAnimal2.getSpecies());
            assertNotNull(retrievedAnimal2.getMostActive());
        } catch (SQLException | NullPointerException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage()); 
    
        } finally {
            // Close database resources
            if (results != null) {
                results.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (dbConnect != null) {
                dbConnect.close();
            }

        }
    } 

    @Test
    // Testing setter and getter functions of Task class
    public void testAnimalSettersGetters() {
        Animal animalSetGet = new Animal (0, "", "", "Diurnal");
        animalSetGet.setAnimalID(40);
        int actualAnimalID = animalSetGet.getAnimalID();
        animalSetGet.setNickname("testNickname");
        String actualAnimalNickname = animalSetGet.getNickname();
        animalSetGet.setSpecies("beaver");
        String actualAnimalSpecies = animalSetGet.getSpecies();
        animalSetGet.setMostActive("Diurnal");
        String actualAnimalMostActive = animalSetGet.getMostActive();
        
        assertEquals("Constructor or getter gave wrong value for Animal ID.", 40, actualAnimalID);
        assertEquals("Constructor or getter gave wrong value for Animal Nickname.", "testNickname", actualAnimalNickname);
        assertEquals("Constructor or getter gave wrong value for Animal Species.", "beaver", actualAnimalSpecies);
        assertEquals("Constructor or getter gave wrong value for Animal Most Active Period.", "Diurnal", actualAnimalMostActive);
    }
    

    // TESTING TREATMENT CLASS

    @Test //Test Treatment() constructor:
    public void testDefaultTreatmentConstructor() {
        Treatment treatment = new Treatment();
        assertNull("Expected Animal object to be null using empty constructor", treatment.getAnimal());
        assertNull("Expected Task object to be null using empty constructor", treatment.getTask());
        assertEquals("Expected start hour to be set to default value 0 using empty constructor", 0, treatment.getStartHour());
    }

    @Test //Test Treatment(animal, task, startHour) constructor
    public void testTreatmentConstructorWithAllParams() {
        Animal animal = new Animal(5, "Lion", "coyote", "Crepuscular");
        Task task = new Task(2, "Feed", 10, 3);
        Treatment treatment = new Treatment(animal, task, 9);
        assertEquals("Expected Animal object to be set correctly using constructor", animal, treatment.getAnimal());
        assertEquals("Expected Task object to be set correctly using constructor", task, treatment.getTask());
        assertEquals("Expected start hour to be set correctly using constructor", 9, treatment.getStartHour());
    }
    
    @Test //Test Treatment(animal, task) constructor\\\
    public void testTreatmentConstructorWithTwoParams() {
        Animal animal = new Animal(12, "Giraffe", "beaver", "Diurnal");
        Task task = new Task(3, "Clean", 1, 1);
        Treatment treatment = new Treatment(animal, task);
        assertEquals("Expected Animal object to be set correctly using constructor", animal, treatment.getAnimal());
        assertEquals("Expected Task object to be set correctly using constructor", task, treatment.getTask());
        assertEquals("Expected start hour to be set to default value 0 using constructor", 0, treatment.getStartHour());
    }

    public void testTreatmentStoreHashMap() throws SQLException {
        Statement insertStmt = null;
        try {
            // insert a dummy treatment in the database
            insertStmt = dbConnect.createStatement();
            insertStmt.executeUpdate("INSERT INTO treatments (AnimalID, TaskID, StartHour) VALUES (5, 8, 8)");
            insertStmt.close();
            
            // Run the storeHashMap() method
            Treatment treatment = new Treatment();
            treatment.storeHashMap();
            HashMap<Integer, Treatment> treatmentsMap = Treatment.getTreatments();
            
            // Check that the dummy task was loaded from the database
            Treatment retrievedTreatment = null;
            for (Treatment t : treatmentsMap.values()) {
                if (t.getAnimal().getAnimalID() == 50 && t.getTask().getTaskID() == 100 && t.getStartHour() == 8) {
                    retrievedTreatment = t;
                    break;
                }
            }
            assertNotNull(retrievedTreatment);
            assertEquals(50, retrievedTreatment.getAnimal().getAnimalID());
            assertEquals(100, retrievedTreatment.getTask().getTaskID());
            assertEquals(8, retrievedTreatment.getStartHour());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown: " + e.getMessage());
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException thrown: " + e.getMessage());
        }

        // Clean up database resources
        try {
            insertStmt.executeUpdate("DELETE FROM treatments WHERE AnimalID = 50 AND TaskID = 100 AND StartHour = 8");
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        } finally {
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
    }

    @Test //if the setAnimal() method correctly assigns an Animal object to a Treatment object.
    public void testTreatmentSetAnimal() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setAnimal(animal);
        assertEquals("Expected Animal object to be set correctly using setAnimal method", animal, treatment.getAnimal());
    }
    
    @Test 
    public void testTreatmentSetTask() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setTask(task);
        assertEquals("Expected Task object to be set correctly using setTask method", task, treatment.getTask());
    }
    
    @Test
    public void testTreatmentSetStartHour() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setStartHour(8);
        assertEquals("Expected start hour to be set correctly using setStartHour method", 8, treatment.getStartHour());
    }
    
    @Test
    public void testTreatmentSetTreatments() {
        Animal animal1 = new Animal();
        animal1.setAnimalID(50);
        Task task1 = new Task();
        task1.setTaskID(100);
        Treatment treatment1 = new Treatment( animal1, task1, 8);
    
        Animal animal2 = new Animal();
        animal2.setAnimalID(51);
        Task task2 = new Task();
        task2.setTaskID(101);
        Treatment treatment2 = new Treatment(animal2, task2, 9);
    
        HashMap<Integer, Treatment> treatmentsMap = new HashMap<>();
        treatmentsMap.put(1, treatment1);
        treatmentsMap.put(2, treatment2);
    
        Treatment.setTreatments(treatmentsMap);
    
        assertEquals("Expected treatments map to be set correctly using setTreatments method", treatmentsMap, Treatment.getTreatments());
    }
    
    @Test
    public void testTreatmentGetAnimal() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getAnimal method to return the correct Animal object", animal, treatment.getAnimal());
    }
    
    @Test
    public void testTreatmentGetTask() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getTask method to return the correct Task object", task, treatment.getTask());
    }
    
    @Test
    public void testTreatmentGetStartHour() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getStartHour method to return the correct start hour", 8, treatment.getStartHour());
    }
    
    @Test
    public void testTreatmentGetTreatments() {
        Animal animal1 = new Animal();
        animal1.setAnimalID(50);
        Task task1 = new Task();
        task1.setTaskID(100);
        Treatment treatment1 = new Treatment(animal1, task1, 8);
    
        Animal animal2 = new Animal();
        animal2.setAnimalID(51);
        Task task2 = new Task();
        task2.setTaskID(101);
        Treatment treatment2 = new Treatment(animal2, task2, 9);
    
        HashMap<Integer, Treatment> treatmentsMap = new HashMap<>();
        treatmentsMap.put(1, treatment1);
        treatmentsMap.put(2, treatment2);
    
        Treatment.setTreatments(treatmentsMap);
    
        assertEquals("Expected getTreatments method to return the correct treatments map", treatmentsMap, Treatment.getTreatments());
    }
    
    @AfterClass
    public static void tearDown() throws SQLException {
        dbConnect.close();
    }


    // TESTING SCHEDULE CLASS

    // Testing default constructor
    @Test 
    public void testScheduleDefaultConstructor() {
        Schedule scheduleDefault = new Schedule();
        assertEquals(0, scheduleDefault.getTreatmentIndices().size());
        assertNull(scheduleDefault.getTask());
        assertEquals(0, scheduleDefault.getAnimalList().size());
        assertNull(scheduleDefault.getTaskString());
        assertEquals(0, scheduleDefault.getStartTime());
        assertEquals(1, scheduleDefault.getQuantity());
        assertEquals(0, scheduleDefault.getTimeSpent());
        assertEquals(60, scheduleDefault.getTimeRemaining());
        assertFalse(scheduleDefault.getBackupRequired());
    }

    // testing constructor with only task, animal, startTime, and timeSpent arguments
    @Test 
    public void testScheduleConstructorTAST() {
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
    @Test
    public void testScheduleGettersSetters() {
        Schedule scheduleSetGet = new Schedule();
        scheduleSetGet.setTreatmentIndices(1);
        int actualScheduleTreatID = scheduleSetGet.getTreatmentIndices().get(0);
        scheduleSetGet.setTask("cleaning");
        String actualScheduleTask = scheduleSetGet.getTask();
        scheduleSetGet.setAnimalList("animalTest");
        String actualScheduleAnimal = scheduleSetGet.getAnimalList().get(0);
        scheduleSetGet.setTaskString("* cleaning (1: animal)");
        String actualScheduleTaskString = scheduleSetGet.getTaskString();
        scheduleSetGet.setStartTime(1);
        int actualScheduleStartTime = scheduleSetGet.getStartTime();
        scheduleSetGet.setQuantity(1);
        int actualScheduleQuantity = scheduleSetGet.getQuantity();
        scheduleSetGet.setTimeSpent(10);
        int actualScheduleTimeSpent = scheduleSetGet.getTimeSpent();
        scheduleSetGet.setTimeRemaining(50);
        int actualScheduleTimeRemaining = scheduleSetGet.getTimeRemaining();
        scheduleSetGet.setBackupRequired(true);
        boolean actualScheduleBackup = scheduleSetGet.getBackupRequired();

        assertEquals("Constructor or getter gave wrong value for Schedule Treatment ID.", 1, actualScheduleTreatID);
        assertEquals("Constructor or getter gave wrong value for Schedule Task.", "cleaning", actualScheduleTask);
        assertEquals("Constructor or getter gave wrong value for Schedule Animal.", "animalTest", actualScheduleAnimal);
        assertEquals("Constructor or getter gave wrong value for Schedule Task String.", "* cleaning (1: animal)", actualScheduleTaskString);
        assertEquals("Constructor or getter gave wrong value for Schedule Start Time.", 1, actualScheduleStartTime);
        assertEquals("Constructor or getter gave wrong value for Schedule Quantity.", 1, actualScheduleQuantity);
        assertEquals("Constructor or getter gave wrong value for Schedule Time Spent.", 10, actualScheduleTimeSpent);
        assertEquals("Constructor or getter gave wrong value for Schedule Time Remaining.", 50, actualScheduleTimeRemaining);
        assertEquals("Constructor or getter gave wrong value for Schedule Backup.", true, actualScheduleBackup);
    }
    
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

    
    // TESTING SCHEDULEBUILDER CLASS

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

    // // Test case for currentTask, currentAnimal, currentTreatment, newSchedule
    // @Test
    // public void testCreateScheduleMaxWindow1() {
    //     ScheduleGenerator sg = new ScheduleGenerator();
    //     sg.addAnimal(new Animal("Fluffy"));
    //     sg.addTask(new Task("task1", 60, 1));
    //     sg.addTreatment("Fluffy", "task1", 1, 8);

    //     try {
    //         sg.createScheduleMaxWindow1();
    //         assertEquals(1, sg.getSchedule().size());
    //         assertEquals(1, sg.getSchedule().get(0).getTreatmentKey());
    //         assertEquals("task1", sg.getSchedule().get(0).getTaskDescription());
    //         assertEquals("Fluffy", sg.getSchedule().get(0).getAnimalNickname());
    //         assertEquals(8, sg.getSchedule().get(0).getStartHour());
    //         assertEquals(60, sg.getSchedule().get(0).getTimeSpent());
    //     } catch (IllegalArgumentException e) {
    //         fail("Unexpected IllegalArgumentException thrown");
    //     }
    // }


    // // Test case for exception handling
    // @Test
    // public void testCreateScheduleMaxWindow1Exception() {
    //     ScheduleGenerator sg = new ScheduleGenerator();
    //     sg.addAnimal(new Animal("Fluffy"));
    //     sg.addTask(new Task("task1", 60, 1));
    //     sg.addTreatment("Fluffy", "task1", 1, 25);

    //     try {
    //         sg.createScheduleMaxWindow1();
    //         fail("Expected IllegalArgumentException not thrown");
    //     } catch (IllegalArgumentException e) {
    //         assertEquals("IllegalArgumentException exception when creating schedule object", e.getMessage());
    //     }
    // }  

}

