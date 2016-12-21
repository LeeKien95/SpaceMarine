package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import iohelper.SocketIO;

public class ClientIO extends SocketIO {
	
	public ClientIO () {
		super();
		try {
			this.serverSocket = new DatagramSocket();
			this.host = serverSocket.getInetAddress();
			this.port = serverSocket.getPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendData(byte[] data) {
		try {
			serverSocket.send(new DatagramPacket(data, data.length, host, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		
	}
}
