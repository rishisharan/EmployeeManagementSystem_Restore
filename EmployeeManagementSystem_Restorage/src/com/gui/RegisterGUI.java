package com.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class RegisterGUI {

	
	JFrame registerFrame;
	Connection con;
    Statement stmt;
    PreparedStatement preStatement;
    JLabel registerLabel,firstNameLabel, lastNameLabel,genderLabel,phoneLabel,createPasswordLabel,confirmPasswordLabel,maleLabel,femaleLabel;
    JTextField firstNameTextBox, lastNameTextBox,phoneTextBox;
    JPasswordField createPasswordTextBox,confirmPasswordTextBox;
    JButton registerButton, clearButton,exitButton;
    JRadioButton male, female;
    ButtonGroup bg;
    JPanel registerPanel;
    String firstName,lastName,gender,createPassword,confirmPassword,phone;
	int count =0;
    public RegisterGUI() 
	{
		
		registerFrame=new javax.swing.JFrame("Registration Form");
		registerPanel=new javax.swing.JPanel();
		registerFrame.add(registerPanel);
		registerPanel.setLayout(null);
		registerFrame.setSize(970,700);
		registerFrame.show();
		
	    
		registerLabel=new javax.swing.JLabel("Registration Form");
		registerLabel.setFont(new Font("Dialog", Font.ITALIC, 24));
		registerLabel.setBounds(300,20,400,40);
		registerPanel.add(registerLabel);
	    registerPanel.add(registerLabel);

		firstNameLabel=new javax.swing.JLabel("First name");
		firstNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		firstNameLabel.setBounds(100,80,150,40);
		registerPanel.add(firstNameLabel);
		firstNameTextBox=new javax.swing.JTextField();
		firstNameTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
		firstNameTextBox.setBounds(350,80,180,30);
		registerPanel.add(firstNameTextBox);
	      
		lastNameLabel = new JLabel("Last name"); 
		lastNameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		lastNameLabel.setBounds(100, 140, 150, 40);
	    registerPanel.add(lastNameLabel);
		lastNameTextBox = new JTextField(); 
		lastNameTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
		lastNameTextBox.setBounds(350, 140, 180, 30);
		registerPanel.add(lastNameTextBox);
		
		createPasswordLabel = new JLabel("Create Password"); 
		createPasswordLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		createPasswordLabel.setBounds(100, 190, 150, 50);
	    registerPanel.add(createPasswordLabel);
	    createPasswordTextBox = new JPasswordField(); 
	    createPasswordTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
	    createPasswordTextBox.setBounds(350, 200, 180, 30);
	    registerPanel.add(createPasswordTextBox);
		
		confirmPasswordLabel = new JLabel("Confirm Password"); 
		confirmPasswordLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		confirmPasswordLabel.setBounds(100, 250, 150, 40);
	    registerPanel.add(confirmPasswordLabel);
	    confirmPasswordTextBox = new JPasswordField(); 
	    confirmPasswordTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
	    confirmPasswordTextBox.setBounds(350, 260, 180, 30);
	    registerPanel.add(confirmPasswordTextBox);
			    

		phoneLabel = new JLabel("Phone"); 
		phoneLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		phoneLabel.setBounds(100, 320, 150, 40);
	    registerPanel.add(phoneLabel);
	    phoneTextBox = new JTextField(); 
	    phoneTextBox.setFont(new Font("Dialog", Font.BOLD, 16));
	    phoneTextBox.setBounds(350, 320, 180, 30);
	    registerPanel.add(phoneTextBox);
		
		
		genderLabel = new JLabel("Gender"); 
		genderLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		genderLabel.setBounds(100, 380, 150, 40);
		registerPanel.add(genderLabel);
		maleLabel = new JLabel("Male"); 
		maleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		maleLabel.setBounds(350, 380, 150, 40);
		registerPanel.add(maleLabel);

		male=new javax.swing.JRadioButton();
		male.setFont(new Font("Dialog", Font.BOLD, 16));
		male.setBounds(400,390,20,30);
		registerPanel.add(male);

		femaleLabel = new JLabel("Female"); 
		femaleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		femaleLabel.setBounds(425, 380, 150, 40);
		registerPanel.add(femaleLabel);
		female=new javax.swing.JRadioButton();
		female.setFont(new Font("Dialog", Font.BOLD, 16));
		female.setBounds(495,390,20,30);
		
		registerPanel.add(female);

		ButtonGroup jb = new ButtonGroup();
		jb.add(male);
		jb.add(female);

		// Defining Register Button
        registerButton = new JButton("Register");
		registerButton.setFont(new Font("Dialog", Font.BOLD, 16));
		registerButton.setBounds(100,450,100,30);
		registerPanel.add(registerButton);
		registerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{

			getUserInformation();
			validation();
			registerUserData();

			}

		});
		male.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			gender = "M";
			}
			});
		female.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			gender = "F";
			}
		});


		

		// Defining Register Button
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Dialog", Font.BOLD, 16));
        clearButton.setBounds(250,450,100,30);
		registerPanel.add(clearButton);

		// Defining Register Button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Dialog", Font.BOLD, 16));
        exitButton.setBounds(400,450,100,30);
		registerPanel.add(exitButton);
        registerFrame.add(registerPanel);
		registerFrame.setSize(700,700);  
		registerFrame.setLayout(null);  
		registerFrame.setVisible(true);
		
        

        
		
	}
	public void getUserInformation() {
		firstName = firstNameTextBox.getText();
		lastName = lastNameTextBox.getText();
		createPassword = createPasswordTextBox.getText();
		confirmPassword = confirmPasswordTextBox.getText();
		phone=phoneTextBox.getText();
		
	}
	public void validation()
	{
		count=0;
		if(firstName.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Firstname");
		}
		else if(lastName.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Lastame");
		}
		else if(createPassword.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Create password");
		}
		else if(confirmPassword.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Confirm password");
		}
		else if(phone.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Enter Mobile Number");
		}
		else if(gender.equals(""))
		{
			JOptionPane.showMessageDialog(null,"Select Gender");
		}
		else if(phone.length()<10||phone.length()>10){
			JOptionPane.showMessageDialog(null,"Phone number should be of length 10");
		}
		else 
		{
			if(createPassword.equals(confirmPassword))
			{
				System.out.println("Password matched, all validations successfull");
				count=1;
			}
			else{
				JOptionPane.showMessageDialog(null,"Password mismatch");	
			}
		}
	}
	public void registerUserData()
	{
		try
		{
			if(count==1)
			{
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeems","root","system");
				stmt = con.createStatement();
				stmt.execute("INSERT INTO employee (firstname,lastname,password,phone,gender) VALUES('"+firstName+"','"+lastName+"','"+createPassword+"','"+phone+"','"+gender+"')");
				stmt.close();
				con.close();
				JOptionPane.showMessageDialog(null,"Registered Successfully");
			}

		}
		catch (Exception e) 
		{
				System.out.println("Exception1 is " + e);
		}	
	}
		
	

}
