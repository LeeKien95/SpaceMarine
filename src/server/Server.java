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
	private Object flag;

	public Server() {
		this.io = new ServerIO();
		this.game = new Game();
		this.flag = new Object();
		game.isServer = true;
		game.isClient = false;
		game.start();
	}
	
	

	

	
    
	public Object getFlag() {
		return flag;
	}







	public void setFlag(Object flag) {
		this.flag = flag;
	}







	@Override
	public void run() {
		// The game for all client
		

		// Continuously get request and send response to proper client (TODO
		// implement Room feature)
		// LIEN TUC GUI RESPONSE LA GAME STATE CHO TAT CA CLIENT
		// getRequest();

		ClientListen listen = new ClientListen(this);
		Thread listenThread = new Thread(listen);
		listenThread.start();

		SyncGameState syncGameState = new SyncGameState(this);
		Thread syncThread = new Thread(syncGameState);
		syncThread.start();
		
		// while (true) {
		// getRequest();
		// // sendResponse();
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

//	public void sendResponse() {
//		// Send current game state to all client (TODO send to each specific
//		// room)
////		Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
////		responsePacket.writeData(this);
//		System.out.println("sending...");
//		
//
//		 synchronized(game) {
//			
//			 System.out.println("In sync");
//			 while (game.isLocked()) {
//				 System.out.println("Wait for it...");
//				 try {
//					game.wait();
//				 } catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				 }
//			 }
//			
////			 Packet01SyncState responsePacket = new
////			 Packet01SyncState(game.composeState());
////			 responsePacket.writeData(this);
////				System.out.println("state of send" + this.getState());
//
//			 
//			 Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
//			 responsePacket.writeData(this);
//			 
//		 }
//		 
//		 System.out.println(Thread.currentThread().getState());
//	}
//
//	private void getRequest() throws InterruptedException {
//		// Get the packets and parse them
//		synchronized (game) {
//			 System.out.println("In sync");
//
//			while (game.isLocked()) {
//				System.out.println("Wait for it...");
//				game.wait();
//			}
//			
//			System.out.println("In sync");
//
//			DatagramPacket packet = io.getPacket();
//			// System.out.println("got a request");
//
//			game.setLocked(true);
//			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			game.setLocked(false);
//			
//			System.out.println("Still run getRequest");
//		}
//	}

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
			System.out.println(address + ":" + port + " has connected...");

			// Them player moi vao game
			Player newPlayer = new Player(packet.getPlayerName(), address, port);
			game.setCurrentPlayerName(packet.getPlayerName());
			game.jetfighters.add(newPlayer);

//			// Send response to all client
//			Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
//			responsePacket.writeData(this);
		case SYNC:

			break;

		case ACTION:
			Packet02ClientAction actionPacket = new Packet02ClientAction(data);
			// System.out.println("Got an action from client " +
			// address.getHostName() + ":" + port);
			// SERVER SE XU LY ACTION O DAY (CAP NHAT STATE)
			// DUNG actionPacket.getClientName(), getXDirection()....
//			System.out.println("received " + actionPacket.isShot());
			game.moveJet(actionPacket.getClientName(), actionPacket.getxDirection(), actionPacket.getyDirection(),
					actionPacket.isMoving(), actionPacket.isShot());
			break;

		case DISCONNECT:
			break;
		}
	}

	public Game getGame() {
		return this.game;
	}

	public ServerIO getIO() {
		return io;
	}

	public void sendDataToProperClient(byte[] data) {
		for (Player p : game.getPlayers()) {
			// System.out.println("Sent to" + p.getPort());
			io.sendData(data, p.getIpAddress(), p.getPort());
		}
	}
}
