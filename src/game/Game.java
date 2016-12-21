package game;

import network.GameClient;
import network.GameServer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame implements Runnable {
	Image dbImage, backgroundImage, enemyBullet, jetBullet;
	HashMap<String, Image> jetImg, enemyImg;
	ArrayList<Projectile> projectiles, explosions;
	ArrayList<Enemy> enemies;
	ArrayList<Image> aestroids, enemyExplosion, jetExplosion;
	
	Graphics dbg;
	Animation myAnimation;
	JPanel background;
	ArrayList<Player> jetfighters;
	Player jetfighter;
	
	public Game() {
		super("Test");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		String name = JOptionPane.showInputDialog(this, "Please enter a username");
		
		jetImg = new HashMap<String, Image>();
		enemyImg = new HashMap<String, Image>();
		jetExplosion = new ArrayList<Image>();
		enemyExplosion = new ArrayList<Image>();
		aestroids = new ArrayList<Image>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Projectile>();
		jetfighters = new ArrayList<Player>();
		
		jetfighter = new Player();
		jetfighter.setName(name);
		jetfighters.add(jetfighter);
		myAnimation = new Animation();
		
		//load images
		try {
			int i;
			for(i = 1; i <= 8; i++) {
				jetImg.put("idle_" + i, ImageIO.read(new File("images/Blue/Animation/" + i + ".png")));
				enemyImg.put("idle_" + i, ImageIO.read(new File("images/Red/Enemy_animation/" + i + ".png")));
			}
			for(i = 0; i <= 16; i++) {
				jetExplosion.add(ImageIO.read(new File("images/Effects/Blue Effects/1_" + i + ".png")));
				enemyExplosion.add(ImageIO.read(new File("images/Effects/Red Explosion/1_" + i + ".png")));
			}
			
			aestroids.add(ImageIO.read(new File("images/Aestroids/aestroid_brown.png")));
			aestroids.add(ImageIO.read(new File("images/Aestroids/aestroid_dark.png")));
			aestroids.add(ImageIO.read(new File("images/Aestroids/aestroid_gay_2.png")));
			aestroids.add(ImageIO.read(new File("images/Aestroids/aestroid_gray.png")));
			
			
			backgroundImage = ImageIO.read(new File("images/background.jpg"));
			jetBullet = ImageIO.read(new File("images/Blue/bullet.png"));
			enemyBullet = ImageIO.read(new File("images/Red/bullet_red.png"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addKeyListener(myAnimation);
		setVisible(true);
	}

	
	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paintComponent(Graphics g) {
		paintBackground(g);
		paintJet(g);
		paintEnemies(g);
		paintProjectiles(g);
		paintExplosion(g);
		g.setColor(Color.green);
		
		if(myAnimation.lastKeyPressed!=null) {
			g.drawString(myAnimation.lastKeyPressed, 100, 100);
		}
	}
	
	private void paintBackground(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, this);
	}

	private void paintEnemies(Graphics g) {	
		if(enemies.size() != 0) {
			for(int i = 0; i < enemies.size(); i++) {
				g.drawImage(enemyImg.get("idle_"+ enemies.get(i).getImage()), enemies.get(i).getX(), enemies.get(i).getY(), 40,40, this);
				enemies.get(i).rolling();
			}
		}
	}

	private void paintJet(Graphics g) {
		if(jetfighters.size() != 0) {
			for(int i = 0; i < jetfighters.size(); i ++) {
				if(!jetfighters.get(i).getStatus().equals("dead")) {
					g.drawImage(jetImg.get("idle_"+ jetfighters.get(i).getImage()), jetfighters.get(i).getX(), jetfighters.get(i).getY(), 40,40, this);
					g.setColor(Color.green);
					g.drawString(jetfighters.get(i).getName(), jetfighters.get(i).getX(),  jetfighters.get(i).getY());
				}
			}
		}
//		if(!jetfighter.getStatus().equals("dead")) {
//			g.drawImage(jetImg.get("idle_"+ jetfighter.getImage()), jetfighter.getX(), jetfighter.getY(), 40,40, this);	
//		}
	}
	
	//paint bullets, aestroid.
	private void paintProjectiles(Graphics g) {
		// TODO Auto-generated method stub
		if(projectiles.size() != 0) {
			for(int i = 0; i < projectiles.size(); i++) {
				if(projectiles.get(i).type.equals("bullet")) {
					g.drawImage(jetBullet, projectiles.get(i).x, projectiles.get(i).y, 40, 40, this);
				} else {
					if(projectiles.get(i).type.equals("enemyBullet")) {
						g.drawImage(enemyBullet, projectiles.get(i).x, projectiles.get(i).y, 40, 40, this);
					} else {
						if(projectiles.get(i).type.equals("aestroid")) {
							Image tmp = aestroids.get(projectiles.get(i).image);
							g.drawImage(tmp, projectiles.get(i).x, projectiles.get(i).y, 60, 60, this);
						}
					}
				}
			}	
		}
	}
	
	//paint explosion
	public void paintExplosion(Graphics g) {
		if(explosions.size() != 0) {
			for(int i = 0; i < explosions.size(); i++) {
				if(explosions.get(i).type.equals("enemyExplosion")) {
					g.drawImage(enemyExplosion.get(explosions.get(i).image), explosions.get(i).x, explosions.get(i).y, 60, 60, this);
				} else {
					if(explosions.get(i).type.equals("jetExplosion")) {
						g.drawImage(jetExplosion.get(explosions.get(i).image), explosions.get(i).x, explosions.get(i).y, 60, 60, this);
					}
				}
				explosions.get(i).rolling();
				if(explosions.get(i).image == 16) {	
					if(explosions.get(i).type.equals("jetExplosion")) {
						if(jetfighters.get(0).getStatus().equals("dead")) {
							over();
						}
					}
					explosions.remove(i);
				}
			}
		}
	}
	
	//spam enemies
	public void spamEnemies() {
		for(int i = 0; i < 5; i++) {
			enemies.add(new Enemy());	
		}
	}
	
	//spam aestroid
	public void spamAestroids() {
		for(int i = 0; i < 2; i++) {
			Projectile aestroid = new Projectile();
			Random rand = new Random();
			aestroid.x = rand.nextInt(770) + 1;
			aestroid.y = 1;
			aestroid.image = rand.nextInt(4);
			int number = rand.nextInt(2) - 1;
			aestroid.xDirection = number;
			aestroid.yDirection = 1;
			aestroid.step = rand.nextInt(5) + 1;
			aestroid.type = "aestroid";
			projectiles.add(aestroid);
		}
	}
	
	//control enemies firearms
	public void enemyFire() {
		Random rand = new Random();
		if(enemies.size() > 3) {
			for(int i = 0; i < 2; i++) {
				Projectile bullet = new Projectile();
				bullet.xDirection = 0;
				bullet.yDirection = 1;
				int number = rand.nextInt(enemies.size());
				bullet.x = enemies.get(number).getX();
				bullet.y = enemies.get(number).getY();
				bullet.visible = true;
				bullet.type = "enemyBullet";
				projectiles.add(bullet);
			}
		}
	}

	//control fire arms
	public void armedJet() {
		if(myAnimation.isShot) {
			Projectile bullet = new Projectile();
			bullet.xDirection = 0;
			bullet.yDirection = -1;
			bullet.x = jetfighter.getX();
			bullet.y = jetfighter.getY();
			bullet.visible = true;
			bullet.type = "bullet";
			projectiles.add(bullet);
		}
	}
	
	//control jetfighter
	public void moveJet() {
		if(myAnimation.lastKeyPressed != null ) {
			if(jetfighters.size() != 0) {
				jetfighters.get(0).move(myAnimation.getxDirection(), myAnimation.getyDirection());	
			}	
		}
	}
	
	//move projectiles: bullet, aetroid..
	public void moveProjectiles() {
		if(projectiles.size()!= 0) {
			for(int i = 0; i < projectiles.size(); i++) {
				if(projectiles.get(i).visible) {
					projectiles.get(i).move();
				} else {
					projectiles.remove(i);
				}
			}
		}
	}
	
	//move enemies
	public void moveEnemies() {
		if(enemies.size() != 0) {
			for (int i = 0; i < enemies.size(); i++) {
				if(enemies.get(i).status.equals("visible")) {
					enemies.get(i).move();
				} else {
					enemies.remove(i);
				}
			}	
		}
	}
	
	//detect collision
	public boolean collision(int x1, int y1, int x2, int y2){
		double x = Math.abs(x2 - x1);
		double y = Math.abs(y2 - y1);
		double distance = Math.sqrt(x*x + y*y);
		if(distance < 40) {
			return true;
		} else {
			return false;	
		}
	}
	
	
	//check if enemy collision with player or player's bullet collision with enemies.
	public void startGameplay() {
		int i = 0;
		//check player's bullet
		for(int count = 0; count < jetfighters.size(); count ++) {
			for(i = 0; i < enemies.size(); i++) {
				for(int j = 0; j < projectiles.size(); j++) {
					//detect collision bw enemies and bullet
					if(projectiles.get(j).type.equals("bullet")) {
						if(collision(projectiles.get(j).x + 20, projectiles.get(j).y + 20, enemies.get(i).x + 20, enemies.get(i).y + 20)) {
							enemies.get(i).status = "dead";
							projectiles.get(j).visible = false;
							Projectile explosion = new Projectile();
							explosion.x = enemies.get(i).x;
							explosion.y = enemies.get(i).y;
							explosion.image = 0;
							explosion.type = "enemyExplosion";
							explosions.add(explosion);
						}
					} else {//detect collision bw player and aestroid
						if(collision(projectiles.get(j).x + 30, projectiles.get(j).y + 30, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
							Projectile explosion = new Projectile();
							explosion.x = jetfighters.get(count).getX();
							explosion.y = jetfighters.get(count).getY();
							explosion.image = 0;
							explosion.type = "jetExplosion";
							explosions.add(explosion);
							jetfighters.get(count).setStatus("dead");
						}
					}
				}
				
				//detect collision bw enemies and player
				if(collision(enemies.get(i).x + 20, enemies.get(i).y + 20, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
					enemies.get(i).status = "dead";
					Projectile explosion = new Projectile();
					explosion.x = jetfighters.get(count).getX();
					explosion.y = jetfighters.get(count).getY();
					explosion.image = 0;
					explosion.type = "jetExplosion";
					explosions.add(explosion);
					jetfighters.get(count).setStatus("dead");
				}
			}
		}
		
		
	}
	
	//dead
	public void over() {
		
		System.exit(0);
	}
	
	
	private GameClient socketClient;
	private GameServer socketServer;
	
	@Override
	public void run() {
		int count_frame = 0;
		while(true) {
//			if(jetfighters.size() == 0) break;
			startGameplay();
			repaint();
			count_frame++;
			
			moveJet();
			moveEnemies();
			moveProjectiles();
			
			if(count_frame % 7 == 0) {
				jetfighter.rolling();
				
			}
			
			if(count_frame % 5 == 0) {
				armedJet();
			}
			
			if(count_frame % 120 == 0) {
				spamEnemies();
			}
			
			if(count_frame % 200 == 0) {
				spamAestroids();
			}
			
			if(count_frame % 300 == 0) {
				enemyFire();
			}
			
			if(count_frame > 1000) count_frame = 1;
//			System.out.println(count_frame);
			String data = "" + count_frame;
			socketClient.sendData(data.getBytes());
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public synchronized void start() {
		

		socketServer = new GameServer(this);
		socketServer.start();
		socketClient = new GameClient(this, "localhost");
		socketClient.start();
		new Thread(this).start();
	}

	public static void main(String[] args) {
		Game myGame = new Game();
		myGame.start();
	}
	
}
