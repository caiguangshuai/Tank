package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import client.TankClient;

public class CommonWall extends Element {
	
	public static final int WIDTH = 22, HEIGHT = 21;
	private boolean alive = true;
	
	private TankClient client;
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image image = toolkit.getImage(CommonWall.class.getClassLoader().getResource("images/commonWall.gif"));
	
	public CommonWall(int x, int y, TankClient client) {
		this.x = x;
		this.y = y;
		this.client = client;
	}
	
	public void draw(Graphics g) {
		if(!alive) {
			client.remove(this);
			return;
		}
		g.drawImage(image, x, y, null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
