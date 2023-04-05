/** 
 * @author ENSF380 Group 20
 * Animal is a java class representing one row of data from the Animal table of the EWR database. 
 * @version     1.2
 * @since       1.0
*/
package edu.ucalgary.oop;

public class Animal {
    private int animalID;
    private String animalName;
    private String animalSpecies;
    private final String MOSTACTIVE;

    /** Constructor
     * Initializes the data members of the Animal class
     * Sets the MOSTACTIVE data member using the Species enumeration:
     *      Species: coyote or porcupine --> mostActive = Crepuscular
     *      Species: fox or raccon --> mostActive = Nocturnal
     *      Species: beaver--> mostActive = Diurnal
     * @param  id  an integer value of the animal ID
     * @param  name a string of the animal nickname
     * @param  species a string of the animal species
     * @throws SpeciesNotFoundException
     * @return  
    */
    public Animal (int id, String name, String species) throws SpeciesNotFoundException {
        this.animalID = id;
        this.animalName = name;
        this.animalSpecies = species;

        if (this.animalSpecies.equals(Species.COYOTE.toString()) || this.animalSpecies.equals(Species.PORCUPINE.toString())) 
            MOSTACTIVE = Species.COYOTE.mostActivePeriod();
        else if (this.animalSpecies.equals(Species.FOX.toString()) || this.animalSpecies.equals(Species.RACCOON.toString())) 
            MOSTACTIVE = Species.FOX.mostActivePeriod();
        else if (this.animalSpecies.equals(Species.BEAVER.toString())) 
            MOSTACTIVE = Species.BEAVER.mostActivePeriod();
        else { 
            throw new SpeciesNotFoundException("the animal " + name + " is not of a permitted species: " 
            + species + "please only enter animals of the following species: coyote, porcupine, fox, raccoon, or beaver"); }
    }

    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setID(int id) { this.animalID = id; }
    public void setNickname(String name) { this.animalName = name; }
    public void setSpecies(String species) { this.animalSpecies = species; }

    /** Getters
     * getter methods returning the stored object requested
    */
    public int getID() { return this.animalID; }
    public String getNickname() { return this.animalName; }
    public String getSpecies() { return this.animalSpecies; }
    public String getMostActive() { return this.MOSTACTIVE; }
}