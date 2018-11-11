package com.project.server;

import java.text.DecimalFormat;
import java.util.Date;

public class Transaction {
	private DecimalFormat precision = new DecimalFormat("0.00");
	
//	private int tid;
	private Date trans_date;
	private Account account;
	private int amount;
	private String type;
	private int uptodate_balance;
	
	public Transaction(int amount, String type, int uptodate_balance) {
//		this.tid = tid;
		this.amount = amount;
		this.type = type;
		this.uptodate_balance = 0;
		trans_date = new Date(System.currentTimeMillis());
	}
	
//	public int getTid() {
//		return tid;
//	}
//	public void setTid(int tid) {
//		this.tid = tid;
//	}
	public Date getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(Date trans_date) {
		this.trans_date = trans_date;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		int amt = this.account.getBalance();
		this.uptodate_balance = this.type.equals("Deposit") ? this.uptodate_balance = amt + this.amount : amt - this.amount;
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Type: " + type +
				"\nAmount: " + precision.format(amount) + "đ" +
			    "\nBalance: " + precision.format(uptodate_balance) + "đ" +
			    "\nDate: " + trans_date.toString() + "\n";
	}
}
