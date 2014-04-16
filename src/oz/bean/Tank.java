package oz.bean;

import java.awt.Point;
import java.io.Serializable;

public class Tank implements Serializable{
	
	private int    id;
	private String name;
	private Point  l;
	
	
	
	
	
	public Tank() {
	}
	public Tank(int id, String name, Point l) {
		super();
		this.id = id;
		this.name = name;
		this.l = l;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Point getL() {
		return l;
	}
	public void setL(Point l) {
		this.l = l;
	}
	
	
	@Override
	public String toString() {
		return "Tank [id=" + id + ", name=" + name + ", l=" + l + "]";
	}
	
	

}
