package com.database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Database {

	public Connection conn;
    private PreparedStatement statement;
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
    
    private Database() {
		// TODO Auto-generated constructor stub
		String url= "jdbc:mysql://localhost:3306/";
        String dbName = "employeems";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "system";
        try {
            Class.forName(driver).newInstance();
            conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
            stmt = conn.createStatement();
            statement = conn.prepareStatement("select * from employee where firstname=? and password=?");
            
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
	}
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized Database getDbCon() {
        if ( db == null ) {
            db = new Database();
        }
        return db;
 
    }
    public  boolean authenticateUser(String username,String password) throws SQLException{

    	
    	 statement.setString(1, username); 
    	 statement.setString(2, password); 
    	res = statement.executeQuery();
     	if(res.next()){
     		
     		clockIn(username);
     		return true;
     	}
        else{
        	
        	return false;
        }
	}
    public void clockIn(String username2) throws SQLException
    {
    	statement.setString(1, username2); 
    	
    	if(checkIfAlreadyClockedIn(username2)==true){
    		

    		clockOutTime=System.currentTimeMillis();
            LocalDate localDate = LocalDate.now();
            clockOutDate=DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
    		String clockOutnow="update daily_attendance SET flag='O', clock_out_date='"+clockOutDate+"' , clock_out_time='"+clockOutTime+"' where id=(select id from employee where firstname='"+username2+ "' and flag='N')";
    		statement.executeUpdate(clockOutnow);
    		String getClockOutTime="select clock_out_time from daily_attendance where id=(select id from employee where firstname='"+username2+ "' and flag='O')";
    		res=statement.executeQuery(getClockOutTime);
    		if(res.first())
    		{
        		Long temp_flag = res.getLong("clock_out_time");
        		System.out.println("Clocked out at "+timeFormat.format(temp_flag));	
        		    		
    		}
    		calculateTotalHoursMinutes(username2);
    	}
    	else{
    		clockInTime=System.currentTimeMillis();
    		String clockInnow="insert into daily_attendance(id,clock_in_date,clock_in_time,flag) values ((select id from employee where firstname='"+username2+"'),now(),'"+clockInTime+"','N')";
        	int result = statement.executeUpdate(clockInnow);
        	String getClockInTime="select clock_in_time,clock_in_date from daily_attendance where id=(select id from employee where firstname='"+username2+ "' and flag='N')";
    		res=statement.executeQuery(getClockInTime);
    		if(res.first()){
        		Long temp_getClockInTime = res.getLong("clock_in_time");
        		Date temp_getClockInDate=res.getDate("clock_in_date");
        		System.out.println("Clocked In on "+temp_getClockInDate);		
        		System.out.println("Clocked In at "+timeFormat.format(temp_getClockInTime));	
        		    		
    		}
    	}
    }
    public String calculateTotalHoursMinutes(String uname) throws SQLException
    {
    	String getClockInOutDetails="select * from daily_attendance where id=(select id from employee where firstname='"+uname+"' and flag='O')";
    	res = stmt.executeQuery(getClockInOutDetails);
    	long compStartTimes;
    	long compEndTimes;
    	long compTimeWorked;
    	String humanTimeWorked;
    	if(res.first()){
    		
    		String clockInDate = res.getString("clock_in_date");
//    		String clockInTime = res.getString("clock_in_time");
    		Long clockInTime = res.getLong("clock_in_time");
    		
    		String clockOutDate = res.getString("clock_out_date");
    		Long clockOutTime = res.getLong("clock_out_time");
    		String updateFlag="update daily_attendance SET  clock_out_date='"+clockOutDate+"' , clock_out_time='"+clockOutTime+"' where id=(select id from employee where firstname='"+uname+ "' and flag='O')";
    		statement.executeUpdate(updateFlag);
    		
    		compEndTimes=clockOutTime;
    		compStartTimes=clockInTime;
			// calculates time worked in milliseconds: end time - start time, and stores it at the corresponding position in compTimeWorked array
			compTimeWorked = compEndTimes - compStartTimes;
			// formats this number of milliseconds into hours and minutes, and stores it at the corresponding position in humanTimeWorked array
			// http://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
			int minutes = (int) ((compTimeWorked / (1000*60)) % 60);
			int hours   = (int) ((compTimeWorked / (1000*60*60)) % 24);
			humanTimeWorked = String.format("%dh %dm", hours, minutes);
//    		System.out.println("emplo enterd"+clockInDate);	
//    		System.out.println("emplo enterd time"+timeFormat.format(compStartTimes));	
    		System.out.println("Clocked out on "+clockOutDate);	
//    		System.out.println("Clocked out at time "+timeFormat.format(compEndTimes));	
    		System.out.println("Total hours worked "+humanTimeWorked);	
    		String saveTotalTimeWorked="update daily_attendance SET flag='X',total_hours_minutes='"+humanTimeWorked+"' where id=(select id from employee where firstname='"+uname+ "' and flag='O')";
    		statement.executeUpdate(saveTotalTimeWorked);
    	}
    	return null;
    }
    public boolean checkIfAlreadyClockedIn(String username3) throws SQLException{
    	
    	String checkIfAlreadyClockedIn="select * from daily_attendance where id=(select id from employee where firstname='"+username3+"' and flag='N')";
    	res = stmt.executeQuery(checkIfAlreadyClockedIn);
    	System.out.println("Used"+res.first());
    	if(res.first()){
    		String flag = res.getString("flag");    		
    		if(flag.equals("N")){
    			return true;

    		}
     	}
     	return false;
    }
}