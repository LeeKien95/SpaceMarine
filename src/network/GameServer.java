package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.Game;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String message = new String(packet.getData());
			System.out.println("Client ["+ packet.getAddress().getHostAddress() +": "+ packet.getPort()+"] > " + message);
//			System.out.println("Server > Recived:" + new String(packet.getData()));
			String respond = "Seen "+ message;
			sendData(respond.getBytes()  , packet.getAddress(), packet.getPort());
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send((packet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
