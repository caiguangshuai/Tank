package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.TankClient;

public class Bullet extends Element implements Moveable {

	public static final int WIDTH = 12, HEIGHT = 12;
	public static final int YSPEED = 30, XSPEED = 30;
	
	private Direction dir;
	private Group group;
	private boolean alive = true;
	
	private TankClient client;
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image[] images;
	private static Map<String, Image> imgs = new HashMap<>();
	
	static {
		images = new Image[] {
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/bulletU.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/bulletD.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/bulletL.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/bulletR.gif"))
		};
		imgs.put("up", images[0]);
		imgs.put("down", images[1]);
		imgs.put("left", images[2]);
		imgs.put("right", images[3]);
	}
	
	public Bullet(int x, int y, Group group, Direction dir, TankClient client) {
		this.x = x;
		this.y = y;
		this.group = group;
		this.dir = dir;
		this.client = client;
	}
	
	@Override
	public void draw(Graphics g) {
		if(!alive) {
			client.remove(this);
			client.add(new Explode(x, y, client));
			return;
		}
		switch(dir) {
		case up:
			g.drawImage(imgs.get("up"), x, y, null);
			break;
		case down:
			g.drawImage(imgs.get("down"), x, y, null);
			break;
		case left:
			g.drawImage(imgs.get("left"), x, y, null);
			break;
		case right:
			g.drawImage(imgs.get("right"), x, y, null);
			break;
		}
		move();
	}
	
	@Override
	public void move() {
		switch(dir) {
		case up:
			y -= YSPEED;
			break;
		case down:
			y += YSPEED;
			break;
		case left:
			x -= XSPEED;
			break;
		case right:
			x += XSPEED;
			break;
		}
		if(x < -100 || x > TankClient.WIDTH || y < -100 || y > TankClient.HEIGHT) alive = false;
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

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	@Override
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	@Override
	public boolean hit(Element d) {
		Class c = d.getClass();
		if(c == Tank.class) {
			Tank t = (Tank) d;
			if(t != null && t.isAlive() && t.getGroup() != group && t.getRect().intersects(getRect())) {
				alive = false;
				t.setAlive(false);
				return true;
			}
		} else if(c == CommonWall.class) {
			CommonWall cw = (CommonWall) d;
			if(cw.getRect().intersects(getRect())) {
				alive = false;
				cw.setAlive(false);
				return true;
			}
		} else if(c == MetalWall.class) {
			MetalWall mw = (MetalWall) d;
			if(mw.getRect().intersects(getRect())) {
				alive = false;
				return true;
			}
		} else if(c == River.class) {
			River r = (River) d;
			if(r.getRect().intersects(getRect())) {
				alive = false;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hitThey(List<Element> ds) {
		for(int i=0; i<ds.size(); i++) {
			Element d = ds.get(i);
			if(hit(d)) return true;
		}
		return false;
	}
}
