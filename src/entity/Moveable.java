package entity;

import java.util.List;

public interface Moveable {

	public void move();
	public boolean hit(Element d);
	public boolean hitThey(List<Element> ds);
}
