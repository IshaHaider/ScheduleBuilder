package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

public class LoadDataTest{ 

    //////Start of Animal.java testing:
    
    // Values used for all tests involving Recipe class
    int expectedAnimalID = 1;
    String expectedAnimalNickname = "Emma";
    String expectedAnimalSpecies = "fox";  
    
    Animal sharedTestObj = new Animal(expectedAnimalID, expectedAnimalNickname,
    expectedAnimalSpecies);

    @Test  // will the animal constructor create an Animal object without an exception?
    public void testAnimalGetters() {
        int actualAnimalID = sharedTestObj.getID();
        String actualAnimalNickname = sharedTestObj.getNickname();
        String actualAnimalSpecies = sharedTestObj.getSpecies();

        assertEquals("Constructor or getter gave wrong value for Animal ID", expectedAnimalID, actualAnimalID);
        assertEquals("Constructor or getter gave wrong value for Animal Nickname", expectedAnimalNickname, actualAnimalNickname);
        assertEquals("Constructor or getter gave wrong value for Animal spieces", actualAnimalSpecies, actualAnimalSpecies);
    }
    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorCrepuscular() {
        Animal animal1 = new Animal(2, "Olivia", Species.COYOTE.toString());
        assertEquals("Crepuscular", animal1.getMostActive());

        Animal animal2 = new Animal(3, "Emily", Species.PORCUPINE.toString());
        assertEquals("Crepuscular", animal2.getMostActive());
    }
    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorNocturnal() {
        Animal animal1 = new Animal(4, "Sly", Species.FOX.toString());
        assertEquals("Nocturnal", animal1.getMostActive());

        Animal animal2 = new Animal(5, "Rocky", Species.RACCOON.toString());
        assertEquals("Nocturnal", animal2.getMostActive());
    }

    @Test // has mostActive set attribute to different values depending on the species?
    public void testConstructorDiurnal() {
        Animal animal = new Animal(6, "Bevy", Species.BEAVER.toString());
        assertEquals("Diurnal", animal.getMostActive());
    }

    @Test(expected = NullPointerException.class) // tests the constructor with invalid data that should throw a NullPointerException
    public void testConstructorInvalidSpecies() {
        new Animal(7, "Socks", null);
    }

    @Test // tests the setters and getters lol 
    public void testSettersAndGetters() {
        Animal animal = new Animal(8, "Spot", Species.FOX.toString());

        animal.setID(9);
        assertEquals(9, animal.getID());

        animal.setNickname("Patches");
        assertEquals("Patches", animal.getNickname());

        animal.setSpecies(Species.RACCOON.toString());
        assertEquals("raccoon", animal.getSpecies());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSpecies() {
        Animal invalidAnimal = new Animal(1, "Random Animal", "Random species");
    }

    @Test
    public void testValidSpecies() {
        Animal validAnimal = new Animal(2, "Valid Animal", Species.COYOTE.toString());
        assertNotNull(validAnimal);
    }
}
