package com.project.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.mysql.jdbc.StringUtils;

public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    private Query query = new Query();

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
        PrintWriter pw = new PrintWriter(outputStream, true);
        pw.println(msg);

        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> tokens = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
//            System.out.println("token receive: " + line);
            tokens.add(line);
//            System.out.println("tokens size: " + tokens.size());
            if (tokens.size() == 2) {
                break;
            }
        }
        handleLogin(pw, tokens, reader);

    }

    //    private void handleLogin(OutputStream outputStream, List<String> tokens) throws SQLException, IOException {
    private void handleLogin(PrintWriter pw, List<String> tokens, BufferedReader reader) throws SQLException, IOException {
        if (tokens.size() == 2) {
            String user = tokens.get(0);
            String password = tokens.get(1);
            pw.println("Dang login xin doi....");

            if (query.isInDB(user, password)) {
//            if (user.equals("aa") && password.equals("bb")) {
                Identification identification = new Identification(user, "port::" + clientSocket.getPort());
                System.out.println(identification.toString());
//                MessageQueue.addMQueue(identification);
                String msg = "Login successfully!";
                pw.println(msg);
                Bank bank = new Bank();
//                outputStream.write(msg.getBytes());
                boolean out = false;
                while (!out) {
                    String menu =
                            "------------Choose action you want to perform----------\n"
                                    + "1. Deposit\n"
                                    + "2. Withdraw\n"
                                    + "3. Inquiry\n"
                                    + "4. Statement\n"
                                    + "5. Log off\n"
                                    + "Enter the number you want to choose: ";

                    pw.println(menu);

                    List<String> strings = new ArrayList();
                    switch (Integer.parseInt(reader.readLine())) {
                        case 1:// Deposit
//                            if (!identification.isReadOnly()) {
                            pw.println("case 1");
                            strings = enterArgs(outputStream);
                            int depositResult = bank.deposit(strings.get(0), Integer.parseInt(strings.get(1)));
                            if (depositResult != 0) {
                                String mString = "Your balance now is: " + depositResult;
                                pw.println(mString);
//                            outputStream.write(mString.getBytes());
                            }
//                            } else {
//                                pw.println("da co nguoi dang nhap truoc ban, vui long cho");
//
//                            }
                            break;
                        case 2: // Withdraw
                            MessageQueue.addMQueue(identification);
                            if (!identification.isReadOnly()) {
                                strings = enterArgs(outputStream);
                                int withdrawResult = bank.withdraw(strings.get(0), Integer.parseInt(strings.get(1)));
                                String mString1 = "Your balance now is: " + withdrawResult;
                                pw.println(mString1);
//                        outputStream.write(mString1.getBytes());
                            } else {
                                MessageQueue.removeQueue(identification);
                                pw.println("da co nguoi dang nhap truoc ban, vui long cho");
                            }
                            break;
                        case 3:
                            String msg1 = "Enter account number: ";
                            pw.println(msg1);
//                        outputStream.write(msg1.getBytes());
                            InputStream inputStream1 = clientSocket.getInputStream();
                            BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
                            int balance = bank.inquiry(String.valueOf(reader1.read()));
                            String mString2 = "Your current balance is: " + balance;
                            pw.println(mString2);
//                        outputStream.write(mString2.getBytes());
                        case 4:
                            break;
                        case 5:
                            handleLogoff(identification);
                            out = true;
                            break;
                        case 1507:
                            pw.println("permission:" + identification.isReadOnly());
                            break;
                        default:
                            break;
                    }
                }
            } else {
                String msg = "Login failed! Not valid username or password!";
                pw.println(msg);
//                outputStream.write(msg.getBytes());
            }
        }
    }

    private List<String> enterArgs(OutputStream outputStream) throws IOException {
        String msg = "Enter account number and amount respectively: ";
        PrintWriter pw = new PrintWriter(outputStream, true);
        pw.println(msg);
//        outputStream.write(msg.getBytes());
        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> strings = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            System.out.println("token receive: " + line);
            strings.add(line);
            if (strings.size() == 2) {
                break;
            }
        }
        return strings;
    }

    private void handleLogoff(Identification identification) throws IOException {
        MessageQueue.removeQueue(identification);
        server.removeWorker(this);
        clientSocket.close();
    }
}
