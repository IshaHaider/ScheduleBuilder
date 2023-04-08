
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



    // // Unit test for adding an animal to the HashMap<Integer, Animal>
    // @Test
    // public void testAddAnimalToHashMap() throws SpeciesNotFoundException {
    //     Animal animal = new Animal(1, "cae", "coyote", "Crepuscular");
    //     HashMap<Integer, Animal> a = Animal.getAnimals();
    //     a.put(1, animal);
    //     assertTrue(Animal.getAnimals().containsValue(animal));
    // }
           
    // @Test // Unit test for removing an animal from the HashMap<Integer, Animal>
    // public void testRemoveAnimalFromHashMap() throws SpeciesNotFoundException {
    //     a.put(1, animal);

    //     Animal.removeAnimalFromHashMap(animal.getId());
    //     assertFalse(Animal.getAnimals().containsValue(animal));
    // }
    @Test //test case checks if the storeHashMap() method can successfully retrieve animal 
          //records from the database and store them in a HashMap, as well as if the HashMap contains the expected values
    public void testStoreHashMap() throws Exception {
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
    
    
}
    

