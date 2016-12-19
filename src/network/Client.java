package network;

import java.io.*;
import java.net.*;


public class Client {
	static Socket socket;
	static DataInputStream in;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Connecting...");
		socket = new Socket("localhost", 8084);
		System.out.println("Connetion successful");
		in = new DataInputStream(socket.getInputStream());
		System.out.println("Receiving Data...");
		String test = in.readUTF();
		System.out.println("Message fron server: " + test);
	}
}
