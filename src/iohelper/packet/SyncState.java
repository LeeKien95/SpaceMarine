package iohelper.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import game.Enemy;
import game.Player;
import game.Projectile;

public class SyncState implements Serializable {
    private ArrayList<Projectile> projectiles, explosions;
	private ArrayList<Enemy> enemies;
	private ArrayList<Player> jetfighters;
	private Player jetfighter;
    
	
	public SyncState(ArrayList<Projectile> projectiles, ArrayList<Projectile>explosions, ArrayList<Enemy> enemies, ArrayList<Player> jetfighters, Player jetfighter) {
		this.projectiles = projectiles;
		this.explosions = explosions;
		this.enemies = enemies;
		this.jetfighters = jetfighters;
		this.jetfighter = jetfighter;
	}
	
	

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public ArrayList<Projectile> getExplosions() {
		return explosions;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Player> getJetfighters() {
		return jetfighters;
	}



	public Player getJetfighter() {
		return jetfighter;
	}
	
	
	
	
}
