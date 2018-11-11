package com.project.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;

public class ServerWorker extends Thread{
	private final Socket clientSocket;
	private final Server server;
	private OutputStream outputStream;
	private Query query;
	
	public ServerWorker(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			handleSocketClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleSocketClient() throws IOException, SQLException {
		// TODO Auto-generated method stub
		String msg = "Enter username and password respectively: ";
		this.outputStream = clientSocket.getOutputStream();
		outputStream.write(msg.getBytes());
		InputStream inputStream = clientSocket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		List<String> tokens = new ArrayList<>();
		while((line = reader.readLine()) != null) {
			tokens.add(line);
		}
		handleLogin(outputStream, tokens);
	}

	private void handleLogin(OutputStream outputStream, List<String> tokens) throws SQLException, IOException {
		if (tokens.size() == 2) {
			String user = tokens.get(0);
			String password = tokens.get(1);
			
			if (query.isInDB(user, password)) {
				String msg = "Login successfully!";
				outputStream.write(msg.getBytes());
				String menu = 
						"------------Choose action you want to perform----------\n"
						+ "1. Deposit\n"
						+ "2. Withdraw\n"
						+ "3. Inquiry\n"
						+ "4. Statement\n"
						+ "5. Log off\n"
						+ "Enter the number you want to choose: ";
				InputStream inputStream = clientSocket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				int selection;
				Bank bank = new Bank();
				List<String> strings = new ArrayList<>();
				switch (selection = reader.read()) {
				case 1:
					strings = enterArgs(outputStream);
					int depositResult = bank.deposit(strings.get(0), Integer.parseInt(strings.get(1)));
					if (depositResult != 0) {
						String mString = "Your balance now is: " + depositResult;
						outputStream.write(mString.getBytes());
					}
					break;
				case 2:
					strings = enterArgs(outputStream);
					int withdrawResult = bank.withdraw(strings.get(0), Integer.parseInt(strings.get(1)));
					String mString1 = "Your balance now is: " + withdrawResult;
					outputStream.write(mString1.getBytes());
					break;
				case 3:
					String msg1 = "Enter account number: ";
					outputStream.write(msg1.getBytes());
					InputStream inputStream1 = clientSocket.getInputStream();
					BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
					int balance = bank.inquiry(String.valueOf(reader1.read()));
					String mString2 = "Your current balance is: " + balance;
					outputStream.write(mString2.getBytes());
				case 4:
					break;
				case 5:
					handleLogoff();
				default:
					break;
				}
			} else {
				String msg = "Login failed! Not valid username or password!";
				outputStream.write(msg.getBytes());
			}
		}
	}
	
	private List<String> enterArgs(OutputStream outputStream) throws IOException {
		String msg = "Enter account number and amount respectively: ";
		outputStream.write(msg.getBytes());
		InputStream inputStream = clientSocket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		List<String> strings = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			strings.add(line);
		}
		return strings;
	}
	
	private void handleLogoff() throws IOException {
		server.removeWorker(this);
		clientSocket.close();
	}
}
