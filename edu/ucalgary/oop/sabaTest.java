
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

    @Test //test case checks if the storeHashMap() method can successfully retrieve animal 
          //records from the database and store them in a HashMap, as well as if the HashMap contains the expected values
          //also accounts for the exception
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


    //TREATMENT FILE TESTS  
    //PROBLEM IN testTreatmentStoreHashMap BELLOW
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

    @Test //if the setAnimal() method correctly assigns an Animal object to a Treatment object.
    public void testSetAnimal() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setAnimal(animal);
        assertEquals("Expected Animal object to be set correctly using setAnimal method", animal, treatment.getAnimal());
    }
    
    @Test 
    public void testSetTask() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setTask(task);
        assertEquals("Expected Task object to be set correctly using setTask method", task, treatment.getTask());
    }
    
    @Test
    public void testSetStartHour() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment();
        treatment.setStartHour(8);
        assertEquals("Expected start hour to be set correctly using setStartHour method", 8, treatment.getStartHour());
    }
    
    @Test
    public void testSetTreatments() {
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
    public void testGetAnimal() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getAnimal method to return the correct Animal object", animal, treatment.getAnimal());
    }
    
    @Test
    public void testGetTask() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getTask method to return the correct Task object", task, treatment.getTask());
    }
    
    @Test
    public void testGetStartHour() {
        Animal animal = new Animal();
        animal.setAnimalID(50);
        Task task = new Task();
        task.setTaskID(100);
        Treatment treatment = new Treatment(animal, task, 8);
        assertEquals("Expected getStartHour method to return the correct start hour", 8, treatment.getStartHour());
    }
    
    @Test
    public void testGetTreatments() {
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
    @Test //Test Treatment(animal, task, startHour) constructor
    public void testTreatmentConstructorWithAllParams() {
        Animal animal = new Animal("Lion", 5);
        Task task = new Task("Feed", 2, 3);
        Treatment treatment = new Treatment(animal, task, 9);
        assertEquals("Expected Animal object to be set correctly using constructor", animal, treatment.getAnimal());
        assertEquals("Expected Task object to be set correctly using constructor", task, treatment.getTask());
        assertEquals("Expected start hour to be set correctly using constructor", 9, treatment.getStartHour());
    }
    
    @Test //Test Treatment(animal, task) constructor\\\
    public void testTreatmentConstructorWithTwoParams() {
        Animal animal = new Animal("Giraffe", 10);
        Task task = new Task("Clean", 1, 1);
        Treatment treatment = new Treatment(animal, task);
        assertEquals("Expected Animal object to be set correctly using constructor", animal, treatment.getAnimal());
        assertEquals("Expected Task object to be set correctly using constructor", task, treatment.getTask());
        assertEquals("Expected start hour to be set to default value 0 using constructor", 0, treatment.getStartHour());
    }
    
    @Test //Test Treatment() constructor:
    public void testEmptyTreatmentConstructor() {
        Treatment treatment = new Treatment();
        assertNull("Expected Animal object to be null using empty constructor", treatment.getAnimal());
        assertNull("Expected Task object to be null using empty constructor", treatment.getTask());
        assertEquals("Expected start hour to be set to default value 0 using empty constructor", 0, treatment.getStartHour());
    }
    
    
   //SCHEDULEBUILDER FILE TESTS     


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
   
    
    
    

