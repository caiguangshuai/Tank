package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Element {

	protected int x;
	protected int y;
	
	public abstract void draw(Graphics g);
	public abstract Rectangle getRect();
}
