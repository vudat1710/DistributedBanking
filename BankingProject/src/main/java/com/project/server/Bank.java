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
		if(!Consistency.check(account_num, dbId)) {
			Consistency.doConsistence(account_num, dbId);
		}
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			account.setBalance(account.getBalance() + amount);
			//account.addTransaction("Deposit", amount);
			query.updateAcc(account);
			Consistency.setLastModifiedDb(account_num, dbId);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
	
	public int withdraw(String account_num, int amount) throws SQLException {
		int dbId = query.getDbId();
		if(!Consistency.check(account_num, dbId)) {
			Consistency.doConsistence(account_num, dbId);
		}
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			account.setBalance(account.getBalance() - amount);
			query.updateAcc(account);
//			account.addTransaction("Deposit", amount);
			Consistency.setLastModifiedDb(account_num, dbId);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
	
	public int inquiry(String account_num) throws SQLException {
		int dbId = query.getDbId();
		if(!Consistency.check(account_num, dbId)) {
			Consistency.doConsistence(account_num, dbId);
		}
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			Consistency.setLastModifiedDb(account_num, 0);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
	
	public List<Transaction> getStatement(String account_num, Date startDate, Date endDate) throws SQLException{
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			return account.geTransactionsByDate(startDate, endDate);
		} else {
			System.out.println("Not valid account number!");
			return null;
		}
	}


	public int transfer(String account_num, String receiver, int amount) throws SQLException {
		int dbId = query.getDbId();
		if(!Consistency.check(account_num, dbId)) {
			System.out.println("sent");
			Consistency.doConsistence(account_num, dbId);
		}
		if(!Consistency.check(receiver, dbId)) {
			System.out.println("rev");
			Consistency.doConsistence(receiver, dbId);
		}
		Account account = query.selectByAccNum(account_num);
		Account receive = query.selectByAccNum(receiver);
		if (account != null) {
			account.setBalance(account.getBalance() - amount);
			receive.setBalance(receive.getBalance() + amount);
			query.updateAcc(account);
			query.updateAcc(receive);
//			account.addTransaction("Deposit", amount);
			Consistency.setLastModifiedDb(account_num, dbId);
			Consistency.setLastModifiedDb(receiver, dbId);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
}
