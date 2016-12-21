package client;

import java.net.DatagramPacket;

public class Client extends Thread {
  private ClientIO io;
  
  public Client () {
	  this.io = new ClientIO();
  }
  
  public ClientIO getIO() {
	  return io;
  }
  
  public void run (){
	  while (true) {
		  byte[] data = new byte[1024];
		  DatagramPacket packet = new DatagramPacket(data, data.length);
		  packet = io.getPacket();
		  System.out.println("SERVER < " + new String(packet.getData()));
	  }
  }
 
}

