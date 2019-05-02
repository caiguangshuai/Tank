package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import client.TankClient;

public class Explode extends Element {

	private int count = 0;
	private TankClient client;
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image[] images = new Image[] {
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),	
		toolkit.getImage(Explode.class.getClassLoader().getResource("images/10.gif")),	
	};
	
	public Explode(int x, int y, TankClient client) {
		this.x = x;
		this.y = y;
		this.client = client;
	}
	
	@Override
	public void draw(Graphics g) {
		if(count >= images.length) {
			client.remove(this);
			return;
		}
		g.drawImage(images[count], x, y, null);
		count++;
	}

	@Override
	public Rectangle getRect() {
		return null;
	}
}
