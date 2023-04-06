/** 
 * @author ENSF380 Group 20
 * ScheduleBuilder is the java class containing the main() method
 * it produces the daily schedule based on the data retrieved from LoadData
 * @version     2.1
 * @since       1.0
*/

package edu.ucalgary.oop;

import java.io.*;
import java.sql.*;
import java.util.*;

import edu.ucalgary.oop.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Terminal Commands
 * the following commands can be used for...
 * code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
 * code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
 * test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar edu/ucalgary/oop/ScheduleBuilderTest.java
 * test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest
*/

public class ScheduleBuilder {
    private ArrayList<Animal> animals;
    private ArrayList<Task> tasks;
    private LoadData db; 
    private ArrayList<Treatment> treatments;
    private ArrayList<Schedule> schedule = new ArrayList<Schedule>();
    private static int[][] times = { {0, 60}, {1, 60}, {2, 60},{3, 60},{4, 60},{5, 60},{6, 60},{7, 60},{8, 60},{9, 60},{10, 60},{11, 60},
    {12, 60},{13, 60},{14, 60},{15, 60},{16, 60},{17, 60},{18, 60},{19, 60},{20, 60},{21, 60},{22, 60},{23, 60} };

    /** Constructor
     * @param data  the LoadData object containing all required data for building the schedule
     * @return 
    */
    public ScheduleBuilder(LoadData data){
        this.db = data; 
        this.animals = new ArrayList<>(data.getAnimals());
        this.tasks = new ArrayList<>(data.getTasks());
        this.treatments = new ArrayList<>(data.getTreatments());
    }

    /** Setters 
     * setter methods assigning the parameter value to the stored object
     * @return   void
    */
    public void setAnimals(ArrayList<Animal> animals) { this.animals = animals; }
    public void setTasks(ArrayList<Task> tasks) { this.tasks = tasks; }
    public void setTreatments(ArrayList<Treatment> treatments) { this.treatments = treatments; }
    public void setSchedule(ArrayList<Schedule> schedule) { this.schedule = schedule; }
    public static void setTimes(int[][] newTimes) { times = newTimes; }
    
    /** Getters
     * getter methods returning the stored object requested
    */
    public ArrayList<Animal> getAnimals() { return this.animals; }
    public ArrayList<Task> getTasks() { return this.tasks; }
    public ArrayList<Treatment> getTreatments() { return this.treatments; }
    public ArrayList<Schedule> getSchedule() { return this.schedule; }
    public static int[][] getTimes() { return times; }


    /** createSchedule()
     * Calls on all required methods to create the schedule. 
     * First check for all treatments with MaxWindow ranging from 1 to 5
     * add them to the schedule ArrayList in that respective order of priority.
     * Once all treatments are added, check for available space in schedule
     * add the cage cleaning tasks to those spaces, as they don't have any priority scheduling
     * Once all treatments have been added, call the remaining methods to clean up the schedule,
     * check for backup volunteer need, and then finally print the schedule.
     * @throws IOException
     * @throws IncorrectTimeException
     * @throws ScheduleFailException
     * @return   void
    */
    public void createSchedule() throws IOException, IncorrectTimeException, ScheduleFailException{
        createScheduleMaxWindow1();
        
        // add cage cleaning tasks...
        // iterate through treatments, if treatmentID == taskID where taskDescr == "cage cleaning - species", 
        // iterate through times and find a place where timeremaining >= taskTotalTime  
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - coyote")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()) { // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++) {
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        { newSchedule.setAnimalList(animals.get(i).getNickname()); }
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - fox")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()) { // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);
        
                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++) {
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        { newSchedule.setAnimalList(animals.get(i).getNickname()); }
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - porcupine")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);
    
                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++) {
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        { newSchedule.setAnimalList(animals.get(i).getNickname()); }
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                        
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - raccoon")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++) {
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        { newSchedule.setAnimalList(animals.get(i).getNickname()); }
                                }
                                this.schedule.add(newSchedule);
                                break;
                            } 
                        }
                    }
                    
                    else if (tasks.get(taskIndex).getDescription().equals("Cage cleaning - beaver")) {
                        for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            if (times[timeIndex][1] >= tasks.get(taskIndex).getTotalTime()){ // if timeRemaining of that hour >= taskTotalTime 
                                Schedule newSchedule = new Schedule();
                                newSchedule.setTask(tasks.get(taskIndex).getDescription());
                                newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                                newSchedule.setStartTime(times[timeIndex][0]);

                                times[timeIndex][1] -= newSchedule.getTimeSpent();
                                newSchedule.setTimeRemaining(times[timeIndex][1]);
                                
                                // set the animal name to schedule object...
                                for (int i=0; i<animals.size(); i++) {
                                    if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                        { newSchedule.setAnimalList(animals.get(i).getNickname()); }
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
        addBackupVolunteer();
        printToText();
    }

    /** createScheduleMaxWindow1()
     * Iterates through treatments ArrayList and finds the treatment
     * that has MaxWindow == 1. This means that this object is first priority
     * and must be set in its preferred start hour first.
     * Creates Schedule object and sets the startTime, timeSpent, task, and animalList
     * data members. Then sets the timeRemaining data member according to the time 
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList 
     * @throws IncorrectTimeException
     * @return   void
    */
    public void createScheduleMaxWindow1() throws IncorrectTimeException{
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
                        for (int i=0; i<animals.size(); i++) {
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                { newSchedule.setAnimalList(animals.get(i).getNickname()); }
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
        createScheduleMaxWindow2();
    }

    /** createScheduleMaxWindow2()
     * Iterates through treatments ArrayList and finds the treatment
     * that has MaxWindow == 2. This means that this object is second priority
     * and can be set in its preferred start hour or the next one.
     * Creates Schedule object and sets the startTime, timeSpent, task, and animalList
     * data members. Then sets the timeRemaining data member according to the time 
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList 
     * @throws IncorrectTimeException
     * @return   void
    */
    public void createScheduleMaxWindow2() throws IncorrectTimeException{
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
                        if (startHourPlus1 > 23) { startHourPlus1 -= 24; }

                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++) {
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                { newSchedule.setAnimalList(animals.get(i).getNickname()); }
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
        createScheduleMaxWindow3();
    }

    /** createScheduleMaxWindow3()
     * Iterates through treatments ArrayList and finds the treatment
     * that has MaxWindow == 3. This means that this object is third priority
     * and can be set in its preferred start hour or a maximum of 2 hours after that.
     * Creates Schedule object and sets the startTime, timeSpent, task, and animalList
     * data members. Then sets the timeRemaining data member according to the time 
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList 
     * @throws IncorrectTimeException
     * @return   void
    */
    public void createScheduleMaxWindow3() throws IncorrectTimeException{
        // iterate through treatments for maxWindow = 3...
        for (int treatmentIndex = 0; treatmentIndex < treatments.size(); treatmentIndex++) {
            // iterate through tasks to get maxWindow = 3...
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                if (tasks.get(taskIndex).getID() == treatments.get(treatmentIndex).getTaskID()) {
                    if (tasks.get(taskIndex).getMaxWindow() == 3) {
                        boolean alreadyExists = false;
                        for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { // iterate through schedule to see if the task already exists
                            if (times[schedule.get(scheduleIndex).getStartTime()][1] >= tasks.get(taskIndex).getDuration()){
                                if (tasks.get(taskIndex).getDescription().equals("Feeding - coyote") && schedule.get(scheduleIndex).getTask().equals("Feeding - coyote")) {
                                    // add the animal name to schedule object...
                                    for (int i=0; i<animals.size(); i++) {
                                        if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                            { schedule.get(scheduleIndex).setAnimalList(animals.get(i).getNickname()); }
                                    }
                                    
                                    // edit timeSpent and timeRemaining
                                    schedule.get(scheduleIndex).setTimeSpent(schedule.get(scheduleIndex).getTimeSpent() + tasks.get(taskIndex).getDuration());
                                    times[schedule.get(scheduleIndex).getStartTime()][1] -= tasks.get(taskIndex).getDuration();
                                    schedule.get(scheduleIndex).setTimeRemaining(times[schedule.get(scheduleIndex).getStartTime()][1]);
                                    
                                    alreadyExists = true;
                                }

                                else if (tasks.get(taskIndex).getDescription().equals("Feeding - fox") && schedule.get(scheduleIndex).getTask().equals("Feeding - fox")) {                               
                                    // add the animal name to schedule object...
                                    for (int i=0; i<animals.size(); i++) {
                                        if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                            { schedule.get(scheduleIndex).setAnimalList(animals.get(i).getNickname()); }
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
                        
                        if (!alreadyExists) {
                            Schedule newSchedule = new Schedule();
                            newSchedule.setTask(tasks.get(taskIndex).getDescription());
                            newSchedule.setTimeSpent(tasks.get(taskIndex).getTotalTime());
                            newSchedule.setStartTime(treatments.get(treatmentIndex).getStartHour());
    
                            int startHourPlus1 = treatments.get(treatmentIndex).getStartHour() + 1;
                            if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                            int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                            if (startHourPlus2 > 23) { startHourPlus2 -= 24; }
                            
                            // set the animal name to schedule object...
                            for (int i=0; i<animals.size(); i++) {
                                if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                    { newSchedule.setAnimalList(animals.get(i).getNickname()); }
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
        createScheduleMaxWindow4();
    }

    /** createScheduleMaxWindow4()
     * Iterates through treatments ArrayList and finds the treatment
     * that has MaxWindow == 4. This means that this object is fourth priority
     * and can be set in its preferred start hour or a maximum of 3 hours after that.
     * Creates Schedule object and sets the startTime, timeSpent, task, and animalList
     * data members. Then sets the timeRemaining data member according to the time 
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList 
     * @throws IncorrectTimeException
     * @return   void
    */
    public void createScheduleMaxWindow4() throws IncorrectTimeException{
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
                        if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) { startHourPlus2 -= 24; }
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) { startHourPlus3 -= 24; }
                         
                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++) {
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                { newSchedule.setAnimalList(animals.get(i).getNickname()); }
                        }

                        // if there is no equal or additional time remaining in the original time slot...
                        if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                            // if there is no equal or additional time remaining in the new time slot...
                            if (! (times[newSchedule.getStartTime()][1] >= tasks.get(taskIndex).getTotalTime())) {
                                newSchedule.setStartTime(startHourPlus2); // set the startHour to + 2 of original
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
        createScheduleMaxWindow5();
    }

    /** createScheduleMaxWindow5()
     * Iterates through treatments ArrayList and finds the treatment
     * that has MaxWindow == 5. This means that this object is fifth priority
     * and can be set in its preferred start hour or a maximum of 4 hours after that.
     * Creates Schedule object and sets the startTime, timeSpent, task, and animalList
     * data members. Then sets the timeRemaining data member according to the time 
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList 
     * @throws IncorrectTimeException
     * @return   void
    */
    public void createScheduleMaxWindow5() throws IncorrectTimeException{
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
                        if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                        int startHourPlus2 = treatments.get(treatmentIndex).getStartHour() + 2;
                        if (startHourPlus2 > 23) { startHourPlus2 -= 24; }
                        int startHourPlus3 = treatments.get(treatmentIndex).getStartHour() + 3;
                        if (startHourPlus3 > 23) { startHourPlus3 -= 24; }
                        int startHourPlus4 = treatments.get(treatmentIndex).getStartHour() + 4;
                        if (startHourPlus4 > 23) { startHourPlus4 -= 24; }
                             
                        // set the animal name to schedule object...
                        for (int i=0; i<animals.size(); i++) {
                            if (animals.get(i).getID() == treatments.get(treatmentIndex).getAnimalID())
                                { newSchedule.setAnimalList(animals.get(i).getNickname()); }
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

    }

    /** combineSimilarTasks()
     * iterates over the times array and checks all of the scheduled tasks per hour, 
     * storing only the first occurrence of each task. A temporary HashMap stores these tasks 
     * Strings as keys with temporary "ID" values. These ID values are used as the keys for a 
     * second temporary HashMap that stores the Schedule objects. 
     * 1st nested for-loop: iterates over the schedule ArrayList. If the task is new, it 
     * is added to the two HashMaps. If the task re-occurs, the pre-existing task and Schedule object are
     * found in the HashMaps and updated accordingly. Finally, the repeated task is removed 
     * from the schedule ArrayList.
     * 2nd nested for-loop: iterates over the HashMap containing the non-duplicate Schedule 
     * objects of that hour. Re-calculates the remaining time after each task is performed.
     * @return   void
    */
    public void combineSimilarTasks() throws IncorrectTimeException{
        for (int i = 0; i < times.length; i++) { // iterate through the times
            HashMap<String, Integer> mapForString = new HashMap<String, Integer>();
            HashMap<Integer, Schedule> mapForSchedule = new HashMap<Integer, Schedule>();
            int count =  0;
            times[i][1] = 60;
            
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { //iterate through the schedule arrayList
                if (schedule.get(scheduleIndex).getStartTime() == times[i][0]) {
                    String key = schedule.get(scheduleIndex).getTask();
                    if (mapForString.containsKey(key)){ // if the task exists in the list
                        Schedule tempSchedule = mapForSchedule.get(mapForString.get(key));
                        tempSchedule.setAnimalList(schedule.get(scheduleIndex).getAnimalList().get(0));
                        int time = tempSchedule.getTimeSpent() + schedule.get(scheduleIndex).getTimeSpent();
                        tempSchedule.setTimeSpent(time);
                        // since it is a duplicate task, remove it from the schedule ArrayList and decrement the index
                        schedule.remove(scheduleIndex);
                        scheduleIndex--;
                    }
                    else { 
                        mapForString.put(key, count); 
                        mapForSchedule.put(count, schedule.get(scheduleIndex));
                        count++; 
                    }
                }
            }
            for (int mapIndex = 0; mapIndex < mapForSchedule.size(); mapIndex++){ //iterate through the HashMap containing the schedules
                // update the remaining time in the times array along with the timeRemaining data member of the object
                times[i][1] -= mapForSchedule.get(mapIndex).getTimeSpent();
                mapForSchedule.get(mapIndex).setTimeRemaining(times[i][1]);
            }
        }
    }

    /** addBackupVolunteer()
     * This method is called after the schedule is roughly created because 
     * it is the last resort if no other options of combining or moving 
     * tasks around is possible. It checks if the second value of each array of 
     * values (meaning the time remaining for each hour) in the times array is 
     * negative, if it is, then it finds all Schedule objects associated with that 
     * hour and updates them to have a backup volunteer. Additionally, it doubles 
     * the time remaining as now there are two volunteers for that hour. To calls on a GUI class
     * to inform the user that there is a need and will not continue the program untill the user 
     * confirms that they have gotten the backup volunteer
     * @throws IncorrectTimeException
     * @return   void
    */
    public void addBackupVolunteer() throws IncorrectTimeException{
        for (int i = 0; i < times.length; i++) { //iterate through the times
            int count = 0;
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { //iterate through the schedule arrayList
                if ((times[i][1] < 0) || count != 0) { // if the timeRemaining for any time slot is negative...
                    if (times[i][0] == schedule.get(scheduleIndex).getStartTime()) { // find the time slots in the schedule arrayList
                    schedule.get(scheduleIndex).setBackupRequired(true); // make those objects have backup volunteer
                    if (count == 0 ) { // get the original time remaining (which is not negative)
                        times[i][1] = schedule.get(scheduleIndex).getTimeRemaining() * 2; // double the TimeRemaining attribute for initial remaining time
                        count++; }
                    else { times[i][1] -= schedule.get(scheduleIndex).getTimeSpent(); }
                    schedule.get(scheduleIndex).setTimeRemaining(times[i][1]);
                    }
                }
            }
        }

        // // THIS IS TEMPORARY COMMENT TO SEE THE OUTPUT, DO NOT DELETE THANK YOU!
        // for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) {
        //     if (schedule.get(scheduleIndex).getTask() != null){
        //         System.out.println("Task: " + schedule.get(scheduleIndex).getTask() + "\n    Start Time: " + schedule.get(scheduleIndex).getStartTime() + "\n    Time spent: " 
        //         + schedule.get(scheduleIndex).getTimeSpent() + "\n    Time remaining: " + schedule.get(scheduleIndex).getTimeRemaining() + "\n    Backup: " + schedule.get(scheduleIndex).getBackupRequired());
        //         System.out.println();
        //     }
        // }
        
    }
    
    /** printToText()
     * This method is called after the schedule is entirely created. After making
     * sure that it is a valid schedule, it writes the schedule to an output text 
     * file. First it prints the current date, then iterates through the times
     * array and prints the time and its corresponding tasks under it. 
     * This is done by iterating through each element of the schedule ArrayList. 
     * @throws IOException
     * @throws ScheduleFailException
     * @return   void
    */
    public void printToText() throws IOException, ScheduleFailException{
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        BufferedWriter outputStream = new BufferedWriter(new FileWriter("output.txt"));
        outputStream.write("Schedule for " + date + ":\n\n");

        //A big string of all of the values previous to it all
        for (int i = 0; i < times.length; i++) { // iterate through the times
            String textForHour = String.valueOf(times[i][0]) + ":00";
            int count = 0;
            //this.db.changes(1, 5, 9, 22);
            
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { // iterate through the schedule arrayList
                if (schedule.get(scheduleIndex).getTimeRemaining() < 0) { 
                    //how do i know which animal contrubited to this...?
                    // getthe AnimalID and the TaskID and pass it to the user so they know where, but also store that 
                    String animal; 
                    if(schedule.get(scheduleIndex).getAnimalList().size() == 1){
                        ArrayList<String> x = (schedule.get(scheduleIndex).getAnimalList());
                        animal = x.get(0);  
                    }
                    //HELLOOOOOOO ISHA IDK HOW TO DO THIS PART TO FIND THE ONE PET WHERE IT IS MAKING THAT MISTAKE......!!!! CALL ME PLS 
                    else{
                        animal = "print"; 
                    }
                    errorGUI error = new errorGUI("negative", schedule.get(scheduleIndex).getStartTime(), schedule.get(scheduleIndex).getTask(), animal); 
                   
                    while(error.getStates()){ 
                       continue; 
                   
                    } 
                    //retriving the values
                    int newHour = error.getSelectedHour(); 
                    int petID = 0; 
                    int taskID = 0 ;

                    for(Animal x : this.animals){
                        if(x.getNickname() == animal){
                            petID = x.getID();
                        }
                    }

                    for(Task y: this.tasks){
                        if(y.getDescription() == schedule.get(scheduleIndex).getTask()){
                            taskID = y.getID(); 
                        }
                    }

                    //changing the database.. we want to change treatments
                    this.db.changes(petID, newHour, taskID, schedule.get(scheduleIndex).getStartTime());
                }
                else if (schedule.get(scheduleIndex).getBackupRequired() && (schedule.get(scheduleIndex).getTimeRemaining() + schedule.get(scheduleIndex).getTimeSpent()) > 120) {
                    String animal; 
                    if(schedule.get(scheduleIndex).getAnimalList().size() == 1){
                        ArrayList<String> x = (schedule.get(scheduleIndex).getAnimalList());
                        animal = x.get(0);  
                    }
                    //HELLOOOOOOO ISHA IDK HOW TO DO THIS PART TO FIND THE ONE PET WHERE IT IS MAKING THAT MISTAKE......!!!! CALL ME PLS 
                    else{
                        animal = "print"; 
                    }
                    errorGUI error = new errorGUI("over", schedule.get(scheduleIndex).getStartTime(), schedule.get(scheduleIndex).getTask(), animal); 
                    while(error.getStates()){
                        continue; 
                    } int newHour = error.getSelectedHour(); 
                    int petID = 0;
                    int taskID = 0;
                    
                    for(Animal x : this.animals){
                        if(x.getNickname() == animal){
                            petID = x.getID();
                        }
                    }

                    for(Task y: this.tasks){
                        if(y.getDescription() == schedule.get(scheduleIndex).getTask()){
                            taskID = y.getID(); 
                        }
                    }

                    //changing the database....we want to change treatments
                    this.db.changes(petID, newHour, taskID, schedule.get(scheduleIndex).getStartTime()); 
                }
                
                if (times[i][0] == schedule.get(scheduleIndex).getStartTime()) { // find the time slots in the schedule arrayList
                    if (count == 0 ) { // if it is first task under this time slot, check for backup volunteer
                        if (schedule.get(scheduleIndex).getBackupRequired() == true) { 
                            textForHour += " [+ backup volunteer] \n"; 
                            VolunteerGUI volunteer = new VolunteerGUI(Integer.toString(times[i][0]));
                            while(volunteer.getState()){
                                continue; 
                            }
                        }
                        
                        else { textForHour += "\n"; }
                        count++;
                    }
                    schedule.get(scheduleIndex).createTaskString();
                    textForHour += schedule.get(scheduleIndex).getTaskString();
                    textForHour += "\n";
                } 
            }
            if (count == 0 ) { textForHour += "\nno tasks \n"; }
            textForHour += "\n";
            outputStream.write(textForHour);
        } 
    
        // try/catch for closing file
        try { 
            outputStream.close();
        } catch (IOException e) { 
            System.out.println("I/O exception when trying to close file");
            e.printStackTrace(); }

        displaysch displas = new displaysch("start"); 
        this.db.close(); 
    
    }

    /** main()
     * This is the main method called to create the daily schedule
     * @param args the String array of arguments passed from the user (unused) 
     * @throws IOException
     * @throws IncorrectTimeException
     * @throws SpeciesNotFoundException
     * @throws ScheduleFailException
     * @return   void
    */
    public static void main(String[] args) throws IOException, IncorrectTimeException, SpeciesNotFoundException, ScheduleFailException{
        LoadData database = new LoadData();
        database.createConnection();
        database.selectAnimals();
        database.selectTasks();
        database.selectTreatments();

        ScheduleBuilder schedule = new ScheduleBuilder(database);
        schedule.createSchedule();
        //database.close();
    }
}
