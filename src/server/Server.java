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
		this.game = new Game(false, true);
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
		// LIEN TUC NHAN PACKET TU CLIENT
		ClientListen listen = new ClientListen(this);
		Thread listenThread = new Thread(listen);
		listenThread.start();
        
		// LIEN TUC GUI STATE CHO CLIENTS
		SyncGameState syncGameState = new SyncGameState(this);
		Thread syncThread = new Thread(syncGameState);
		syncThread.start();
	}

	public Game getGame() {
		return this.game;
	}

	public ServerIO getIO() {
		return io;
	}

	public void sendDataToProperClient(byte[] data) {
		for (Player p : game.getPlayers()) {
			io.sendData(data, p.getIpAddress(), p.getPort());
		}
	}
}

class ClientListen implements Runnable {
	private Server server;

	public ClientListen(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			// WAIT FOR A PACKET FROM CLIENT
			DatagramPacket packet = server.getIO().getPacket();
			
			// THEN SYNCHRONIZE THE GAME
			synchronized (this.server.getGame()) {
				while (this.server.getGame().isLocked()) {
					System.out.println("Wait for it...");
					try {
						this.server.getGame().wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.server.getGame().notifyAll();
				parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
				this.server.getGame().notifyAll();
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
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
			server.getGame().setCurrentPlayerName(packet.getPlayerName());
			server.getGame().jetfighters.add(newPlayer);
		case SYNC:

			break;

		case ACTION:
			// DEAL WITH AN ACTION FROM CLIENT
			Packet02ClientAction actionPacket = new Packet02ClientAction(data);
			server.getGame().moveJet(actionPacket.getClientName(), actionPacket.getxDirection(), actionPacket.getyDirection(),
					actionPacket.isMoving(), actionPacket.isShot());
			break;

		case DISCONNECT:
			break;
		}
	}
}

class SyncGameState implements Runnable {
	private Server server;

	public SyncGameState(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			synchronized(this.server.getGame()) {
				
				 while (this.server.getGame().isLocked()) {
					 System.out.println("Wait for it...");
					 try {
						this.server.getGame().wait();
					 } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
				 }
				 
				 // SEND STATE TO ALL CLIENTS
				 Packet01SyncState responsePacket = new Packet01SyncState(this.server.getGame().composeState());
				 responsePacket.writeData(this.server);
				 
				 this.server.getGame().notifyAll();
			 }
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}