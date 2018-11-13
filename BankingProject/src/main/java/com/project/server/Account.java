package com.project.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {
    private String username;
    private int userid;
    private String password;
    private int balance;
    private String account_num;
    private List<Transaction> transactions;

    public Account(String username, int userid, String password, int balance, String account_num) {
        this.username = username;
        this.userid = userid;
        this.password = password;
        this.balance = balance;
        this.account_num = account_num;
        transactions = new ArrayList<Transaction>();
    }

    public Account() {
        // TODO Auto-generated constructor stub
    }

    public void addTransaction(String type, int amount) {
        Transaction e = new Transaction(amount, type, getBalance());
        transactions.add(e);
    }

    public List<Transaction> geTransactionsByDate(Date startDate, Date endDate) {
        List<Transaction> statementList = new ArrayList<Transaction>();
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getTrans_date().after(startDate) && t.getTrans_date().before(endDate)) {
                statementList.add(t);
            }
        }
        return statementList;
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
