package iohelper.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import client.Client;
import common.Serializer;
import server.Server;

public class Packet01SyncState extends Packet {
	private SyncState state;
	
	public Packet01SyncState (SyncState syncState) {
		super(01);
		this.state = syncState;
	}
	
	public Packet01SyncState (byte[] data) {
		super(01);
		
		try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));

			this.state = (SyncState) iStream.readObject();
			iStream.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			
	}

	@Override
	public byte[] getData() {
		//return ("01" + state.toString().trim()).getBytes();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = null;
		try {
			oo = new ObjectOutputStream(bStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		try {
			oo.writeObject(state);
			oo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] serializedMessage = bStream.toByteArray();
		return serializedMessage;
		
		// Serialize to a byte array

//		byte[] serializedMessage = bStream.toByteArray();
//
//		
//		return Serializer.serialize(state);
        // return ("01" + new String(serializedMessage)).getBytes();
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToProperClient(getData());
	}

	@Override
	public void writeData(Client client) {
		
	}
	
	public SyncState getState() {
		return state;
	}

}
