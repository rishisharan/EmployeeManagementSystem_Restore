package com.gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import com.controller.MainController;

public class MainDashBoardGUI {

	JFrame mainDashBoardFrame;
	JMenu menu;  
    JMenuItem register, admin;
    JPanel employeePanel;
    JLabel userNameLabel,passwordLabel,banner;
    JTextField userNameTextBox;
    JPasswordField passwordTextBox;
    JButton clockInClockOutButton;
	
	Connection con;
    Statement stmt;
    PreparedStatement preStatement;
    MainController mainController;
    
	public MainDashBoardGUI() 
	{
		// TODO Auto-generated constructor stub

		 mainDashBoardFrame=new javax.swing.JFrame("Employee Management System");
		 employeePanel=new javax.swing.JPanel();
		 mainDashBoardFrame.add(employeePanel);
		 employeePanel.setLayout(null);
		 mainDashBoardFrame.setSize(550,500);
		 mainDashBoardFrame.show();
		 mainDashBoardFrame.setResizable(false);
			
			

  
         mainDashBoardFrame.add(employeePanel);
	     
         JMenuBar mb=new JMenuBar();  
         menu=new JMenu("Menu");  
         admin=new JMenuItem("Admin");  
         register=new JMenuItem("Register");  
         menu.add(admin); menu.add(register);  
         mb.add(menu);  
         mainDashBoardFrame.setJMenuBar(mb);  
 
           
         
         banner=new javax.swing.JLabel("Employee Login");
         banner.setFont(new Font("Dialog", Font.ITALIC, 24));
         banner.setBounds(150, 25, 200, 30);
         employeePanel.add(banner);
        
         

 		userNameLabel=new javax.swing.JLabel("User name");
 		userNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
 		userNameLabel.setBounds(100,80,150,40);
 		employeePanel.add(userNameLabel);
 		userNameTextBox=new javax.swing.JTextField();
 		userNameTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
 		userNameTextBox.setBounds(250,80,180,30);
 		employeePanel.add(userNameTextBox);
 	     
 	
 		passwordLabel=new javax.swing.JLabel("Password");
 		passwordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
 		passwordLabel.setBounds(100,120,150,40);
 		employeePanel.add(passwordLabel);
 		passwordTextBox=new javax.swing.JPasswordField();
 		passwordTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
 		passwordTextBox.setBounds(250,120,180,30);
 		employeePanel.add(passwordTextBox);
 	      
 		clockInClockOutButton = new JButton("Clock In");
 		clockInClockOutButton.setFont(new Font("Dialog", Font.BOLD, 16));
 		clockInClockOutButton.setBounds(100,210,100,30);
 		employeePanel.add(clockInClockOutButton);
 	    
 		mainDashBoardFrame.setVisible(true);
         admin.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent a) {
                   // calling method resetFields()
                   new AdminLoginGUI();
             }
         });
         register.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent a) {
                   // calling method resetFields()
                   new RegisterGUI();
             }
         });
         
         
         clockInClockOutButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent an) {
                 
            
            	 String password = String.valueOf(passwordTextBox.getPassword());
            	 try {
					mainController=new MainController();
					if(mainController.checkUser(userNameTextBox.getText(),password)==true){
					JOptionPane.showMessageDialog(null, "Clocked In/Out");
					}
					else{
						JOptionPane.showMessageDialog(null, "Invalid User name and Password");	
					}
            	 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
       });
         
	}
	
	public static void main(String args[]){
	 new MainDashBoardGUI();	
	}

}
