package oz.client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import oz.bean.Tank;

public class Client {
	public static final int SERVER_PORT = 9090;
	public static void main(String[] args) {
		try {
			Socket server = new Socket("127.0.0.1",SERVER_PORT);
			
//			ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
//			ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
//			
////			Tank tank = (Tank) 
//			
//			ArrayList<Tank> tanks = (ArrayList<Tank>) ois.readObject();
//			System.out.println(tanks);
//			
//			oos.writeObject(new Tank(2, "坦克2", new Point(10, 0)));
//			
//			oos.flush();
		
//			ois.close();
//			oos.close();
//			
//			server.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("目标主机不存在！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO流错误！");
		} 
	}

}
