package com.database;

import java.sql.*;

public class Database {

	public Connection conn;
    private PreparedStatement statement;
    public static Database db;
    ResultSet res;
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
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
    		res = statement.executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        int result = statement.executeUpdate(insertQuery);
        return result;
 
    }
    public  boolean authenticateUser(String username,String password) throws SQLException{

    	System.out.println(username);
    	 statement.setString(1, username); 
    	 statement.setString(2, password); 
    	res = statement.executeQuery();
     	if(res.next()){
     		System.out.println("User found");
     		return true;
     	}
        else{
        	System.out.println("User not found");
     		
        	return false;
        }
	}
}
