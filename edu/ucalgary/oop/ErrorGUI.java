/** 
 * @author ENSF380 Group 20: Zahwa Fatima, Saba Yarandi, Nessma Mohdy, Isha Haider
 * ErrorGUI is a java class that creates the GUI to get a userinput that changes the hour 
 * that is causing the schedule issue
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
 

public class ErrorGUI {
    public int startingHour;
    public String reason;
    public String task;
    public String user_change;
    public boolean states;
    public String given_animal; 

    /** Default Constructor
     * @return  
    */
    public ErrorGUI() {}

    /** Constructor
     * Initializes the data members of the ErrorGUI and calling on the respective GUI
     * that needs to be created 
     * @param  cases  the reason for the error
     * @param  startingHour  the starting hour for the error
     * @param  given_task  the task making the error
     * @param  animal  the animal causing the error
     * @return
    */
    public ErrorGUI(String cases, int startingHour, String given_task, String animal){
        this.startingHour = startingHour;
        this.reason = cases;
        this.task = given_task;
        this.states = true;
        this.given_animal = animal;
        if(this.reason == "negative"){
            negativeFixingGui();
        }
        if(this.reason == "over"){
            overhoursFixingGui();
        }
    }

    /** negativeFixingGui() 
     * when there is negative time for an instance in the schedule we create a GUI 
     * that displays the reasons for the inability to create the schedule and gets a user
     * input for the new hour needed for that task
     * @return   void
    */
    public void negativeFixingGui() {
        JFrame frame = new JFrame("Fixing Schedule");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 

        JPanel panel = new JPanel(new BorderLayout(10, 10)); 

        String display = "There are not enough workers available for the task: '" + this.task + "\n for: " + this.given_animal + 
        "'\nat the hour: " + this.startingHour;
        JLabel titleLabel = new JLabel(display);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 

        JLabel inputLabel = new JLabel("Enter a new starting time in the slot:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14)); 
    
        JTextField inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14)); 
    
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(inputLabel, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
    
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 14)); 
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user_change = inputField.getText();
                states = false; //is this setting the entire thing or?
                frame.dispose();
            }
        });

        panel.add(submitButton, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    /** overhoursFixingGui() 
     * when there are too many tasks in the delgated instance in the schedule we create a GUI 
     * that displays the reasons for the inability to create the schedule and gets a user
     * input for the new hour needed for that task
     * @return   void
    */
    public void overhoursFixingGui(){
        JFrame frame = new JFrame("Fixing Schedule");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        
        JPanel panel = new JPanel(new BorderLayout(10, 10)); 

        String display = "There are too many tasks in this delegated hour: " + this.startingHour + ". Please readjust the task: '" +
        this.task +  " for: " + this.given_animal + "'";
        JLabel titleLabel = new JLabel(display);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 

        JLabel inputLabel = new JLabel("Enter a new starting time slot to fix the problem:");
        //how to change this so the input has to right...
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14)); 
    
        JTextField inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14)); 
    
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(inputLabel, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
    
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 14)); 
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                states = false; //is this setting the entire thing or?
                user_change = inputField.getText();
                frame.dispose();
            }
        });
    
        panel.add(submitButton, BorderLayout.SOUTH);
    
        frame.add(panel);

        frame.setVisible(true);
    }

    /**Getters
     * getter methods returning the stored objected requested 
     */
    public int getSelectedHour(){return Integer.parseInt(this.user_change);}
    public boolean getStates(){return this.states;}

}


