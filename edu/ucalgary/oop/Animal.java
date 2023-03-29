package ENSF380FinalProject.edu.ucalgary.oop;

public class Animal {
    private int animalID;
    private String animalName;
    private String animalSpecies;
    private String mostActive;

    public Animal (int id, String name, String species ){
        this.animalID = id;
        this.animalName = name;
        this.animalSpecies = species;

        if (this.animalSpecies.equals(Species.COYOTE.toString()) || this.animalSpecies.equals(Species.PORCUPINE.toString())) 
            this.mostActive = "Crepuscular";
        if (this.animalSpecies.equals(Species.FOX.toString()) || this.animalSpecies.equals(Species.RACCOON.toString())) 
            this.mostActive = "Nocturnal";
        if (this.animalSpecies.equals(Species.BEAVER.toString())) 
            this.mostActive = "Diurnal";

    }
    public void setID(int id) { this.animalID = id; }
    public int getID() { return this.animalID; }
    public void setNickname(String name) { this.animalName = name; }
    public String getNickname() { return this.animalName; }
    public void setSpecies(String species) { this.animalSpecies = species; }
    public String getSpecies() { return this.animalSpecies; }
    public String getMostActive() { return this.mostActive; }
}
