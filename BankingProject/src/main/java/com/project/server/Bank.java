package com.project.server;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Bank {

    private Query query;

    public Bank(Query query) {
        this.query = query;
    }


    public int deposit(String account_num, int amount) throws SQLException {
        int dbId = query.getDbId();
        int temp_amount;
        if (!Consistency.check(account_num, dbId)) {
            Consistency.doConsistence(account_num, dbId);
            temp_amount = amount;
        } else {
            int temp = Consistency.getWS(account_num);
            temp_amount = temp + amount;
        }
        Account account = query.selectByAccNum(account_num);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            query.updateAcc(account);
            Consistency.setLastModifiedDb(account_num, dbId, temp_amount);
            return account.getBalance();
        } else {
            System.out.println("Not valid account number!");
            return 0;
        }
    }

    public int withdraw(String account_num, int amount) throws SQLException {
        int dbId = query.getDbId();
        int temp_amount;
        Account account = query.selectByAccNum(account_num);
        if (!Consistency.check(account_num, dbId)) {
            int change = (-1) * amount;
            Consistency.doConsistence(account_num, dbId);
            temp_amount = change;
        } else {
            int change = (-1) * amount;
            int temp = Consistency.getWS(account_num);
            temp_amount = temp + change;
        }

        if (account != null) {
            account.setBalance(account.getBalance() - amount);
            query.updateAcc(account);
            Consistency.setLastModifiedDb(account_num, dbId, temp_amount);
            return account.getBalance();
        } else {
            System.out.println("Not valid account number!");
            return 0;
        }
    }

    public int inquiry(String account_num) throws SQLException {
        int dbId = query.getDbId();
        if (!Consistency.check(account_num, dbId)) {
            Consistency.doConsistence(account_num, dbId);
            Consistency.setLastModifiedDb(account_num, 0, 0);
        }
        Account account = query.selectByAccNum(account_num);
        if (account != null) {
            
            return account.getBalance();
        } else {
            System.out.println("Not valid account number!");
            return 0;
        }
    }

    public int transfer(String account_num, String receiver, int amount) throws SQLException {
        int dbId = query.getDbId();
        int temp_amount1;
        int temp_amount2;
        Account account = query.selectByAccNum(account_num);
        Account receive = query.selectByAccNum(receiver);
        if (!Consistency.check(account_num, dbId)) {
            System.out.println("sent");
            Consistency.doConsistence(account_num, dbId);
            temp_amount1 = (-1) * amount;
        } else {
            int change = (-1) * amount;
            int temp = Consistency.getWS(account_num);
            temp_amount1 = temp + change;
        }
        if (!Consistency.check(receiver, dbId)) {
            System.out.println("rev");
            Consistency.doConsistence(receiver, dbId);
            temp_amount2 = amount;
        } else {
            int temp = Consistency.getWS(account_num);
            temp_amount2 = temp + amount;
        }

        if (account != null) {
            account.setBalance(account.getBalance() - amount);
            receive.setBalance(receive.getBalance() + amount);
            query.updateAcc(account);
            query.updateAcc(receive);
            Consistency.setLastModifiedDb(account_num, dbId, temp_amount1);
            Consistency.setLastModifiedDb(receiver, dbId, temp_amount2);
            return account.getBalance();
        } else {
            System.out.println("Not valid account number!");
            return 0;
        }
    }

    public boolean register(String username, String password) throws SQLException {
        boolean done = query.register(username, password);
        return done;
    }

    public boolean deleteAccount(String username) throws SQLException {
        boolean done = query.delete(username);
        return done;
    }
}
