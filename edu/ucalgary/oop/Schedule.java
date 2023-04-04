package edu.ucalgary.oop;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<String> animalList = new ArrayList<String>() ;
    private String task;
    private String tasksList;
    private int startTime;
    private int quantity = 1;
    private int timeSpent;
    private int timeRemaining = 60;
    private boolean backupRequired = false;

    public Schedule(){}
    public Schedule(String task, String animal, int startTime, int quantity, int timeSpent, int timeRemaining, boolean backupRequired){
        this.task = task;
        this.animalList.add(animal);
        
        this.startTime = startTime;
        this.quantity = quantity;
        this.timeSpent = timeSpent;
        this.timeRemaining = timeRemaining;
        this.backupRequired = backupRequired;
    }
    
    public void setTask(String task) { this.task = task; }
    public String getTask() { return this.task; }

    public void setAnimalList(String animal) { 
        this.animalList.add(animal);
        this.quantity = animalList.size(); }
    public ArrayList<String> getAnimalList() { return this.animalList; }
    
    public void setTasksList(String tasksList) { this.tasksList = tasksList; }
    public String getTasksList() { return this.tasksList; }

    public void createTasksList() { 
        String animals = ""; // empty string
        int i;
        for (i = 0; i < this.animalList.size() - 1; i++) { animals += this.animalList.get(i) + ", "; }
        animals += this.animalList.get(i); // string with all animals listed, ex: Loner, Biter, Bitter
        this.tasksList = "* " + task + " (" + quantity + ": " + animals + ")"; }
    

    public void setStartTime(int startTime) { this.startTime = startTime; }
    public int getStartTime() { return this.startTime; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getQuantity() { return this.quantity; }

    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public int getTimeSpent() { return this.timeSpent; }
    public void setTimeRemaining(int timeRemaining) { this.timeRemaining = timeRemaining; }
    public int getTimeRemaining() { return this.timeRemaining; }
    public void setBackupRequired(boolean backupRequired) { this.backupRequired = backupRequired; }
    public boolean getBackupRequired() { return this.backupRequired; }
}
