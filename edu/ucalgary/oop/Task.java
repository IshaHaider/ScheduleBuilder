/** 
 * @author ENSF380 Group 20
 * Task is a java class representing  data from the Task table of the EWR database. 
 * the totalTime data member is an integer array, where:
 * totalTime[0] = the duration per animal
 * totalTime[1] = prepTime
 * totalTime[2] = total time of the task combining the duration and prepTime
 * @version     1.3
 * @since       1.0
*/

package edu.ucalgary.oop;
import java.util.HashMap;
import java.sql.*;

public class Task implements LoadData {
    private final static String TASKQUERY = "SELECT * FROM tasks";
    private static HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private int taskID;
    private String taskDescr;
    private int[] totalTime = new int[3];
    private int maxWindow;

    /** Default Constructor
     * @return  
    */
    public Task () {}

    /** Constructor
     * Initializes the data members of the Task class setting the prepTime member to 0 as default
     * @param  id  an integer value of the task ID
     * @param  descr the description of the task
     * @param  duration length of time the task takes per animal
     * @param  maxWindow maximum flexibility the task has in terms of hours
     * @return
    */
    public Task (int id, String descr, int duration, int maxWindow ){
        this.taskID = id;
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
        this.taskID = id;
        this.taskDescr = descr;
        this.totalTime[0] = duration;
        this.totalTime[1] = prepTime;
        this.totalTime[2] = totalTime[0] + totalTime[1];
        this.maxWindow = maxWindow;
    }

    /** storeHashMap() interface method 
     * loads data from the SQL database into a tasks HashMap
     * then adds all cage cleaning and feeding tasks to the database along with the HashMap
     * @return 
    */
    @Override
    public void storeHashMap() {
        try {
            Connection dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
            Statement myStmt = dbConnect.createStatement();
            ResultSet results = myStmt.executeQuery(TASKQUERY);
        
            while (results.next()) { // for each task entry, add it as a Task object to tasks ArrayList
                this.taskID = results.getInt("TaskID");
                this.taskDescr = results.getString("Description");
                this.totalTime[0] = results.getInt("Duration");
                this.maxWindow = results.getInt("MaxWindow");
                Task newTask = new Task(taskID, taskDescr, totalTime[0], maxWindow);
                
                tasks.put(taskID, newTask);
            }

            // ADD CAGE AND FEEDING TASKS
            // constants: maxWindowFeeding = 3, maxWindowCage = 24, feedingDuration = 5
            int latestTaskID = this.taskID + 1;
            String statement = "INSERT INTO TASKS (TaskID, Description, Duration, MaxWindow) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = dbConnect.prepareStatement(statement);
            PreparedStatement existStatement = dbConnect.prepareStatement(" SELECT COUNT(*) FROM Tasks WHERE Description = ? ");
            ResultSet resultExist;
            int n = 0;

            for (Species species : Species.values()) {
                int prepTime = 0, cageDuration = 5;
                if (species.toString() == "coyote") { prepTime = 10; } 
                else if (species.toString() == "fox") { prepTime = 5; } 
                else if (species.toString() == "porcupine") { cageDuration = 10; }

                // check if the feeding statement already exists
                String feedString = "Feeding - " + species.toString();
                
                existStatement.setString(1,feedString);
                resultExist = existStatement.executeQuery();
                n = 0;
                if ( resultExist.next() ) { n = resultExist.getInt(1); }

                if (n == 0) {
                    // insert feeding task in SQL database
                    insertStatement.setInt(1, latestTaskID);
                    insertStatement.setString(2,feedString);
                    insertStatement.setInt(3, 5);
                    insertStatement.setInt(4,3);
                    insertStatement.execute();

                    // add this feeding task to tasks hashMap
                    Task newFeedingTask = new Task(latestTaskID, feedString, 5, prepTime, 3);
                    tasks.put(latestTaskID++, newFeedingTask);
                }
                
                // check if the cage cleaning statement already exists
                String cageString = "Cage cleaning - " + species.toString();
                
                existStatement.setString(1,cageString);
                resultExist = existStatement.executeQuery();
                n = 0;
                if ( resultExist.next() ) { n = resultExist.getInt(1); }

                if (n == 0) {
                    //insert cage task in SQL database
                    insertStatement.setInt(1, latestTaskID);
                    insertStatement.setString(2, cageString);
                    insertStatement.setInt(3,cageDuration);
                    insertStatement.setInt(4,24);
                    insertStatement.execute();

                    // add this cage cleaning task to tasks hashMap
                    Task newCageTask = new Task(latestTaskID, cageString, cageDuration, 0, 24);
                    tasks.put(latestTaskID++, newCageTask);
                }
            }
            existStatement.close();
            insertStatement.close();
            myStmt.close();
            results.close();
            dbConnect.close();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating animal object");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
    
    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setTaskID (int id) { this.taskID = id; }
    public void setDescription(String descr) { this.taskDescr = descr; }
    public void setDuration(int duration) { 
        this.totalTime[0] = duration; 
        this.totalTime[2] = totalTime[0] + totalTime[1];
    }
    public void setPrepTime(int prepTime) { 
        this.totalTime[1] = prepTime; 
        this.totalTime[2] = totalTime[0] + totalTime[1];
    }
    public void setTotalTime(int totalTime) { this.totalTime[2] = totalTime; }
    public void setMaxWindow(int maxWindow) { this.maxWindow = maxWindow; }
    public static void setTasks(HashMap<Integer, Task> newTasks) {tasks = newTasks; }

    /** Getters
     * getter methods returning the stored object requested
    */
    public int getTaskID() { return this.taskID; }
    public String getDescription() { return this.taskDescr; }
    public int getDuration() { return this.totalTime[0]; } 
    public int getPrepTime() { return this.totalTime[1]; } 
    public int getTotalTime() { return this.totalTime[2]; } 
    public int getMaxWindow() { return this.maxWindow; } 
    public static HashMap<Integer, Task> getTasks() { return tasks; }
}