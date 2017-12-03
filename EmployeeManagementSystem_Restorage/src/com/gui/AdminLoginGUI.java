package com.gui;


import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;



public class AdminLoginGUI {

	
	JFrame adminLoginFrame;
	Connection con;
    Statement stmt;
    PreparedStatement preStatement;
    ResultSet res;
    JLabel userNameLabel,passwordNameLabel, banner;
    JTextField userNameTextBox;
    JPasswordField passwordTextBox;
    JButton loginButton;
    JPanel adminPanel;
    String userName,adminPassword;
	int count =0;
    public AdminLoginGUI() 
	{
		
    	adminLoginFrame=new javax.swing.JFrame("Admin Login");
		adminPanel=new javax.swing.JPanel();
		adminLoginFrame.add(adminPanel);
		adminPanel.setLayout(null);

		adminLoginFrame.setSize(550,450);
		adminLoginFrame.show();
		adminLoginFrame.setResizable(false);
		
	    
		banner=new javax.swing.JLabel("Admin Login");
		banner.setFont(new Font("Dialog", Font.ITALIC, 24));
		banner.setBounds(200,20,400,40);
		adminPanel.add(banner);
	    
		userNameLabel=new javax.swing.JLabel("User name");
		userNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		userNameLabel.setBounds(100,80,150,40);
		adminPanel.add(userNameLabel);
		userNameTextBox=new javax.swing.JTextField();
		userNameTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
		userNameTextBox.setBounds(250,80,180,30);
		adminPanel.add(userNameTextBox);
	      
		passwordNameLabel = new JLabel("Password"); 
		passwordNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		passwordNameLabel.setBounds(100, 140, 150, 40);
		adminPanel.add(passwordNameLabel);
		passwordTextBox = new JPasswordField(); 
		passwordTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
		passwordTextBox.setBounds(250, 140, 180, 30);
		adminPanel.add(passwordTextBox);
		
		

		// Defining Register Button
		loginButton = new JButton("Login");
		loginButton.setFont(new Font("Dialog", Font.BOLD, 16));
		loginButton.setBounds(100,200,100,30);
		adminPanel.add(loginButton);
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{

			getUserInformation();
			validation();
			try {
				registerUserData();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			}

		});
		


		

		
		adminLoginFrame.add(adminPanel);
		adminLoginFrame.setLayout(null);  
		adminLoginFrame.setVisible(true);
		
        

        
		
	}
	public void getUserInformation() {
		userName = userNameTextBox.getText();
		adminPassword = passwordTextBox.getText();
		
	}
	public boolean validation()
	{
		count=0;
		if(userName.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Id");
			return false;
		}
		else if(adminPassword.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter password");
			return false;
		}
		else 
		{
			count=1;
			return true;
		}
	}
	public void registerUserData() throws SQLException
	{
		try
		{
			if(count==1)
			{
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeems","root","system");
				stmt = con.createStatement();
				String adminLogin="select name,admin_password from admin where name='"+userName+"' and admin_password='"+adminPassword+"'";
		    	res = stmt.executeQuery(adminLogin);
		    	if(res.first()){
		    		new AdminDashBoardGUI();
		    		JOptionPane.showMessageDialog(null,"Logged in");	
		    	}else{
		    		JOptionPane.showMessageDialog(null,"Invalid Id and Password");	
		    	}
				
           	 }
		}
		catch (Exception e) 
		{
				System.out.println("Exception1 is " + e);
		}	
		finally{
			stmt.close();
			con.close();
	
		}
	}
		
	

}
