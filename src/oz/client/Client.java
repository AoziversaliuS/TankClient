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
	public static final int SERVER_PORT = 9090;
	public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
	private static Toolkit tool = Toolkit.getDefaultToolkit();
	Image MyTank;
	Image EnemyTank;
	Image OzTank;
	
	Image imageBuffer;
	Graphics gBuffer;
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
		
	}
	
	public void logic(){
		
	}
	public void draw(){
		
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
			
//			try {
//				Thread.sleep(15);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	private static Image getImage(String imagePath){
		return tool.getImage(Client.class.getClassLoader().getResource("Image/"+imagePath));
	}
	
	
	
	
	public static void main(String[] args) {
		
		Toolkit tool = Toolkit.getDefaultToolkit();
//		tool.get
//		 Image i = tool.getImage("/Image/MyTank.png");
		Client c =new Client();
		Thread th = new Thread(c);
		th.start();
	}
	


}
