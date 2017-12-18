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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import com.database.Database;
import com.database.GeneratePDFReport;
import com.database.SQLiteConnection;
import com.itextpdf.*;
public class AdminDashBoardGUI extends JFrame
{

		Connection con;
		Statement stmt;
		PreparedStatement preStatement,updatePreStmt;
		JButton exitButton,viewHistoryButton,deleteEmployeeButton,generatePDFButton;
	    JButton refresh;
		JPanel panel;
		JTable table;
		DefaultTableModel model;
		JScrollPane scrollpane;
		ViewHistoryGUI obj;
		
	    ResultSet rst,rstLast,rt;
	    Object[][] data;
	    int serialNo; 
	    String SHOW = "Show";
	  
	    Object[] row = null;
	    Database db;
	    String employee_id;
	    GeneratePDFReport pdf;
	    ArrayList<String> timestampsList = new ArrayList<String>();
		public AdminDashBoardGUI() throws SQLException 
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

		      // Defining Delete Button
		      generatePDFButton = new JButton("Generate PDF");
		      generatePDFButton.setBounds(50, 130, 150, 30);
		      generatePDFButton.setEnabled(false);
	    
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
			  add(generatePDFButton);
			  
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
		      
		      generatePDFButton.addActionListener(new ActionListener(){
		             public void actionPerformed(ActionEvent a) {
		            	 try {
							generateData();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	 pdf=new GeneratePDFReport();
		            	 
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
	               // Adding records in table model 
	               model.addRow(row);
	         }
	    }
		 public void connect() throws SQLException
		 {

				con = SQLiteConnection.dbConnectorForSQLite();
				 stmt = con.createStatement();
		 }
		 
		  public void deleteEmployeeFrom(int employee_id) throws SQLException{
			  String deleteEmployeeId="DELETE from employee where id='"+employee_id+"'";
			  stmt.executeUpdate(deleteEmployeeId);
			  String deleteEmployeeIdFromAttendance="DELETE from daily_attendance where id='"+employee_id+"'";
			  stmt.executeUpdate(deleteEmployeeIdFromAttendance);
		  }
			 public static Date subtractDays(String date, int days) throws ParseException {
					
				 
				 	GregorianCalendar cal = new GregorianCalendar();
				 	Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(date);
					cal.setTime(date1);
					cal.add(Calendar.DATE, -days);
							
					return cal.getTime();
				}
		  public void generateData() throws SQLException
		  {
			  String eeid=null;
			  String tthm=null;
			  ArrayList<Integer> e_id_list = new ArrayList<>();
			  DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			  	LocalDate localDate = LocalDate.now();
	            String sDate=DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
	            System.out.println("Current date "+sDate);
	   		 	Date newDate = null;
	   		 
				try {
					newDate = subtractDays(sDate, 15);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String nDate=df.format(newDate);
	   		 	System.out.println("newDate 15 days before "+nDate);
	   		
	            String getEmployeeById="SELECT employee.id,employee.firstname,daily_attendance.total_hours_minutes FROM employee LEFT JOIN daily_attendance on employee.id=daily_attendance.id and clock_in_date between '"+sDate+"' and '"+nDate+"'";
					rst = stmt.executeQuery(getEmployeeById);
					while(rst.next())
					{
						e_id_list.add(rst.getInt("id"));
						String eid=rst.getString(1);
						String e_fname=rst.getString(2);
						String e_tthm=rst.getString(3);

						int employeeId=Integer.parseInt(eid);
						System.out.println(employeeId+","+e_fname+","+e_tthm);
						

				    }
					for (int tmp : e_id_list){
			            System.out.println("g"+tmp);
			     }
					
					
			}
			public void calculateTotalTimeForEachEmployee(int employeeId,String current_date,String days_before_date) throws SQLException
			{
				String getTotalHours="SELECT id,firstname FROM employee";
	
				rst = stmt.executeQuery(getTotalHours);
				while(rst.next())
				{
					
//					
//					 while(rst.next())
//					 {
//						 timestampsList.add(rst.getString("total_hours_minutes"));						 
//					 }
//					 for (String tmp : timestampsList){
//				            System.out.print("Time worked"+tmp);
//				     }
//					 long tm = 0;
//				        for (String tmp : timestampsList){
//				            String[] arr = tmp.split(":");
//				          
//				            tm += 60 * Integer.parseInt(arr[1]);
//				            tm += 3600 * Integer.parseInt(arr[0]);
//				            if(tm==0){
//				            	continue;
//				            }
//				        }
//
//				        long hh = tm / 3600;
//				        tm %= 3600;
//				        long mm = tm / 60;
//				        tm %= 60;
//				        String hours=String.valueOf(hh);
//				        String min=String.valueOf(mm);
//				        String total = hours+":"+min;
//
//				        System.out.println("total time"+total);
//				        
				}
				
			}


}