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
		 mainDashBoardFrame= new JFrame("Employee Management System");  
		 employeePanel=new javax.swing.JPanel();
	     employeePanel.setLayout(null);
	     employeePanel.setVisible(true);
	     employeePanel.setSize(600,600);
	   
         mainDashBoardFrame.add(employeePanel);
	     
         JMenuBar mb=new JMenuBar();  
         menu=new JMenu("Menu");  
         admin=new JMenuItem("Admin");  
         register=new JMenuItem("Register");  
         menu.add(admin); menu.add(register);  
         mb.add(menu);  
         mainDashBoardFrame.setJMenuBar(mb);  
         mainDashBoardFrame.setSize(970,700);  
         mainDashBoardFrame.setLayout(null);  
         mainDashBoardFrame.setVisible(true);  
         
         banner=new javax.swing.JLabel("Employee Login");
         banner.setFont(new Font("Dialog", Font.ITALIC, 24));
         banner.setBounds(400, 25, 200, 30);
         employeePanel.add(banner);
        
         

 		userNameLabel=new javax.swing.JLabel("User name");
 		userNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
 		userNameLabel.setBounds(100,80,150,40);
 		employeePanel.add(userNameLabel);
 		userNameTextBox=new javax.swing.JTextField();
 		userNameTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
 		userNameTextBox.setBounds(350,80,180,30);
 		employeePanel.add(userNameTextBox);
 	     
 	
 		passwordLabel=new javax.swing.JLabel("Password");
 		passwordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
 		passwordLabel.setBounds(100,120,150,40);
 		employeePanel.add(passwordLabel);
 		passwordTextBox=new javax.swing.JPasswordField();
 		passwordTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
 		passwordTextBox.setBounds(350,140,180,30);
 		employeePanel.add(passwordTextBox);
 	      
 		clockInClockOutButton = new JButton("Sign In");
 		clockInClockOutButton.setFont(new Font("Dialog", Font.BOLD, 16));
 		clockInClockOutButton.setBounds(350,250,100,30);
 		employeePanel.add(clockInClockOutButton);
 	    
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
                 // calling method resetFields()
            	// getEmployeeInformation();
            	 String password = String.valueOf(passwordTextBox.getPassword());
            	 try {
					mainController=new MainController(userNameTextBox.getText(),password);
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
