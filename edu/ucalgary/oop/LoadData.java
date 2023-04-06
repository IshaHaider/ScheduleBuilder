/** 
 * @author ENSF380 Group 20
 * LoadData is a java class that creates the SQL connection
 * loads all the tables from the SQL data base
 * and closes the connection in the end
 * @version     2.1
 * @since       1.0
*/

package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;

public class LoadData {
    private Connection dbConnect;
    private ResultSet results;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ArrayList<Treatment> treatments = new ArrayList<Treatment>();
    
    /** Default Constructor
     * @return   
    */
    public LoadData() {}

    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setResults(ResultSet results) { this.results = results; }
    public void setAnimals(ArrayList<Animal> animals) { this.animals = animals; }
    public void setTasks(ArrayList<Task> tasks) { this.tasks = tasks; }
    public void setTreatments(ArrayList<Treatment> treatments) { this.treatments = treatments; }
    
    /** Getters
     * getter methods returning the stored object requested
    */
    public ResultSet getResults() { return this.results; }
    public ArrayList<Animal> getAnimals() { return this.animals; }
    public ArrayList<Task> getTasks() { return this.tasks; }
    public ArrayList<Treatment> getTreatments() { return this.treatments; }

    /** createConnection()
     * Connects SQL database with Java application
     * @return   void
    */
    public void createConnection() {
        try { 
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
        } catch (SQLException e) {
            e.printStackTrace();}
    }

    /** selectAnimals()
     * Loads all data from ANIMALS SQL table into animals ArrayList
     * @return   void  
    */
    public void selectAnimals() throws SpeciesNotFoundException {
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM animals"); // select all animals
            while (results.next()) { // for each animal entry, add it as an Animal object to animals ArrayList
                Animal newAnimal = new Animal(results.getInt("AnimalID"), results.getString("AnimalNickname"),
                        results.getString("AnimalSpecies"));
                this.animals.add(newAnimal);
                // System.out.println("Animal: " + results.getInt("AnimalID") + ", " + results.getString("AnimalSpecies")
                //         + ", " + results.getString("AnimalNickname") + ", " + newAnimal.getMostActive());
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating animal object");
        }
    }

    /** selectTasks()
     * Loads all data from TASKS SQL table into tasks ArrayList
     * @return   void  
    */
    public void selectTasks() {
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM tasks"); // select all tasks
            while (results.next()) { // for each task entry, add it as a Task object to tasks ArrayList
                Task newTask = new Task(results.getInt("TaskID"), results.getString("Description"),
                        results.getInt("Duration"), results.getInt("MaxWindow"));
                this.tasks.add(newTask);
                // System.out.println("Task: " + results.getInt("TaskID") + ", " + results.getString("Description") + ", "
                //         + results.getInt("Duration") + ", " + results.getInt("MaxWindow"));
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating task object");
        }

        addCageAndFeedingTasks();
    }

    /** addCageAndFeedingTasks()
     * Manually creates the cage cleaning and feeding (only non-orphaned animals) tasks for animals
     * @return   void  
    */
    public void addCageAndFeedingTasks() {
        // add normal feeding and cage cleaning
        int currentTaskID = tasks.get(tasks.size() - 1).getID() + 1; // retrieve the latest task ID and increment it
        
        // constants: maxWindowFeeding = 3, maxWindowCage = 24, feedingDuration = 5
        for (Species species : Species.values()) {
            int prepTime = 0, cageDuration = 5;
            if (species.toString() == "coyote") { prepTime = 10; } 
            else if (species.toString() == "fox") { prepTime = 5; } 
            else if (species.toString() == "porcupine") { cageDuration = 10; }
            
            try {
                Task newFeedingTask = new Task(currentTaskID++, "Feeding - " + species.toString(), 5,
                    prepTime, 3);
                this.tasks.add(newFeedingTask);
                //System.out.println("Task: " + newFeedingTask.getID() + ", " + newFeedingTask.getDescription()+", " + newFeedingTask.getTotalTime());
                Task newCageTask = new Task(currentTaskID++, "Cage cleaning - " + species.toString(), cageDuration, 0, 24);
                this.tasks.add(newCageTask);
                //System.out.println("Task: " + newCageTask.getID() + ", " + newCageTask.getDescription()+ ", " + newCageTask.getTotalTime());
            } 
            catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException exception when creating task object"); }
        }
    }

    /** selectTreatments()
     * Loads all data from TREATMENTS SQL table into treatments ArrayList
     * @return   void  
    */
    public void selectTreatments() {
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM treatments"); // select all treatments
            while (results.next()) { // for each treatment entry, add it as a Treatment object to treatments
                                     // ArrayList
                Treatment newTreatment = new Treatment(results.getInt("TreatmentID"), results.getInt("AnimalID"),
                        results.getInt("TaskID"), results.getInt("StartHour"));
                this.treatments.add(newTreatment);
                // System.out.println("Treatment: " + results.getInt("TreatmentID") + ", " + results.getInt("AnimalID")
                //         + ", " + results.getInt("TaskID") + ", " + results.getInt("StartHour"));
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating treatment object");
        }
        addCageAndFeedingTreatments();
    }

    /** addCageAndFeedingTreatments()
     * Checks if the animal species is present in the treatments ArrayList
     * If it is, then it adds the corresponding cage cleaning and feeding tasks to the treatments ArrayList
     * @return   void  
    */
    public void addCageAndFeedingTreatments() {
        // iterate through treatments and list all animal ID's that are associated to task ID #1 - all orphaned animal IDs
        ArrayList<Integer> orphanAnimals = new ArrayList<Integer>();
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            if (treatments.get(treatmentIndex).getTaskID() == 1)
                { orphanAnimals.add(treatments.get(treatmentIndex).getAnimalID()); }
        }

        int currentTreatmentID = treatments.get(treatments.size() - 1).getTreatementID(); // retrieve the latest
                                                                                          // treatment ID
        int val1 = 0, val2 = 0, startHour = 0;
        while (animals.size() > val1) {
            while (tasks.size() > val2) {
                if (tasks.get(val2).getDescription().contains("Feeding - " + animals.get(val1).getSpecies())) {
                    if (animals.get(val1).getMostActive() == "Nocturnal") { startHour = 0; }
                    else if (animals.get(val1).getMostActive() == "Crepuscular") { startHour = 19; }
                    else if (animals.get(val1).getMostActive() == "Diurnal") { startHour = 8; }

                    if (!orphanAnimals.contains(animals.get(val1).getID())) { // if not an orphan, add the feeding treatment
                        try {
                            Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                                tasks.get(val2).getID(), startHour);
                            this.treatments.add(newTreatment);
                            // System.out.println(
                            //         "Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                            //                 + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());
                        } catch (IllegalArgumentException e) {
                            System.out.println("IllegalArgumentException exception when creating treatment object");
                        }
                        
                    }
                } 
                else if (tasks.get(val2).getDescription().contains("Cage cleaning - " + animals.get(val1).getSpecies())) {
                    try {
                        Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                            tasks.get(val2).getID());
                        this.treatments.add(newTreatment);
                        // System.out
                        //         .println("Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                        //                 + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException exception when creating treatment object");
                    }
                }
                val2++;
            }
            val2 = 0;
            val1++;
        }
    }

    public void changes(int animalID, int hour, int taskID, int OGhour){ 
        try{
            String changing_statement = "update TREATMENTS set StartHour = ? where AnimalID = ? and TaskID = ? and StartHour = ?"; 
            PreparedStatement ps = dbConnect.prepareStatement(changing_statement);
            ps.setInt(1,hour);
            ps.setInt(2,animalID);
            ps.setInt(3,taskID);
            ps.setInt(4,OGhour); 
            int x = ps.executeUpdate();
            ps.close(); 
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    /** close() 
     * Closes the SQL connection
     * @return   void  
    */
    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}