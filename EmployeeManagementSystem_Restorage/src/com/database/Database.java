package com.database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.gui.MainDashBoardGUI;


public class Database 
{
	public Connection conn;
    private PreparedStatement prepStatement;
    Statement stmt = null;
    public static Database db;
    ResultSet res;
    String username,password;
    String clockOutDate;
	Long clockOutTime,clockInTime;
	// mm/dd/yy date format
 	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

	// time format, i.e. 8:42 AM
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    public Database() {
		
        try {
            conn = SQLiteConnection.dbConnectorForSQLite();
            stmt = conn.createStatement();
            
           
            
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
	}
	public  boolean authenticateUser(String username,String password,int flag) throws SQLException
	{    	
		String sql="select * from employee where firstname=? and password=?";
		prepStatement = conn.prepareStatement(sql); 
		prepStatement.setString(1, username); 
		prepStatement.setString(2, password); 
         res = prepStatement.executeQuery();
     	 if(res.next())
     	 {	
     		clockIn(username,flag);
     		return true;
     	 }
         else
         {	
        	return false;
         }
	}
	public void clockIn(String username2,int flag) throws SQLException
	{
		prepStatement.setString(1, username2);     	
	    	if(checkIfAlreadyClockedIn(username2)==true)
	    	{
	    		clockOutTime=System.currentTimeMillis();
	            LocalDate localDate = LocalDate.now();
	            clockOutDate=DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
	    		String clockOutnow="update daily_attendance SET flag='O', clock_out_date='"+clockOutDate+"' , clock_out_time='"+clockOutTime+"' where id=(select id from employee where firstname='"+username2+ "' and flag='N')";
	    		prepStatement = conn.prepareStatement(clockOutnow); 
	    		prepStatement.executeUpdate();
	    		String getClockOutTime="select clock_out_time from daily_attendance where id=(select id from employee where firstname='"+username2+ "' and flag='O')";
	    		res=stmt.executeQuery(getClockOutTime);
	    		if(res.next())
	    		{
	        		Long temp_flag = res.getLong("clock_out_time");
	        		System.out.println("Clocked out at "+timeFormat.format(temp_flag));	
	        		    		
	    		}
	    		calculateTotalHoursMinutes(username2);
	    		MainDashBoardGUI.flag=1;
	    	}
	    	else
	    	{
	    		clockInTime=System.currentTimeMillis();
	    		 LocalDate localDate = LocalDate.now();
		         String clockInDate=DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
	    		String clockInnow="insert into daily_attendance(id,clock_in_date,clock_in_time,flag) values ((select id from employee where firstname='"+username2+"'),'"+clockInDate+"','"+clockInTime+"','N')";
	    		prepStatement=conn.prepareStatement(clockInnow);
	    		prepStatement.executeUpdate();
	        	String getClockInTime="select clock_in_time,clock_in_date from daily_attendance where id=(select id from employee where firstname='"+username2+ "' and flag='N')";
	        	stmt.executeQuery(getClockInTime);

	    		if(res.next())
	    		{
	        		Long temp_getClockInTime = res.getLong("clock_in_time");
	        		Date temp_getClockInDate=res.getDate("clock_in_date");
	        		System.out.println("Clocked In on "+temp_getClockInDate);		
	        		System.out.println("Clocked In at "+timeFormat.format(temp_getClockInTime));	
	        		    		
	    		}
	    		conn.close();
	    	}
	    }
	 public boolean checkIfAlreadyClockedIn(String username3) throws SQLException
	 {
	    	
	    	String checkIfAlreadyClockedIn="select * from daily_attendance where id=(select id from employee where firstname='"+username3+"' and flag='N')";
	    	
	    	res = stmt.executeQuery(checkIfAlreadyClockedIn);
	    	if(res.next()){
	    		String flag = res.getString("flag");    		
	    		if(flag.equals("N")){
	    			return true;

	    		}
	     	}
	     	return false;
	 }
	 	public String calculateTotalHoursMinutes(String uname) throws SQLException
	    {
	    	String getClockInOutDetails="select * from daily_attendance where id=(select id from employee where firstname='"+uname+"' and flag='O')";
	    	res = stmt.executeQuery(getClockInOutDetails);
	    	long compStartTimes;
	    	long compEndTimes;
	    	long compTimeWorked;
	    	String humanTimeWorked;
	    	if(res.next())
	    	{
	    		
	    		String clockInDate = res.getString("clock_in_date");

	    		Long clockInTime = res.getLong("clock_in_time");
	    		
	    		String clockOutDate = res.getString("clock_out_date");
	    		Long clockOutTime = res.getLong("clock_out_time");
	    		String updateFlag="update daily_attendance SET  clock_out_date='"+clockOutDate+"' , clock_out_time='"+clockOutTime+"' where id=(select id from employee where firstname='"+uname+ "' and flag='O')";
	    		prepStatement = conn.prepareStatement(updateFlag); 
	    		prepStatement.executeUpdate();    		
	    		compEndTimes=clockOutTime;
	    		compStartTimes=clockInTime;
				// calculates time worked in milliseconds: end time - start time, and stores it at the corresponding position in compTimeWorked array
				compTimeWorked = compEndTimes - compStartTimes;
				// formats this number of milliseconds into hours and minutes, and stores it at the corresponding position in humanTimeWorked array
				// http://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
				int minutes = (int) ((compTimeWorked / (1000*60)) % 60);
				int hours   = (int) ((compTimeWorked / (1000*60*60)) % 24);
				humanTimeWorked = String.format("%d:%d", hours, minutes);

	    		System.out.println("Clocked out on "+clockOutDate);	

	    		System.out.println("Total hours worked "+humanTimeWorked);	
	    		String saveTotalTimeWorked="update daily_attendance SET flag='X',total_hours_minutes='"+humanTimeWorked+"' where id=(select id from employee where firstname='"+uname+ "' and flag='O')";
	    		prepStatement = conn.prepareStatement(saveTotalTimeWorked); 
	    		prepStatement.executeUpdate();
	    		conn.close();
	    	}
	    	return null;
	    }
	public void deleteRecordsNintyDaysOld() throws SQLException{
		Date date = new Date();
	
		Date newDate = subtractDays(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String new_date=formatter.format(newDate);

		
		String delete90DaysRecord="delete from daily_attendance where clock_in_date='"+new_date+"'";		 
		stmt.executeUpdate(delete90DaysRecord);

		 stmt.close();
	}
	 public static Date subtractDays(Date date) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.DATE, -90);	
			return cal.getTime();
		}

}