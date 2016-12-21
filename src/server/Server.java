package server;

import java.net.DatagramPacket;

import game.Game;

public class Server extends Thread {
	private ServerIO io;
	private Game game;
	public Server () {
		this.io = new ServerIO();
	}
  public void run() {
	  this.game = new Game();
	  game.start();
	  game.isServer = true;
	  game.isClient = false;
	  game.start();
	  while (true) {
		  byte[] data = new byte[1024];
		  DatagramPacket packet = new DatagramPacket(data, data.length);
		  packet = io.getPacket();
		  if (true) {
			  System.out.println("Client < " + packet.getAddress().toString() + ":" + packet.getPort());
			  DatagramPacket res = new DatagramPacket("pong".getBytes(), "pong".getBytes().length, packet.getAddress(), packet.getPort());
			  io.sendPacket(res);  
			  
			  
		  }
	  }
  }
}
