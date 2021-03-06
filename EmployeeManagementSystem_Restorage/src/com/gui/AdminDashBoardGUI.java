package com.gui;
import java.awt.*; 
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.database.Database;
public class AdminDashBoardGUI extends JFrame
{

		Connection con;
		Statement stmt;
		PreparedStatement preStatement,updatePreStmt;
		JButton exitButton,viewHistoryButton,deleteEmployeeButton;
	    JButton refresh;
		JPanel panel;
		JTable table;
		DefaultTableModel model;
		JScrollPane scrollpane;
		ViewHistoryGUI obj;
		
	    ResultSet rst,rstLast;
	    Object[][] data;
	    int serialNo; 
	    String SHOW = "Show";
	  
	    Object[] row = null;
	    Database db;
	    String employee_id;
		public AdminDashBoardGUI() 
		{
				// TODO Auto-generated constructor stub
		      super("Admin DashBoard");
		      setSize(770, 420);
		      setLayout(null);	      
		      // Defining Labels 
		      connect();
		     

		      
		      
		             
		
		      
		
		      
		      // Defining Exit Button
		      exitButton = new JButton("Exit"); 
		      exitButton.setBounds(25, 250, 80, 25);            
		
		    
		      // Defining Update Button
		      viewHistoryButton = new JButton("View History");
		      viewHistoryButton.setBounds(50, 50, 150, 30);
		      
		      // Defining Delete Button
		      deleteEmployeeButton = new JButton("Delete Employee");
		      deleteEmployeeButton.setBounds(50, 90, 150, 30);
		    
		
		     
		    
		      add(viewHistoryButton);
		  
		      // Defining Panel
		      panel = new JPanel();
		      panel.setLayout(new GridLayout());
		      panel.setBounds(250, 20, 480, 330);
		      panel.setBorder(BorderFactory.createDashedBorder(Color.blue));
		      add(panel);
		
		      // Defining Refresh Button
		      refresh = new JButton("Refresh Table");
		      refresh.setBounds(350, 350, 270, 15);
		      add(refresh);
		      add(deleteEmployeeButton);
				
		      //Defining Model for table
		      model = new DefaultTableModel();
		
		      //Adding object of DefaultTableModel into JTable
		      table = new JTable(model);
		
		      //Fixing Columns move
		      table.getTableHeader().setReorderingAllowed(false);
		
		      // Defining Column Names on model
		    
		      model.addColumn("ID");
		      model.addColumn("First name");
		      model.addColumn("Last name");
		      model.addColumn("Phone");
		      model.addColumn("Gender");
		     
		      // Enable Scrolling on table
		     scrollpane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                                     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		      panel.add(scrollpane);
		
		      //Displaying Frame in Center of the Screen
		      try {
				loadRecords();
		      } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		      }
		      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		      this.setLocation(dim.width/2-this.getSize().width/2,
		                       dim.height/2-this.getSize().height/2);
		      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      setResizable(false);
		      setVisible(true);
		      viewHistoryButton.addActionListener(new ActionListener(){
		             public void actionPerformed(ActionEvent a) {
		                  int temp_employee_id;
		                  try{
		                	  temp_employee_id=Integer.parseInt(employee_id);
		                      obj=new ViewHistoryGUI(temp_employee_id);
		              		
		                  }
		                  catch(NumberFormatException ex){
		                	 	JOptionPane.showMessageDialog(null,"Please Select an Employee.");
		                  }
		                 
		                  
		                  
		               }
		     });
		      
		      deleteEmployeeButton.addActionListener(new ActionListener(){
		             public void actionPerformed(ActionEvent a) {
		                  int temp_employee_id;
		                  try{
		                	  temp_employee_id=Integer.parseInt(employee_id);
		                      int response = JOptionPane.showConfirmDialog(null, "Do you want to Delete?", "Confirm",
		                    	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		                    	    if (response == JOptionPane.NO_OPTION) {
		                    	      System.out.println("No button clicked");
		                    	    } else if (response == JOptionPane.YES_OPTION) {
		                    	      System.out.println("Yes button clicked");
		                    	      try {
										deleteEmployeeFrom(temp_employee_id);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		                    	    } 
		                  }
		                  catch(NumberFormatException ex){
		                	 	JOptionPane.showMessageDialog(null,"Please Select an Employee.");
		                  }
		                 
		                  
		                  
		               }
		     });
		      
		      
		      refresh.addActionListener(new ActionListener(){
		             public void actionPerformed(ActionEvent a) {
		            	 try {
		            		 int rows = model.getRowCount();	
		            			for(int i = rows - 1; i >=0; i--)
		            			 {
		            				 model.removeRow(i); 
		            			 }
							loadRecords();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		               }
		     });
		      
		      table.addMouseListener(new MouseListener(){
                  public void mouseClicked(MouseEvent arg0){ 
                        //Getting Selected Row No
                  int r = table.getSelectedRow();
                  if(r>=0){ 
                        viewHistoryButton.setEnabled(true);

                     
                        employee_id=(String) table.getModel().getValueAt(r,0);
                        System.out.println("Employee id is selected "+employee_id);
                  	}
                  }

//                @Override
                  public void mouseReleased(MouseEvent arg0) {}
//                @Override
                  public void mousePressed(MouseEvent arg0) {}
//                @Override 
                  public void mouseExited(MouseEvent arg0) {}
//                @Override 
                  public void mouseEntered(MouseEvent arg0) {}
            });
		}
		 public void loadRecords() throws SQLException{
			 
	    	 String getUserDetails="select * from employee";
	     	  rst = stmt.executeQuery(getUserDetails);
	     	
	         while(rst.next()){ 
	               String string =rst.getInt("id")+","+rst.getString("lastname")+","+rst.getString("firstname")+","+rst.getString("phone")+","+rst.getString("gender");
	               row = string.split(",");

//	               // Adding records in table model 
	               model.addRow(row);
	         }
	    }
		 public void connect(){
		        try{
		              Class.forName("com.mysql.jdbc.Driver");
		              con =DriverManager.getConnection(
		                              "jdbc:mysql://localhost:3306/employeems","root","system");
		              stmt = con.createStatement();
		        }catch(Exception e){
		              System.out.print(e.getMessage());
		        }
		 }
		 
		  public void deleteEmployeeFrom(int employee_id) throws SQLException{
			  String deleteEmployeeId="DELETE  employee,daily_attendance from employee inner join  daily_attendance on employee.id=daily_attendance.id where employee.id='"+employee_id+"'";
			  stmt.executeUpdate(deleteEmployeeId);
		  }

}