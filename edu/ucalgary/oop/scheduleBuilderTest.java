package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ScheduleBuilderTest{ 

    //////Start of Animal.java testing:
    

    @Test  // will the animal constructor create an Animal object without an exception and therefore are the getters working?
    public void testAnimalConstructor() {
        int expectedAnimalID = 1;
        String expectedAnimalNickname = "Olivia";
        String expectedAnimalSpecies = Species.COYOTE.toString();
        
        try {
            Animal animal1 = new Animal(expectedAnimalID, expectedAnimalNickname, expectedAnimalSpecies);
            assertEquals("Constructor or getter gave wrong value for animalID", expectedAnimalID, animal1.getID());
            assertEquals("Constructor or getter gave wrong value for Animal Nickname", expectedAnimalNickname, animal1.getNickname());
            assertEquals("Constructor or getter gave wrong value for Animal species", expectedAnimalSpecies, animal1.getSpecies());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown when it should not have been");
        }
    }


    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorCrepuscular() {
        try {
            Animal animal1 = new Animal(2, "Olivia", Species.COYOTE.toString());
            assertEquals("mostActive does not return correct activity patterns","Crepuscular", animal1.getMostActive());
    
            Animal animal2 = new Animal(3, "Emily", Species.PORCUPINE.toString());
            assertEquals("mostActive does not return correct activity patterns", animal2.getMostActive());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown when it should not have been");
        }
    }
    
    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorNocturnal() {
        try {
            Animal animal1 = new Animal(4, "Sly", Species.FOX.toString());
            assertEquals("mostActive does not return correct activity patterns", "Nocturnal", animal1.getMostActive());
    
            Animal animal2 = new Animal(5, "Rocky", Species.RACCOON.toString());
            assertEquals("mostActive does not return correct activity patterns", animal2.getMostActive());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown");
        }
    }
    
    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorDiurnal() {
        try {
            Animal animal = new Animal(6, "Bevy", Species.BEAVER.toString());
            assertEquals(animal.getMostActive(), "mostActive does not return correct activity patterns");
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown");
        }
    }
    @Test //if the setID() method properly changes the animal's ID attribute:
    public void testSetID() {
        try {
            Animal animal = new Animal(1, "Olivia", Species.COYOTE.toString());
            animal.setID(2);
            assertEquals("setter gave wrong value for animalID", 2, animal.getID());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown when it should not have been");
        }
    }
    @Test // if the setNickname() method properly changes the animal's nickname attribute
    public void testSetNickname() {
        try {
            Animal animal = new Animal(1, "Olivia", Species.COYOTE.toString());
            animal.setNickname("Olly");
            assertEquals("setter gave wrong value for Animal Nickname", "Olly", animal.getNickname());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown when it should not have been");
        }
    }

    @Test // if the setSpecies() method properly changes the animal's species attribute
    public void testSetSpecies() {
        try {
            Animal animal = new Animal(1, "Olivia", Species.COYOTE.toString());
            animal.setSpecies(Species.RACCOON.toString());
            assertEquals("setter gave wrong value for Animal species" ,"raccoon", animal.getSpecies());
        } catch (SpeciesNotFoundException e) {
            fail("SpeciesNotFoundException was thrown when it should not have been");
        }
    }
    
    @Test //if the setSpecies() method throws a SpeciesNotFoundException for an invalid species string
    public void testSetSpeciesInvalid() {
        try {
            Animal animal = new Animal(1, "Olivia", Species.COYOTE.toString());
            animal.setSpecies("INVALID");
            fail("SpeciesNotFoundException was not thrown when it should have been");
        } catch (SpeciesNotFoundException e) {
            // test passes if SpeciesNotFoundException is thrown
        }
    }
    
    @Test //if the Animal constructor throws a SpeciesNotFoundException for an invalid species string
    public void testConstructorInvalidSpecies() {
        try {
            Animal animal = new Animal(1, "Olivia", "INVALID");
            fail("SpeciesNotFoundException was not thrown when it should have been");
        } catch (SpeciesNotFoundException e) {
            // test passes if SpeciesNotFoundException is thrown
        }
    }
    
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

    // Testing constructor without PrepTime of Task class
    @Test 
    public void testTaskWOPrepTime() {
        int actualTaskId = sharedTaskobj.getID();
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
        int actualTaskIdWP = sharedTaskobjWP.getID();
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

    // Testing setter and getter functions of Task class
    @Test
    public void testTaskSettersGetters() {
        Task testSetGet = new Task(0, null, 0, 0, 0);
        testSetGet.setID(14);;
        int actualTaskId = testSetGet.getID();
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

    // testing valid input parameters when backup is not required for Schedule class
    @Test 
    public void testScheduleConstructorNOBackup() throws IncorrectTimeException{
        try {
            Schedule ValidInNOBackup = new Schedule("Rebandage Leg Wound", "Foxy", 13, 1, 20, 40, false);
            assertEquals("Rebandage Leg Wound", ValidInNOBackup.getTask());
            assertEquals(1, ValidInNOBackup.getAnimalList().size());
            assertTrue(ValidInNOBackup.getAnimalList().contains("Foxy"));
            assertEquals(13, ValidInNOBackup.getStartTime());
            assertEquals(1, ValidInNOBackup.getQuantity());
            assertEquals(20, ValidInNOBackup.getTimeSpent());
            assertEquals(40, ValidInNOBackup.getTimeRemaining());
            assertFalse(ValidInNOBackup.getBackupRequired());
        } catch (IncorrectTimeException e) {
            fail("Unexpected IncorrectTimeException: " + e.getMessage());
        }
    }

    // testing valid input parameters when backup is required
    @Test 
    public void testScheduleConstructorBackup() throws IncorrectTimeException{
        try {
            Schedule ValidInBackup = new Schedule("Medical Treatments", "Coy", 15, 2, 70, 50, true);
            assertEquals("Medical Treatments", ValidInBackup.getTask());
            assertEquals(1, ValidInBackup.getAnimalList().size());
            assertTrue(ValidInBackup.getAnimalList().contains("Coy"));
            assertEquals(15, ValidInBackup.getStartTime());
            assertEquals(2, ValidInBackup.getQuantity());
            assertEquals(70, ValidInBackup.getTimeSpent());
            assertEquals(50, ValidInBackup.getTimeRemaining());
            assertTrue(ValidInBackup.getBackupRequired());
        } catch (IncorrectTimeException e) {
            fail("Unexpected IncorrectTimeException: " + e.getMessage());
        }
    }

    // testing when timeSpent exceeds 60 minutes and backup is not required
    @Test 
    public void testScheduleEX60NOBackup() {
        try {
            Schedule testScheduleEX60NOBackup = new Schedule("Eyedrops", "Porcu", 12, 1, 70, 20, false);
            fail("Expected IncorrectTimeException was not thrown.");
        } catch (IncorrectTimeException e) {
            assertEquals("The time spent exceeds the allowed time in one hour: 70", e.getMessage());
        }
    }

    // testing when sum of timeSpent and timeRemaining exceeds 60 minutes and backup is not required
    @Test 
    public void testScheduleSumEX60NOBackup() {
        try {
            Schedule testScheduleSumEX60NOBackup = new Schedule("Give fluid injection", "Bieber", 12, 1, 30, 40, false);
            fail("Expected IncorrectTimeException was not thrown.");
        } catch (IncorrectTimeException e) {
            assertEquals("The total time exceeds the allowed time in one hour (no backup): 70", e.getMessage());
        }
    }

    // testing when sum of timeSpent and timeRemaining exceeds 120 minutes and backup is required
    @Test  
    public void testScheduleSumEX120Backup() {
        try {
            Schedule testScheduleSumEX120Backup = new Schedule("Medical Treatments", "Coy", 15, 2, 70, 60, true);
            fail("Expected IncorrectTimeException was not thrown.");
        } catch (IncorrectTimeException e) {
            assertEquals("The total time exceeds the allowed time in one hour (with backup): 130", e.getMessage());
        }
    }

    // testing setters and getters for Schedule class
    

    // // testing createTaskString() for Schedule class
    @Test 
    public void testScheduleCreateTaskString() {
        try {    
            Schedule testScheduleCreateTaskString = new Schedule("Rebandage Leg Wound", "Bubble", 13, 1, 20, 40, false);
            testScheduleCreateTaskString.createTaskString();
            assertEquals("* Rebandage Leg Wound (1: Bubble)", testScheduleCreateTaskString.getTaskString());

            // adding another animal to the list
            testScheduleCreateTaskString.setAnimalList("Bubs");
            testScheduleCreateTaskString.createTaskString();
            assertEquals("* Rebandage Leg Wound (2: Bubble, Bubs)", testScheduleCreateTaskString.getTaskString());
        } catch (IncorrectTimeException e) { // WHY DOES THIS FUNCTION NEED AN EXCEPTION BEING THROWN
            fail("Unexpected IncorrectTimeException: " + e.getMessage());
        }
    }

    // TESTING SCHEDULEBUILDER

    // testing constructor for ScheduleBuilder class
    // @Test 
    // public void testScheduleBuilderConstructor() {
    //     // create a LoadData object
    //     LoadData testData = new LoadData();
    //     ArrayList<Animal> animals = new ArrayList<>();
    //     animals.add(new Animal("Cat"));
    //     animals.add(new Animal("Dog"));
    //     testData.setAnimals(animals);
    //     ArrayList<Task> tasks = new ArrayList<>();
    //     tasks.add(new Task("Feed"));
    //     tasks.add(new Task("Walk"));
    //     testData.setTasks(tasks);
    //     ArrayList<Treatment> treatments = new ArrayList<>();
    //     treatments.add(new Treatment("Vaccination"));
    //     treatments.add(new Treatment("Deworming"));
    //     testData.setTreatments(treatments);

    //     // create a ScheduleBuilder Object
    //     ScheduleBuilder testScheduleBuilderConstructor = new ScheduleBuilder(testData);

    //     // check if constructor accurately assigns values
    //     assertEquals(animals, testScheduleBuilderConstructor.getAnimals());
    //     assertEquals(tasks, testScheduleBuilderConstructor.getTasks());
    //     assertEquals(treatments, testScheduleBuilderConstructor.getTreatments());
    // }

    // testing createSchedule() in ScheduleBuilder class
    


}

