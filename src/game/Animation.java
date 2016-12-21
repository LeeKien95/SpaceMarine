package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Animation extends KeyAdapter{
	private int xDirection, yDirection;
	public boolean isMoving = false;
	public boolean isShot = false;
	public String lastKeyPressed, status;


	public boolean isMoving() {
		return isMoving;
	}
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public boolean isShot() {
		return isShot;
	}
	public void setShot(boolean isShot) {
		this.isShot = isShot;
	}
	public String getLastKeyPressed() {
		return lastKeyPressed;
	}
	public void setLastKeyPressed(String lastKeyPressed) {
		this.lastKeyPressed = lastKeyPressed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public void keyPressed(KeyEvent e) {
		String key = KeyEvent.getKeyText(e.getKeyCode());
		key = key.toLowerCase();

		if(key.equals("w") == true) {
			setyDirection(-1);
			setStatus("move");
			isMoving = true;
			lastKeyPressed = "up";
		}
		if(key.equals("s") == true) {
			setyDirection(1);
			setStatus("move");
			isMoving = true;
			lastKeyPressed = "down";
		}
		if(key.equals("a") == true) {
			setxDirection(-1);
			setStatus("move");
			isMoving = true;
			lastKeyPressed = "left";
		}
		if(key.equals("d") == true) {
			setxDirection(1);
			setStatus("move");
			isMoving = true;
			lastKeyPressed = "right";
		}
		if(key.equals("j") == true) {
			if(isShot == false) {
				isShot = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		String key = KeyEvent.getKeyText(e.getKeyCode());
		key = key.toLowerCase();
		if(key.equals("w")) {
			setyDirection(0);
			if((getxDirection() + getyDirection()) == 0) {
				isMoving = false;
			}
		}
		if(key.equals("s")) {
			setyDirection(0);
			if((getxDirection() + getyDirection()) == 0) {
				isMoving = false;
			}
		}
		if(key.equals("a")) {
			setxDirection(0);
			if((getxDirection() + getyDirection()) == 0) {
				isMoving = false;
			}
		}
		if(key.equals("d")) {
			setxDirection(0);
			if((getxDirection() + getyDirection()) == 0) {
				isMoving = false;
			}
		}
		if(key.equals("j")) {
			isShot = false;
		}

	}

}
