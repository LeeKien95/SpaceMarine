package game;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Random;

import client.Client;

public class Player implements Serializable {
	private String name;
	private String type;
	private int image;
	private int x, y;
	private String status;
	private final int step = 5;
	
	private InetAddress ipAddress;
	private int port;
	
	
	public String getUsername() {
		return name;
	}

	public void setUsername(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public int getImage() {
		return image;
	}



	public void setImage(int image) {
		this.image = image;
	}



	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public void rolling() {
		if(this.getImage() < 8) this.setImage(this.getImage() + 1);
		else {
			this.setImage(1);
		}
	}

	public void move(int xDirection, int yDirection) {
		int newX = this.getX() + xDirection*step;
		int newY = this.getY() + yDirection*step;
		if(newX > 0 && newX < 770 && newY > 0 && newY < 560) {
			this.setX(newX);
			this.setY(newY);
		}
	}
	

//	public Player(String name) {
//		super();
//		Random rand = new Random();
//		int number = rand.nextInt(1000) +1;
//		this.setName(name);
//		this.setImage(1);
//		this.setStatus("idle");
//		this.setType("1");
//		this.setX(400);
//		this.setY(500);
//	}
	
	
	
	public InetAddress getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Player(String name, InetAddress ipAddress, int port) {
//		super(ipAddress, port);
		this.ipAddress = ipAddress;
		this.port = port;
		Random rand = new Random();
		int number = rand.nextInt(1000) +1;
//		this.setName(name);
		this.name = name;
		this.setImage(1);
		this.setStatus("idle");
		this.setType("1");
		this.setX(400);
		this.setY(500);
	}
}
