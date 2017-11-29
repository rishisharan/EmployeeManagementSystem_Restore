package com.database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Database {

	public Connection conn;
    private PreparedStatement statement;
    Statement stmt = null;
    public static Database db;
    ResultSet res;
    String username,password;
    String clockOutDate;
	String clockOutTime;
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
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

    	System.out.println(username);
    	 statement.setString(1, username); 
    	 statement.setString(2, password); 
    	res = statement.executeQuery();
     	if(res.next()){
     		System.out.println("User found");
     		clockIn(username);
     		return true;
     	}
        else{
        	System.out.println("User not found");
     		
        	return false;
        }
	}
    public void clockIn(String username2) throws SQLException
    {
    	statement.setString(1, username2); 
    	if(checkIfAlreadyClockedIn(username2)==true){
    		
    		Calendar cal = Calendar.getInstance();
    		clockOutTime=sdf.format(cal.getTime());
            System.out.println("clocked out at Time "+sdf.format(cal.getTime()));
            LocalDate localDate = LocalDate.now();
            System.out.println("clocked out on day "+DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate));
            clockOutDate=DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
            
    		String clockOutnow="update daily_attendance SET flag='O', clock_out_date='"+clockOutDate+"' , clock_out_time='"+clockOutTime+"' where id=(select id from employee where firstname='"+username2+ "' and flag='N')";
    		statement.executeUpdate(clockOutnow);
    		System.out.println("Clocked out");
    	}
    	else{
    		String clockInnow="insert into daily_attendance(id,clock_in_date,clock_in_time,flag) values ((select id from employee where firstname='"+username2+"'),now(),now(),'N')";
        	int result = statement.executeUpdate(clockInnow);
        	
    	}
    }
    public boolean checkIfAlreadyClockedIn(String username3) throws SQLException{
    	
    	String checkIfAlreadyClockedIn="select * from daily_attendance where id=(select id from employee where firstname='"+username3+"' and flag='N')";
    	res = stmt.executeQuery(checkIfAlreadyClockedIn);
    	System.out.println("Used"+res.first());
    	if(res.first()){
    		String flag = res.getString("flag");
    		System.out.println("Used enterd"+flag);	
    		
    		if(flag.equals("N")){
    			System.out.println("returning true");
    			return true;

    		}
     	}
     	return false;
    }
}
