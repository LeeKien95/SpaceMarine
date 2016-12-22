package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import client.Client;

public class ServerMain {
  public static void main (String args[]){
	      System.out.println("Server is running...");
		  Server server = new Server();
		  server.start();
		  
  }
}
