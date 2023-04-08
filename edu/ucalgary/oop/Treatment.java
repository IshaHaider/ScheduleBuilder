/** 
 * @author ENSF380 Group 20
 * Treatment is a java class representing one row of data from the Treatment table of the EWR database. 
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Treatment implements LoadData {
    private final static String TRTMTQUERY = "SELECT * FROM treatments";
    private static HashMap<Integer, Treatment> treatments = new HashMap<Integer, Treatment>();
    private Animal animal;
    private Task task;
    private int startHour = 0;

    /** Default Constructor
     * @return  
    */
    public Treatment () {}
    
    /** Constructor
     * Initializes the data members of the Treatment class
     * @param  animal  an instance of the Animal object
     * @param  task an instance of the Task object
     * @param  startHour an integer value of the time (hour) the task can begin 
     * @return
    */
    public Treatment (Animal animal, Task task, int startHour) {
        this.animal = animal;
        this.task = task;
        this.startHour = startHour;
    }

    /** Constructor
     * Initializes the data members of the Treatment class except for startHour
     * This constructor is used for tasks with lowest priority, as they can be done during any time of the day. 
     * @param  animal  an instance of the Animal object
     * @param  task an instance of the Task object
     * @return 
    */
    public Treatment (Animal animal, Task task) {
        this.animal = animal;
        this.task = task;
    }

    public void storeHashMap() throws SpeciesNotFoundException{
        int latestTreatmentID = 1;
        
        // instantiate animal class to retrieve all SQL data for animals
        Animal animalObject = new Animal();
        animalObject.storeHashMap();
        HashMap<Integer, Animal> allAnimals = Animal.getAnimals();

        // instantiate task class to retrieve all SQL data for tasks
        Task taskObject = new Task();
        taskObject.storeHashMap();
        HashMap<Integer, Task> allTasks = Task.getTasks();

        try {
            Connection dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
            Statement myStmt = dbConnect.createStatement();
            ResultSet results = myStmt.executeQuery(TRTMTQUERY);
            while (results.next()) { // for each treatment entry, add it as a Treatment object to treatments ArrayList
                latestTreatmentID = results.getInt("TreatmentID");
                animalObject = allAnimals.get(results.getInt("AnimalID"));
                taskObject = allTasks.get(results.getInt("TaskID"));

                Treatment newTreatment = new Treatment(animalObject, taskObject, results.getInt("StartHour"));
                treatments.put(latestTreatmentID, newTreatment);
            }
            myStmt.close();
            results.close();

            ArrayList<Integer> orphanAnimals = new ArrayList<Integer>();
            // iterate through treatments and list all animal ID's that are associated to task ID #1 - all orphaned animal IDs
            for ( int treatmentKey : treatments.keySet() ) {
                if (treatments.get(treatmentKey).getTask().getTaskID() == 1) { 
                    int animalID = treatments.get(treatmentKey).getAnimal().getAnimalID();
                    if (!orphanAnimals.contains(animalID)) { orphanAnimals.add(animalID); }
                }
            }

            // add cage and feeding treatments 
            latestTreatmentID++; 
            String statement = "INSERT INTO TREATMENTS (AnimalID, TaskID, StartHour) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = dbConnect.prepareStatement(statement);
            PreparedStatement existStatement = dbConnect.prepareStatement(" SELECT COUNT(*) FROM Treatments WHERE TaskID = ? AND AnimalID = ? ");
            ResultSet resultExist;
            int n = 0;
            
            for (Animal animal : allAnimals.values()) {
                String species = animal.getSpecies();
                String mostActive = animal.getMostActive();
                int startHour = 0;
                
                if (mostActive.equals("Nocturnal")) { startHour = 0; } 
                else if (mostActive.equals("Crepuscular")) { startHour = 19; } 
                else if (mostActive.equals("Diurnal")) { startHour = 8; }
                
                for (Task task : allTasks.values()) {
                    String description = task.getDescription();
                    int animalID = animal.getAnimalID();

                    existStatement.setInt(1,task.getTaskID());
                    existStatement.setInt(2,animalID);
                    resultExist = existStatement.executeQuery();
                    n = 0;
                    if ( resultExist.next() ) { n = resultExist.getInt(1);  }

                    if (description.contains("Feeding - " + species)) {
                        if (!orphanAnimals.contains(animalID)) {
                            try {
                                if (n==0){
                                    // insert feeding treatment in SQL database
                                    insertStatement.setInt(1, animalID);
                                    insertStatement.setInt(2, task.getTaskID());
                                    insertStatement.setInt(3,startHour);
                                    insertStatement.execute();

                                    // add feeding treatment to treatments hashMap
                                    Treatment newTreatment = new Treatment(animal, task, startHour);
                                    treatments.put(latestTreatmentID++, newTreatment);
                                }
                                else { // if the task is retrieved directly from database, then manually set prep time
                                    if (species.equals("coyote")) { task.setPrepTime(10); } 
                                    if (species.equals("fox")) { task.setPrepTime(5); }
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("IllegalArgumentException exception when creating treatment object");
                            }
                        }
                    } else if (description.contains("Cage cleaning - " + species)) {
                        try {
                            if (n==0){
                                // insert add cage cleaning treatment in SQL database
                                insertStatement.setInt(1, animalID);
                                insertStatement.setInt(2, task.getTaskID());
                                insertStatement.setInt(3,startHour);
                                insertStatement.execute();

                                // add cage cleaning treatment to treatments hashMap
                                Treatment newTreatment = new Treatment(animal, task);
                                treatments.put(latestTreatmentID++, newTreatment);
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("IllegalArgumentException exception when creating treatment object");
                        } 
                    }
                }
            }
            insertStatement.close();
            existStatement.close();
            dbConnect.close();

        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating treatment object");
        } catch (SQLException ex) { ex.printStackTrace(); }

    }

    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setAnimal(Animal animal) { this.animal = animal; }
    public void setTask(Task task) { this.task = task; }
    public void setStartHour(int startHour) { this.startHour = startHour; }
    public static void setTreatments(HashMap<Integer, Treatment>  newTreatments) { treatments = newTreatments; } 

    /** Getters
     * getter methods returning the stored object requested
    */
    public Animal getAnimal() { return this.animal; }
    public Task getTask() { return this.task; }
    public int getStartHour() { return this.startHour; }
    public static HashMap<Integer, Treatment> getTreatments() { return treatments; } 
}