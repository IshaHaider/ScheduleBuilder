/** 
@author ENSF380 Group 20
* Treatment is a java class representing one row of data from the Treatment table of the EWR database. 
@version    1.2
@since 1.0
*/

package edu.ucalgary.oop;

public class Treatment {
    private int treatmentID;
    private int animalID;
    private int taskID;
    private int startHour;

    /** Constructor
    * Initializes the data members of the Treatment class
    * @param  treatmentID  an integer value of the treatment ID
    * @param  animalID an integer value of the animal ID associated to the treatment
    * @param  taskID an integer value of the tasks ID associated to the treatment
    * @param  startHour an integer value of the time (hour) the task can begin 
    * @return
    */
    public Treatment (int treatmentID, int animalID, int taskID, int startHour){
        this.treatmentID = treatmentID;
        this.animalID = animalID;
        this.taskID = taskID;
        this.startHour = startHour;
    }

    /** Constructor
    * Initializes the data members of the Treatment class except for startHour
    * This constructor is used for tasks with lowest priority, as they can be done during any time of the day. 
    * @param  treatmentID  an integer value of the treatment ID
    * @param  animalID an integer value of the animal ID associated to the treatment
    * @param  taskID an integer value of the tasks ID associated to the treatment
    * @return 
    */
    public Treatment (int treatmentID, int animalID, int taskID){
        this.treatmentID = treatmentID;
        this.animalID = animalID;
        this.taskID = taskID;
    }

    /** Setters 
    * setter methods assigning the parameter value to the stored object
    * @return   void
    */
    public void setTreatementID(int id) { this.treatmentID = id; }
    public void setAnimalID(int id) { this.animalID = id; }
    public void setTaskID(int id) { this.taskID = id; }
    public void setStartHour(int startHour) { this.startHour = startHour; }

    /** Getters
    * getter methods returning the stored object requested
    */
    public int getTreatementID() { return this.treatmentID; }
    public int getAnimalID() { return this.animalID; }
    public int getTaskID() { return this.taskID; }
    public int getStartHour() { return this.startHour; } 
}
