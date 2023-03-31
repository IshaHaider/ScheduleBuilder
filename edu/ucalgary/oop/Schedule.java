
package ENSF380FinalProject.edu.ucalgary.oop;

public class Schedule {
    private String tasksList;
    private int quantity;
    private int timeSpent;
    private int timeRemaining;
    private boolean backupRequired;

    public Schedule(){}
    public Schedule(String tasksList, int quantity, int timeSpent, int timeRemaining, boolean backupRequired){
        this.tasksList = tasksList;
        this.quantity = quantity;
        this.timeSpent = timeSpent;
        this.timeRemaining = timeRemaining;
        this.backupRequired = backupRequired;
    }
    
    public void setTasksList(String tasksList) { this.tasksList = tasksList; }
    public String getTasksList() { return this.tasksList; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getQuantity() { return this.quantity; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public int getTimeSpent() { return this.timeSpent; }
    public void setTimeRemaining(int timeRemaining) { this.timeRemaining = timeRemaining; }
    public int getTimeRemaining() { return this.timeRemaining; }
    public void setBackupRequired(boolean backupRequired) { this.backupRequired = backupRequired; }
    public boolean getBackupRequired() { return this.backupRequired; }
    
}
