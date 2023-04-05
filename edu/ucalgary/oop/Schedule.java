/** 
 * @author ENSF380 Group 20
 * Schedule is a java class containing a treatment from teh SQL database with proper scheduling applied
 * @version     1.5
 * @since       1.0
*/

package edu.ucalgary.oop;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<String> animalList = new ArrayList<String>() ;
    private String task;
    private String taskString;
    private int startTime;
    private int quantity = 1;
    private int timeSpent;
    private int timeRemaining = 60;
    private boolean backupRequired = false;

    /** Default Constructor
     * @return   
    */
    public Schedule(){}

    /** Constructor
     * Initializes the data members of the Schedule class
     * @param  task  a string of the task description
     * @param  animal a string of the animal nickname, added to the animaList ArrayList
     * @param  startTime an integer value of the start hour of the treatment
     * @param  quantity  an integer value of the number of animals receiving the same treatment at the same startTime
     * @param  timeSpent an integer value of the total time spent on treatment
     * @param  timeRemaining an integer value of the time remaining after the treatment is performed
     * @param  backupRequired a boolean value of whether or not a backup volunteer is required
     * @throws IncorrectTimeException
     * @return   
    */
    public Schedule(String task, String animal, int startTime, int quantity, int timeSpent, int timeRemaining, boolean backupRequired)
    throws IncorrectTimeException {
        this.task = task;
        this.animalList.add(animal);
        this.startTime = startTime;
        this.quantity = quantity;
        this.timeSpent = timeSpent;
        this.timeRemaining = timeRemaining;
        this.backupRequired = backupRequired;

        if (timeSpent > 60 && !backupRequired) 
        { throw new IncorrectTimeException("The time spent exceeds the allowed time in one hour: " + timeSpent ); }
        else if ((timeSpent + timeRemaining) > 60 && !backupRequired)
        { throw new IncorrectTimeException("The total time exceeds the allowed time in one hour (no backup): " + (timeSpent + timeRemaining) ); }
        else if ((timeSpent + timeRemaining) > 120 && backupRequired)
        { throw new IncorrectTimeException("The total time exceeds the allowed time in one hour (with backup): " + (timeSpent + timeRemaining) ); }
    }
    
    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setTask(String task) { this.task = task; }
    public void setAnimalList(String animal) { 
        this.animalList.add(animal);
        this.quantity = animalList.size(); }
    public void setTaskString(String taskString) { this.taskString = taskString; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public void setTimeRemaining(int timeRemaining) throws IncorrectTimeException{ 
        this.timeRemaining = timeRemaining; 
        if ((timeSpent + timeRemaining) > 60 && !backupRequired) { 
            throw new IncorrectTimeException("The total time exceeds the allowed time in one hour for the task " 
            + this.task + ": " + (timeSpent + timeRemaining)); }
    }
    public void setBackupRequired(boolean backupRequired) { this.backupRequired = backupRequired; }

    /** Getters
     * getter methods returning the stored object requested
    */
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
        this.taskString = "* " + task + " (" + quantity + ": " + animals + ")"; }
}