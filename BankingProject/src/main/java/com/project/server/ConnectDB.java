package com.project.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public Connection getConnection() throws SQLException {
		String user = "vudat1710";
		String pass = "17101998";
		String dbClass = "com.mysql.jdbc.Driver";
		String dbDriver = "jdbc:mysql://db4free.net:3306/banking_sys?autoReconnect=true&useSSL=false";
		Connection conn = null;
	    //load driver
	    try {
	        Class.forName(dbClass).newInstance();
	        System.out.println("driver loaded"); 
	    } catch (Exception ex) {
	        System.err.println(ex);
	    }
	    // Connection
	    try {
	        conn = DriverManager.getConnection(dbDriver, user, pass);
	        System.out.println("connected"); 
	    } catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	    }
	    
	    return conn;
	}
}
