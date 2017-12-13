package com.gui;

import java.awt.Color;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.table.DefaultTableModel;

import com.database.Database;
import com.database.SQLiteConnection;

import javax.swing.*;
import java.awt.*;

public class ViewHistoryGUI extends JFrame {

	JLabel fromDateLabel,endDateLabel,totalHoursWorkedLabel,totalHoursWorkedValueLabel;
	JTextField fromDateField,endDateField;
	JButton searchButton;
	
	JPanel viewHistorypanel;
	JTable viewHistorytable;
	DefaultTableModel viewHistorymodel;
	JScrollPane viewHistoryScrollPane;
	Object[] row = null;
	int employeee_id;
	Connection con;
    Statement stmt;
    PreparedStatement preStatement;
    ResultSet res;
 // time format, i.e. 8:42 AM
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
	public ViewHistoryGUI(int employee_id)
	{
	    super("View History DashBoard");
	    System.out.println("EEID"+employee_id);
	    employeee_id=employee_id;
			// TODO Auto-generated constructor stub
	      setSize(770, 420);
	      setLayout(null);	      
	      // Defining Labels 
	      connect();
	      
	      fromDateLabel = new JLabel("From Date"); 
	      fromDateLabel.setBounds(15, 50, 130, 30);
	      
	      // fromDate field
	      fromDateField = new JTextField("yyyy-mm-d"); 
	      fromDateField.setBounds(95, 50, 130, 30);
	      
	
	      endDateLabel = new JLabel("To Date"); 
	      endDateLabel.setBounds(15, 85, 130, 30);
	      // endDateField
	      endDateField = new JTextField("yyyy-mm-d"); 
	      endDateField.setBounds(95, 85, 130, 30);         
	
	      //Totallabel
	      totalHoursWorkedLabel = new JLabel("Total Hours Worked:"); 
	      totalHoursWorkedLabel.setBounds(15, 155, 130, 30);
	      totalHoursWorkedValueLabel = new JLabel(); 
	      totalHoursWorkedValueLabel.setBounds(15, 180, 130, 30);
	      
	      
	      add(fromDateLabel);
	      add(endDateLabel);
	      
	      // fixing all Label,TextField,RadioButton
	      add(fromDateField);
	      add(endDateField);
	      add(totalHoursWorkedLabel);
	      add(totalHoursWorkedValueLabel);
	      
	      // Defining search Button
	      searchButton = new JButton("Search"); 
	      searchButton.setBounds(25, 125, 80, 25);            
	     
	      // fixing all Buttons
	      add(searchButton);
	      
	      // Defining Panel
	      viewHistorypanel = new JPanel();
	      viewHistorypanel.setLayout(new GridLayout());
	      viewHistorypanel.setBounds(250, 20, 480, 330);
	      viewHistorypanel.setBorder(BorderFactory.createDashedBorder(Color.blue));
	      add(viewHistorypanel);
	      //Defining Model for table
	      viewHistorymodel = new DefaultTableModel();
	
	      //Adding object of DefaultTableModel into JTable
	      viewHistorytable = new JTable(viewHistorymodel);
	
	      //Fixing Columns move
	      viewHistorytable.getTableHeader().setReorderingAllowed(false);
	
	      // Defining Column Names on model
	    
	      viewHistorymodel.addColumn("Date");
	      viewHistorymodel.addColumn("Clock_in");
	      viewHistorymodel.addColumn("Clock_out");
	      viewHistorymodel.addColumn("Hours worked");
	     
	      // Enable Scrolling on table
	      viewHistoryScrollPane = new JScrollPane(viewHistorytable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                                     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      viewHistorypanel.add(viewHistoryScrollPane);
	
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
//	      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      setResizable(false);
	      setVisible(true);
	      searchButton.addActionListener(new ActionListener(){
	             public void actionPerformed(ActionEvent a) {
	                   // calling method resetFields()
	            	 
	            	 String start_date=fromDateField.getText();
	            	 String end_date=endDateField.getText();
	            	 try {
						fetchRecordsBasedOnDates(start_date,end_date,employeee_id);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                  
	             }
	     });
	      

	}
	public void fetchRecordsBasedOnDates(String start_date,String end_date,int employeee_id) throws SQLException
	{
	
		String fetchBasedOnDates="select * from daily_attendance where id='"+employeee_id+"' and clock_in_date and clock_out_date between '"+start_date+"' and '"+end_date+"'";
		String getFirstName="select firstname from employee where id='"+employeee_id+"'";
		res = stmt.executeQuery(getFirstName);
		res.next();
		String temp_first_name=res.getString("firstname");
		res = stmt.executeQuery(fetchBasedOnDates);
		int rows = viewHistorymodel.getRowCount();	
		for(int i = rows - 1; i >=0; i--)
		 {
			 viewHistorymodel.removeRow(i); 
		 }
		while(res.next())
		 { 
			 String temp_clock_in_date=res.getString("clock_in_date");
			 Long temp_clock_in=res.getLong("clock_in_time");
			 Long temp_clock_out=res.getLong("clock_out_time");
			 String t_c_in=timeFormat.format(temp_clock_in);
			 String t_c_out=timeFormat.format(temp_clock_out);
			 String temp_tthm=res.getString("total_hours_minutes");
			 String ing =temp_clock_in_date+","+t_c_in+","+t_c_out+","+temp_tthm;  
			 row = ing.split(",");	
			 viewHistorymodel.addRow(row);
		 }
		calculateTotalHoursMinutesWorkedOnADay(employeee_id, temp_first_name, start_date, end_date);
	
	}
	 public void loadRecords() throws SQLException
	 {
		
		 //loads records for 30days from current date
		 Date date = new Date();
		 System.out.println("current date"+date);
		 
		 Date newDate = subtractDays(date, 30);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		 int dayByOne=1;
		 
		 //code to fetch records from current date to 30 days before date
		 while(newDate.before(date)||newDate.equals(date))
		 {
			 String new_date=formatter.format(newDate);
			 String format_current_date=formatter.format(date);
			 String getTotalHoursByDate="SELECT  employee.id, employee.firstname, daily_attendance.clock_in_date, daily_attendance.clock_in_time,daily_attendance.clock_out_time,daily_attendance.total_hours_minutes FROM employee INNER JOIN daily_attendance ON employee.id = daily_attendance.id WHERE   employee.id= '"+employeee_id+"' and clock_in_date='"+new_date+"' and flag='X';";		 
			 res = stmt.executeQuery(getTotalHoursByDate);
			 while(res.next())
			 { 
				 String temp_clock_in_date=res.getString("clock_in_date");
				 Long temp_clock_in=res.getLong("clock_in_time");
				 Long temp_clock_out=res.getLong("clock_out_time");
				 String temp_tthm=res.getString("total_hours_minutes");
				 String t_c_in=timeFormat.format(temp_clock_in);
				 String t_c_out=timeFormat.format(temp_clock_out);
				 
				 String ing =temp_clock_in_date+","+t_c_in+","+t_c_out+","+temp_tthm;  
				 row = ing.split(",");	
				 viewHistorymodel.addRow(row);
			 }
			 
			 
			 newDate=incrementDayByOne(newDate,dayByOne);
		}
		
	 }
	 public String calculateTotalHoursMinutesWorkedOnADay(int temp_emp_id, String temp_emp_firstname,String temp_clock_in_date,String end_date) throws SQLException 
	 {
		 String fetchTotalHoursWorked="select total_hours_minutes from daily_attendance where id= '"+temp_emp_id+"' and clock_in_date and clock_out_date between '"+temp_clock_in_date+"' and '"+end_date+"' and flag='X'";
		 res = stmt.executeQuery(fetchTotalHoursWorked);
		 ArrayList<String> timestampsList = new ArrayList<String>();
		 while(res.next()){
			 timestampsList.add(res.getString("total_hours_minutes"));
			 
		 }
		 for (String tmp : timestampsList){
	            System.out.println("Time worked"+tmp);
	     }
		 long tm = 0;
	        for (String tmp : timestampsList){
	            String[] arr = tmp.split(":");
	          
	            tm += 60 * Integer.parseInt(arr[1]);
	            tm += 3600 * Integer.parseInt(arr[0]);
	            if(tm==0){
	            	continue;
	            }
	        }

	        long hh = tm / 3600;
	        tm %= 3600;
	        long mm = tm / 60;
	        tm %= 60;
	        String hours=String.valueOf(hh);
	        String min=String.valueOf(mm);
	        String total = hours+":"+min;

	     //   System.out.println(total);
	        totalHoursWorkedValueLabel.setText(total);
	        return total;
	 }
	 
	 public static Date incrementDayByOne(Date date, int days) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.DATE, +days);
					
			return cal.getTime();
		}
	 public static Date subtractDays(Date date, int days) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.DATE, -days);
					
			return cal.getTime();
		}
	 public void connect(){
	        try{
//	              Class.forName("com.mysql.jdbc.Driver");
//	              con =DriverManager.getConnection(
//	                              "jdbc:mysql://localhost:3306/employeems","root","system");
	              con = SQLiteConnection.dbConnectorForSQLite();
	              stmt = con.createStatement();

	        }catch(Exception e){
	              System.out.print(e.getMessage());
	        }
	 }
	 private static String format(long s){
	        if (s < 10) return "0" + s;
	        else return "" + s;
	    }
	

}
