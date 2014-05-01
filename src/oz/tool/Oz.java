package oz.tool;

import java.awt.Point;
import java.util.ArrayList;

import oz.bean.Bullet;
import oz.bean.Tank;
import oz.type.DirKey;

public class Oz {
	
	public static String tankString(Tank t){
		String tankString = 
							t.getId()+","
							+t.getName()+","
							+t.getX()+","
							+t.getY()+","
							+dirString(t.getLastDir())+","
							+booleanString(t.isAlive())+","
							+t.getHp()+","
							+t.getCx()+","
							+t.getCy()+","
							+t.getCwidth()+","
							+t.getCheight()+","
							+t.getCount()+","
							+booleanString(t.isDeadFinish())+","
							+t.getDeadStatus()+","
							+t.getType()+","
							+t.getClientMessage()+","
							+t.getServerMsg()+","
							+t.getRoundNum()+","
							+t.getRoundCount()+",";
		for(Bullet b:t.getBullets()){
			tankString = tankString 
					+b.getId()+"+"
					+b.getX()+"+"
					+b.getY()+"+"
					+dirString(b.getDir())+"+"
					+booleanString(b.isAlive())+"/";
		}
		return tankString;
	}
	public static Tank getTank(String tS){
		Tank tank = null;
		String[] d = tS.split(",");
		tank = new Tank(
				dI(d[0]),
				d[1],
				dI(d[2]),
				dI(d[3]),
				decodeDir(d[4]),
				decodeBoolean(d[5]),
				dI(d[6]),
				dI(d[7]),
				dI(d[8]),
				dI(d[9]),
				dI(d[10]),
				dI(d[11]),
				decodeBoolean(d[12]),
				dI(d[13]),
				dI(d[14]),
				dI(d[15]),
				dI(d[16]),
				dI(d[17]),
				dI(d[18])
				);
		if( d.length==20 ){
			String[] db = d[d.length-1].split("/");
			for(int i=0;i<db.length;i++){

				String[] bs=db[i].split("[+]");
				
				tank.getBullets().add(new Bullet(
						dI(bs[0]),
						dI(bs[1]),
						dI(bs[2]),
						decodeDir(bs[3]),
						decodeBoolean(bs[4])
						));
			}
		}
		
		return tank;
	}
	public static String tanksString(ArrayList<Tank> tanks){
		String tanksString = "";
		
		for(Tank t:tanks){
			String buf = tankString(t);
			tanksString = tanksString + buf + ":";
		}
		
		return tanksString;
	}
	public static ArrayList<Tank> getTanks(String tsS){
		ArrayList<Tank> tanks = new ArrayList<Tank>();
		
		String data[] = tsS.split(":");
		for(int i=0;i<data.length;i++){
			tanks.add(getTank(data[i]));
		}
		return tanks;
	}
	
	
	private static int booleanString(boolean b){
		if(b){
			return 1;
		}
		else{
			return 0;
		}
	}
	private static String dirString(DirKey dk){
		if(dk==DirKey.Up){
			return "up";
		}
		else if(dk==DirKey.Down){
			return "down";
		}
		else if(dk==DirKey.Left){
			return "left";
		}
		else if(dk==DirKey.Right){
			return "right";
		}
		else{
			return "else";
		}
	}
	private static DirKey decodeDir(String dirS){
		if(dirS.trim().equals("up")){
			return DirKey.Up;
		}
		else if(dirS.trim().equals("down")){
			return DirKey.Down;
		}
		else if(dirS.trim().equals("left")){
			return DirKey.Left;
		}
		else if(dirS.trim().equals("right")){
			return DirKey.Right;
		}
		else{
			return DirKey.Else;
		}
	}
	private static boolean decodeBoolean(String bString){
		int b = Integer.parseInt(bString);
		if( b==1 ){
			return true;
		}
		return false;
	}
	private static int dI(String iString){
		return Integer.parseInt(iString.trim());
	}
	
	public static void main(String[] args) {
		String s = tankString(new Tank(new Point(5, 5), 7, "asd"));
//		String s = tankString(n);
		System.out.println(s);
//		Tank t = getTank(s);
//		System.out.println("生成的坦克是:"+t.toString());
//		ArrayList<Tank> tanks = new ArrayList<Tank>();
//		tanks.add(new Tank(new Point(5, 5), 7, "asd"));
//		tanks.add(new Tank(new Point(5, 5), 7, "asd"));
//		tanks.add(new Tank(new Point(4, 4), 6, "212"));
//		tanks.add(new Tank(new Point(4, 4), 6, "212"));
//		String s = tanksString(tanks);
//		System.out.println("tanksStrin="+s);
//		ArrayList<Tank> tanks2 = new ArrayList<Tank>();
//		for(Tank t:tanks2){
//			System.out.println(t);
//		}
//		System.out.println("获取到tanks2="+tanks2.size());
	}

}
