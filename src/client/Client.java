package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.JOptionPane;

import game.Game;
import iohelper.packet.Packet00Login;
import iohelper.packet.Packet01SyncState;

public class Client extends Thread {
  private ClientIO io;
  
  public Client () {
	  this.io = new ClientIO();
  }
  
  public Client (InetAddress ipAddress, int port) {
	  this.io = new ClientIO(ipAddress, port);
  }
  
  public ClientIO getIO() {
	  return io;
  }
  
  public void run (){
	  // Sent login to server
	  String name = JOptionPane.showInputDialog(this, "Please enter a username");
	  Packet00Login loginPacket = new Packet00Login(name==null? name: "Starfighters " + new Random().nextInt(1000) +1);
	  loginPacket.writeData(this);
	  
	  Game game = new Game();
	  DatagramPacket packet;
	  
	  // Get the initial state of the game from server
	  packet = io.getPacket();
	  System.out.println("Connected to server by IP: " + packet.getAddress().toString() + ":" + packet.getPort()+". Received state.");
	  Packet01SyncState statePacket = new Packet01SyncState(packet.getData());
	  game.decomposeState(statePacket.getState());
	  game.start();

  }
 
}

