package game;

import java.util.Random;

public class Enemy {
	public int step = 5;
	public int image;
	public int x, y;
	public int xDirection, yDirection;
	public String status;
	
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
	
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getxDirection() {
		return xDirection;
	}
	public void setxDirection(int xDirection) {
		this.xDirection = xDirection;
	}
	public int getyDirection() {
		return yDirection;
	}
	public void setyDirection(int yDirection) {
		this.yDirection = yDirection;
	}
	
	public Enemy() {
		Random rand = new Random();
		setImage(1);
		int number = rand.nextInt(770) +1;
		setX(number);
		setY(1);
		setStep(rand.nextInt(7)+1);
		number = rand.nextInt(3) - 1;
		setxDirection(number);
		setyDirection(1);
		setStatus("visible");
	}
	
	public void move() {
		int newX = this.getX() + getxDirection()*step;
		int newY = this.getY() + getyDirection()*step;
		if(newX > 0 && newX < 770 && newY > 0 && newY < 560) {
			this.setX(newX);
			this.setY(newY);
		} else {
			setStatus("out");
		}
	}
	
	public void rolling() {
		if(this.getImage() < 8) this.setImage(this.getImage() + 1);
		else {
			this.setImage(1);
		}
	}
}
