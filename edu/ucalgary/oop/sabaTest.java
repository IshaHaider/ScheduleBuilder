
package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import java.io.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;



public class SabaTest {


    //ANIMAL FILE TESTS
    
    //Unit tests for Animal class
    
    @Test //Does the default constructor creates an object with null or default values for all data members
    public void testDefaultConstructor() {
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

    @Test 
        /** test case checks if the storeHashMap() method can successfully retrieve animal 
          *  records from the database and store them in a HashMap, as well as if the HashMap contains the expected values
          *  also accounts for the exception
          */
          public void testAnimalStoreHashMap() throws Exception {
            Connection dbConnect = null;
            Statement myStmt = null;
            ResultSet results = null;
        
            try {
                // Connect to the database
                dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
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
                assertNotNull("HashMap is null", animalsMap);
                assertEquals("HashMap size is not as expected", expectedSize, animalsMap.size());
        
                Animal retrievedAnimal1 = animalsMap.get(50);
                assertNotNull("Retrieved animal1 is null", retrievedAnimal1);
                assertEquals("Animal ID for animal1 is not as expected", 50, retrievedAnimal1.getAnimalID());
                assertEquals("Animal nickname for animal1 is not as expected", "Fido", retrievedAnimal1.getNickname());
                assertEquals("Animal species for animal1 is not as expected", "coyote", retrievedAnimal1.getSpecies());
                assertNotNull("Most active period for animal1 is null", retrievedAnimal1.getMostActive());
        
                Animal retrievedAnimal2 = animalsMap.get(51);
                assertNotNull("Retrieved animal2 is null", retrievedAnimal2);
                assertEquals("Animal ID for animal2 is not as expected", 51, retrievedAnimal2.getAnimalID());
                assertEquals("Animal nickname for animal2 is not as expected", "Mittens", retrievedAnimal2.getNickname());
                assertEquals("Animal species for animal2 is not as expected", "beaver", retrievedAnimal2.getSpecies());
                assertNotNull("Most active period for animal2 is null", retrievedAnimal2.getMostActive());
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
        
        
        
        
         


    //TREATMENT FILE TESTS  

    private Animal animal;
    private Task task;
    private int startHour;

    //PROBLEM IN testTreatmentStoreHashMap BELLOW //PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM

    @Test
    public void testTreatmentStoreHashMap() throws SQLException {
        // Create test data
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        int startHour = 8;
        Treatment treatment = new Treatment(animal, task, startHour);
    
        // Connect to the database and insert test data
        Connection dbConnect = null;
        Statement myStmt = null;
        ResultSet results = null;
        try {
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
            myStmt = dbConnect.createStatement();
            myStmt.executeUpdate("INSERT INTO treatments (AnimalID, TaskID, StartHour) VALUES (50, 100, 8)");
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    
        // Get the number of rows in the treatments table
        results = myStmt.executeQuery("SELECT COUNT(*) FROM treatments");
        results.next();
        int expectedSize = results.getInt(1);
    
        // Call storeHashMap and retrieve HashMap
        try {
            treatment.storeHashMap();
        } catch (SpeciesNotFoundException e) {
            fail("Exception thrown: " + e.getMessage());
        } catch (NullPointerException e) {
            // Handle the NullPointerException here
            fail("NullPointerException thrown: " + e.getMessage());
        }
    
        HashMap<Integer, Treatment> treatmentsMap = Treatment.getTreatments();
    
        // Verify HashMap contents
        assertNotNull(treatmentsMap);
        assertEquals(expectedSize, treatmentsMap.size());
    
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
    
        // Clean up database resources
        try {
            myStmt.executeUpdate("DELETE FROM treatments WHERE AnimalID = 50 AND TaskID = 100 AND StartHour = 8");
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        } finally {
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


    
    @Before
    public void TreatmentsetUp() {
        animal = new Animal(1, "lolol", "coyote", "Crepuscular");
        task = new Task(0, "Feed", 30, 3);;
        startHour = 8;
    }
    
    /**
     * Tests the constructor with start hour
     * It creates a Treatment object and verifies if the values passed as arguments are correctly set.
     */
    @Test
    public void testTreatmentConstructorWithStartHour() {
        Treatment t = new Treatment(animal, task, startHour);
        assertEquals("Animal object was not set correctly", animal, t.getAnimal());
        assertEquals("Task object was not set correctly", task, t.getTask());
        assertEquals("Start hour was not set correctly", startHour, t.getStartHour());
    }
    
    /**
     * Tests the constructor without start hour
     * It creates a Treatment object and verifies if the values passed as arguments are correctly set. 
     * The start hour should be set to 0.
     */
    @Test
    public void testTreatmentConstructorWithoutStartHour() {
        Treatment t = new Treatment(animal, task);
        assertEquals("Animal object was not set correctly", animal, t.getAnimal());
        assertEquals("Task object was not set correctly", task, t.getTask());
        assertEquals("Start hour was not set to 0", 0, t.getStartHour());
    }
    
    /**
     * Tests the getters and setters of the Treatment class
     * It creates a Treatment object, sets new values using the setter methods and verifies if the values were correctly set. 
     */
    @Test
    public void testTreatmentGettersAndSetters() {
        Treatment t = new Treatment(animal, task, startHour);
        Animal newAnimal = new Animal(1, "Ali", Species.BEAVER.toString(), Species.BEAVER.mostActivePeriod());
        Task newTask = new Task(1, "Walk", 60, 0, 4);
        int newStartHour = 12;
        t.setAnimal(newAnimal);
        t.setTask(newTask);
        t.setStartHour(newStartHour);
        assertEquals("Animal object was not set correctly", newAnimal, t.getAnimal());
        assertEquals("Task object was not set correctly", newTask, t.getTask());
        assertEquals("Start hour was not set correctly", newStartHour, t.getStartHour());
    }
    
    /**
     * Tests the static setTreatments method of the Treatment class
     * It creates a new HashMap, puts a Treatment object in it and sets it using the setTreatments method. 
     * It then verifies if the HashMap was correctly set by comparing it with the one obtained using the getTreatments method. 
     */
    @Test
    public void testSetTreatments() {
        HashMap<Integer, Treatment> newTreatments = new HashMap<>();
        newTreatments.put(1, new Treatment(animal, task, startHour));
        Treatment.setTreatments(newTreatments);
        assertEquals("Treatments HashMap was not set correctly", newTreatments, Treatment.getTreatments());
    }




    /**
    * Tests the {@link ScheduleBuilder#createScheduleMaxWindow5() } method 
    * however it considers all of the window methods as they each call eachother
    * by adding treatments with different maxWindows to the allTreatments HashMap
    * and checking that the resulting schedule is correct. In this case, two
    * treatments are added, one with maxWindow = 2 and one with maxWindow = 1.
    * The test verifies that the treatment with maxWindow = 1 is scheduled first
    * at its preferred start hour and that the resulting schedule is correct.
    */

    @Test
    public void testCreateScheduleMaxWindow5() {
        try {
            // Create a new ScheduleBuilder object
            ScheduleBuilder scheduleBuilder = new ScheduleBuilder();
    
            // Add some treatments to the allTreatments HashMap, including one with maxWindow=1
            Task task1 = new Task(1, "Task 1", 2, 5);
            Task task2 = new Task(2, "Task 2", 2, 5);
            Animal animal1 = new Animal(1, "Animal 1", "Nickname 1", "Most Active");
            Animal animal2 = new Animal(2, "Animal 2", "Nickname 2", "Most Active");
            Treatment treatment1 = new Treatment(animal1, task1, 2);
            Treatment treatment2 = new Treatment(animal2, task2, 1);
            HashMap<Integer, Treatment> allTreatments = scheduleBuilder.getAllTreatments();
            allTreatments.put(70, treatment1);
            allTreatments.put(71, treatment2);
            
            scheduleBuilder.setAllTreatments(allTreatments);
    
            // Call the createScheduleMaxWindow1 method
            scheduleBuilder.createScheduleMaxWindow5();
    
            // Check that a new schedule object was added to the schedule ArrayList
            ArrayList<Schedule> schedule = scheduleBuilder.getSchedule();
            int maxSize = schedule.size();
            // Check that the schedule object has the correct treatmentID, startTime, timeSpent, and timeRemaining
            Schedule newSchedule = schedule.get(maxSize-2);
            int treatmentIndex1 = newSchedule.getTreatmentIndices().get(0);
            assertEquals("Expected treatment index 70, but got treatment index" + treatmentIndex1, 70, treatmentIndex1);
            assertEquals("Expected start time of 2, but got " + newSchedule.getStartTime(), 2, newSchedule.getStartTime());
            assertEquals("Expected time spent of 2, but got " + newSchedule.getTimeSpent(), 2, newSchedule.getTimeSpent());
 
            newSchedule = schedule.get(maxSize-1);
            int treatmentIndex2 = newSchedule.getTreatmentIndices().get(0);
            assertEquals("Expected treatment index 71, but got treatment index " + treatmentIndex2, 71, treatmentIndex2);
            assertEquals("Expected start time of 1, but got " + newSchedule.getStartTime(), 1, newSchedule.getStartTime());
            assertEquals( "Expected time spent of 2, but got " + newSchedule.getTimeSpent(), 2, newSchedule.getTimeSpent());
        } catch ( SpeciesNotFoundException e) {
            fail("IllegalArgumentException exception when creating schedule object");
        }
     }
 }

   
    
    
    

