package iohelper.packet;

import client.Client;
import server.Server;

public class Packet00Login extends Packet {
	private String name;
	
    public Packet00Login (String name) {
    	super(00);
	    this.name = name;
    }
    
    public Packet00Login (byte[] data) {
    	super(00);
    	this.name = new String(data).trim().substring(2);
    }
    
    @Override
    public byte[] getData() {
    	return ("00" + name).getBytes(); 
    }

	public void writeData(Server server) {
		server.sendDataToProperClient(getData());
	}

	public void writeData(Client client) {
		client.getIO().sendData(getData());		
	}
	
	public String getPlayerName() {
		return name;
	}
}
