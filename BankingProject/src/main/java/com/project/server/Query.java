package com.project.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.server.DynamicQuery.Insert;
import com.project.server.DynamicQuery.Select;
import com.project.server.DynamicQuery.Update;

public class Query {
    private Director dr = new Director();
    private DynamicQuery d = new DynamicQuery();
    private ConnectDB connectDB;

    public Query() {
        connectDB = new ConnectDB();
    }

    public Query(int a) {
        connectDB = new ConnectDB(a);
    }

    public int getDbId() {
        return connectDB.getDbId();
    }

    public int updateBalance(String acc_num, int balance) throws SQLException {
        Connection connection = connectDB.getConnection();
        Account acc = selectByAccNum(acc_num);
        int new_bal = balance + acc.getBalance();
        Update sql = d.new Update()
                .table("account")
                .set(dr.equals("balance", "\"" + new_bal + "\""))
                .whereAnd(dr.equals("account_num", "\"" + acc_num + "\""));
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(String.valueOf(sql));
        int rs = ps.executeUpdate();
        if (rs > 0) {
            return 1;
        } else return 0;
    }

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

    public String isInDB(String username, String password) throws SQLException {
        String acc_num = null;
        Connection connection = connectDB.getConnection();
        Select sql = d.new Select()
//                .column("account_num")
                .from("account")
                .where(dr.equals("username", "\"" + username + "\""))
                .where(dr.equals("password", "\"" + password + "\""));
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(String.valueOf(sql));
        try {
            ResultSet rs = ps.executeQuery(String.valueOf(sql));
            if (rs.next()) {
                acc_num = rs.getString(5);
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
        return acc_num;
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
                account = new Account(username, userid, password, balance, account_num, connectDB.getDbId());
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

    public boolean register(String username, String password) throws SQLException {
        boolean done = false;
        Connection connection = connectDB.getConnection();
        String sql = "SELECT * FROM account ORDER BY userid DESC LIMIT 1";
        System.out.println(String.valueOf(sql));
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                int userid = rs.getInt(2);
                int acc_num = Integer.parseInt(rs.getString(5));
                int balance = 0;
                Insert sql1 = d.new Insert()
                        .table("account")
                        .value("\"" + username + "\"")
                        .value(Integer.toString(userid + 1))
                        .value(password)
                        .value(Integer.toString(balance))
                        .value("\"" + Integer.toString(acc_num + 1) + "\"");
                PreparedStatement ps1 = connection.prepareStatement(String.valueOf(sql1));
                System.out.println(String.valueOf(sql1));
                try {
                    ps1.executeUpdate();
                    done = true;
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    throw e;
                }
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
        return done;
    }
}
