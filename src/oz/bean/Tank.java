package oz.bean;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import oz.type.DirKey;

public class Tank implements Serializable{
	
	public static final int  WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public static final int FULL_HP = 40;
	public static final int M_DEGFAULT=0,M_EXIT_REQUEST=1,M_EXIT_PERMIT=2,ROUND_SWITCHING=3;
	
	public static final int DEAD_1=3,DEAD_2=4;

	public static final int GENERAL = 5,OZ_TANK=6,BLACK_TANK=7;
	
	
	
	private int id;
	private String  name;
	private int x;
	private int y;
	private DirKey lastDir = DirKey.Up;
	private boolean alive;
	private int hp;
	private int cx;
	private int cy;
	private int cwidth;
	private int cheight;
	private int count = 0;
	private boolean deadFinish=false;
	private int deadStatus=0;
	private int type = GENERAL;
	private int clientMessage = Tank.M_DEGFAULT;
	


	
	public Tank(int id, String name, int x, int y, DirKey lastDir,
			boolean alive, int hp, int cx, int cy, int cwidth, int cheight,
			int count, boolean deadFinish, int deadStatus, int type,
			int clientMessage) {
		super();
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.lastDir = lastDir;
		this.alive = alive;
		this.hp = hp;
		this.cx = cx;
		this.cy = cy;
		this.cwidth = cwidth;
		this.cheight = cheight;
		this.count = count;
		this.deadFinish = deadFinish;
		this.deadStatus = deadStatus;
		this.type = type;
		this.clientMessage = clientMessage;
	}


	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}




	public boolean isDeadFinish() {
		return deadFinish;
	}




	public Tank(Point p, int id,String name) {

		this.x = p.x;
		this.y = p.y;
		this.id = id;
		this.name = name;

		this.clientMessage = Tank.M_DEGFAULT;
		this.alive = true;
		this.hp = FULL_HP;
		this.lastDir = DirKey.Up;
	}
	
	


	public Tank() {
	}

	
	public Tank(int clientMessage, int x, int y, DirKey lastDir, int id,
			String name, boolean alive, int hp) {
		super();
		this.clientMessage = clientMessage;
		this.x = x;
		this.y = y;
		this.lastDir = lastDir;
		this.id = id;
		this.name = name;
		this.alive = alive;
		this.hp = hp;
	}
	
	public void fire(){
		final int dX = 3,dY = 3;
		if( lastDir==DirKey.Up || lastDir==DirKey.Down ){
			bullets.add(new Bullet(id,this.getCenter().x - dX, this.getCenter().y, lastDir));
		}
		else{
			bullets.add(new Bullet(id,this.getCenter().x, this.getCenter().y - dY, lastDir));
		}
			
	}



	public void active(DirKey key,int speed,int screenWidth,int screenHeight){
		if( alive ){
			final int OFFSET = 25;
			if( key!=DirKey.Else ){
				//保存坦克方向,画图时用
				lastDir = key;
			}
			
			if( key==DirKey.Up ){
				if( this.y>OFFSET ){
					this.y = this.y - speed;
				}
				
			}
			else if( key==DirKey.Down ){
				if( this.y<screenHeight-HEIGHT ){
					this.y = this.y + speed;
				}
			}
			else if( key==DirKey.Left ){
				if( this.x>0 ){
					this.x = this.x - speed;
				}
			}
			else if( key==DirKey.Right ){
				if( this.x<screenWidth-WIDTH ){
					this.x = this.x + speed;
				}
			}
		}
		else{
			final int RANGE = 3;
			final int MAX_COUNT = 25;
			if( deadStatus==DEAD_1 ){
				cx = cx - RANGE;
				cy = cy - RANGE;
				cwidth = cwidth + 2*RANGE;
				cheight = cheight + 2*RANGE;
				count++;
				if( count>=MAX_COUNT ){
					deadStatus=DEAD_2;
					count=0;
				}
			}
			else if( deadStatus==DEAD_2 ){
				cx = cx + RANGE;
				cy = cy + RANGE;
				cwidth = cwidth - 2*RANGE;
				cheight = cheight - 2*RANGE;
				count++;
				if( count>=MAX_COUNT ){
					deadStatus++;
					deadFinish = true;
				}
			}
		}

	}
	
	
	private Point getCenter(){
		return new Point(x+WIDTH/2, y+HEIGHT/2);
	}
	
	
	
	public boolean hit(ArrayList<Bullet> bullets,int damage){
		if( this.isAlive() ){
				for(Bullet b:bullets){
					//子弹活着的时候才有杀伤力
					if( b.isAlive() && b.getId()!=id ){
						if(inside(b)){
							//减HP
							if( hp>0 ){
								hp = hp - damage;
							}
							if( hp<=0 ){
								this.setAlive(false);
								cx = this.x;
								cy = this.y;
								cwidth = WIDTH;
								cheight = HEIGHT;
								deadStatus = DEAD_1;
							}
							return true;
						}
					}
			}
		}
		return false;
	}
	
	public boolean inside(Bullet b){
		
		if(b.getX()>x && b.getX()<x+WIDTH && b.getY()>y && b.getY()<y+HEIGHT ){
			return true;
		}
		return false;
		
	}


	
	
	
	


	public DirKey getLastDir() {
		return lastDir;
	}




	public void setLastDir(DirKey lastDir) {
		this.lastDir = lastDir;
	}





	
	
	
	
	
	
	
	
	
	
	
	public int getCount() {
		return count;
	}




	public int getDeadStatus() {
		return deadStatus;
	}




	public int getType() {
		return type;
	}




	public void setType(int type) {
		this.type = type;
	}




	public int getCx() {
		return cx;
	}




	public int getCy() {
		return cy;
	}




	public int getCwidth() {
		return cwidth;
	}




	public int getCheight() {
		return cheight;
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
