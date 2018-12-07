package com.project.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    private Query query;


    public ServerWorker(Server server, Socket clientSocket) {
        query = new Query();
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
        boolean done = false;
        while (!done) {
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter pwInput = new PrintWriter(out, true);
            String menu =
                    "------------Choose action you want to perform----------\n"
                            + "1. Register\n"
                            + "2. Login\n"
                            + "Enter the number you want to choose: ";
            pwInput.println(menu);
            InputStream in = clientSocket.getInputStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(in));
            switch (Integer.parseInt(reader1.readLine())) {
                case 1:
                    clearScreen(pwInput);
                    String msg = "Enter username and password respectively: ";
                    this.outputStream = clientSocket.getOutputStream();
                    PrintWriter pw = new PrintWriter(outputStream, true);
                    pw.println(msg);

                    InputStream inputStream = clientSocket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    List<String> tokens = new ArrayList<String>();
                    while ((line = reader.readLine()) != null) {
                        System.out.println("token receive: " + line);
                        tokens.add(line);
                        System.out.println("tokens size: " + tokens.size());
                        if (tokens.size() == 2) {
                            break;
                        }
                    }

                    done = handleRegister(pw, tokens, reader);
                    if (done) {

                        String acc_num = query.isInDB(tokens.get(0), tokens.get(1));
                        pw.println("\n\n Your account number is: " + acc_num);
                        pw.println("\n\n---Press anything to login---");
                        reader.read();
                        clearScreen(pw);
                        handleLogin(pw, tokens, reader);
                        done = false;
                    }
                    break;
                case 2:
                    clearScreen(pwInput);
                    String msg2 = "Enter username and password respectively: ";
                    this.outputStream = clientSocket.getOutputStream();
                    PrintWriter pw2 = new PrintWriter(outputStream, true);
                    pw2.println(msg2);
                    InputStream inputStream2 = clientSocket.getInputStream();
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));
                    String line2;
                    List<String> tokens2 = new ArrayList<String>();
                    while ((line2 = reader2.readLine()) != null) {
                        System.out.println("token receive: " + line2);
                        tokens2.add(line2);
                        System.out.println("tokens size: " + tokens2.size());
                        if (tokens2.size() == 2) {
                            break;
                        }
                    }
                    handleLogin(pw2, tokens2, reader2);
                default:
                    break;
            }
        }
    }

    private boolean handleLogin(PrintWriter pw, List<String> tokens, BufferedReader reader) throws SQLException, IOException {
        boolean out = false;
        if (tokens.size() == 2) {
            String user = tokens.get(0);
            String password = tokens.get(1);
            pw.println("Loging in please wait....");
            String acc_num = query.isInDB(user, password);
            if (acc_num != null) {
//            if (user.equals("aa") && password.equals("bb")) {
                Identification identification = new Identification(user, "port::" + clientSocket.getPort());
                System.out.println(identification.toString());
                String msg = "Login successfully!";
                pw.println(msg);
                pw.println("\n\n---Press anything to continue---");
                reader.readLine();

                //clearScreen(pw);
                Bank bank = new Bank(query);
//                outputStream.write(msg.getBytes());

                while (!out) {
                    clearScreen(pw);
                    String menu =
                            "------------Choose action you want to perform----------\n"
                                    + "1. Deposit\n"
                                    + "2. Withdraw\n"
                                    + "3. Inquiry\n"
                                    + "4. Transfer\n"
                                    + "5. Log off\n"
                                    + "Enter the number you want to choose: ";

                    pw.println(menu);

                    List<String> strings = new ArrayList();
                    switch (Integer.parseInt(reader.readLine())) {
                        case 1:
                            MessageQueue.addMQueue(identification);
                            if (!identification.isReadOnly()) {
                                pw.println("case 1");
                                strings = enterAmount(outputStream);
                                int depositResult = bank.deposit(acc_num, Integer.parseInt(strings.get(0)));
                                if (depositResult != 0) {
                                    String mString = "Your balance now is: " + depositResult;
                                    MessageQueue.removeQueue(identification);
                                    identification.setReadOnly(false);
                                    pw.println(mString);
                                    pw.println("\n\n---Press anything to continue---");
                                    reader.readLine();
//                            outputStream.write(mString.getBytes());
                                }
                            } else {
                                MessageQueue.removeQueue(identification);
                                identification.setReadOnly(false);
                                pw.println("da co nguoi dang nhap truoc ban, vui long cho");
                                reader.readLine();
                            }
                            break;
                        case 2:
                            MessageQueue.addMQueue(identification);
                            if (!identification.isReadOnly()) {
                                strings = enterAmount(outputStream);
                                if (query.selectByAccNum(acc_num).getBalance() < Integer.parseInt(strings.get(0))) {
                                    pw.println("You do not have enough money to compelete the transaction!");
                                }
                                int withdrawResult = bank.withdraw(acc_num, Integer.parseInt(strings.get(0)));
                                String mString1 = "Your balance now is: " + withdrawResult;
                                MessageQueue.removeQueue(identification);
                                identification.setReadOnly(false);
                                pw.println(mString1);
                                pw.println("\n\n---Press anything to continue---");
                                reader.readLine();
//                        outputStream.write(mString1.getBytes());
                            } else {
                                MessageQueue.removeQueue(identification);
                                identification.setReadOnly(false);
                                pw.println("da co nguoi dang nhap truoc ban, vui long cho");
                                reader.readLine();
                            }
                            break;
                        case 3:
//                            String msg1 = "Enter account number: ";
//                            pw.println(msg1);
////                        outputStream.write(msg1.getBytes());
//                            InputStream inputStream1 = clientSocket.getInputStream();
//                            BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
                            int balance = bank.inquiry(acc_num);
                            String mString2 = "Your current balance is: " + balance;
                            pw.println(mString2);
                            pw.println("\n\n---Press anything to continue---");
                            reader.readLine();
//                        outputStream.write(mString2.getBytes());
                            break;
                        case 4:
                            MessageQueue.addMQueue(identification);
                            if (!identification.isReadOnly()) {
                                strings = enterArgs(outputStream);
                                if (query.selectByAccNum(acc_num).getBalance() < Integer.parseInt(strings.get(0))) {
                                    pw.println("You do not have enough money to compelete the transaction!");
                                }
                                int withdrawResult = bank.transfer(acc_num, strings.get(0), Integer.parseInt(strings.get(1)));
                                String mString1 = "Your balance now is: " + withdrawResult;
                                MessageQueue.removeQueue(identification);
                                identification.setReadOnly(false);
                                pw.println(mString1);
                                pw.println("\n\n---Press anything to continue---");
                                reader.readLine();
//                      outputStream.write(mString1.getBytes());
                            } else {
                                MessageQueue.removeQueue(identification);
                                identification.setReadOnly(false);
                                pw.println("da co nguoi dang nhap truoc ban, vui long cho");
                                reader.readLine();
                            }
                            break;
                        case 5:
                            handleLogoff(identification);
                            out = true;
                            clearScreen(pw);
                            break;
                        case 1507:
                            System.out.println("permission:" + identification.isReadOnly());
                            pw.println("permission:" + identification.isReadOnly());
                            reader.readLine();
                            break;
                        default:
                            reader.readLine();
                            break;
                    }
                }
            } else {
                String msg = "Login failed! Not valid username or password!";
                pw.println(msg);
                pw.println("\n\n---Press anything to continue---");
                reader.read();
                //server.removeWorker(this);
                //clientSocket.close();
                return out;
            }
        }
        return out;
    }

    private boolean handleRegister(PrintWriter pw, List<String> tokens, BufferedReader reader) throws SQLException {
        boolean done = false;
        boolean done2 = false;
        if (tokens.size() == 2) {
            String username = tokens.get(0);
            String password = tokens.get(1);
            pw.println("Dang dang ki xin doi....");
            try {
                done = new Query(1).register(username, password);
                done2 = new Query(2).register(username, password);
                if (done && done2) {
                    pw.println("Register successful!");
                    return true;
                }
            } catch (SQLException e) {
                pw.println("Register Failed! Your username is being used");
                return false;
            }
        }
        return false;
    }

    private List<String> enterArgs(OutputStream outputStream) throws IOException {
        String msg = "Enter account number and amount respectively: ";
        PrintWriter pw = new PrintWriter(outputStream, true);
        pw.println(msg);
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

    private List<String> enterAmount(OutputStream outputStream) throws IOException {
        String msg = "Enter amount : ";
        PrintWriter pw = new PrintWriter(outputStream, true);
        pw.println(msg);
        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> strings = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            System.out.println("token receive: " + line);
            strings.add(line);
            if (strings.size() == 1) {
                break;
            }
        }
        return strings;
    }

    public static void clearScreen(PrintWriter pw) {
        pw.print("\033[H\033[2J");
        pw.flush();
    }

    private boolean handleLogoff(Identification identification) throws IOException {
        MessageQueue.removeQueue(identification);
        //server.removeWorker(this);
        //clientSocket.close();
        return true;
    }
}
