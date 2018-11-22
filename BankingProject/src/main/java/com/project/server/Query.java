package com.project.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.server.DynamicQuery.Select;
import com.project.server.DynamicQuery.Update;

public class Query {
    private Director dr = new Director();
    private DynamicQuery d = new DynamicQuery();
    private ConnectDB connectDB = new ConnectDB();

    public int updateAcc(Account acc) throws SQLException {
        Connection connection = connectDB.getConnection();
        Update sql = d.new Update()
                .table("account")
                .set(dr.equals("balance", "\"" + acc.getBalance() + "\""))
                .whereAnd(dr.equals("username", "\"" + acc.getUsername() + "\""))
                .whereAnd(dr.equals("password", "\"" + acc.getPassword() + "\""));
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(String.valueOf(sql));
        int rs = ps.executeUpdate();
        if (rs > 0) {
            return 1;
        } else return 0;

    }

    public boolean isInDB(String username, String password) throws SQLException {
        boolean isIn = true;
        Connection connection = connectDB.getConnection();
        Select sql = d.new Select()
                .column("username")
                .column("password")
                .from("account")
                .where(dr.equals("username", "\"" + username + "\""))
                .where(dr.equals("password", "\"" + password + "\""));
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(String.valueOf(sql));
        try {
            ResultSet rs = ps.executeQuery(String.valueOf(sql));
            if (!rs.next()) {
                isIn = false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
                connection.close();
            }
        }
        return isIn;
    }

    public Account selectByAccNum(String acc_num) throws SQLException {
        Account account = new Account();
        Connection connection = connectDB.getConnection();
        Select sql = d.new Select()
//                .column("account_num")
                .from("account")
                .where(dr.equals("account_num", "\"" + acc_num + "\""));
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(String.valueOf(sql));
        try {
            ResultSet rs = ps.executeQuery(String.valueOf(sql));
            while (rs.next()) {
                String username = rs.getString(1);
                int userid = rs.getInt(2);
                String password = rs.getString(3);
                int balance = rs.getInt(4);
                String account_num = rs.getString(5);
                account = new Account(username, userid, password, balance, account_num);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
                connection.close();
            }
        }
        return account;
    }
}
