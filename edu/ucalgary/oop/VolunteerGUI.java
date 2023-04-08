/** 
 * @author ENSF380 Group 20
 * VolunteerGUI is a java class that creates the GUI when a volunteer is needed 
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop; 

import javax.swing.*;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.*; 



public class VolunteerGUI implements ActionListener {
    public int hour;
    public boolean exists; 
    public String reason;
    public JFrame frame; 

    /** Constructor
     * Initializes the data members of the VolunteerGUI and calls on createGUI() 
     * @param  string  an instance of the treatment that needs a volunteer
     * @return
    */
    public VolunteerGUI(String string) {
        this.reason = string; 
        this.exists = true; 
        createGUI();
    }

    /** createGUI() 
     * creates the GUI that asks the user to confirm that they have gotten an additonal 
     * volunteer, it lets the user know which times and tasks are needed 
     * @return   void
    */
    public void createGUI(){
        frame = new JFrame("Volunteer Portal"); 
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(this); 
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("ALERT"); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("Need for a Backup Volunteer"); 
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String volunteerString = "You will be needing a volunteer for this hour: " + this.reason;
        JLabel reasoningLabel = new JLabel(volunteerString); 
        reasoningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel toTerminate = new JLabel("To terminate the entire program, press the X button"); 
        toTerminate.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel); 

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(messageLabel); 

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(reasoningLabel); 
 
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(confirmButton); 

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(toTerminate); 

        mainPanel.add(Box.createVerticalGlue()); 

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    /** actionPerformed() 
     * recognizes that the enter statement has occured and the frame is no longer visible
     * @return   void
    */
    public void actionPerformed(ActionEvent event){
        this.exists = false; 
        this.frame.setVisible(false); 
    }

    /**Getters
     * getter methods returning the stored objected requested 
     */
    public boolean getState(){
        return this.exists; 
    }
}