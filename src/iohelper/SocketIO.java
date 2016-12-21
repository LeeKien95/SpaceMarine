package iohelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import common.Constant;
import common.Serializer;

public abstract class SocketIO {
	protected DatagramSocket serverSocket;
	// protected int port;
	protected DatagramPacket packet;
	// private byte[] buf;
	protected InetAddress host;
	protected int port;
	
	public SocketIO() {
		try {
			this.host = InetAddress.getByName(Constant.HOST);
			this.port = Constant.PORT;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public InetAddress getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}

	public DatagramPacket getPacket() {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			serverSocket.receive(packet);
		} catch (IOException e) {
			System.err.println("Error: error while recieving packet");
			return null;
		}
		return packet;
	}
    
	// trong packet se co ca thong tin ipaddress va port
	public void sendPacket(DatagramPacket packet) {
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			System.err.println("Error: error while sending packet");
		}
	}

	public void send(Object obj) {
		byte[] buf = Serializer.serialize(obj);
		DatagramPacket packet;
		packet = new DatagramPacket(buf, buf.length, host, port);
		sendPacket(packet);
	}
	
	public abstract void sendData(byte[] data);
	public abstract void sendData(byte[] data, InetAddress ipAddress, int port);

}
