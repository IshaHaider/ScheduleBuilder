package edu.ucalgary.oop;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.regex.*;

public class LoadDataTest{ 

    int expectedAnimalID = 1;
    String expectedAnimalNickname = "Emma";
    String expectedAnimalSpecies = "fox";  
    
    Animal sharedTestObj = new Animal(expectedAnimalID, expectedAnimalNickname,
    expectedAnimalSpecies);

    @Test
    public void testAnimalGetters() {
        int actualAnimalID = sharedTestObj.getID();
        String actualAnimalNickname = sharedTestObj.getNickname();
        String actualAnimalSpecies = sharedTestObj.getSpecies();

        assertEquals("Constructor or getter gave wrong value for Animal ID", expectedAnimalID, actualAnimalID);
        assertEquals("Constructor or getter gave wrong value for Animal Nickname", expectedAnimalNickname, actualAnimalNickname);
        assertEquals("Constructor or getter gave wrong value for Animal spieces", actualAnimalSpecies, actualAnimalSpecies);
    }
}
