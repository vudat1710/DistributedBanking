package com.project.server;

public class ServerMain {
	public static void main(String[] args) {
		int port = 8888;
		Server server = new Server(port);
		server.start();
	}
}
