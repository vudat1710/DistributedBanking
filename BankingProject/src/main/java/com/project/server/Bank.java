package com.project.server;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Bank {
	private Query query = new Query();
	public int deposit(String account_num, int amount) throws SQLException {
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			account.setBalance(account.getBalance() + amount);
			account.addTransaction("Deposit", amount);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
	
	public int withdraw(String account_num, int amount) throws SQLException {
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
			account.setBalance(account.getBalance() - amount);
			account.addTransaction("Deposit", amount);
			return account.getBalance();
		} else {
			System.out.println("Not valid account number!");
			return 0;
		}
	}
	
	public int inquiry(String account_num) throws SQLException {
		Account account = query.selectByAccNum(account_num);
		if (account != null) {
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
}
