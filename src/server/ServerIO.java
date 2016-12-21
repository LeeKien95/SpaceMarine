package server;

import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constant;
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
}
