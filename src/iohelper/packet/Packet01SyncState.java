package iohelper.packet;

import client.Client;
import server.Server;

public class Packet01SyncState extends Packet {
	private SyncState state;
	
	public Packet01SyncState (SyncState syncState) {
		super(01);
		this.state = syncState;
	}
	
	public Packet01SyncState (byte[] data) {
		super(01);
		this.state = (SyncState) Packet.deserialize(data);
	}

	@Override
	public byte[] getData() {
		return Packet.serialize(this.state);
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
