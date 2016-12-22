package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.JOptionPane;

import client.Client;
import game.Game;
import game.Player;
import iohelper.packet.Packet;
import iohelper.packet.Packet.PacketTypes;
import iohelper.packet.Packet00Login;
import iohelper.packet.Packet01SyncState;
import iohelper.packet.Packet02ClientAction;

public class Server extends Thread {
	private ServerIO io;
	private Game game;
	public Server () {
		this.io = new ServerIO();
	}
	
	
  class SyncGameState implements Runnable {
	    private Server server;

		public SyncGameState(Server server) {
			this.server = server;
		}
	
		@Override
		public void run() {
//			while(true) {
//				try {
//					server.sendResponse();
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//	//			if(server.game.isChanged) {
//	//				Packet02ClientAction actionPacket = client.game.getClientPacket();
//	//				actionPacket.writeData(client);
//	//			}	
//			}
			
			synchronized(server.game) {
				while (true) {
				  while (game.isLocked()) {
					  System.out.println("Wait for it...");
					  try {
						server.game.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  }
				  
				  server.sendResponse();
				  game.notifyAll();
				}  
			}
		}
	  
  }
  
  class RequestListener implements Runnable {
	  private Server server;

	public RequestListener(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
//		while(true) {
//			try {
//				server.getRequest();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		synchronized(server.game) {
			while (true) {
			  while (game.isLocked()) {
				  System.out.println("Wait for it...");
				  try {
					server.game.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			  
			  server.getRequest();
			}  
		}
		
		
	}
	  
  }

	
  public void run() {
	  // The game for all client
	  this.game = new Game();
	  game.isServer = true;
	  game.isClient = false;
	  game.setLocked(false);
	  game.start();
	  
	  
	  // Continuously get request and send response to proper client (TODO implement Room feature)
	  // LIEN TUC GUI RESPONSE LA GAME STATE CHO TAT CA CLIENT
	  // getRequest();

	  
	  RequestListener RequestListenerThread = new RequestListener(this);
	  Thread listenerThread = new Thread(RequestListenerThread);
	  listenerThread.start();
	  
	  
	  SyncGameState syncGameState = new SyncGameState(this);
	  Thread syncThread = new Thread(syncGameState);
	  syncThread.start();
	  // System.out.println(syncThread.getName() + " state: "+syncThread.getState());
	  
//	  while (true) {
//		  try {
//			getRequest();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		  // sendResponse();
//		  
////		  try {
////				Thread.sleep(100);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//	  }
  }
  
  public void sendResponse()  {
//	  synchronized (game) {
//		  while (game.isLocked()) {
//			  System.out.println("Wait for it...");
//			  game.wait();
//		  }
		// Send current game state to all client (TODO send to each specific room)
		  Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
		  responsePacket.writeData(this);
//		  game.notifyAll();
//	  }
  }
  
  private void getRequest()  {
//	  synchronized (game) {
//		  while (game.isLocked()) {
//			  System.out.println("Wait for it...");
//			  game.wait();
//		  }
	  		game.setLocked(true);
		  DatagramPacket packet = io.getPacket();
//		  game.setLocked(true);
//		  System.out.println("Lock");
		  parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		  game.setLocked(false);
		  game.notifyAll();
//		  game.setLocked(false);
//		  System.out.println("Unlock");
//		  game.notifyAll();
//	  }
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
    	// Convert data to Packet to use the actual meaningful data
    	Packet00Login packet = new Packet00Login(data);
    	System.out.println(address+":"+port+" has connected...");
    	
    	// Them player moi vao game
		Player newPlayer = new Player(packet.getPlayerName(), address, port);
    	game.setCurrentPlayerName(packet.getPlayerName());
		game.jetfighters.add(newPlayer);
		
		// Send response to all client
		Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
		responsePacket.writeData(this);
    	break;
    case SYNC:
    	
    	break;
    	
    case ACTION:
    	Packet02ClientAction actionPacket = new Packet02ClientAction(data);
    	System.out.println("Got an action from client " + address.getHostName() + ":" + port);
    	// SERVER SE XU LY ACTION O DAY (CAP NHAT STATE)
    	// DUNG actionPacket.getClientName(), getXDirection()....
    	game.moveJet(actionPacket.getClientName(), actionPacket.getxDirection(), actionPacket.getyDirection(), actionPacket.isMoving(), actionPacket.isShot());
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
