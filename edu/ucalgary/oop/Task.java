package ENSF380FinalProject.edu.ucalgary.oop;

public class Task {
    private int taskId;
    private String taskDescr;
    private int duration;
    private int maxWindow;

    public Task (int id, String descr, int duration, int maxWindow ){
        this.taskId = id;
        this.taskDescr = descr;
        this.duration = duration;
        this.maxWindow = maxWindow;
    }
    public void setID(int id) { this.taskId = id; }
    public int getID() { return this.taskId; }
    public void setDescription(String descr) { this.taskDescr = descr; }
    public String getDescription() { return this.taskDescr; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getDuration() { return this.duration; } 
    public void setMaxWindow(int maxWindow) { this.maxWindow = maxWindow; }
    public int getMaxWindow() { return this.maxWindow; } 
    
}
