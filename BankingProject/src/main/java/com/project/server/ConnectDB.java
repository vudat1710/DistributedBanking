package com.project.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConnectDB {
	
	private int dbId;
    private String user;
    private String pass;
    private String dbDriver;
    private String dbClass = "com.mysql.jdbc.Driver";
    
    public ConnectDB() {
    	int a = Server.getWorkerListSize();
    	dbId = (int)(a%2) + 1;
    	switch(dbId) {
    	case 1:
    		System.out.println("1");
    		user = "sql12265331";
            pass = "yVSnP7gTYi";
            dbDriver = "jdbc:mysql://sql12.freemysqlhosting.net/sql12265331?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            break;
    	case 2:
    		System.out.println("2");
    		user = "sql12266641";
            pass = "xx1r2QVuXx";
            dbDriver = "jdbc:mysql://sql12.freemysqlhosting.net/sql12266641?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            break;
    	}
    }
    
    public ConnectDB(int a) {
    	switch(a) {
    	case 1:
    		System.out.println("1");
    		user = "sql12265331";
            pass = "yVSnP7gTYi";
            dbDriver = "jdbc:mysql://sql12.freemysqlhosting.net/sql12265331?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            break;
    	case 2:
    		System.out.println("2");
    		user = "sql12266641";
            pass = "xx1r2QVuXx";
            dbDriver = "jdbc:mysql://sql12.freemysqlhosting.net/sql12266641?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            break;
    	}
    }
    
    public int getDbId() {
    	return dbId;
    }
    
    public Connection getConnection() throws SQLException {
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
//            conn = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/btl_cnpm?useSSL=false&useUnicode=true&characterEncoding=UTF-8", "rootbtl", "25126711");
            System.out.println("connected");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) throws SQLException {
        new ConnectDB().getConnection();
    }
}
