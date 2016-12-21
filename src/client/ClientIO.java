package client;

import java.net.DatagramSocket;

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
}
