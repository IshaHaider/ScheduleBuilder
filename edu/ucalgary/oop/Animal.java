/** 
 * @author ENSF380 Group 20: Zahwa Fatima, Saba Yarandi, Nessma Mohdy, Isha Haider
 * Animal is a java class representing one row of data from the Animal table of the EWR database. 
 * @version     1.4
 * @since       1.0
*/
package edu.ucalgary.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

public class Animal implements LoadData {
    private final static String ANIMALQUERY = "SELECT * FROM animals";
    private static HashMap<Integer, Animal> animals = new HashMap<Integer, Animal>();

    private int animalID;
    private String animalName;
    private String animalSpecies;
    private String mostActive;

    /** Default Constructor
     * @return  
    */
    public Animal() {}

    /** Constructor
     * Initializes the data members of the Animal class
     * @param  id  an integer value of the animal ID
     * @param  name a string of the animal nickname
     * @param  species a string of the animal species
     * @param  mostActive a string of the animal's mostActive period
     * @return  
    */
    public Animal (int id, String name, String species, String mostActive) {
        this.animalID = id;
        this.animalName = name;
        this.animalSpecies = species;
        this.mostActive = mostActive;
    }

    /** storeHashMap() interface method 
     * loads data from the SQL database into a animals HashMap
     * @throws SpeciesNotFoundException
     * @return   void
    */
    @Override
    public void storeHashMap() throws SpeciesNotFoundException{
        try {
            Connection dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
            Statement myStmt = dbConnect.createStatement();
            ResultSet results = myStmt.executeQuery(ANIMALQUERY);
            while (results.next()) { // for each animal entry, add it as an Animal object to animals ArrayList
                this.animalID = results.getInt("AnimalID");
                this.animalName = results.getString("AnimalNickname");
                this.animalSpecies = results.getString("AnimalSpecies");
                
                if (this.animalSpecies.equals(Species.COYOTE.toString()) || this.animalSpecies.equals(Species.PORCUPINE.toString())) 
                    this.mostActive = Species.COYOTE.mostActivePeriod();
                else if (this.animalSpecies.equals(Species.FOX.toString()) || this.animalSpecies.equals(Species.RACCOON.toString())) 
                    this.mostActive = Species.FOX.mostActivePeriod();
                else if (this.animalSpecies.equals(Species.BEAVER.toString())) 
                    this.mostActive = Species.BEAVER.mostActivePeriod();
                else { 
                    throw new SpeciesNotFoundException("the animal " + animalName + " is not of a permitted species: " 
                    + animalSpecies + " please only enter animals of the following species: coyote, porcupine, fox, raccoon, or beaver"); } 
                
                Animal newAnimal = new Animal(animalID, animalName, animalSpecies, mostActive);
                animals.put(animalID, newAnimal);
            }
            myStmt.close();
            results.close();
            dbConnect.close();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating animal object");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setAnimalID(int id) { this.animalID = id; }
    public void setNickname(String name) { this.animalName = name; }
    public void setSpecies(String species) { this.animalSpecies = species; }
    public void setMostActive(String mostActive) { this.mostActive = mostActive; }
    public static void setAnimals(HashMap<Integer, Animal> newAnimals) { animals = newAnimals; }

    /** Getters
     * getter methods returning the stored object requested
    */
    public int getAnimalID() { return this.animalID; }
    public String getNickname() { return this.animalName; }
    public String getSpecies() { return this.animalSpecies; }
    public String getMostActive() { return this.mostActive; }
    public static HashMap<Integer, Animal> getAnimals() { return animals; }
}