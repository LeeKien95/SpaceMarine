package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.Client;
import iohelper.packet.Packet02ClientAction;
import iohelper.packet.SyncState;
import server.Server;



public class Game extends JFrame implements Runnable, Serializable {
	public Image dbImage, backgroundImage, enemyBullet, jetBullet;
	public HashMap<String, Image> jetImg, enemyImg;
	public ArrayList<Projectile> projectiles, explosions;
	public ArrayList<Enemy> enemies;
	public ArrayList<Image> aestroids, enemyExplosion, jetExplosion;

	public Graphics dbg;
	public Animation myAnimation;
	public JPanel background;
	public ArrayList<Player> jetfighters;
	public Client client ;
	public int kill_mark;
	public int kill_count = 0;
	public int level = 1;
	public String message="";
	public int count_frame = 0, respawn= 0, messageTimeOut = 0;
	public boolean isServer = false;
	public boolean isClient = false;
    
	public String currentUsername;
	
	
	private boolean isChanged;
	
	//test
	//
	
	private Packet02ClientAction clientPacket;
	private boolean locked = false;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean isLocked) {
		this.locked = isLocked;
	}

	public Game() {
		super("Test");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

//		String name = JOptionPane.showInputDialog(this, "Please enter a username");

		jetImg = new HashMap<String, Image>();
		enemyImg = new HashMap<String, Image>();
		jetExplosion = new ArrayList<Image>();
		enemyExplosion = new ArrayList<Image>();
		aestroids = new ArrayList<Image>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Projectile>();
		jetfighters = new ArrayList<Player>();

//		jetfighter = new Player();
//		jetfighter.setName(name==null? name: "Starfighters " + new Random().nextInt(1000) +1);
//		jetfighters.add(jetfighter);
		myAnimation = new Animation();
		

		// add multiplayer to jetfighters;

		if(jetfighters.size() > 0) {
			kill_mark = level*10*jetfighters.size();
		} else {
			kill_mark = level * 10;
		}

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
		this.setChanged(false);
		this.clientPacket = new Packet02ClientAction(currentUsername, myAnimation);
		setVisible(true);
	}
	
	
	
	public Player getCurrentPlayer() {
		for (Player p : jetfighters) {
			if (p.getName().equals(currentUsername)) {
				return p;
			}
		}
		return null;
	}
	
	public void setCurrentPlayerName(String name) {
		this.currentUsername = name;
	}
    
	public ArrayList<Player> getPlayers(){
		return jetfighters;
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
			g.drawString("Kill Count: " + kill_count, 50, 50);
			if(messageTimeOut > 0) {
				g.drawString(this.message, 300, 300);
			}
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
					jetfighters.get(i).rolling();
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
					explosions.remove(i);
				}
			}
		}
	}

	//spam enemies
	public void spamEnemies() {
		for(int i = 0; i < (5 + this.level); i++) {
			enemies.add(new Enemy());
		}
	}

	//spam aestroid
	public void spamAestroids() {
		for(int i = 0; i < 3; i++) {
			Projectile aestroid = new Projectile();
			Random rand = new Random();
			aestroid.x = rand.nextInt(770) + 1;
			aestroid.y = 1;
			aestroid.image = rand.nextInt(4);
			int number = rand.nextInt(3) - 1;
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
		if(enemies.size() > this.level) {
			for(int i = 0; i < this.level; i++) {
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
	
	public Packet02ClientAction getClientPacket() {
		return clientPacket;
	}

	public void setClientPacket(Packet02ClientAction clientPacket) {
		this.clientPacket = clientPacket;
	}


	//control fire arms
	public void armedJet() {
		if(myAnimation.isShot) {
			Projectile bullet = new Projectile();
			bullet.xDirection = 0;
			bullet.yDirection = -1;
			bullet.x = getCurrentPlayer().getX();
			bullet.y = getCurrentPlayer().getY();
			bullet.visible = true;
			bullet.type = "bullet";
			projectiles.add(bullet);
// 			send to server
			Packet02ClientAction packet = new Packet02ClientAction(getCurrentPlayer().getName(), myAnimation);
			setClientPacket(packet);
			sendData(client);
//			setChanged(true);
		}
	}

	//control jetfighter
	public void moveJet() {
		if(myAnimation.lastKeyPressed != null && myAnimation.isMoving) {
			if(jetfighters.size() != 0) {
				getCurrentPlayer().move(myAnimation.getxDirection(), myAnimation.getyDirection());
			}
//			send data to server
			Packet02ClientAction packet = new Packet02ClientAction(getCurrentPlayer().getName(), myAnimation);
			setClientPacket(packet);
			sendData(client);
//			this.setChanged(true);
		} 
	}
	
	public void sendData(Client client) {
//		System.out.println("packet about to send: isShot: " + getClientPacket().isShot());
		getClientPacket().writeData(client);
	}
	
	public void moveJet(String name, int xDirection, int yDirection, boolean isMoving, boolean isShot) {
		for(Player p : jetfighters) {
			if(p.getName().equals(name)) {
				if(isShot) {
					Projectile bullet = new Projectile();
					bullet.xDirection = 0;
					bullet.yDirection = -1;
					bullet.x = p.getX();
					bullet.y = p.getY();
					bullet.visible = true;
					bullet.type = "bullet";
					projectiles.add(bullet);
				}
				if(isMoving) {
					if(!p.getStatus().equals("dead")) {
						p.move(xDirection, yDirection);
					}
				}
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
		if(distance < 30) {
			return true;
		} else {
			return false;
		}
	}

	public void levelup() {
		this.message = "Level UP!";
		this.level++;
		this.kill_mark = level*10*jetfighters.size();
		this.kill_count = 0;
		this.messageTimeOut = 300;
	}
	
	public void serverProcess() {
		int i = 0;
		int countAliveJet = 0;
		
		
		//check if game over
		for(i = 0; i < jetfighters.size(); i++) {
			if(!jetfighters.get(i).getStatus().equals("dead")) countAliveJet++;
		}
//		if(jetfighters.size() > 0 && countAliveJet == 0) over();

		//check collision bw 2 projectiles
		for(i = 0; i < projectiles.size(); i++) {
			for(int j = 0; j < projectiles.size(); j++) {
				if(i!=j && collision(projectiles.get(i).x + 20, projectiles.get(i).y + 20, projectiles.get(j).x + 20, projectiles.get(j).y + 20)) {
					if(!projectiles.get(i).type.equals(projectiles.get(j).type)) {
						if(!projectiles.get(i).type.equals("aestroid")) {
							projectiles.get(i).visible = false;
						}
						if(!projectiles.get(j).type.equals("aestroid")) {
							projectiles.get(j).visible = false;
						}
					}
				}
			}
		}

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
							kill_count ++;
						}
					} else {//detect collision bw player and aestroid
						if(respawn == 0 && collision(projectiles.get(j).x + 30, projectiles.get(j).y + 30, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
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
				if(respawn == 0 && collision(enemies.get(i).x + 20, enemies.get(i).y + 20, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
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
		
		
		if(kill_count >= kill_mark) {
			levelup();
		}

		if(respawn > 0) respawn--;
		if(messageTimeOut > 0) messageTimeOut--;
		
//		run() 's work
//		count_frame++;  
		
//		moveJet();
		moveEnemies();
		moveProjectiles();

//		if(count_frame % 5 == 0) {
//			armedJet();
//		}

		if(count_frame % 120 == 0) {
			spamEnemies();
		}

		if(count_frame % 200 == 0) {
			spamAestroids();
		}

		if(count_frame % 250 == 0) {
			enemyFire();
		}
		
//		run()'s work
//		if(count_frame > 1000) count_frame = 1;
//		System.out.println(count_frame);
	}
	
	
	public void clientProcess() {
		moveJet();
		if(count_frame % 5 == 0) {
			armedJet();
		}
//		System.out.println(count_frame);
	}

	//check if enemy collision with player or player's bullet collision with enemies.
	public void startGameplay() {
		int i = 0;
		int countAliveJet = 0;
		
		for(i = 0; i < jetfighters.size(); i++) {
			if(!jetfighters.get(i).getStatus().equals("dead")) countAliveJet++;
		}
		if(jetfighters.size() > 0 && countAliveJet == 0) over();


		for(i = 0; i < projectiles.size(); i++) {
			for(int j = 0; j < projectiles.size(); j++) {
				if(i!=j && collision(projectiles.get(i).x + 20, projectiles.get(i).y + 20, projectiles.get(j).x + 20, projectiles.get(j).y + 20)) {
					if(!projectiles.get(i).type.equals(projectiles.get(j).type)) {
						if(!projectiles.get(i).type.equals("aestroid")) {
							projectiles.get(i).visible = false;
						}
						if(!projectiles.get(j).type.equals("aestroid")) {
							projectiles.get(j).visible = false;
						}
					}
				}
			}
		}

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
							kill_count ++;
						}
					} else {//detect collision bw player and aestroid
						if(respawn == 0 && collision(projectiles.get(j).x + 30, projectiles.get(j).y + 30, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
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
				if(respawn == 0 && collision(enemies.get(i).x + 20, enemies.get(i).y + 20, jetfighters.get(count).getX() + 20, jetfighters.get(count).getY() + 20)) {
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
		
		if(kill_count >= kill_mark) {
			levelup();
		}

		if(respawn > 0) respawn--;
		if(messageTimeOut > 0) messageTimeOut--;
		count_frame++;
		
		moveJet();
		moveEnemies();
		moveProjectiles();

		if(count_frame % 7 == 0) {
			for(Player p : jetfighters) {
				p.rolling();
			}
		}

		if(count_frame % 5 == 0) {
			armedJet();
		}

		if(count_frame % 200 == 0) {
			spamEnemies();
		}

		if(count_frame % 200 == 0) {
			spamAestroids();
		}

		if(count_frame % 250 == 0) {
			enemyFire();
		}
		if(count_frame > 1000) count_frame = 1;

	}

	//dead
	public void over() {
		String message = "I'm the one with the force, the force is with me..";
		messageTimeOut = 120;
		int option = JOptionPane.showConfirmDialog(null, message, message, JOptionPane.YES_NO_OPTION);
		if(option == 0) {
			for(int i = 0; i < jetfighters.size(); i ++) {
				jetfighters.get(i).setStatus("alive");
				myAnimation.isMoving = false;
				myAnimation.isShot = false;
			}
			respawn = 120;
			level = 1;
		} else {
			System.exit(0);
		}
		
	}

	@Override
	public void run() {
		while(true) {
			count_frame ++;
//			startGameplay();
			if(isServer) serverProcess();
			
			if(isClient) {
				clientProcess();
				repaint();
			}	
			
			if(count_frame > 1000) count_frame = 1;
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void start() {
		new Thread(this).start();
	}

	public static void main(String[] args) {
		String message = "Do you want to run the server?";
		int option = JOptionPane.showConfirmDialog(null, message, message, JOptionPane.YES_NO_OPTION);
		if(option == 0) {
			Server server = new Server();
//			server.start();
		}
//		Client client = new Client();
//		client.start();
	}

	public SyncState composeState() {
		return new SyncState(new ArrayList(projectiles), new ArrayList(explosions), new ArrayList(enemies), new ArrayList(jetfighters), currentUsername);
	}
	
	public void decomposeState(SyncState state) {
		this.projectiles = state.getProjectiles();
		this.explosions = state.getExplosions();
		this.enemies = state.getEnemies();
		this.jetfighters = state.getJetfighters();
		this.currentUsername = state.getCurrentUsername();
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
}
