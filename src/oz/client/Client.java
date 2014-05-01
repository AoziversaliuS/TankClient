package oz.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import oz.bean.Bullet;
import oz.bean.Tank;
import oz.tool.Oz;
import oz.type.DirKey;

public class Client extends JFrame implements Runnable,KeyListener,WindowListener{
	private static Toolkit tool = Toolkit.getDefaultToolkit();
	
	public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
	public static  int SERVER_PORT = 9090;
	private boolean firstTime=true;
	
	private static String ip="127.0.0.1";
	
	private Socket socket;
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos;
	private PrintWriter out;
	private BufferedReader in;
	
	private int id;
	private int message = Tank.M_DEGFAULT;
	
	//BackGround
	Image BackGround;
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
	//Bullet
	Image OzBullet;
	Image EnemyBullet;
	Image MyBullet;
	
	Image imageBuffer;
	Graphics gBuffer;
	
	Tank clientTank;
	ArrayList<Tank> tanks;
	
	private DirKey selectKey = DirKey.Else;
	
	private  int tankSpeed = 2;
	private  int bulletSpeed = 6;
	private  int bulletDamage = 3;
	
	private boolean fire = false;
	
	public Client(String ip,String playerName,String tankName,int tankSpeed,int bulletSpeed,int bulletDamage){
		Client.ip = ip;
		
		
		
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
//			ois = new ObjectInputStream(socket.getInputStream());
//			oos = new ObjectOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			id = Integer.parseInt(in.readLine());
			System.out.println("从服务器收到id="+id);
			this.setTitle("[奥茨制作]多人坦克大战客户端 ["+id+"]");
			//坦克初始化
			clientTank = new Tank(randomPoint(), id,playerName);
			
			if( tankName.trim().equals("OZTANK") ){
				clientTank.setType(Tank.OZ_TANK);
			}
			this.tankSpeed = tankSpeed;
			this.bulletSpeed = bulletSpeed;
			this.bulletDamage = bulletDamage;
			
			System.out.println(clientTank);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "无法连接服务器！请确保服务器防火墙已关闭！");
			System.exit(0);
			System.out.println("链接出错！！！1");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "无法连接服务器！请确保服务器防火墙已关闭！");
			System.exit(0);
			System.out.println("链接出错！！！2");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "无法连接服务器！请确保服务器防火墙已关闭！");
			System.exit(0);
			System.out.println("链接出错！！！3");
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
		//Bullet
		OzBullet  = getImage("OzBullet.png");
		EnemyBullet  = getImage("EnemyBullet.png");
		MyBullet  = getImage("MyBullet.png");
		//BackGround
		BackGround = getImage("BackGround.png");
	}
	
	public void sendAndGet(){
		try {
			//发送自己的坦克信息给服务器
				String sendBuf = Oz.tankString(clientTank);
				out.println(sendBuf);
				out.flush();
				//获取服务器中所有玩家的坦克数据
				String recvBuf = in.readLine();
				tanks = Oz.getTanks(recvBuf);
				
		} catch (IOException e) {
			System.exit(0);
//			e.printStackTrace();
		} catch(NullPointerException e){
			System.exit(0);
		}
	}
	
	
	public void logic(){
		
		//坦克移动
		clientTank.active(selectKey, tankSpeed,SCREEN_WIDTH,SCREEN_HEIGHT);
		//开火
		if( fire==true ){
			clientTank.fire();
			fire = false;
		}
		//子弹移动
		for(Bullet b:clientTank.getBullets()){
			b.active(bulletSpeed);
		}
		//删除越界的子弹
		for(int i=0; i<clientTank.getBullets().size(); i++){
			if( !clientTank.getBullets().get(i).inRange(SCREEN_WIDTH, SCREEN_HEIGHT) ){
				clientTank.getBullets().remove(i);
				break;
			}
		}
		
		//保存要发给服务器的信息
		clientTank.setClientMessage(message);
		
		
		//发送自己的坦克数据给服务器，并从服务器拿到所有玩家的坦克数据
		sendAndGet();

		//检测自己有无被子弹打中
		for(Tank tank:tanks){
				clientTank.hit(tank.getBullets(),bulletDamage);
		}
		
		//检测自己的子弹有无打中其它玩家
		for(Bullet b:clientTank.getBullets()){
			b.hit(tanks);
		}
			
		
	}
	
	
	
	public void draw(){
		
		
		
		//画子弹
		if( tanks!=null ){
			
			for(Tank tank:tanks){
				if( tank.getId()==id && tank.getType()!=Tank.OZ_TANK ){
					for(Bullet b:tank.getBullets()){
						if( b.isAlive() ){
							gBuffer.drawImage(MyBullet, b.getX(), b.getY(), null);
						}
					}
				}
				else if( tank.getType()==Tank.OZ_TANK ){
					for(Bullet b:tank.getBullets()){
						if( b.isAlive() ){
							gBuffer.drawImage(OzBullet, b.getX(), b.getY(), null);
						}
					}
				}
				else{
					for(Bullet b:tank.getBullets()){
						if( b.isAlive() ){
							gBuffer.drawImage(EnemyBullet, b.getX(), b.getY(), null);
						}
					}
				}
				
			}
			//画坦克
			for(Tank tank:tanks){
				if( tank.isAlive() ){
					if( tank.getType()==Tank.OZ_TANK ){
						//奥兹专用坦克
						drawTank(OzTank_Up, OzTank_Down, OzTank_Left, OzTank_Right, tank);
					}
				    else if( tank.getId()==id ){
						//根据方向来画对应的图片  玩家
						drawTank(MyTank_Up, MyTank_Down, MyTank_Left, MyTank_Right, tank);
						
						
					}
					else{
						//根据方向来画对应的图片 敌人
						drawTank(EnemyTank_Up, EnemyTank_Down, EnemyTank_Left, EnemyTank_Right, tank);
					}
				    
				  //若服务器允许此客户端退出，则关闭与服务器的链接并退出
					if(tank.getId()==id && tank.getClientMessage()==Tank.M_EXIT_PERMIT ){
//						try {
//							oos.close();
//							ois.close();
//							socket.close();
							System.exit(0);
							System.out.println("退出了！！！！");
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
					}
				}
				else{
					final int dX=7,dY=7;
					
					if( !tank.isDeadFinish() ){
						gBuffer.setColor(new Color(50,205,50));
						gBuffer.fillOval(tank.getCx(), tank.getCy(), tank.getCwidth(), tank.getCheight());
						gBuffer.setColor(new Color(255,242,0));
						gBuffer.fillOval(tank.getCx()+dX, tank.getCy()+dY, tank.getCwidth()-2*dX, tank.getCheight()-2*dY);
					}
				}
			}
		}
		
	}



	public void drawTank(Image up,Image down,Image left,Image right,Tank tank){
		if( tank.getLastDir()==DirKey.Up ){
			gBuffer.drawImage(up, tank.getX(), tank.getY(), null);
		}
		else if( tank.getLastDir()==DirKey.Left ){
			gBuffer.drawImage(left, tank.getX(), tank.getY(), null);
		}
		else if( tank.getLastDir()==DirKey.Right ){
			gBuffer.drawImage(right, tank.getX(), tank.getY(), null);
		}
		else if( tank.getLastDir()==DirKey.Down ){
			gBuffer.drawImage(down, tank.getX(), tank.getY(), null);
		}
		final int dY=10;
		final int dY2=5;
		gBuffer.setColor(Color.GRAY);
		gBuffer.drawRect(tank.getX(),tank.getY()-dY, Tank.FULL_HP, 6);
		gBuffer.setFont(new Font("黑体", Font.BOLD, 10));
		gBuffer.drawString(tank.getName(), tank.getX(), tank.getY()-dY-dY2);
		if( tank.getId()==id && tank.getType()!=Tank.OZ_TANK ){
			gBuffer.setColor(new Color(0,162,232));
		}
		else if( tank.getType()==Tank.OZ_TANK ){
			gBuffer.setColor(new Color(34,177,36));
		}
		else{
			gBuffer.setColor(new Color(237,28,36));
		}
		gBuffer.fillRect(tank.getX()+1,tank.getY()-dY+1, tank.getHp()-1, 5);
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
		}
		else if( e.getKeyCode()==KeyEvent.VK_LEFT ){
			if( selectKey==DirKey.Left ){
				selectKey = DirKey.Else;
			}
		}
		else if( e.getKeyCode()==KeyEvent.VK_RIGHT ){
			if( selectKey==DirKey.Right ){
				selectKey = DirKey.Else;
			}
		}
		else if( e.getKeyCode()==KeyEvent.VK_DOWN ){
			if( selectKey==DirKey.Down ){
				selectKey = DirKey.Else;
			}
		}
		else if( e.getKeyCode()==KeyEvent.VK_SPACE ){
			if( clientTank.isAlive() ){
				fire = true;
			}
		}
		
	}
	int count=0;
	//窗口关闭
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("按下了退出按钮");
		//设置信息为 请求退出
		message = Tank.M_EXIT_REQUEST;
		count++;
		if( count>=2 ){
			System.exit(0);
		}
		
	}






	@Override
	public void run() {
		while(true){
			long start = System.currentTimeMillis();
			logic();
			long cost = System.currentTimeMillis() - start;
			repaint();
			
//			System.out.println("[客户端]逻辑耗时: "+cost);
		}
		
	}
	@Override
	public void paint(Graphics g) {
		bufferReset();
		draw();
		g.drawImage(imageBuffer, 0, 0, null);
	}
	private Color bgColor = new Color(239,228,176);
	private void bufferReset(){
		if( imageBuffer==null ){
			imageBuffer = this.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
			gBuffer = imageBuffer.getGraphics();
			System.out.println("初始化");
		}
		gBuffer.setColor(bgColor);
		gBuffer.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gBuffer.drawImage(BackGround, 0, 0, null);
		if(firstTime){
			//去除闪烁
			gBuffer.drawImage(EnemyTank_Down, 2000, 2000, null);
			gBuffer.drawImage(EnemyTank_Left, 2000, 2000, null);
			gBuffer.drawImage(EnemyTank_Right, 2000, 2000, null);
			gBuffer.drawImage(EnemyTank_Up, 2000, 2000, null);
			
			gBuffer.drawImage(MyTank_Down, 2000, 2000, null);
			gBuffer.drawImage(MyTank_Left, 2000, 2000, null);
			gBuffer.drawImage(MyTank_Right, 2000, 2000, null);
			gBuffer.drawImage(MyTank_Up, 2000, 2000, null);
			
			gBuffer.drawImage(OzTank_Down, 2000, 2000, null);
			gBuffer.drawImage(OzTank_Left, 2000, 2000, null);
			gBuffer.drawImage(OzTank_Right, 2000, 2000, null);
			gBuffer.drawImage(OzTank_Up, 2000, 2000, null);
			
			gBuffer.drawImage(EnemyBullet, 2000, 2000, null);
			gBuffer.drawImage(OzBullet, 2000, 2000, null);
			gBuffer.drawImage(MyBullet, 2000, 2000, null);
			
			firstTime = false;
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
		
		
//		String ip = JOptionPane.showInputDialog("请输入服务器ip:");
//		String name = JOptionPane.showInputDialog("请输入玩家名:");
////		new Client(ip, playerName, tankName, tankSpeed, bulletSpeed, bulletDamage)
//		Client c =new Client(ip,name,"OZTANK",4,6,3);
//		Thread th = new Thread(c);
//		th.start();
		
		
		
		
		String name=null;
		String ip = JOptionPane.showInputDialog("请输入服务器ip:");
//		SERVER_PORT = Integer.parseInt(JOptionPane.showInputDialog("请输入服务器端口:"));
		if( ip!=null ){
			 name = JOptionPane.showInputDialog("请输入玩家名:");
		}

//		new Client(ip, playerName, tankName, tankSpeed, bulletSpeed, bulletDamage)
		if( ip!=null && name!=null ){
			
		String str[] = name.split("/");
		if( str.length==5 ){
			Client c =new Client(ip,str[0],str[1],Integer.parseInt(str[2]),Integer.parseInt(str[3]),Integer.parseInt(str[4]));
			Thread th = new Thread(c);
			th.start();
		}
		else{
				Client c =new Client(ip,name,"",2,3,3);
				Thread th = new Thread(c);
				th.start();
			}
			
		}
		
		
//		
//		if( input!=null ){
//			String s[] = input.split("/");
//			System.out.println(s.length);
//			String ip,playerName;
//			
//			if( s.length==2 ){
//				ip = s[0];
//				playerName = s[1];
//				
//				Client c =new Client(ip,playerName);
//				Thread th = new Thread(c);
//				th.start();
//			}
//		}
		
//		Client c =new Client("127.0.0.1","LUXION");
//		Thread th = new Thread(c);
//		th.start();
		
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
