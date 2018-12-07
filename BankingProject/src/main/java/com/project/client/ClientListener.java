package com.project.client;

import java.io.BufferedReader;

public class ClientListener extends Thread {
    private BufferedReader reader;

    public ClientListener(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String s;
                while ((s = reader.readLine()) != null) {
                    System.out.println(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

