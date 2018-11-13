package com.project.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public Connection getConnection() throws SQLException {
        String user = "sql12265331";
        String pass = "yVSnP7gTYi";
        String dbClass = "com.mysql.jdbc.Driver";
        String dbDriver = "jdbc:mysql://sql12.freemysqlhosting.net/sql12265331?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
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
