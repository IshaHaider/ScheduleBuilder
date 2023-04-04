package edu.ucalgary.oop;

public class Schedule {
    private String tasksList;
    private int startTime;
    private int quantity = 1;
    private int timeSpent;
    private int timeRemaining = 60;
    private boolean backupRequired = false;

    public Schedule(){}
    public Schedule(String tasksList, int startTime, int quantity, int timeSpent, int timeRemaining, boolean backupRequired){
        this.tasksList = tasksList;
        this.startTime = startTime;
        this.quantity = quantity;
        this.timeSpent = timeSpent;
        this.timeRemaining = timeRemaining;
        this.backupRequired = backupRequired;
    }
    
    public void setTasksList(String tasksList) { this.tasksList = tasksList; }
    public String getTasksList() { return this.tasksList; }
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
