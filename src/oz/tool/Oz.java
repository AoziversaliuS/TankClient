package oz.tool;

import java.awt.Point;
import java.util.ArrayList;

import oz.bean.Tank;

public class Oz {
	
	public static String tankString(Tank t){
		String tankString = null;
		tankString = t.getId()+","+t.getX()+","+t.getY();
		return tankString;
	}
	public static Tank getTank(String tS){
		Tank tank = null;
		String[] data = tS.split(",");
//		System.out.println("data="+data.length);
		int id = Integer.parseInt(data[0]);
		Point p = new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
		
		tank = new Tank(p, id, "tank");
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
	public static void getTanks(String tsS,ArrayList<Tank> tanks){
		while(tanks.size()!=0){
			tanks.remove(tanks.size()-1);
		}
		
		String data[] = tsS.split(":");
		for(int i=0;i<data.length;i++){
			tanks.add(getTank(data[i]));
		}
		
	}
	
//	public static void main(String[] args) {
////		String s = tankString(new Tank(new Point(5, 5), 7, "asd"));
////		Tank t = getTank(s);
////		System.out.println("生成的坦克是:"+t.toString());
//		ArrayList<Tank> tanks = new ArrayList<Tank>();
//		tanks.add(new Tank(new Point(5, 5), 7, "asd"));
//		tanks.add(new Tank(new Point(5, 5), 7, "asd"));
//		tanks.add(new Tank(new Point(4, 4), 6, "212"));
//		tanks.add(new Tank(new Point(4, 4), 6, "212"));
//		String s = tanksString(tanks);
//		System.out.println("tanksStrin="+s);
//		ArrayList<Tank> tanks2 = new ArrayList<Tank>();
//		getTanks(s, tanks2);
//		for(Tank t:tanks2){
//			System.out.println(t);
//		}
////		System.out.println("获取到tanks2="+tanks2.size());
//	}

}
