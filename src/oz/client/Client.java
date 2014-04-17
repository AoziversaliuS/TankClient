package oz.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;

import oz.bean.Tank;

public class Client extends JFrame implements Runnable{
	private static Toolkit tool = Toolkit.getDefaultToolkit();
	
	public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
	public static final int SERVER_PORT = 9090;
	
	private static String ip="127.0.0.1";
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private int id;
	
	Image MyTank;
	Image EnemyTank;
	Image OzTank;
	
	Image imageBuffer;
	Graphics gBuffer;
	
	Tank tank;
	ArrayList<Tank> tanks;
	
	public Client(){
		
		MyTank = getImage("MyTank.png");
		EnemyTank = getImage("EnemyTank.png");
		OzTank = getImage("OzTank.png");
		
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize(); 
		this.setLocation(scrSize.width/2-SCREEN_WIDTH/2, scrSize.height/2-SCREEN_HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("奥茨多人坦克大战客户端");
		this.setVisible(true);
		
		try {
			socket = new Socket(ip, SERVER_PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			id = Integer.parseInt((String)ois.readObject());
			System.out.println("从服务器收到id="+id);
			//坦克初始化
//			tank = new Tank(id, "坦克["+id+"]", randomPoint());
			tank = new Tank(randomPoint(), id, "坦克["+id+"]");
			System.out.println(tank);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void logic(){
		try {
//			System.out.println(tank);
			if(tank.getX()<SCREEN_WIDTH){
				tank.setX(tank.getX()+2);
			}
			else{
				tank.setX(0);
			}
			
			oos.reset();
			oos.writeObject(tank);
			oos.flush();
			
			tanks = (ArrayList<Tank>) ois.readObject();
			System.out.println(tanks);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void draw(){
		
		if( tanks!=null ){
			for(Tank tank:tanks){
				if( tank.getId()==id ){
					gBuffer.drawImage(MyTank, tank.getX(), tank.getY(), null);
				}
				else{
					gBuffer.drawImage(EnemyTank, tank.getX(), tank.getY(), null);
				}
				
				
			}
		}
		
	}





	@Override
	public void paint(Graphics g) {
		
		
		bufferReset();

		draw();
		
		g.drawImage(imageBuffer, 0, 0, null);
	}
	
	private void bufferReset(){
		if( imageBuffer==null ){
			imageBuffer = this.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
			gBuffer = imageBuffer.getGraphics();
			System.out.println("初始化");
		}
		gBuffer.setColor(Color.yellow);
		gBuffer.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	
	
	






	@Override
	public void run() {
		while(true){
			
			logic();
			repaint();
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	private static Image getImage(String imagePath){
		return tool.getImage(Client.class.getClassLoader().getResource("Image/"+imagePath));
	}
	private static Point randomPoint(){
		Point p= new Point();
		int x = (int) (Math.random()*(SCREEN_WIDTH-Tank.WIDTH));
		int y = (int) (Math.random()*(SCREEN_HEIGHT-Tank.HEIGHT));
		p.setLocation(x, y);
		return p;
	}
	
	
	
	
	public static void main(String[] args) {
		
		Client c =new Client();
		
		Thread th = new Thread(c);
		
		th.start();
	}
	


}
