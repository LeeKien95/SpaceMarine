package iohelper.packet;

public abstract class Packet {
  public static enum PacketTypes {
	  INVALID(-1), LOGIN(00), DISCONNECT(10);
	  
	  private int packetId;
	  private PacketTypes (int packetId) {
		  this.packetId = packetId;
	  }
	  
	  public int getId(){
		  return packetId;
	  }
  }
  
  public byte packetId;
  
  public Packet (int packetId) {
	  this.packetId = (byte) packetId;
  }
  
  public abstract byte[] getData ();
  
  public abstract void writeData(Server server);
  public abstract void writeData(Client client);
}
