package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import client.TankClient;

public class Tank extends Element implements Moveable{

	public static final int WIDTH = 35, HEIGHT = 35;
	public static int YSPEED = 15, XSPEED = 15;
	
	private int oldx, oldy;
	private Direction dir;
	private Direction pdir;
	private Group group;
	private boolean alive = true;
	
	private boolean up, down, left, right;
	private TankClient client;
	private Random random = new Random();
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image[] images;
	private static Map<String, Image> imgs = new HashMap<>();
	
	static {
		images = new Image[] {
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				toolkit.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif"))
		};
		imgs.put("up", images[0]);
		imgs.put("down", images[1]);
		imgs.put("left", images[2]);
		imgs.put("right", images[3]);
	}
	
	public Tank(int x, int y, Group group, Direction dir, Direction pdir, TankClient client) {
		this.x = x;
		this.y =y;
		this.group = group;
		this.dir = dir;
		this.pdir = pdir;
		this.client = client;
	}
	
	@Override
	public void draw(Graphics g) {
		if(group == Group.blue && !alive) {
			client.remove(this);
			return;
		}
		if(group == Group.red && !alive) {
			return;
		}
		switch(pdir) {
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
		oldx = x;
		oldy = y;
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
		
		if(dir != Direction.stop) {
			pdir = dir;
		}
		
		if(x < 0) x = 0;
		if(x > TankClient.WIDTH - WIDTH) x = TankClient.WIDTH - WIDTH;
		if(y < 0) y = 0;
		if(y > TankClient.HEIGHT - HEIGHT - 55) y = TankClient.HEIGHT - HEIGHT - 55;
		
		if(group == Group.blue) {
			int number = random.nextInt(30);
			switch(number) {
			case 15:
				Direction[] dirs = Direction.values();
				dir = dirs[random.nextInt(dirs.length)];
				break;
			case 20:
				openFire();
			}
		}
	}
	
	public void redirect() {
		if(up && !down && !left && !right) dir = Direction.up;
		else if(!up && down && !left && !right) dir = Direction.down;
		else if(!up && !down && left && !right) dir = Direction.left;
		else if(!up && !down && !left && right) dir = Direction.right;
		else if(!up && !down && !left && !right) dir = Direction.stop;
	}
	
	public void openFire() {
		int bx = x + WIDTH/2;
		int by = y + HEIGHT/2;
		Bullet b = new Bullet(bx, by, group, pdir, client);
		client.add(b);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
		}
		redirect();
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_F:
			if(alive) openFire();
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
		}
		redirect();
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

	public Direction getPdir() {
		return pdir;
	}

	public void setPdir(Direction pdir) {
		this.pdir = pdir;
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
	
	public void resetPosition() {
		x = oldx;
		y = oldy;
	}
	
	@Override
	public boolean hit(Element d) {
		Class c = d.getClass();
		if(c == Tank.class) {
			Tank t = (Tank) d;
			if(t != null && t != this && t.getRect().intersects(getRect())) {
				resetPosition();
				this.resetPosition();
				return true;
			}
		} else if(c == CommonWall.class || c == MetalWall.class || c == River.class) {
			if(d.getRect().intersects(getRect())) {
				resetPosition();
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
