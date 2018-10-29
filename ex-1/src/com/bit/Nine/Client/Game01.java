package com.bit.Nine.Client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class Game01 extends Frame implements Runnable,ActionListener{
	
	static final String IP_ADDRESS = "localhost";
//	static final String IP_ADDRESS = "192.168.0.7";
	static final int PORT = 9999;
	
	Button startButton=new Button("시작");	// �뱹 ���� ��ư
	Button stopButton=new Button("기권");		// ��� ��ư
	
	Label infoView=new Label("NINE OMOK", 1);
	
	OmokBoard board=new OmokBoard(13,24);		// ������ ��ü
	BufferedReader reader;						// �Է� ��Ʈ��
	PrintWriter writer;							// ��� ��Ʈ��
	Socket socket;								// ����
	Panel p;
	JButton submit;
	TextField tf;
	TextArea ta;
	TextArea ta2;
	JScrollPane jsp;
	String name;
	

	public Game01(String title,String name){	// ������
		super(title);
		this.name=name;
//		setLayout(null);
		
		 p=new Panel(new BorderLayout());
		
		Panel p_top=new Panel(new FlowLayout());
		ImageIcon img= new ImageIcon("Title_game_495_60.png");
		JLabel jl1 = new JLabel(img);
		jl1.setPreferredSize(new Dimension(495,60));
		
		p_top.add(jl1);
		
		Panel center=new Panel(new FlowLayout());
		Panel center_1=new Panel(new BorderLayout());
		
		//-------------------------------------------
		Panel center_11=new Panel();	// ����..
		center_11.setPreferredSize(new Dimension(340,375));
		
		infoView.setBounds(10+4,32+70,238,38);
		infoView.setBackground(new Color(200,200,255));
		board.setLocation(10+4,70+70);
		add(infoView);
		add(board);
		
		Panel p_button=new Panel();
		p_button.add(startButton); 
		p_button.add(stopButton);
		p_button.setBackground(new Color(200,200,255));
		p_button.setBounds(252,102,100,38);
		add(p_button);
		
		startButton.setEnabled(true); 
		stopButton.setEnabled(false);
		
		startButton.addActionListener(this);	// �뱹����	Button
		stopButton.addActionListener(this);		// ���		Button
//-------------------------------------------
		Panel center_12=new Panel();	// ä��â
		center_12.setPreferredSize(new Dimension(340,130));
		
		ta= new TextArea();
		ta.setPreferredSize(new Dimension(340,130));
		jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(340,130));
		jsp.getViewport().add(ta);
		center_12.add(ta);
		
		
		center_1.add(center_11,BorderLayout.NORTH);
		center_1.add(center_12,BorderLayout.SOUTH);
		
//		//---------------------------------------------
		
		Panel center_2=new Panel();	// �渮��Ʈ
		ta2= new TextArea();
		ta2.setEditable(false);
		ta2.setPreferredSize(new Dimension(134,500));
		ta2.setBackground(new Color(240,240,240));
		center_2.add(ta2);
		
		center.add(center_1);
		center.add(center_2);
		
		
//		//---------------------------------------------
		
		Panel bottom=new Panel(new FlowLayout());
		
		tf= new TextField();	// �Է�â
		tf.setBackground(Color.WHITE);
		tf.setPreferredSize(new Dimension(340,40));
		
		submit = new JButton("전송");
		submit.setPreferredSize(new Dimension(140,40));
		
		bottom.add(tf);
		bottom.add(submit);
		
		p.add(p_top,BorderLayout.NORTH);
		p.add(center, BorderLayout.CENTER);
		p.add(bottom, BorderLayout.SOUTH);
		
//		addWindowListener(this);
		
//-------------------------------------------
		add(p);
		setBounds(500,200,500,645);
		setVisible(true);
		setResizable(false);
		
		// �̺�Ʈ �����ʸ� ����Ѵ�.

		
		// ������ �ݱ� ó��
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
		connect();
		
	}	//Game01(String title){	// ������
	
	@Override
	public void actionPerformed(ActionEvent ae) {
//		System.out.println("# actionPerformed : "+ ae.getSource());
		
		if(ae.getSource()==startButton){		// �뱹 ���� ��ư
//			System.out.println("# startButton : ");
			
			writer.println("[START]");
			infoView.setText("상대의 결정을 기다립니다.");
			startButton.setEnabled(false);
		}else if(ae.getSource()==stopButton){	// ��� ��ư
			System.out.println("# stopButton : ");
			
			writer.println("[DROPGAME]");
			endGame("기권하였습니다.");
		}
	}

	@Override
	public void run() {
		String msg;			// �����κ����� �޽���
//		System.out.println("# ---run()---");
		
		try {
			while((msg=reader.readLine())!=null){
//				System.out.println("# run():"+msg);
				
				if(msg.startsWith("[STONE]")){			// ������� ���� ���� ��ǥ
					String temp=msg.substring(7);
					int x=Integer.parseInt(temp.substring(0,temp.indexOf(" ")));
					int y=Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
					board.putOpponent(x, y);	// ������� ���� �׸���.
					board.setEnable(true);		// ����ڰ� ���� ���� �� �ֵ��� �Ѵ�.
					
					System.out.println("[STONE] x:"+x+" y:"+y+" msg:"+temp);
				     
				}else if(msg.startsWith("[EXIT]")){		// �մ� ����
					System.out.println("[EXIT]");
					endGame("상대가 나갔습니다.");
					
				}else if(msg.startsWith("[DISCONNECT]")){	// �մ� ���� ����
					System.out.println("[DISCONNECT]");
					endGame("상대가 나갔습니다.");
					
				}else if(msg.startsWith("[COLOR]")){	// ���� ���� �ο��޴´�.
					String color=msg.substring(7);
					board.startGame(color);           // ������ �����Ѵ�.
					if(color.equals("BLACK")){
						infoView.setText("흑돌을 잡았습니다.");
					}else{
						infoView.setText("백돌을 잡았습니다.");
					}
					stopButton.setEnabled(true);         // ��� ��ư Ȱ��ȭ
 
					System.out.println("[COLOR] :"+color);
					
				}else if(msg.startsWith("[DROPGAME]")){	// ��밡 ����ϸ�
					System.out.println("[DROPGAME]");
					endGame("상대가 기권하였습니다");
					
				}else if(msg.startsWith("[WIN]")){		// �̰�����
					System.out.println("[WIN]");
					endGame("이겼습니다");
					
				}else if(msg.startsWith("[LOSE]")){		// ������
					System.out.println("[LOSE]");
					endGame("졌습니다.");

				}else{
					System.out.println("default:"+msg);
				}
			}	//while((msg=reader.readLine())!=null){
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("������ ������ϴ�.");
		}	//try
	}	// public void run() {
	
	private void endGame(String msg){					// ������ �����Ű�� �޼ҵ�
		System.out.println("# endGame() : "+msg);
			 
		infoView.setText(msg);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		
		try{ Thread.sleep(1000); }catch(Exception e){}	// 2�ʰ� ���
		
		if(board.isRunning()){
			board.stopGame();
		}
		startButton.setEnabled(true);
	}	// private void endGame(String msg){

	private void connect(){          // ����
		OutputStream out=null;
		try {
//			System.out.println("������ ������ ��û�մϴ�.");
			socket=new Socket(IP_ADDRESS, PORT);
//			socket=new Socket("10.10.10.25", PORT);
			Client_Listener cl= new Client_Listener(socket,ta,name,tf,submit,jsp,ta2);
			p.revalidate();
			submit.addActionListener(cl);
			tf.addKeyListener(cl);
			//-------------------------------
			out =socket.getOutputStream();
			out.write((byte)'@');
			//-------------------------------
			
			reader=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			writer=new PrintWriter(socket.getOutputStream(), true);
			new Thread(this).start();
			board.setWriter(writer);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("���� ����");
		}
//		System.out.println("���� ����.");
		
	}	//private void connect()
	
	public static void main(String[] args) {
//		Game01 client=new Game01("NINE OMOK");
//		client.setSize(436,540);
//		client.setVisible(true);
//		client.connect();
	}
	
}	// public class OmokClient extends Frame implements Runnable,ActionListener{


class OmokBoard extends Canvas{
	
	static final int BLACK=1, WHITE=2;	// ��� ���� ��Ÿ���� ���
	int[][] map;						// ������ �迭
	int size;							// size�� ������ ���� �Ǵ� ���� ����, 15�� ���Ѵ�.
	int cell;							// ������ ũ��(pixel)
	String info="";			// ������ ���� ��Ȳ�� ��Ÿ���� ���ڿ�
	int color=BLACK;		// ������� �� ���� ����
	int myColor=BLACK;		// ���� �� ����
	int youColor=BLACK;		// ����� �� ����
	
	
	boolean running=false;	// ������ ���� ���ΰ��� ��Ÿ���� ����
	PrintWriter writer;     // ������� �޽����� �����ϱ� ���� ��Ʈ��
	Graphics gbuff;			// ĵ������ ���۸� ���� �׷��Ƚ� ��ü
	Image buff;				// ���� ���۸��� ���� ����

	boolean enable=false;	// true�̸� ����ڰ� ���� ���� �� �ִ� ����
							// false�̸� ����ڰ� ���� ���� �� ���� ����
	
	public OmokBoard(int s, int c) {	// ������
		this.size=s; 
		this.cell=c;
	
		map=new int[size+2][];			// ���� ũ�⸦ ���Ѵ�.
		for (int i = 0; i < map.length; i++) {
			map[i]=new int[size+2];
		}
		
		setBackground(new Color(200,200,100));				// �������� ������ ���Ѵ�.
		setSize(size*(cell+1)+size, size*(cell+1)+size);	// �������� ũ�⸦ ����Ѵ�.
//		System.out.println("setSize x:"+(size*(cell+1)+size)+"y:"+(size*(cell+1)+size));
		

		// �������� ���콺 �̺�Ʈ ó��
		addMouseListener(new MouseAdapter(){
			 public void mousePressed(MouseEvent me){		// ���콺�� ������
				 if(!enable)return;							// ����ڰ� ���� �� ���� �����̸� ���� ���´�.
				 
				// ���콺�� ��ǥ�� map ��ǥ�� ����Ѵ�.
				int x=(int)Math.round(me.getX()/(double)cell);
				int y=(int)Math.round(me.getY()/(double)cell);
				
				// ���� ���� �� �ִ� ��ǥ�� �ƴϸ� ���� ���´�.
				if(x==0 || y==0 || x==size+1 || y==size+1)return;
				 
				// �ش� ��ǥ�� �ٸ� ���� ������ ������ ���� ���´�.
				if(map[x][y]==BLACK || map[x][y]==WHITE)return;
				
				// ������� ���� ���� ��ǥ�� �����Ѵ�.
				writer.println("[STONE]"+x+" "+y);
				
				if(myColor==BLACK){
					System.out.println("[STONE] 좌표표시 전송 ("+x+","+y+") BLACK");
				}else if(myColor==WHITE){
					System.out.println("[STONE] 좌표표시 전송 ("+x+","+y+") WHITE");
				}else{
					System.out.println("[STONE] 좌표표시 전송 ("+x+","+y+") myColor :"+myColor);
				}
				
				map[x][y]=myColor;
				
				// �̰���� �˻��Ѵ�.
				if(check(new Point(x, y), myColor)){
					info="이겼습니다.";
					writer.println("[WIN]");
				}else{
					info="상대가 두기를 기다립니다.";
				}
				repaint();		// �������� �׸���.
				
				// ����ڰ� �� �� ���� ���·� �����.
				// ������� �θ� enable�� true�� �Ǿ� ����ڰ� �� �� �ְ� �ȴ�.
				enable=false;
			 }
		});	// addMouseListener(new MouseAdapter(){
		
	}	// public OmokBoard() {		������

	public boolean isRunning(){				// ������ ���� ���¸� ��ȯ�Ѵ�.
		System.out.println("# isRunning() : "+running);
		return running; 
	}
	
	public void startGame(String col){		// ������ �����Ѵ�.
		System.out.println("# startGame() col:"+col);
		running=true;
		if(col.equals("BLACK")){			// ���� ���õǾ��� ��
			enable=true; 
			myColor=BLACK;
			youColor=WHITE;
			info="게임 시작..두세요.";
		}else{								// ���� ���õǾ��� ��
			enable=false; 
			myColor=WHITE;
			youColor=BLACK;
			info="게임 시작.. 기다리세요.";
		}
	}

	public void stopGame(){				// ������ �����.
		System.out.println("# stopGame()");
		reset();						// �������� �ʱ�ȭ�Ѵ�.
		writer.println("[STOPGAME]");	// ������� �޽����� ������.
		enable=false;
		running=false;
	}
 
	public void putOpponent(int x, int y){    // ������� ���� ���´�.
		if(youColor==BLACK){
			System.out.println("# putOpponent() 상대편돌 둠 ("+x+","+y+") BLACK");
		}else if(youColor==WHITE){
			System.out.println("# putOpponent() 상대편돌 둠 ("+x+","+y+") WHITE");
		}else{
			System.out.println("# putOpponent() 상대편돌 둠 ("+x+","+y+") 색 없음");
		}
		
		map[x][y]=youColor;
		info="상대가 두었습니다. 두세요";
		repaint();
	}

	public void setEnable(boolean enable){
		System.out.println("# setEnable() : "+ enable);
		
		this.enable=enable;
	}

	public void setWriter(PrintWriter writer){
		System.out.println("# setWriter :"+writer.toString());
		this.writer=writer;
	}
	
	public void update(Graphics g){    // repaint�� ȣ���ϸ� �ڵ����� ȣ��ȴ�.
		System.out.println("# update()");
		paint(g);               // paint�� ȣ���Ѵ�.
	}
	 
	public void paint(Graphics g){		// ȭ���� �׸���.
//		System.out.println("# paint()");
		
		if(gbuff==null){				// ���۰� ������ ���۸� �����.
			buff=createImage(getWidth(),getHeight());
			gbuff=buff.getGraphics();
		}
		drawBoard(g);  // �������� �׸���.
	} // public void paint(Graphics g){
	
	public void reset(){             // �������� �ʱ�ȭ��Ų��.
		System.out.println("# reset()");
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				map[i][j]=0;
			}
		}
		info="게임중지";
		repaint();
	}
 
	public void drawLine(){				// �����ǿ� ���� �ߴ´�.
		System.out.println("# drawLine()");
		gbuff.setColor(Color.black);
		for(int i=1; i<=size;i++){
			gbuff.drawLine(cell, i*cell, cell*size, i*cell);
			gbuff.drawLine(i*cell, cell, i*cell , cell*size);
		}
	}
	
	public void drawBlack(int x, int y){     // �� ���� (x, y)�� �׸���.
		System.out.println("# drawBlack() x:"+x+"y:"+y);
		Graphics2D gbuff=(Graphics2D)this.gbuff;
		gbuff.setColor(Color.black);
		gbuff.fillOval(x*cell-cell/2, y*cell-cell/2, cell-1, cell-1);
		gbuff.setColor(Color.white);
		gbuff.drawOval(x*cell-cell/2, y*cell-cell/2, cell-1, cell-1);
	}
	
	public void drawWhite(int x, int y){     // �� ���� (x, y)�� �׸���.
		System.out.println("# drawWhite() x:"+x+"y:"+y);
		gbuff.setColor(Color.white);
		gbuff.fillOval(x*cell-cell/2, y*cell-cell/2, cell-1, cell-1);
		gbuff.setColor(Color.black);
		gbuff.drawOval(x*cell-cell/2, y*cell-cell/2, cell-1, cell-1);
	}
 
	public void drawStones(){			// map ������ ������ ��� �׸���.
		System.out.println("# drawStones");	 
		for(int x=1; x<=size;x++){
			for(int y=1; y<=size;y++){
				if(map[x][y]==BLACK){
					System.out.println("--BLACK ["+x+","+y+"]");
				}else if(map[x][y]==WHITE){
					System.out.println("--WHITE ["+x+","+y+"]");
				}
			} 
		}
		
		for(int x=1; x<=size;x++){
			for(int y=1; y<=size;y++){
				if(map[x][y]==BLACK){
					drawBlack(x, y);
				}else if(map[x][y]==WHITE){
					drawWhite(x, y);
				}
			} 
		}
	}
	
	synchronized private void drawBoard(Graphics g){	// �������� �׸���.
		// ���ۿ� ���� �׸��� ������ �̹����� �����ǿ� �׸���.
//		System.out.println("# drawBoard");
		
		gbuff.clearRect(0, 0, getWidth(), getHeight());
		drawLine();
		drawStones();
		gbuff.setColor(Color.red);
		gbuff.drawString(info, 20, 15);
		g.drawImage(buff, 0, 0, this);
	}	// synchronized private void drawBoard(Graphics g)

	boolean check(Point p, int col){
//		System.out.println("# check()"+"x:"+p.x+"y:"+p.y+"col:"+col);
		
		if(count(p, 1, 0, col)+count(p, 1, 0, col)==4)
			return true;
		if(count(p, 0, 1, col)+count(p, 0, 1, col)==4)
			return true;
		if(count(p, 1, 1, col)+count(p, 1, 1, col)==4)
			return true;
		if(count(p, 1, 1, col)+count(p, 1, 1, col)==4)
			return true;
		return false;
	 }	// boolean check(Point p, int col){	
	
	int count(Point p, int dx, int dy, int col){
		int i=0;
		
//		System.out.println("# count()"+"x:"+p.x+"y:"+p.y+"dx:"+dx+"dy:"+dy+"col:"+col);
		
		for(; map[p.x+(i+1)*dx][p.y+(i+1)*dy]==col ;i++);
		return i;
	}	// int count(Point p, int dx, int dy, int col)

}	// class OmokBoard extends Canvas{

