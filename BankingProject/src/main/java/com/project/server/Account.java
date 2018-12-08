package com.project.server;

public class Account {
    private String username;
    private int userid;
    private String password;
    private int balance;
    private String account_num;


    public Account(String username, int userid, String password, int balance, String account_num, int DbId) {
        this.username = username;
        this.userid = userid;
        this.password = password;
        this.balance = balance;
        this.account_num = account_num;
    }

    public Account() {
        // TODO Auto-generated constructor stub
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = "\'" + account_num + "\'";
    }
}
