package network;

import java.io.IOException;
import java.net.*;

public class UdpUnicastServer implements Runnable {

	private final int clientPort;
	
	public UdpUnicastServer(int clientPort) {
		this.clientPort = clientPort;
	}
	
	public void run() {
		try(DatagramSocket serverSocket = new DatagramSocket(8084)) {
			for(int i = 0; i < 3; i++) {
				String message = "Message number "  + i;
				DatagramPacket datagramPacket = new DatagramPacket(
							message.getBytes(),
							message.length(),
							InetAddress.getLocalHost(),
							clientPort
						);
				serverSocket.send(datagramPacket);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
