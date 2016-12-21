package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import client.Client;

public class ServerMain {
  public static void main (String args[]){
		  Server server = new Server();
		  server.start();
		  
//		  Client client = new Client();
//		  client.start();
//		  try {
//			client.getIO().sendPacket(new DatagramPacket("ping".getBytes(), "ping".getBytes().length, InetAddress.getByName("localhost"), 1331));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
  }
}
