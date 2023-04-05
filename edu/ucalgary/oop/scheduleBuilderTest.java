package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

public class scheduleBuilderTest{ 

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
            assertEquals("mostActive does not return correct activity patterns", animal.getMostActive());
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
    
    
}

