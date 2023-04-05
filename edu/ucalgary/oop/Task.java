/** 
@author ENSF380 Group 20
* Task is a java class representing one row of data from the Task table of the EWR database. 
* the totalTime data member is an integer array, where:
* totalTime[0] = the duration per animal
* totalTime[1] = prepTime
* totalTime[2] = total time of the task combining the duration and prepTime
@version    1.2
@since 1.0
*/

package edu.ucalgary.oop;

public class Task {
    private int taskId;
    private String taskDescr;
    private int[] totalTime = new int[3];
    private int maxWindow;

    /** Constructor
    * Initializes the data members of the Task class setting the prepTime member to 0 as default
    * @param  id  an integer value of the task ID
    * @param  descr the description of the task
    * @param  duration length of time the task takes per animal
    * @param  maxWindow maximum flexibility the task has in terms of hours
    * @return
    */
    public Task (int id, String descr, int duration, int maxWindow ){
        this.taskId = id;
        this.taskDescr = descr;
        this.totalTime[0] = duration; //time per animal
        this.totalTime[1] = 0; //preptime
        this.totalTime[2] = totalTime[0] + totalTime[1];
        this.maxWindow = maxWindow;
    }

    /** Constructor
    * Initializes the data members of the Task class
    * @param  id  an integer value of the task ID
    * @param  descr the description of the task
    * @param  duration length of time the task takes per animal
    * @param  prepTime length of prep time the task takes, regardless of number of animals
    * @param  maxWindow maximum flexibility the task has in terms of hours
    * @return 
    */
    public Task (int id, String descr, int duration, int prepTime, int maxWindow ){
        this.taskId = id;
        this.taskDescr = descr;
        this.totalTime[0] = duration;
        this.totalTime[1] = prepTime;
        this.totalTime[2] = totalTime[0] + totalTime[1];
        this.maxWindow = maxWindow;
    }

    /** Setters 
    * setter methods assigning the parameter value to the stored object
    * @return   void
    */
    public void setID(int id) { this.taskId = id; }
    public void setDescription(String descr) { this.taskDescr = descr; }
    public void setDuration(int duration) { this.totalTime[0] = duration; }
    public void setPrepTime(int prepTime) { this.totalTime[1] = prepTime; }
    public void setTotalTime(int totalTime) { this.totalTime[2] = totalTime; }
    public void setMaxWindow(int maxWindow) { this.maxWindow = maxWindow; }

    /** Getters
    * getter methods returning the stored object requested
    */
    public int getID() { return this.taskId; }
    public String getDescription() { return this.taskDescr; }
    public int getDuration() { return this.totalTime[0]; } 
    public int getPrepTime() { return this.totalTime[1]; } 
    public int getTotalTime() { return this.totalTime[2]; } 
    public int getMaxWindow() { return this.maxWindow; } 
}
