package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import client.TankClient;

public class MetalWall extends Element {
	
	public static final int WIDTH = 36, HEIGHT = 37;
	
	private TankClient client;
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image image = toolkit.getImage(CommonWall.class.getClassLoader().getResource("images/metalWall.gif"));
	
	public MetalWall(int x, int y, TankClient client) {
		this.x = x;
		this.y = y;
		this.client = client;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	@Override
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

}
