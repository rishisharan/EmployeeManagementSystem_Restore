package com.gui;

import java.sql.*;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class RegisterGUI {

	
	JFrame registerFrame;
	Connection con;
    Statement stmt;
    PreparedStatement preStatement;
    JLabel firstNameLabel, lastNameLabel,genderLabel,phoneLabel,createPasswordLabel,confirmPasswordLabel;
    JTextField firstNameTextBox, lastNameTextBox,phoneTextBox,createPasswordTextBox,confirmPasswordTextBox;
    JButton registerButton, exitButton,updateButton,deleteButton,resetButton,refresh;
    JRadioButton male, female;
    ButtonGroup bg;
    JPanel registerPanel;
	public RegisterGUI() {
		// TODO Auto-generated constructor stub
	}

}
