package edu.ucalgary.oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.io.*;
import java.util.*; 

public class displaysch {
    String total_display;

    public displaysch(String string) throws IOException {
        JFrame frame = new JFrame("Schedule for the day");
        JLabel title; 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel sch = new JPanel(); 
        sch.setLayout(new BoxLayout(sch, BoxLayout.Y_AXIS));

        File file = new File("output.txt"); 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            st = br.readLine();
            title = new JLabel(st);
            title.setBackground(Color.RED); 
            title.setOpaque(true);  // make sure to set opaque to true to see the background color
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            sch.add(Box.createRigidArea(new Dimension(0, 10)));
            sch.add(title);
            
            while((st = br.readLine()) != null){
                title = new JLabel(st); 
                title.setAlignmentX(Component.CENTER_ALIGNMENT);
                sch.add(Box.createRigidArea(new Dimension(0, 10)));
                sch.add(title); 
            }
        }

        JScrollPane scrollPane = new JScrollPane(sch);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(scrollPane);
        frame.setVisible(true);
    }
}

