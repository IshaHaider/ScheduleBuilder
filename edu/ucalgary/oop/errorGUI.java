package edu.ucalgary.oop; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
//modify the start hour of one
//changing the database 

public class errorGUI {
    public int startingHour;
    public String reason;
    public String task;
    public String user_change;
    public boolean states;
    public String given_animal; 

    public errorGUI(String cases, int StartingHour, String given_task, String animal){
        this.startingHour = StartingHour;
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


    public void negativeFixingGui() {
        JFrame frame = new JFrame("Fixing Schedule");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 

        JPanel panel = new JPanel(new BorderLayout(10, 10)); 

        String display = "There are not enough workers available for this task: " + this.task + "with pet: " + this.given_animal;
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
    

    public void overhoursFixingGui(){
        JFrame frame = new JFrame("Fixing Schedule");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        
        JPanel panel = new JPanel(new BorderLayout(10, 10)); 

        String display = "There are too many tasks in this delgated hour: " + this.task + "with pet: " + this.given_animal;
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

    public int getSelectedHour(){return Integer.parseInt(this.user_change);}
    public boolean getStates(){return this.states;}

}


