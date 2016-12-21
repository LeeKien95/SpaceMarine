package iohelper.packet;

import client.Client;
import server.Server;

public class Packet00Login extends Packet {
	private String name;
	
    public Packet00Login (String name) {
    	super(00);
	    this.name = name;
    }
    
    @Override
    public byte[] getData() {
    	return ("00" + name).getBytes(); 
    }

	public void writeData(Server server) {
		server.getIO().sendDataToProperClient(getData());
	}

	public void writeData(Client client) {
		client.getIO().sendData(getData());		
	}
    
}
