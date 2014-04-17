package oz.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;

import oz.bean.Tank;
import oz.type.DirKey;

public class Client extends JFrame implements Runnable,KeyListener,WindowListener{
	private static Toolkit tool = Toolkit.getDefaultToolkit();
	
	public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
	public static final int SERVER_PORT = 9090;
	
	private static String ip="127.0.0.1";
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private int id;
	private boolean closed = false;
	private int message = Tank.M_DEGFAULT;
	
	//MyTank
	Image MyTank_Left;
	Image MyTank_Right;
	Image MyTank_Up;
	Image MyTank_Down;
	//EnemyTank
	Image EnemyTank_Left;
	Image EnemyTank_Right;
	Image EnemyTank_Up;
	Image EnemyTank_Down;
	//OzTank
	Image OzTank_Left;
	Image OzTank_Right;
	Image OzTank_Up;
	Image OzTank_Down;
	
	Image imageBuffer;
	Graphics gBuffer;
	
	Tank tank;
	ArrayList<Tank> tanks;
	
	private DirKey selectKey = DirKey.Else;
	private final int speed = 4;
	private boolean fire = false;
	
	public Client(){
		
		imageInit();
		
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize(); 
		this.setLocation(scrSize.width/2-SCREEN_WIDTH/2, scrSize.height/2-SCREEN_HEIGHT/2);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addKeyListener(this);
		this.addWindowListener(this);
		
		this.setVisible(true);
		
		try {
			socket = new Socket(ip, SERVER_PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			id = Integer.parseInt((String)ois.readObject());
			System.out.println("从服务器收到id="+id);
			this.setTitle("[奥茨制作]多人坦克大战客户端 ["+id+"]");
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
	
	
	private void imageInit(){
		//MyTank
		MyTank_Left  = getImage("MyTank_Left.png");
		MyTank_Right = getImage("MyTank_Right.png");
		MyTank_Up    = getImage("MyTank_Up.png");
		MyTank_Down  = getImage("MyTank_Down.png");
		//EnemyTank
		EnemyTank_Left  = getImage("EnemyTank_Left.png");
		EnemyTank_Right = getImage("EnemyTank_Right.png");
		EnemyTank_Up    = getImage("EnemyTank_Up.png");
		EnemyTank_Down  = getImage("EnemyTank_Down.png");
		//OzTank 
		OzTank_Left  = getImage("OzTank_Left.png");
		OzTank_Right = getImage("OzTank_Right.png");
		OzTank_Up    = getImage("OzTank_Up.png"); 
		OzTank_Down  = getImage("OzTank_Down.png");
	}
	
	
	
	public void logic(){
		
		
		tank.active(selectKey, speed);
		
		//保存要发给服务器的信息
		tank.setClientMessage(message);
		
		
		
		if(!closed){
//			System.out.println("还在");
			try {
				
				//发送自己的坦克信息给服务器
					oos.reset();
					oos.writeObject(tank);
					oos.flush();
					//获取服务器数据
					tanks = (ArrayList<Tank>) ois.readObject();
					
					
			} catch (IOException e) {
				System.exit(0);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void draw(){
		
		if( tanks!=null ){
			for(Tank tank:tanks){
				if( tank.getId()==id ){
					//根据方向来画对应的图片  玩家
					if( tank.getLastDir()==DirKey.Up ){
						gBuffer.drawImage(MyTank_Up, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Left ){
						gBuffer.drawImage(MyTank_Left, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Right ){
						gBuffer.drawImage(MyTank_Right, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Down ){
						gBuffer.drawImage(MyTank_Down, tank.getX(), tank.getY(), null);
					}
					//若服务器允许此客户端退出，则关闭与服务器的链接并退出
					if( tank.getClientMessage()==Tank.M_EXIT_PERMIT ){
						try {
							oos.close();
							ois.close();
							socket.close();
							closed = true;
							System.exit(0);
							System.out.println("退出了！！！！");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					//根据方向来画对应的图片 敌人
					if( tank.getLastDir()==DirKey.Up ){
						gBuffer.drawImage(EnemyTank_Up, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Left ){
						gBuffer.drawImage(EnemyTank_Left, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Right ){
						gBuffer.drawImage(EnemyTank_Right, tank.getX(), tank.getY(), null);
					}
					else if( tank.getLastDir()==DirKey.Down ){
						gBuffer.drawImage(EnemyTank_Down, tank.getX(), tank.getY(), null);
					}
				}
				
				
			}
		}
		
	}





	

	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if( e.getKeyCode()==KeyEvent.VK_UP ){
			selectKey = DirKey.Up;
		}
		else if( e.getKeyCode()==KeyEvent.VK_LEFT ){
			selectKey = DirKey.Left;
		}
		else if( e.getKeyCode()==KeyEvent.VK_RIGHT ){
			selectKey = DirKey.Right;
		}
		else if( e.getKeyCode()==KeyEvent.VK_DOWN ){
			selectKey = DirKey.Down;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if( e.getKeyCode()==KeyEvent.VK_UP ){
			if( selectKey==DirKey.Up ){
				selectKey = DirKey.Else;
			}
			System.out.println(selectKey);
		}
		else if( e.getKeyCode()==KeyEvent.VK_LEFT ){
			if( selectKey==DirKey.Left ){
				selectKey = DirKey.Else;
			}
			System.out.println(selectKey);
		}
		else if( e.getKeyCode()==KeyEvent.VK_RIGHT ){
			if( selectKey==DirKey.Right ){
				selectKey = DirKey.Else;
			}
			System.out.println(selectKey);
		}
		else if( e.getKeyCode()==KeyEvent.VK_DOWN ){
			if( selectKey==DirKey.Down ){
				selectKey = DirKey.Else;
			}
			System.out.println(selectKey);
		}
		else if( e.getKeyCode()==KeyEvent.VK_SPACE ){
			fire = true;
		}
		
	}
	//窗口关闭
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("按下了退出按钮");
		//设置信息为 请求退出
		message = Tank.M_EXIT_REQUEST;
		
	}






	@Override
	public void run() {
		while(!closed){
			
			logic();
			repaint();
//			System.out.println("select: "+selectKey);
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










	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	


}
