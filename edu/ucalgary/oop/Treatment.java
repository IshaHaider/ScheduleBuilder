package ENSF380FinalProject.edu.ucalgary.oop;

public class Treatment {
    private int treatmentID;
    private int animalID;
    private int taskID;
    private int startHour;

    public Treatment (int treatmentID, int animalID, int taskID, int startHour){
        this.treatmentID = treatmentID;
        this.animalID = animalID;
        this.taskID = taskID;
        this.startHour = startHour;
    }
    public Treatment (int treatmentID, int animalID, int taskID){
        this.treatmentID = treatmentID;
        this.animalID = animalID;
        this.taskID = taskID;
        this.startHour = 24;
    }
    public void setTreatementID(int id) { this.treatmentID = id; }
    public int getTreatementID() { return this.treatmentID; }
    public void setAnimalID(int id) { this.animalID = id; }
    public int getAnimalID() { return this.animalID; }
    public void setTaskID(int id) { this.taskID = id; }
    public int getTaskID() { return this.taskID; }
    public void setStartHour(int startHour) { this.startHour = startHour; }
    public int getStartHour() { return this.startHour; } 
}
