package network;

import java.io.IOException;
import java.net.*;

public class UdpUnicastClient implements Runnable{
	private final int port;

	public UdpUnicastClient(int port) {
		this.port = port;
	}
	
	
	@Override
	public void run() {
		try(DatagramSocket clientSocket = new DatagramSocket(port)) {
			byte[] buffer = new byte[65507];
			clientSocket.setSoTimeout(3000);
			while (true)  {
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
				clientSocket.receive(datagramPacket);
				
				String receivedMessage = new String(datagramPacket.getData());
				System.out.println(receivedMessage);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Timeout. Client is closing.");
		}
	}


	
	
}
