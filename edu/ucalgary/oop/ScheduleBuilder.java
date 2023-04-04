package edu.ucalgary.oop;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// for compilation...
//javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
//java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder

// for tests...
// javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar edu/ucalgary/oop/ScheduleBuilderTest.java
// java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest

public class ScheduleBuilder {
    
    private ArrayList<Animal> animals;
    private ArrayList<Task> tasks;
    private ArrayList<Treatment> treatments;

    private ArrayList<Schedule> schedule = new ArrayList<Schedule>();
    private static int[][] times = { {0, 60}, {1, 60}, {2, 60},{3, 60},{4, 60},{5, 60},{6, 60},{7, 60},{8, 60},{9, 60},{10, 60},{11, 60},
    {12, 60},{13, 60},{14, 60},{15, 60},{16, 60},{17, 60},{18, 60},{19, 60},{20, 60},{21, 60},{22, 60},{23, 60},
    };

    public ScheduleBuilder(LoadData data){
        this.animals = new ArrayList<>(data.getAnimals());
        this.tasks = new ArrayList<>(data.getTasks());
        this.treatments = new ArrayList<>(data.getTreatments());

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

    public void createSchedule() throws IOException{
        // iterate through treatments for maxWindow = 1...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 1...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 1) {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                        newSchedule.setTask(tasks.get(taskIndex).getDescription());
                        newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        
                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++){
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                newSchedule.setAnimalList(animals.get(i).getNickname());
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
        
        // iterate through treatments for maxWindow = 2...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 2...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 2) {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setTask(tasks.get(taskIndex).getDescription());
                        newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
                        
                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;

                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++){
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                newSchedule.setAnimalList(animals.get(i).getNickname());
                        }

                        // if there is no equal or additional time remaining in the original time slot...
                        if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()))
                         { newSchedule.setStartTime(startHourPlus1);  }

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
                        boolean alreadyExists = false;
                        for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { // iterate through schedule to see if the task already exists
                            if (times[schedule.get(scheduleIndex).getStartTime()][1] >= tasks.get(taskIndex).getDuration()){
                                if (tasks.get(taskIndex).getDescription().equals("Feeding - coyote") && schedule.get(scheduleIndex).getTask().equals("Feeding - coyote")){
                                    // add the animal name to schedule object...
                                    for (int i=0; i<animals.size(); i++){
                                        if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                            schedule.get(scheduleIndex).setAnimalList(animals.get(i).getNickname());
                                    }
                                    
                                    // edit timeSpent and timeRemaining
                                    schedule.get(scheduleIndex).setTimeSpent(schedule.get(scheduleIndex).getTimeSpent() + tasks.get(taskIndex).getDuration());
                                    times[schedule.get(scheduleIndex).getStartTime()][1] -= tasks.get(taskIndex).getDuration();
                                    schedule.get(scheduleIndex).setTimeRemaining(times[schedule.get(scheduleIndex).getStartTime()][1]);
                                    
                                    alreadyExists = true;
                                }

                                else if (tasks.get(taskIndex).getDescription().equals("Feeding - fox") && schedule.get(scheduleIndex).getTask().equals("Feeding - fox")){                               
                                    // add the animal name to schedule object...
                                    for (int i=0; i<animals.size(); i++){
                                        if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                            schedule.get(scheduleIndex).setAnimalList(animals.get(i).getNickname());
                                    }

                                    // edit timeSpent and timeRemaining
                                    schedule.get(scheduleIndex).setTimeSpent(schedule.get(scheduleIndex).getTimeSpent() + tasks.get(taskIndex).getDuration());

                                    if (times[schedule.get(scheduleIndex).getStartTime()][0] == schedule.get(scheduleIndex).getStartTime()) { 
                                        times[schedule.get(scheduleIndex).getStartTime()][1] -= tasks.get(taskIndex).getDuration();
                                        schedule.get(scheduleIndex).setTimeRemaining(times[schedule.get(scheduleIndex).getStartTime()][1]);
                                    }
                                    alreadyExists = true;
                                }
                            }
                            
                        }
                        
                        if (!alreadyExists){
                            Schedule newSchedule = new Schedule();
                            newSchedule.setTask(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
    
                            int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                            if (startHourPlus1 > 23) startHourPlus1 -= 24;
                            int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                            if (startHourPlus2 > 23) startHourPlus2 -= 24;
                            
                            // set the animal name to schedule object...
                            for (int i=0; i<animals.size(); i++){
                                if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                    newSchedule.setAnimalList(animals.get(i).getNickname());
                            }
    
                            // if there is no equal or additional time remaining in the original time slot...
                            if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                                newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                                // if there is no equal or additional time remaining in the new time slot...
                                if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) 
                                    {  newSchedule.setStartTime(startHourPlus2); } // set the startHour to + 2 of original
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
        }
        
        // iterate through treatments for maxWindow = 4...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 4...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 4) {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setTask(tasks.get(taskIndex).getDescription());
                        newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());

                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) startHourPlus2 -= 24;
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) startHourPlus3 -= 24;
                         
                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++){
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                newSchedule.setAnimalList(animals.get(i).getNickname());
                        }

                        // if there is no equal or additional time remaining in the original time slot...
                        if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                            // if there is no equal or additional time remaining in the new time slot...
                            if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) 
                                { newSchedule.setStartTime(startHourPlus2); // set the startHour to + 2 of original
                                // if there is no equal or additional time remaining in the new time slot...
                                if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()))
                                    { newSchedule.setStartTime(startHourPlus3);} // set the startHour to + 3 of original
                            }
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
                        newSchedule.setTask(tasks.get(taskIndex).getDescription());
                        newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                        newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());

                        int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                        if (startHourPlus1 > 23) startHourPlus1 -= 24;
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) startHourPlus2 -= 24;
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) startHourPlus3 -= 24;
                        int startHourPlus4 = treatments.get(treatmentIndex).getStartHour() + 4;
                        if (startHourPlus4 > 23) startHourPlus4 -= 24;
                             
                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++){
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                newSchedule.setAnimalList(animals.get(i).getNickname());
                        }

                        // if there is no equal or additional time remaining in the original time slot...
                        if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                            // if there is no equal or additional time remaining in the new time slot...
                            if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                                newSchedule.setStartTime(startHourPlus2); // set the startHour to + 2 of original
                                // if there is no equal or additional time remaining in the new time slot...
                                if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) { 
                                    newSchedule.setStartTime(startHourPlus3); // set the startHour to + 3 of original
                                    // if there is no equal or additional time remaining in the new time slot...
                                    if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime()))
                                        { newSchedule.setStartTime(startHourPlus4); } // set the startHour to + 3 of original
                                }
                            } 
                                
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

        // add cage cleaning tasks...
        // iterate through treatments, if treatmentID == taskID where taskDescr == "cage cleaning - species", 
        // iterate through times and find a place where timeremaining >= taskTotalTime  
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - coyote")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++){
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++){
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        newSchedule.setAnimalList(animals.get(i).getNickname());
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - fox")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++){
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);
        
                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++){
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        newSchedule.setAnimalList(animals.get(i).getNickname());
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - porcupine")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++){
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);
    
                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++){
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        newSchedule.setAnimalList(animals.get(i).getNickname());
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                        
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - raccoon")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++){
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++){
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        newSchedule.setAnimalList(animals.get(i).getNickname());
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - beaver")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++){
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++){
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        newSchedule.setAnimalList(animals.get(i).getNickname());
                                }
                                this.schedule.add(newSchedule);
                                break;
                            }
                        } 
                    }
                }
            }
        }

        combineSimilarTasks();
        addBackupVolunteers();
        printToText();
    }

    public void combineSimilarTasks(){
        // for (int i = 0; i < times.length; i++){ //iterate through the times
        //     for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { //iterate through the schedule arrayList
        //         int existCount = 0;
        //         String tempTask = "";
        //         if (times[i][0] == schedule.get(scheduleIndex).getStartTime()){
        //             tempTask = schedule.get(scheduleIndex).getTask();
        //             existCount++;
        //         }
        //         if (existCount > 0){
        //             if (schedule.get(scheduleIndex).getTask().equals(tempTask))
        //                 existCount++;
        //         }
        //         System.out.println("TIME: " + times[i][0]);
        //         System.out.println("TASK: " + schedule.get(scheduleIndex).getTask());
        //         System.out.println("existCount: " + existCount);

        //     }
        // }
    }

    public void addBackupVolunteers(){
        // this method is called AFTER the schedule is created because it is the last resort if no other options of combining or moving 
        // tasks around is possible

        for (int i = 0; i < times.length; i++){ //iterate through the times
            int count = 0;
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { //iterate through the schedule arrayList
                
                if ((times[i][1] < 0) || count != 0) { // if the timeRemaining for any time slot is negative...
                    if (times[i][0] == schedule.get(scheduleIndex).getStartTime()) { // find the time slots in the schedule arrayList
                        schedule.get(scheduleIndex).setBackupRequired(true); // make those objects have backup volunteer
                        if (count == 0 ){ // get the original time remaining (which is not negative)
                            times[i][1] = schedule.get(scheduleIndex).getTimeRemaining() * 2; // double the TimeRemaining attribute for initial remaining time
                            count++;
                        }
                        else { times[i][1] -= schedule.get(scheduleIndex).getTimeSpent(); }
                        schedule.get(scheduleIndex).setTimeRemaining(times[i][1]);
                    }
                }
            }
        }

        for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) {
            if (schedule.get(scheduleIndex).getTask() != null){
                System.out.println("Task: " + schedule.get(scheduleIndex).getTask() + "\n    Start Time: " + schedule.get(scheduleIndex).getStartTime() + "\n    Time spent: " 
                + schedule.get(scheduleIndex).getTimeSpent() + "\n    Time remaining: " + schedule.get(scheduleIndex).getTimeRemaining() + "\n    Backup: " + schedule.get(scheduleIndex).getBackupRequired());
                System.out.println();
            }
        }

        
    }
    
    public void printToText() throws IOException{
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        BufferedWriter outputStream = new BufferedWriter(new FileWriter("output.txt"));
        outputStream.write("Schedule for " + date + ":\n\n");

        for (int i = 0; i < times.length; i++){ // iterate through the times
            String textForHour = String.valueOf(times[i][0]) + ":00";
            int count = 0;
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { // iterate through the schedule arrayList
                if (times[i][0] == schedule.get(scheduleIndex).getStartTime()) { // find the time slots in the schedule arrayList
                    if (count == 0 ){ // if it is first task under this time slot, check for backup volunteer
                        if (schedule.get(scheduleIndex).getBackupRequired() == true) textForHour += " [+ backup volunteer] \n";
                        else textForHour += "\n";
                        count++;
                    }
                    schedule.get(scheduleIndex).createTasksList();
                    textForHour += schedule.get(scheduleIndex).getTasksList();
                    textForHour += "\n";
                } 
            }
            if (count == 0 ) {
                textForHour += "\nno tasks \n";
            }
            textForHour += "\n";
            outputStream.write(textForHour);
        }
    
        // try/catch for closing file
        try{ 
            outputStream.close();}
            catch (IOException e) { 
            System.out.println("I/O exception when trying to close file");
            e.printStackTrace(); }
    
    }

    public static void main(String[] args) throws IOException{

        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Please enter your SQL root password: ");
        String passInput = myObj.nextLine(); // Read user input

        LoadData database = new LoadData();
        database.createConnection(passInput);
        database.selectAnimals();
        database.selectTasks();
        database.selectTreatments();

        ScheduleBuilder schedule = new ScheduleBuilder(database);
        schedule.createSchedule();
        // System.out.println(database.animals.get(0).getMostActive());
        // System.out.println(database.tasks.get(10).getID());
        // System.out.println(database.tasks.get(10).getDescription());
        // System.out.println(database.tasks.size());
        // System.out.println(database.treatments.size());
        // System.out.println(database.treatments.get(29).getTaskID());
        database.close();
        myObj.close();
    }
}