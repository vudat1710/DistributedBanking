package com.project.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        try {
            String ip = args[0];
            Socket client = new Socket(ip, 8888);
            System.out.println("Client port is " + client.getLocalPort());
            OutputStream os = client.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);

            InputStream sin = client.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(sin));
            String s;

            Scanner sc = new Scanner(System.in);

            ClientListener listener = new ClientListener(br);
            listener.start();
            while (true) {
                s = sc.nextLine();
                System.out.println("ban gui::" + s);
                pw.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}