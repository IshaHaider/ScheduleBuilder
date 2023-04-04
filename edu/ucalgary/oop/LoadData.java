package edu.ucalgary.oop;


import java.sql.*;
import java.util.ArrayList;

public class LoadData {
    private Connection dbConnect;
    private ResultSet results;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ArrayList<Treatment> treatments = new ArrayList<Treatment>();
    
    public LoadData() {}

    // getters and setters
    public void setResults(ResultSet results){this.results = results;}
    public ResultSet getResults(){return this.results;}
    public void setAnimals(ArrayList<Animal> animals){this.animals = animals;}
    public ArrayList<Animal> getAnimals(){return this.animals;}
    public void setTasks(ArrayList<Task> tasks){this.tasks = tasks;}
    public ArrayList<Task> getTasks(){return this.tasks;}
    public void setTreatments(ArrayList<Treatment> treatments){this.treatments = treatments;}
    public ArrayList<Treatment> getTreatments(){return this.treatments;}

    public void createConnection() {
        try { // connect SQL database with Java application
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectAnimals() {
        try {
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM animals"); // select all animals
            while (results.next()) { // for each animal entry, add it as an Animal object to animals ArrayList
                Animal newAnimal = new Animal(results.getInt("AnimalID"), results.getString("AnimalNickname"),
                        results.getString("AnimalSpecies"));
                this.animals.add(newAnimal);
                // System.out.println("Animal: " + results.getInt("AnimalID") + ", " + results.getString("AnimalSpecies")
                //         + ", " + results.getString("AnimalNickname"));
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException e) {
        }
    }

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
        }

        addCageAndFeedingTasks();
    }

    public void addCageAndFeedingTasks() {
        // add normal feeding and cage cleaning
        int currentTaskID = tasks.get(tasks.size() - 1).getID() + 1; // retrieve the latest task ID and increment it
        int prepTime = 0;
        int feedingDuration = 0;
        int cageDuration = 5;

        // maxWindowFeeding = 3 and maxWindowCage = 24
        for (Species species : Species.values()) {
            if (species.toString() == "coyote") {
                prepTime = 10;
                feedingDuration = 5;
                cageDuration = 5;
            } else if (species.toString() == "fox") {
                prepTime = 5;
                feedingDuration = 5;
                cageDuration = 5;
            } else if (species.toString() == "porcupine") {
                prepTime = 0;
                feedingDuration = 5;
                cageDuration = 10;
            } else if (species.toString() == "raccoon") {
                prepTime = 0;
                feedingDuration = 5;
                cageDuration = 5;
            } else {
                prepTime = 0;
                feedingDuration = 5;
                cageDuration = 5;
            }
            Task newFeedingTask = new Task(currentTaskID++, "Feeding - " + species.toString(), feedingDuration,
                    prepTime, 3);
            this.tasks.add(newFeedingTask);
            System.out.println("Task: " + newFeedingTask.getID() + ", " + newFeedingTask.getDescription());
            Task newCageTask = new Task(currentTaskID++, "Cage cleaning - " + species.toString(), cageDuration, 0, 24);
            this.tasks.add(newCageTask);
            System.out.println("Task: " + newCageTask.getID() + ", " + newCageTask.getDescription());
        }

    }

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
        }
        addCageAndFeedingTreatments();
    }

    public void addCageAndFeedingTreatments() {
        // iterate through treatments and list all animal ID's that are associated to task ID #1 - all orphaned animal IDs
        ArrayList<Integer> orphanAnimals = new ArrayList<Integer>();
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            if (treatments.get(treatmentIndex).getTaskID() == 1)
                orphanAnimals.add(treatments.get(treatmentIndex).getAnimalID());
        }

        int currentTreatmentID = treatments.get(treatments.size() - 1).getTreatementID(); // retrieve the latest
                                                                                          // treatment ID
        int val1 = 0, val2 = 0, startHour = 0;
        while (animals.size() > val1) {
            while (tasks.size() > val2) {
                if (tasks.get(val2).getDescription().contains("Feeding - " + animals.get(val1).getSpecies())) {
                    if (animals.get(val1).getMostActive() == "Nocturnal")
                        startHour = 0;
                    else if (animals.get(val1).getMostActive() == "Crepuscular")
                        startHour = 19;
                    else if (animals.get(val1).getMostActive() == "Diurnal")
                        startHour = 8;

                    if (!orphanAnimals.contains(animals.get(val1).getID())) { // if not an orphan, add the feeding treatment
                        Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                                tasks.get(val2).getID(), startHour);
                        this.treatments.add(newTreatment);
                        System.out.println(
                                "Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                                        + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());
                    }
                } else if (tasks.get(val2).getDescription()
                        .contains("Cage cleaning - " + animals.get(val1).getSpecies())) {
                    Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                            tasks.get(val2).getID());
                    this.treatments.add(newTreatment);
                    System.out
                            .println("Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                                    + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());

                }
                val2++;
            }
            val2 = 0;
            val1++;
        }
    }

    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
