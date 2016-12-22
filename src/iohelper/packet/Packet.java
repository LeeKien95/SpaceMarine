package iohelper.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import client.Client;
import server.Server;

public abstract class Packet {
  public static enum PacketTypes {
	  INVALID(-1), LOGIN(00), SYNC(01), ACTION(02), DISCONNECT(10);
	  
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
  
  public static PacketTypes lookupPacket(int id) {
	  for (PacketTypes p : PacketTypes.values()) {
		  if (p.getId() == id) {
			  return p;
		  }
	  }
	  return PacketTypes.INVALID;
  }
  
  public static byte[] serialize(Object object) {
	    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = null;
		try {
			oo = new ObjectOutputStream(bStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		try {
			oo.writeObject(object);
			oo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] serializedMessage = bStream.toByteArray();
		return serializedMessage;
  }
  
  public static Object deserialize(byte[] data) {
	  Object result = null;
	  try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));

			result = (SyncState) iStream.readObject();
			iStream.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  return result;
  }
}
