package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constant;
import iohelper.SocketIO;

public class ClientIO extends SocketIO {
	
	public ClientIO () {
		super();
		try {
			this.serverSocket = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ClientIO (InetAddress ipAddress, int port) {
		super();
		try {
			this.serverSocket = new DatagramSocket();
			this.host = ipAddress;
			this.port = port;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendData(byte[] data) {
		try {
			serverSocket.send(new DatagramPacket(data, data.length, InetAddress.getByName(Constant.HOST), Constant.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		
	}
}
