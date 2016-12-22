package iohelper.packet;

import client.Client;
import game.Animation;
import server.Server;

public class Packet02ClientAction extends Packet {
	private String clientName;
	private int xDirection, yDirection;
	private boolean isMoving;
	private boolean isShot;

	// Convert the data received from the packet to the data that we can actually use 
	public Packet02ClientAction(byte[] data) {
		super(02);
		String parts[] = new String(data).split(",");
		this.clientName = parts[0];
		this.xDirection = Integer.parseInt(parts[1]);
		this.yDirection = Integer.parseInt(parts[2]);
		this.isMoving = Boolean.parseBoolean(parts[3]);
		this.isShot = Boolean.parseBoolean(parts[4]);
	}
	
	public Packet02ClientAction(String clientName, Animation action) {
		super(02);
		this.clientName = clientName;
		this.xDirection = action.getxDirection();
		this.yDirection = action.getyDirection();
		this.isMoving = action.isMoving();
		this.isShot = action.isShot;
	}

	@Override
	public byte[] getData() {
		return new String("02"+clientName+xDirection+yDirection+isMoving+isShot).getBytes();
	}

	@Override
	public void writeData(Server server) {
		
	}

	@Override
	public void writeData(Client client) {
		client.getIO().sendData(getData());
	}

	public String getClientName() {
		return clientName;
	}

	public int getxDirection() {
		return xDirection;
	}

	public int getyDirection() {
		return yDirection;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public boolean isShot() {
		return isShot;
	}
	
	

}
