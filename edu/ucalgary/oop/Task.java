package edu.ucalgary.oop;

public class Task {
    private int taskId;
    private String taskDescr;
    private int[] totalTime = new int[3]; //int[0] = time, int[1] = prep time (not required)
    private int maxWindow;

    public Task (int id, String descr, int duration, int maxWindow ){
        this.taskId = id;
        this.taskDescr = descr;
        this.totalTime[0] = duration; //time per animal
        this.totalTime[1] = 0; //preptime
        this.totalTime[2] = totalTime[0] + totalTime[1];
        this.maxWindow = maxWindow;
        
    }

    public Task (int id, String descr, int duration, int prepTime, int maxWindow ){
        this.taskId = id;
        this.taskDescr = descr;
        this.totalTime[0] = duration;
        this.totalTime[1] = prepTime;
        this.totalTime[2] = totalTime[0] + totalTime[1];
        this.maxWindow = maxWindow;
    }

    public void setID(int id) { this.taskId = id; }
    public int getID() { return this.taskId; }
    public void setDescription(String descr) { this.taskDescr = descr; }
    public String getDescription() { return this.taskDescr; }

    public void setDuration(int duration) { this.totalTime[0] = duration; }
    public int getDuration() { return this.totalTime[0]; } 
    public void setPrepTime(int prepTime) { this.totalTime[1] = prepTime; }
    public int getPrepTime() { return this.totalTime[1]; } 
    public void setTotalTime(int totalTime, int quantity) { this.totalTime[2] = this.totalTime[1] + (this.totalTime[0] * quantity); }
    public int getTotalTime() { return this.totalTime[2]; } 

    public void setMaxWindow(int maxWindow) { this.maxWindow = maxWindow; }
    public int getMaxWindow() { return this.maxWindow; } 
}
