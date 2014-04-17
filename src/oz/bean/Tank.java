package oz.bean;

import java.awt.Point;
import java.io.Serializable;

import oz.type.DirKey;

public class Tank implements Serializable{
	
	public static final int  WIDTH = 40;
	public static final int HEIGHT = 50;
	
	public static final int M_DEGFAULT=0,M_EXIT=1;
	private int clientMessage = Tank.M_DEGFAULT;
	
	private int x;
	private int y;

	

	
	private int     id;
	private String  name;
	private boolean alive;
	private int     hp;
	
	
	
	
	public Tank(Point p, int id,String name) {

		this.x = p.x;
		this.y = p.y;
		this.id = id;
		this.name = name;

		this.clientMessage = Tank.M_DEGFAULT;
		this.alive = true;
		this.hp = 100;
	}
	
	
	public Tank() {
	}

	
	
	
	public Tank(int clientMessage, int x, int y, int id, String name,
			boolean alive, int hp) {
		super();
		this.clientMessage = clientMessage;
		this.x = x;
		this.y = y;
		this.id = id;
		this.name = name;
		this.alive = alive;
		this.hp = hp;
	}


	
	
	
	public void active(DirKey key,int speed){
		
		if( key==DirKey.Up ){
			this.y = this.y - speed;
		}
		else if( key==DirKey.Down ){
			this.y = this.y + speed;
		}
		else if( key==DirKey.Left ){
			this.x = this.x - speed;
		}
		else if( key==DirKey.Right ){
			this.x = this.x + speed;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public int getClientMessage() {
		return clientMessage;
	}


	public void setClientMessage(int clientMessage) {
		this.clientMessage = clientMessage;
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


	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	@Override
	public String toString() {
		return "Tank [clientMessage=" + clientMessage + ", x=" + x + ", y=" + y
				+ ", id=" + id + ", name=" + name + ", alive=" + alive
				+ ", hp=" + hp + "]";
	}


	
	
	
	

}
