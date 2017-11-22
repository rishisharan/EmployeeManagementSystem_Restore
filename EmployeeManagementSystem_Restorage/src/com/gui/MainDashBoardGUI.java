package com.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainDashBoardGUI {

	JFrame mainDashBoardFrame;
	JMenu menu;  
    JMenuItem register, admin;
	public MainDashBoardGUI() 
	{
		// TODO Auto-generated constructor stub
		 JFrame f= new JFrame("Employee Management System");  
         JMenuBar mb=new JMenuBar();  
         menu=new JMenu("Menu");  
         admin=new JMenuItem("Admin");  
         register=new JMenuItem("Register");  
         menu.add(admin); menu.add(register);  
         mb.add(menu);  
         f.setJMenuBar(mb);  
         f.setSize(400,400);  
         f.setLayout(null);  
         f.setVisible(true);  
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
         
         
	}
	
	public static void main(String args[]){
	 new MainDashBoardGUI();	
	}

}
