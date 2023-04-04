package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.SysexMessage;

// for compilation...
//javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/LoadData.java
//java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.LoadData

// for tests...
// javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar edu/ucalgary/oop/LoadDataTest.java
// java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.oop.LoadDataTest

public class LoadData {
    private Connection dbConnect;
    private ResultSet results;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ArrayList<Treatment> treatments = new ArrayList<Treatment>();
    private ArrayList<String> taskStrings = new ArrayList<String>();

    private ArrayList<Schedule> schedule = new ArrayList<Schedule>();
    private static int[][] times = { {0, 60}, {1, 60}, {2, 60},{3, 60},{4, 60},{5, 60},{6, 60},{7, 60},{8, 60},{9, 60},{10, 60},{11, 60},
    {12, 60},{13, 60},{14, 60},{15, 60},{16, 60},{17, 60},{18, 60},{19, 60},{20, 60},{21, 60},{22, 60},{23, 60},
    };

    public LoadData() {}

    public void createConnection(String password) {
        try { // connect SQL database with Java application
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "root", password);
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
            // System.out.println("Task: " + newFeedingTask.getID() + ", " + newFeedingTask.getDescription());
            Task newCageTask = new Task(currentTaskID++, "Cage cleaning - " + species.toString(), cageDuration, 0, 24);
            this.tasks.add(newCageTask);
            // System.out.println("Task: " + newCageTask.getID() + ", " + newCageTask.getDescription());
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

                    if (!animals.get(val1).getNickname().contains("and")) { // if not an orphan, add the feeding
                                                                            // treatment
                        Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                                tasks.get(val2).getID(), startHour);
                        this.treatments.add(newTreatment);
                        // System.out.println(
                        //         "Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                        //                 + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());
                    }
                } else if (tasks.get(val2).getDescription()
                        .contains("Cage cleaning - " + animals.get(val1).getSpecies())) {
                    Treatment newTreatment = new Treatment(++currentTreatmentID, animals.get(val1).getID(),
                            tasks.get(val2).getID());
                    this.treatments.add(newTreatment);
                    // System.out
                    //         .println("Treatment: " + newTreatment.getTreatementID() + ", " + newTreatment.getAnimalID()
                    //                 + ", " + newTreatment.getTaskID() + ", " + newTreatment.getStartHour());

                }
                val2++;
            }
            val2 = 0;
            val1++;
        }
    }

    // find all tasks that have maxWindow = 1, set their times
    // check if (taskDescriptions are equal and startTimes are equal) --> combine

    // find all tasks that have maxWindow = 2, set their times
    // check if (taskDescriptions are equal and startTimes are equal) --> combine



    // if times overlap with tasks...
    // first check if (taskDescriptions are equal and timeRemaining >= totalTime)
    // --> combine
    // --> setTotalTime
    // else, make backupVolunter = true
    // set their timeRemaining's according to whether a backup volunteer is called
    // or not

    // do the above with maxWindows = 2, 3, 4, and 5 (in that order)
    // once done placing all treatments in their spots, cage cleaning remains
    // (recall maxWindow = 24)
    // check places where timeRemaining >= (totalTime of cage cleaning)

    public void createSchedule() {
        // iterate through treatments for maxWindow = 1...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 1...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 1) {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                        newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                        newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());

                        if (times[newSchedule.getStartTime()][0] == newSchedule.getStartTime()) { 
                            times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);
                        }
                        
                        this.schedule.add(newSchedule);
                    }
                }
            }
        }
        
        // iterate through treatments for maxWindow = 2...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 2...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 2) {
                        Schedule newSchedule = new Schedule();
                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        
                        if (times[newSchedule.getStartTime()][0] == treatments.get(treatmentIndex).getStartHour() && 
                        times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else { // else set the startHour to + 1 of original
                            newSchedule.setStartTime(startHourPlus1);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        if (times[newSchedule.getStartTime()][0] == newSchedule.getStartTime()) { 
                            times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);
                        }
                        
                        this.schedule.add(newSchedule);
                        
                    }
                }
            }
        }
        
        // iterate through treatments for maxWindow = 3...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 3...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 3) {
                        Schedule newSchedule = new Schedule();
                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) startHourPlus2 -= 24;

                        // if the startTime is same as startHour and there is equal or additional time remaining in that slot...
                        if (times[newSchedule.getStartTime()][0] == treatments.get(treatmentIndex).getStartHour() 
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) { 
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else if (times[newSchedule.getStartTime()][0] == startHourPlus1
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 1 of original
                            newSchedule.setStartTime(startHourPlus1);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else { // else set the startHour to + 2 of original
                            newSchedule.setStartTime(startHourPlus2);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        if (times[newSchedule.getStartTime()][0] == newSchedule.getStartTime()) { 
                            times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);
                        }
                        this.schedule.add(newSchedule);
                    }
                }
            }
        }
        
        // iterate through treatments for maxWindow = 4...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 4...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 4) {
                        Schedule newSchedule = new Schedule();
                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) startHourPlus2 -= 24;
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) startHourPlus3 -= 24;
                                    
                        // if the startTime is same as startHour and there is equal or additional time remaining in that slot...
                        if (times[newSchedule.getStartTime()][0] == treatments.get(treatmentIndex).getStartHour() 
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else if (times[newSchedule.getStartTime()][0] == startHourPlus1
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 1 of original
                            newSchedule.setStartTime(startHourPlus1);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else if (times[newSchedule.getStartTime()][0] == startHourPlus2
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 2 of original
                            newSchedule.setStartTime(startHourPlus2);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else { // else set the startHour to + 3 of original
                            newSchedule.setStartTime(startHourPlus3);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        if (times[newSchedule.getStartTime()][0] == newSchedule.getStartTime()) { 
                            times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);
                        }
                        this.schedule.add(newSchedule);
                    }
                }
            }
        }
        
        // iterate through treatments for maxWindow = 5...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 5...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 5) {
                        Schedule newSchedule = new Schedule();
                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) startHourPlus2 -= 24;
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) startHourPlus3 -= 24;
                        int startHourPlus4 = treatments.get(treatmentIndex).getStartHour() + 4;
                        if (startHourPlus4 > 23) startHourPlus4 -= 24;
                             
                        // if the startTime is same as startHour and there is equal or additional time remaining in that slot...
                        if (times[newSchedule.getStartTime()][0] == treatments.get(treatmentIndex).getStartHour() 
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else if (times[newSchedule.getStartTime()][0] == startHourPlus1
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 1 of original
                            newSchedule.setStartTime(startHourPlus1);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }
                        else if (times[newSchedule.getStartTime()][0] == startHourPlus2
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 2 of original
                            newSchedule.setStartTime(startHourPlus2);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        else if (times[newSchedule.getStartTime()][0] == startHourPlus3
                        && times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()) {
                        // else set the startHour to + 3 of original
                            newSchedule.setStartTime(startHourPlus3);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        else { // else set the startHour to + 4 of original
                            newSchedule.setStartTime(startHourPlus4);
                            newSchedule.setTasksList(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        }

                        if (times[newSchedule.getStartTime()][0] == newSchedule.getStartTime()) { 
                            times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);
                        }
                        this.schedule.add(newSchedule);
                    }
                }
            }
        }
        
        for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) {
            if (schedule.get(scheduleIndex).getTasksList() != null){
                System.out.println("Task: " + schedule.get(scheduleIndex).getTasksList() + "\n    Start Time: " + schedule.get(scheduleIndex).getStartTime() + "\n    Time spent: " 
                + schedule.get(scheduleIndex).getTimeSpent() + "\n    Time remaining: " + schedule.get(scheduleIndex).getTimeRemaining() );
            }
        }

    }
    

    // public void createTasksList() {
    //     for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
    //         String taskString = "";
    //         int taskMaxWindow;
    //         for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
    //             if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
    //                 taskString = tasks.get(taskIndex).getDescription();
    //                 taskMaxWindow = tasks.get(taskIndex).getMaxWindow();
    //             }
    //         }
    //         for (int animalIndex = 0; animalIndex < animals.size(); animalIndex++) {
    //             if (animals.get(animalIndex).getID() == treatments.get(treatmentIndex).getAnimalID()) {
    //                 taskString += " (" + animals.get(animalIndex).getNickname() + ")";
    //             }
    //         }
    //         System.out.println(taskString);
    //         this.taskStrings.add(taskString);
    //     }
    // }

    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Please enter your SQL root password: ");
        String passInput = myObj.nextLine(); // Read user input

        LoadData database = new LoadData();

        database.createConnection(passInput);
        database.selectAnimals();
        database.selectTasks();
        database.selectTreatments();
        database.createSchedule();
        // System.out.println("TIME[21][0]: " + times[22][0]);
        // System.out.println("TIME[21][1]: " + times[21][1]);
        // System.out.println(database.animals.get(0).getMostActive());
        // System.out.println(database.tasks.get(10).getID());
        // System.out.println(database.tasks.get(10).getDescription());
        // System.out.println(database.tasks.size());
        // System.out.println(database.treatments.size());
        // System.out.println(database.treatments.get(29).getTaskID());
        // System.out.println("HELLO TEST " + database.taskStrings.get(30));
        database.close();
        myObj.close();
    }
}
