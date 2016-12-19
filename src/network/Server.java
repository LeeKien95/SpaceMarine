package network;


import java.io.*;
import java.net.*;

public class Server {
	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream out;
	public static void main(String[] args) throws IOException {
		System.out.println("Starting Server...");
		serverSocket = new ServerSocket(8084);
		System.out.println("Server started...");
		socket = serverSocket.accept();
		System.out.println("Connection from: " + socket.getInetAddress());
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF("Hello sexy.");
		System.out.println("Data has been seen.");
	}
}
