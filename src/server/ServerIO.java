package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constant;
import game.Player;
import iohelper.SocketIO;

public class ServerIO extends SocketIO {
	public ServerIO() {
		super();
		try {
			this.serverSocket = new DatagramSocket(Constant.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendData(byte[] data) {
		
	}

	@Override
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		try {
			serverSocket.send(new DatagramPacket(data, data.length, ipAddress, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
