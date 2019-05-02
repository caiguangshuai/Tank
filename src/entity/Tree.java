package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import client.TankClient;

public class Tree extends Element {
	
	public static final int WIDTH = 30, HEIGHT = 30;
	
	private TankClient client;
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image image = toolkit.getImage(CommonWall.class.getClassLoader().getResource("images/tree.gif"));
	
	public Tree(int x, int y, TankClient client) {
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
		return null;
	}
	
}
