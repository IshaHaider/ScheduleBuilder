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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Terminal Commands
 * the following commands can be used for...
 * code compilation: javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/ScheduleBuilder.java
 * code execution: java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleBuilder
 * test compilation: javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar edu/ucalgary/oop/ScheduleBuilderTest.java
 * test execution: java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.oop.ScheduleBuilderTest
 */

public class ScheduleBuilder {
    private HashMap<Integer, Treatment> allTreatments = new HashMap<Integer, Treatment>();
    private ArrayList<Schedule> schedule = new ArrayList<Schedule>();
    private static int[][] times = { { 0, 60 }, { 1, 60 }, { 2, 60 }, { 3, 60 }, { 4, 60 }, { 5, 60 }, { 6, 60 },
            { 7, 60 }, { 8, 60 }, { 9, 60 }, { 10, 60 }, { 11, 60 },
            { 12, 60 }, { 13, 60 }, { 14, 60 }, { 15, 60 }, { 16, 60 }, { 17, 60 }, { 18, 60 }, { 19, 60 }, { 20, 60 },
            { 21, 60 }, { 22, 60 }, { 23, 60 } };

    /** Default Constructor
     * @return
     */
    public ScheduleBuilder() throws SpeciesNotFoundException, IllegalArgumentException {
        try {
            Treatment treatment = new Treatment();
            treatment.storeHashMap();
            allTreatments = Treatment.getTreatments();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException exception when creating treatment object");
        }
    }

    /** Setters
     * setter methods assigning the parameter value to the stored object
     * @return void
     */
    public void setAllTreatments(HashMap<Integer, Treatment> newTreatments) { this.allTreatments = newTreatments; }
    public void setSchedule(ArrayList<Schedule> schedule) { this.schedule = schedule;}
    public static void setTimes(int[][] newTimes) { times = newTimes;}

    /** Getters
     * getter methods returning the stored object requested
     */
    public HashMap<Integer, Treatment> getAllTreatments() { return this.allTreatments; }
    public ArrayList<Schedule> getSchedule() { return this.schedule; }
    public static int[][] getTimes() { return times;
}

    /** createSchedule()
     * Calls on all required methods to create the schedule.
     * First check for all treatments with MaxWindow ranging from 1 to 5
     * add them to the schedule HashMap in that respective order of priority.
     * Once all treatments are added, check for available space in schedule
     * add the cage cleaning tasks to those spaces, as they don't have any priority
     * scheduling. Once all treatments have been added, call the remaining methods to 
     * clean up the schedule, check for backup volunteer need, and then finally print the schedule.
     * This method returns true if the schedule prints properly, else false.
     * 
     * @throws IOException
     * @throws IllegalArgumentException
     * @return boolean
     */
    public boolean createSchedule() throws IOException, IllegalArgumentException {
        createScheduleMaxWindow1();

        // add cage cleaning tasks to schedule...
        // iterate through treatments, if treatmentID == taskID where taskDescr == "cage
        // cleaning - species", iterate through times and find a place where timeremaining >= taskTotalTime
        
        String[] searchStrings = { "Cage cleaning - coyote", "Cage cleaning - fox", "Cage cleaning - porcupine",
                "Cage cleaning - raccoon", "Cage cleaning - beaver" };
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            for (String search : searchStrings) {
                if (allTreatments.get(treatmentKey).getTask().getDescription().equals(search)) {
                    for (int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                        if (times[timeIndex][1] >= currentTask.getTotalTime()) { // if timeRemaining of that hour >=
                                                                                 // taskTotalTime
                            try { Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                            times[timeIndex][0], currentTask.getTotalTime());

                            times[timeIndex][1] -= newSchedule.getTimeSpent();
                            newSchedule.setTimeRemaining(times[timeIndex][1]);

                            this.schedule.add(newSchedule);
                            break;
                            } catch (IllegalArgumentException e) {
                                System.out.println("IllegalArgumentException exception when creating schedule object");}
                        }
                    }
                }
            }
        }
        
        combineSimilarTasks();
        addBackupVolunteer();
        return printToText();
    }

    /** createScheduleMaxWindow1()
     * Iterates through treatments HashMap and finds the treatment
     * that has MaxWindow == 1. This means that this object is first priority
     * and must be set in its preferred start hour first.
     * Creates Schedule object and sets the treatmentID, startTime, timeSpent, task, and
     * animalList data members. Then sets the timeRemaining data member according to the time
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList
     * @throws IllegalArgumentException
     * @return void
     */
    public void createScheduleMaxWindow1() throws IllegalArgumentException{
        // iterate through treatments for maxWindow = 1...
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            if (currentTask.getMaxWindow() == 1) {
                try {
                Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                        currentTreatment.getStartHour(), currentTask.getTotalTime());

                times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);

                this.schedule.add(newSchedule);
                } catch (IllegalArgumentException e) {
                    System.out.println("IllegalArgumentException exception when creating schedule object");}
            }
        }
        createScheduleMaxWindow2();
    }

    /** createScheduleMaxWindow2()
     * Iterates through treatments HashMap and finds the treatment
     * that has MaxWindow == 2. This means that this object is second priority
     * and can be set in its preferred start hour or the next one.
     * Creates Schedule object and sets the treatmentID, startTime, timeSpent, task, and
     * animalList data members. Then sets the timeRemaining data member according to the time
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList
     * @throws IllegalArgumentException
     * @return void
     */
    public void createScheduleMaxWindow2() throws IllegalArgumentException{
        // iterate through treatments for maxWindow = 2...
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            if (currentTask.getMaxWindow() == 2) {
                try {
                Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                        currentTreatment.getStartHour(), currentTask.getTotalTime());

                int startHourPlus1 = currentTreatment.getStartHour() + 1;
                if (startHourPlus1 > 23) { startHourPlus1 -= 24; }

                // if there is no equal or additional time remaining in the original time slot...
                if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                    newSchedule.setStartTime(startHourPlus1);
                }

                times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);

                this.schedule.add(newSchedule);
                } catch (IllegalArgumentException e) {
                    System.out.println("IllegalArgumentException exception when creating schedule object");}
            }

        }
        createScheduleMaxWindow3();
    }

    /** createScheduleMaxWindow3()
     * Iterates through treatments HashMap and finds the treatment
     * that has MaxWindow == 3. This means that this object is third priority
     * and can be set in its preferred start hour or a maximum of 2 hours after
     * that. Creates Schedule object and sets the treatmentID, startTime, timeSpent, task, and
     * animalList data members. Then sets the timeRemaining data member according to the time
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList
     * @throws IllegalArgumentException
     * @return void
     */
    public void createScheduleMaxWindow3() throws IllegalArgumentException{
        String[] searchStrings = { "Feeding - coyote", "Feeding - fox" }; // the only two tasks that require a prepTime
        // iterate through treatments for maxWindow = 3...
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            if (currentTask.getMaxWindow() == 3) {
                boolean alreadyExists = false;
                // iterate through schedule to see if the task already exists
                for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { 
                    if (times[schedule.get(scheduleIndex).getStartTime()][1] >= currentTask.getDuration()) {
                        for (String search : searchStrings) {
                            if (currentTask.getDescription().equals(search)
                                    && schedule.get(scheduleIndex).getTask().equals(search)) {
                                // add the animal name to schedule object...
                                schedule.get(scheduleIndex).setAnimalList(currentAnimal.getNickname());
                                schedule.get(scheduleIndex).setTreatmentIndices(treatmentKey);

                                // edit timeSpent and timeRemaining
                                schedule.get(scheduleIndex).setTimeSpent(
                                        schedule.get(scheduleIndex).getTimeSpent() + currentTask.getDuration());
                                times[schedule.get(scheduleIndex).getStartTime()][1] -= currentTask.getDuration();

                                schedule.get(scheduleIndex).setTimeRemaining(times[schedule.get(scheduleIndex).getStartTime()][1]);
                                alreadyExists = true;
                            }

                        }
                    }
                }
                if (!alreadyExists) {
                    try {
                    Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                            currentTreatment.getStartHour(), currentTask.getTotalTime());

                    int startHourPlus1 = currentTreatment.getStartHour() + 1;
                    if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                    int startHourPlus2 = currentTreatment.getStartHour() + 2;
                    if (startHourPlus2 > 23) { startHourPlus2 -= 24; }

                    // if there is no equal or additional time remaining in the original time
                    // slot...
                    if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                        newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                        // if there is no equal or additional time remaining in the new time slot...
                        if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus2);
                        } // set the startHour to + 2 of original
                    }

                    times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                    newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);

                    this.schedule.add(newSchedule);
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException exception when creating schedule object");}
                }
            }
        }
        createScheduleMaxWindow4();
    }

    /** createScheduleMaxWindow4()
     * Iterates through treatments HashMap and finds the treatment
     * that has MaxWindow == 4. This means that this object is fourth priority
     * and can be set in its preferred start hour or a maximum of 3 hours after
     * that. Creates Schedule object and sets the treatmentID, startTime, timeSpent, task, and
     * animalList data members. Then sets the timeRemaining data member according to the time
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList
     * @throws IllegalArgumentException
     * @return void
     */
    public void createScheduleMaxWindow4() throws IllegalArgumentException {
        // iterate through treatments for maxWindow = 4...
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            if (currentTask.getMaxWindow() == 4) {
                try {
                Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                        currentTreatment.getStartHour(), currentTask.getTotalTime());

                int startHourPlus1 = currentTreatment.getStartHour() + 1;
                if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                int startHourPlus2 = currentTreatment.getStartHour() + 2;
                if (startHourPlus2 > 23) { startHourPlus2 -= 24; }
                int startHourPlus3 = currentTreatment.getStartHour() + 3;
                if (startHourPlus3 > 23) { startHourPlus3 -= 24; }

                // if there is no equal or additional time remaining in the original time slot...
                if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                    newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                    // if there is no equal or additional time remaining in the new time slot...
                    if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                        newSchedule.setStartTime(startHourPlus2); // set the startHour to + 2 of original
                        // if there is no equal or additional time remaining in the new time slot...
                        if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus3);
                        } // set the startHour to + 3 of original
                    }
                }

                times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);

                this.schedule.add(newSchedule);
                } catch (IllegalArgumentException e) {
                    System.out.println("IllegalArgumentException exception when creating schedule object");}
            }
        }
        createScheduleMaxWindow5();
    }

    /** createScheduleMaxWindow5()
     * Iterates through treatments HashMap and finds the treatment
     * that has MaxWindow == 5. This means that this object is fifth priority
     * and can be set in its preferred start hour or a maximum of 4 hours after
     * that. Creates Schedule object and sets the treatmentID, startTime, timeSpent, task, and
     * animalList data members. Then sets the timeRemaining data member according to the time
     * remaining in the times 2D array. Finally, adds this object to schedule ArrayList
     * @throws IllegalArgumentException
     * @return void
     */
    public void createScheduleMaxWindow5() throws IllegalArgumentException {
        // iterate through treatments for maxWindow = 5...
        for (int treatmentKey : allTreatments.keySet()) {
            Task currentTask = allTreatments.get(treatmentKey).getTask();
            Animal currentAnimal = allTreatments.get(treatmentKey).getAnimal();
            Treatment currentTreatment = allTreatments.get(treatmentKey);

            if (currentTask.getMaxWindow() == 5) {
                try {
                Schedule newSchedule = new Schedule(treatmentKey, currentTask.getDescription(), currentAnimal.getNickname(),
                        currentTreatment.getStartHour(), currentTask.getTotalTime());

                int startHourPlus1 = currentTreatment.getStartHour() + 1;
                if (startHourPlus1 > 23) { startHourPlus1 -= 24; }
                int startHourPlus2 = currentTreatment.getStartHour() + 2;
                if (startHourPlus2 > 23) { startHourPlus2 -= 24; }
                int startHourPlus3 = currentTreatment.getStartHour() + 3;
                if (startHourPlus3 > 23) { startHourPlus3 -= 24; }
                int startHourPlus4 = currentTreatment.getStartHour() + 4;
                if (startHourPlus4 > 23) { startHourPlus4 -= 24; }

                // if there is no equal or additional time remaining in the original time  slot...
                if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                    newSchedule.setStartTime(startHourPlus1); // set the startHour to + 1 of original
                    // if there is no equal or additional time remaining in the new time slot...
                    if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                        newSchedule.setStartTime(startHourPlus2); // set the startHour to + 2 of original
                        // if there is no equal or additional time remaining in the new time slot...
                        if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                            newSchedule.setStartTime(startHourPlus3); // set the startHour to + 3 of original
                            // if there is no equal or additional time remaining in the new time slot...
                            if (!(times[newSchedule.getStartTime()][1] >= currentTask.getTotalTime())) {
                                newSchedule.setStartTime(startHourPlus4);
                            } // set the startHour to + 3 of original
                        }
                    }
                }
                times[newSchedule.getStartTime()][1] -= newSchedule.getTimeSpent();
                newSchedule.setTimeRemaining(times[newSchedule.getStartTime()][1]);

                this.schedule.add(newSchedule);
                } catch (IllegalArgumentException e) {
                    System.out.println("IllegalArgumentException exception when creating schedule object");}
            }
        }
    }

    /** combineSimilarTasks()
     * iterates over the times array and checks all of the scheduled tasks per hour,
     * storing only the first occurrence of each task. A temporary HashMap stores
     * these tasks Strings as keys with temporary "ID" values. These ID values are used as the
     * keys for a second temporary HashMap that stores the Schedule objects.
     * 1st nested for-loop: iterates over the schedule ArrayList. If the task is
     * new, it is added to the two HashMaps. If the task re-occurs, the pre-existing task
     * and Schedule object are found in the HashMaps and updated accordingly. Finally, the repeated task is
     * removed from the schedule ArrayList. 2nd nested for-loop: iterates over the HashMap containing the non-duplicate
     * Schedule objects of that hour. Re-calculates the remaining time after each task is performed.
     * @return void
     */
    public void combineSimilarTasks() {
        for (int hour = 0; hour < times.length; hour++) { // iterate through the times
            HashMap<String, Integer> mapForString = new HashMap<String, Integer>();
            HashMap<Integer, Schedule> mapForSchedule = new HashMap<Integer, Schedule>();
            int count = 0;
            times[hour][1] = 60;

            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { 
                // iterate through the schedule arrayList
                if (schedule.get(scheduleIndex).getStartTime() == times[hour][0]) {
                    String key = schedule.get(scheduleIndex).getTask();
                    if (mapForString.containsKey(key)) { // if the task exists in the list
                        Schedule tempSchedule = mapForSchedule.get(mapForString.get(key));
                        tempSchedule.setTreatmentIndices(schedule.get(scheduleIndex).getTreatmentIndices().get(0));
                        tempSchedule.setAnimalList(schedule.get(scheduleIndex).getAnimalList().get(0));
                        
                        int time = tempSchedule.getTimeSpent() + schedule.get(scheduleIndex).getTimeSpent();
                        tempSchedule.setTimeSpent(time);
                        // since it is a duplicate task, remove it from the schedule ArrayList and
                        // decrement the index
                        schedule.remove(scheduleIndex);
                        scheduleIndex--;
                    } else {
                        mapForString.put(key, count);
                        mapForSchedule.put(count, schedule.get(scheduleIndex));
                        count++;
                    }
                }
            }
            for (int mapIndex = 0; mapIndex < mapForSchedule.size(); mapIndex++) { 
                // iterate through the HashMap containing the schedules
                // update the remaining time in the times array along with the timeRemaining
                // data member of the object
                times[hour][1] -= mapForSchedule.get(mapIndex).getTimeSpent();
                mapForSchedule.get(mapIndex).setTimeRemaining(times[hour][1]);
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
     * the time remaining as now there are two volunteers for that hour. To calls on
     * a GUI class to inform the user that there is a need and will not continue the 
     * program until the user confirms that they have gotten the backup volunteer
     * @return void
     */
    public void addBackupVolunteer() {
        for (int hour = 0; hour < times.length; hour++) { // iterate through the times
            int count = 0;
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { 
                // iterate through the schedule arrayList
                if ((times[hour][1] < 0) || count != 0) { // if the timeRemaining for any time slot is negative...
                    if (times[hour][0] == schedule.get(scheduleIndex).getStartTime()) { 
                        // find the time slots in the schedule arrayList
                        schedule.get(scheduleIndex).setBackupRequired(true); // make those objects have backup volunteer
                        if (count == 0) { // get the original time remaining (which is not negative)
                            times[hour][1] = 120;
                            // double the TimeRemaining attribute for initial remaining time
                            times[hour][1] -= schedule.get(scheduleIndex).getTimeRemaining(); 
                            count++;
                        } else {
                            times[hour][1] -= schedule.get(scheduleIndex).getTimeSpent();
                        }
                        schedule.get(scheduleIndex).setTimeRemaining(times[hour][1]);
                    }
                }
            }
        }

        // // THIS IS TEMPORARY COMMENT TO SEE THE OUTPUT, DO NOT DELETE THANK YOU!
        // for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) {
        //     if (schedule.get(scheduleIndex).getTask() != null){
        //     System.out.println("Task: " + schedule.get(scheduleIndex).getTask() + "\n Start Time: " 
        //     + schedule.get(scheduleIndex).getStartTime() + "\n Time spent:" 
        //     + schedule.get(scheduleIndex).getTimeSpent() + "\n Time remaining: " +
        //     schedule.get(scheduleIndex).getTimeRemaining() + "\n Backup: " +
        //     schedule.get(scheduleIndex).getBackupRequired()
        //     + "\n Quantity: " + schedule.get(scheduleIndex).getQuantity());
        //     System.out.println();
        //     }
        // }
    }

    /** printToText()
     * This method is called after the schedule is entirely created. After making
     * sure that it is a valid schedule, it writes the schedule to an output text 
     * file. The GUI retrieves this txt file and displays it on screen.
     * First it prints the current date, then iterates through the times
     * array and prints the time and its corresponding tasks under it. 
     * This is done by iterating through each element of the schedule ArrayList. 
     * If the schedule is not made, the method returns false, causing the program
     * to run once again after retrieving user input for changing the start hour
     * @throws IOException
     * @return   boolean
    */
    public boolean printToText() throws IOException {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);
        BufferedWriter outputStream;

        outputStream = new BufferedWriter(new FileWriter("output.txt"));
        outputStream.write("Schedule for " + date + ":\n\n");
        //A big string of all of the values previous to it all
        for (int i = 0; i < times.length; i++) { // iterate through the times
            String textForHour = String.valueOf(times[i][0]) + ":00";
            int count = 0;
            for (int scheduleIndex = 0; scheduleIndex < schedule.size(); scheduleIndex++) { // iterate through the schedule arrayList
                if (schedule.get(scheduleIndex).getTimeRemaining() < 0) { 
                    
                    int maxSize = schedule.get(scheduleIndex).getTreatmentIndices().size()-1;
                    int treatmentToChange = schedule.get(scheduleIndex).getTreatmentIndices().get(maxSize);
                    
                    ErrorGUI error = new ErrorGUI("negative", schedule.get(scheduleIndex).getStartTime(), schedule.get(scheduleIndex).getTask(), allTreatments.get(treatmentToChange).getAnimal().getNickname()); 
                    while(error.getStates()){ System.out.println(error.getStates()); }
                    int newHour = error.getSelectedHour();
                        try {
                            Connection bismallah = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password"); 
                            String changing_statement = "UPDATE TREATMENTS SET StartHour = ? WHERE TreatmentID = ?"; 
                            PreparedStatement ps = bismallah.prepareStatement(changing_statement);
                            ps.setInt(1,newHour);
                            ps.setInt(2,treatmentToChange); 
                            int x = ps.executeUpdate(); 
                            ps.close();
                            bismallah.close(); 
                        }
                        catch(SQLException e) { e.printStackTrace(); }
                    return false;
                }
                else if (schedule.get(scheduleIndex).getBackupRequired() && (schedule.get(scheduleIndex).getTimeRemaining() + schedule.get(scheduleIndex).getTimeSpent()) > 120) {
                    int maxSize = schedule.get(scheduleIndex).getTreatmentIndices().size()-1;
                    int treatmentToChange = schedule.get(scheduleIndex).getTreatmentIndices().get(maxSize);
                    ErrorGUI error = new ErrorGUI("over", schedule.get(scheduleIndex).getStartTime(), schedule.get(scheduleIndex).getTask(), allTreatments.get(treatmentToChange).getAnimal().getNickname()); 
                    while(error.getStates()){ System.out.println(error.getStates()) ; }
                    int newHour = error.getSelectedHour();
                    Connection bismallah = null;
                        try{
                            bismallah = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "oop", "password"); 
                            String changing_statement = "UPDATE TREATMENTS SET StartHour = ? WHERE TreatmentID = ?"; 
                            PreparedStatement ps = bismallah.prepareStatement(changing_statement);
                            ps.setInt(1,newHour);
                            ps.setInt(2,treatmentToChange); 
                            int x = ps.executeUpdate(); 
                            ps.close();
                            bismallah.close(); 
                        }
                        catch(SQLException e) { e.printStackTrace(); }
                    return false; 
                }
                
                if (times[i][0] == schedule.get(scheduleIndex).getStartTime()) { // find the time slots in the schedule arrayList
                    if (count == 0 ) { // if it is first task under this time slot, check for backup volunteer
                        if (schedule.get(scheduleIndex).getBackupRequired() == true) { 
                            VolunteerGUI volunteer = new VolunteerGUI(Integer.toString(times[i][0]));
                            while(volunteer.getState()){
                                System.out.println(volunteer.getState()); 
                            }
                            textForHour += " [+ backup volunteer] \n"; 
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

        try { outputStream.close(); } 
        catch (IOException e) { 
            System.out.println("I/O exception when trying to close file");
            e.printStackTrace(); }

        DisplaySch display = new DisplaySch("start"); 
        return true;
    }

    /** main()
     * This is the main method called to create the daily schedule. It runs in a 
     * while loop until the schedule is made properly
     * @param args the String array of arguments passed from the user (unused)
     * @throws IOException
     * @throws SpeciesNotFoundException
     * @throws IllegalArgumentException
     * @return void
     */
    public static void main(String[] args) throws IOException, SpeciesNotFoundException, IllegalArgumentException {
        ScheduleBuilder schedule = new ScheduleBuilder();
        boolean scheduleWorks = schedule.createSchedule();
        
        while (!scheduleWorks){
            schedule.allTreatments.clear();
            schedule = new ScheduleBuilder();
            scheduleWorks = schedule.createSchedule();
        }
    }
}