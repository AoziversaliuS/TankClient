package oz.bean;

import java.io.Serializable;
import java.util.ArrayList;

import oz.type.DirKey;

public class Bullet implements Serializable{
	
	//子弹即是一个点
	private int id;
	private int x;
	private int y;
	private DirKey dir;
	private boolean alive = true;
	
	
	
	
	
	
	




	public Bullet(int id, int x, int y, DirKey dir) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		
		this.alive = true;
	}








	public int getId() {
		return id;
	}








	public void setId(int id) {
		this.id = id;
	}








	public Bullet(int id, int x, int y, DirKey dir, boolean alive) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.alive = alive;
	}








	public boolean isAlive() {
		return alive;
	}




	public void setAlive(boolean alive) {
		this.alive = alive;
	}




	public Bullet() {
	}
	
	
	public void hit(ArrayList<Tank> tanks){
		for(Tank tank:tanks){
			if(tank.inside(this) && tank.getId()!=id && tank.isAlive() ){
				this.setAlive(false);
			}
		}
	}
	
	
	public void active(int speed){
		
		if( dir==DirKey.Up ){
			this.y = this.y - speed;
		}
		else if( dir==DirKey.Down ){
			this.y = this.y + speed;
		}
		else if( dir==DirKey.Left ){
			this.x = this.x - speed;
		}
		else if( dir==DirKey.Right ){
			this.x = this.x + speed;
		}
		
	}
	
	public boolean inRange(int screenWidth,int screenHeight){
		//判断子弹是否在屏幕范围内
		if(this.x<screenWidth && this.y<screenHeight && this.x>0 && this.y>0 ){
			return true;
		}
		else{
			return false;
		}
		
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
	public DirKey getDir() {
		return dir;
	}
	public void setDir(DirKey dir) {
		this.dir = dir;
	}
	
	
	

}
