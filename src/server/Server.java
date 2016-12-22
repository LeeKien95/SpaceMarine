package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.JOptionPane;

import game.Game;
import game.Player;
import iohelper.packet.Packet;
import iohelper.packet.Packet.PacketTypes;
import iohelper.packet.Packet00Login;
import iohelper.packet.Packet01SyncState;

public class Server extends Thread {
	private ServerIO io;
	private Game game;
	public Server () {
		this.io = new ServerIO();
	}
	
  public void run() {
	  // The game for all client
	  this.game = new Game();
	  game.isServer = true;
	  game.isClient = false;
	  game.start();
	  
	  // Continuously get request and send response to proper client (TODO implement Room feature)
	  while (true) {
		  getRequest();
		  sendResponse();
	  }
  }
  
  private void sendResponse() {
	  // Send current game state to all client (TODO send to one specific room)
	  Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
	  responsePacket.writeData(this);
  }

  private void getRequest() {
	  DatagramPacket packet =  io.getPacket();
	  parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
  }
  
  // Xu ly packet truyen toi tuy theo packetType
  private void parsePacket(byte[] data, InetAddress address, int port) {
	String message = new String(data).trim();
	PacketTypes type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
    switch (type) {
    default:
    case INVALID:
    	break;
    case LOGIN:
    	Packet00Login packet = new Packet00Login(data);
    	System.out.println(address+":"+port+" has connected...");
    	
    	// Them player moi vao game
		game.jetfighter = new Player(packet.getPlayerName(), address, port);
		game.jetfighters.add(game.jetfighter);
    	break;
    case SYNC:
    	
    	break;
    
    case DISCONNECT:
    	break;
    }
  }
  
  public Game getGame() {
	  return game;
  }
  
  public ServerIO getIO () {
	  return io;
  }
  

	public void sendDataToProperClient(byte[] data) {
		for (Player p : game.getPlayers()) {
			System.out.println("Sent to" + p.getPort());
			io.sendData(data, p.getIpAddress(), p.getPort());
		}
	}
}
