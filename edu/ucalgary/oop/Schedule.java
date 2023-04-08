/** 
 * @author ENSF380 Group 20
 * Schedule is a java class containing a treatment from teh SQL database with proper scheduling applied
 * @version     1.7
 * @since       1.0
*/

package edu.ucalgary.oop;
import java.util.ArrayList;

public class Schedule{
    private ArrayList<String> animalList = new ArrayList<String>() ;
    private ArrayList<Integer> treatmentIndices = new ArrayList<Integer>();
    private String task;
    private int startTime;
    private int timeSpent;
    private String taskString;
    private int quantity = 1;
    private int timeRemaining = 60;
    private boolean backupRequired = false;

    /** Default Constructor
     * @return   
    */
    public Schedule(){}

    /** Constructor
     * Initializes the data members of the Schedule class
     * @param  treatmentID an integer value of treatmentID
     * @param  task  a string of the task description
     * @param  animal a string of the animal nickname, added to the animaList ArrayList
     * @param  startTime an integer value of the start hour of the treatment
     * @param  timeSpent an integer value of the total time spent on treatment
     * @return   
    */
    public Schedule(int treatmentID, String task, String animal, int startTime, int timeSpent) {
        this.treatmentIndices.add(treatmentID);
        this.task = task;
        this.animalList.add(animal);
        this.startTime = startTime;
        this.timeSpent = timeSpent;
    }

    /** Constructor
     * Initializes the data members of the Schedule class
     * @param  treatmentID an integer value of treatmentID, to be added to the treatmentIndices array
     * @param  task  a string of the task description
     * @param  animal a string of the animal nickname, added to the animaList ArrayList
     * @param  startTime an integer value of the start hour of the treatment
     * @param  quantity  an integer value of the number of animals receiving the same treatment at the same startTime
     * @param  timeSpent an integer value of the total time spent on treatment
     * @param  timeRemaining an integer value of the time remaining after the treatment is performed
     * @param  backupRequired a boolean value of whether or not a backup volunteer is required
     * @return   
    */
    public Schedule(int treatmentID, String task, String animal, int startTime, int quantity, int timeSpent, int timeRemaining, boolean backupRequired) {
        this.treatmentIndices.add(treatmentID);
        this.task = task;
        this.animalList.add(animal);
        this.startTime = startTime;
        this.quantity = quantity;
        this.timeSpent = timeSpent;
        this.timeRemaining = timeRemaining;
        this.backupRequired = backupRequired;
    }
    
    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setTreatmentIndices(int treatmentID) { this.treatmentIndices.add(treatmentID);}
    public void setTask(String task) { this.task = task; }
    public void setAnimalList(String animal) { 
        this.animalList.add(animal);
        this.quantity = animalList.size(); }
    public void setTaskString(String taskString) { this.taskString = taskString; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public void setTimeRemaining(int timeRemaining) { this.timeRemaining = timeRemaining; }
    public void setBackupRequired(boolean backupRequired) { this.backupRequired = backupRequired; }

    /** Getters
     * getter methods returning the stored object requested
    */
    public ArrayList<Integer> getTreatmentIndices() { return treatmentIndices;}
    public String getTask() { return this.task; }
    public ArrayList<String> getAnimalList() { return this.animalList; }
    public String getTaskString() { return this.taskString; }
    public int getStartTime() { return this.startTime; }
    public int getQuantity() { return this.quantity; }
    public int getTimeSpent() { return this.timeSpent; }
    public int getTimeRemaining() { return this.timeRemaining; }
    public boolean getBackupRequired() { return this.backupRequired; }
    
    /** createTaskString() 
     * Assigns data member taskString with the treatment containing the task and animals.
     * Example string: "* Feeding - coyote (3: Boots, Spin, Spot)"
     * @return   void
    */
    public void createTaskString() { 
        String animals = ""; // empty string
        int i;
        for (i = 0; i < this.animalList.size() - 1; i++) { animals += this.animalList.get(i) + ", "; }
        animals += this.animalList.get(i); // string with all animals listed, ex: Loner, Biter, Bitter
        this.taskString = "* " + task + " (" + quantity + ": " + animals + ")"; 
    }
}