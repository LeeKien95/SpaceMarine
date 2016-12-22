package game;

import java.io.Serializable;

public class Projectile implements Serializable{
	public int x, y;
	public int xDirection, yDirection;
	public boolean visible;
	public int image;
	public String type;
	public int step = 5;
	
	public Projectile() {
		xDirection = 0;
		yDirection = 0;
		visible = true;
	}
	
	public void move() {
		int newX = x + xDirection*step;
		int newY = y + yDirection*step;
		if(newX > 0 && newX < 800 && newY > 0 && newY < 560) {
			x = newX;
			y = newY;
		} else {
			visible = false;
		}
	}
	
	//roll images for explosion
	public void rolling() {
		if(this.image < 16) this.image++;
		else {
			this.image = 0;
		}
	}
}
