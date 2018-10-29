package com.bit.Nine.Server;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	static final int PORT = 9999;
	
	private JFrame jframe;
	private Container con;
	private JPanel jplTextarea;
	private JPanel jplChoice;
	private Choice choList;
	private JTextArea textarea;
	private JPanel jplList;
	private JScrollPane jspTextarea;
	
	private BManager bMan=new BManager();  // Game01: �޽��� �����
	private Random rnd= new Random();  // ��� ���� ���Ƿ� ���ϱ� ���� ����
	
	public void drawUI(){
		jframe = new JFrame("Server");
		con = jframe.getContentPane();
		jplTextarea = new JPanel();
		jplChoice = new JPanel();
		choList = new Choice();
		jplChoice.setLayout(new GridLayout(1,1));
		jplChoice.add(choList);
		textarea = new JTextArea();
		jplList = new JPanel();
		jplList.setLayout(new BorderLayout(1,1));
		jspTextarea = new JScrollPane();
		jspTextarea.setPreferredSize(new Dimension(300,400));
		jspTextarea.getViewport().add(textarea);
		jplTextarea.add(jspTextarea);
		con.add(jplTextarea,"North");con.add(jplList);con.add(jplChoice,"South");
		textarea.setEditable(false);
		jframe.setLocation(200,200);
		jframe.pack();
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	// public void drawUI(){
	
	public void connect(){
		InputStream in=null;
		int su=0;
		try{
			ServerSocket serverSocket = new ServerSocket(PORT);
			textarea.append("서버에 접속하였습니다.\n");
			jspTextarea.getVerticalScrollBar().setValue(jspTextarea.getVerticalScrollBar().getMaximum());
			jspTextarea.validate();
			
			while(true){
				Socket socket = serverSocket.accept();
//				//-------------------------
				in=socket.getInputStream();
				while (true) {
					su=in.read();
					if((su=='!')||(su=='@')) break;	
				}//while
//				//-------------------------
				
				if(su=='@'){
					System.out.println("[START] Game---");
					Omok_Thread ot=new Omok_Thread(socket);
					ot.start();
					bMan.add(ot);
				
				}else{
					System.out.println("[START] Trans---");
					Thread acc = new Accept(socket);
					acc.start();
				}
				
			}	// while
			
		}catch(Exception e){
			System.out.println(e);
		}	// try
	}	// public void connect(){

	public static void main(String [] arg){

		Server s = new Server();
		s.drawUI();
		s.connect();

	}	// public static void main(String [] arg){
	
	////////////////////////////////////////////////////////////
	
	// Ŭ���̾�Ʈ�� ����ϴ� ������ Ŭ����
		  class Omok_Thread extends Thread{
		    private int roomNumber= 1;        // �� ��ȣ
		    private String userName=null;       // ����� �̸�
		    private Socket socket;              // ����
		 
		    // ���� �غ� ����, true�̸� ������ ������ �غ� �Ǿ����� �ǹ��Ѵ�.
		    private boolean ready=false;
		 
		    private BufferedReader reader;     // �Է� ��Ʈ��
		    private PrintWriter writer;           // ��� ��Ʈ��
		    Omok_Thread(Socket socket){     // ������
		      this.socket=socket;
		      
		    }
		    Socket getSocket(){               // ������ ��ȯ�Ѵ�.
		      return socket; 
		    }
		    int getRoomNumber(){             // �� ��ȣ�� ��ȯ�Ѵ�.
		      return roomNumber;
		    }
		    String getUserName(){             // ����� �̸��� ��ȯ�Ѵ�.
		      return userName;
		    }
		    boolean isReady(){                 // �غ� ���¸� ��ȯ�Ѵ�.
		      return ready;
		    }
		    public void run(){
		      try{
		        reader=new BufferedReader(
		                            new InputStreamReader(socket.getInputStream()));
		        writer=new PrintWriter(socket.getOutputStream(), true);
		 
		        String msg;                     // Ŭ���̾�Ʈ�� �޽���
		 
		        while((msg=reader.readLine())!=null){
		 
		          // msg�� "[NAME]"���� ���۵Ǵ� �޽����̸�
		        	
		        	System.out.println("--- run() :"+msg);
		          if(msg.startsWith("[NAME]")){
		            userName=msg.substring(6);          // userName�� ���Ѵ�.
		          }
		 
		          // msg�� "[ROOM]"���� ���۵Ǹ� �� ��ȣ�� ���Ѵ�.
		          else if(msg.startsWith("[ROOM]")){
		            int roomNum=Integer.parseInt(msg.substring(6));
		            if( !bMan.isFull(roomNum)){             // ���� �� ���°� �ƴϸ�
		 
		              // ���� ���� �ٸ� ��뿡�� ������� ������ �˸���.
		              if(roomNumber!= 1)
		                bMan.sendToOthers(this, "[EXIT]"+userName);
		 
		              // ������� �� �� ��ȣ�� �����Ѵ�.
		              roomNumber=roomNum;
		 
		              // ����ڿ��� �޽����� �״�� �����Ͽ� ������ �� ������ �˸���.
		              writer.println(msg);
		 
		              // ����ڿ��� �� �濡 �ִ� ����� �̸� ����Ʈ�� �����Ѵ�.
		              writer.println(bMan.getNamesInRoom(roomNumber));
		 
		              // �� �濡 �ִ� �ٸ� ����ڿ��� ������� ������ �˸���.
		              bMan.sendToOthers(this, "[ENTER]"+userName);
		            }
		            else writer.println("[FULL]");        // ����ڿ� ���� á���� �˸���.
		          }
		 
		          // "[STONE]" �޽����� ������� �����Ѵ�.
		          else if(roomNumber>=1 && msg.startsWith("[STONE]"))
		            bMan.sendToOthers(this, msg);
		 
		          // ��ȭ �޽����� �濡 �����Ѵ�.
		          else if(msg.startsWith("[MSG]"))
		            bMan.sendToRoom(roomNumber,
		                              "["+userName+"]: "+msg.substring(5));
		 
		          // "[START]" �޽����̸�
		          else if(msg.startsWith("[START]")){
		            ready=true;   // ������ ������ �غ� �Ǿ���.
		            
		            // �ٸ� ����ڵ� ������ ������ �غ� �Ǿ�����
		            if(bMan.isReady(roomNumber)){
		 
		              // ��� ���� ���ϰ� ����ڿ� ������� �����Ѵ�.
		              int a=rnd.nextInt(2);
		              if(a==0){
		                writer.println("[COLOR]BLACK");
		                bMan.sendToOthers(this, "[COLOR]WHITE");
		              }
		              else{
		                writer.println("[COLOR]WHITE");
		                bMan.sendToOthers(this, "[COLOR]BLACK");
		              }
		            }
		          }	// [START]
		 
		          // ����ڰ� ������ �����ϴ� �޽����� ������
		          else if(msg.startsWith("[STOPGAME]"))
		            ready=false;
		 
		          // ����ڰ� ������ ����ϴ� �޽����� ������
		          else if(msg.startsWith("[DROPGAME]")){
		            ready=false;
		            // ������� ������� ����� �˸���.
		            bMan.sendToOthers(this, "[DROPGAME]");
		          }
		 
		          // ����ڰ� �̰�ٴ� �޽����� ������
		          else if(msg.startsWith("[WIN]")){
		            ready=false;
		            // ����ڿ��� �޽����� ������.
		            writer.println("[WIN]");
		 
		            // ������� ������ �˸���.
		            bMan.sendToOthers(this, "[LOSE]");
		          }  // if
		        }	// while
		      }catch(Exception e){
		      }finally{
		        try{
		          bMan.remove(this);
		          if(reader!=null) reader.close();
		          if(writer!=null) writer.close();
		          if(socket!=null) socket.close();
		          reader=null; writer=null; socket=null;
		          System.out.println(userName+"���� ������ �������ϴ�.");
		          System.out.println("������ ��: "+bMan.size());
		          // ����ڰ� ������ �������� ���� �濡 �˸���.
		          bMan.sendToRoom(roomNumber,"[DISCONNECT]"+userName);
		        }catch(Exception e){}
		      }	// try
		    }	// public void run(){
		  }		// class Omok_Thread extends Thread{
		  
		  class BManager extends Vector{       // �޽����� �����ϴ� Ŭ����
			 
		    BManager(){
		    	System.out.println("#@@ --BManager Start--");
		    }
		    
		    void add(Omok_Thread ot){           // �����带 �߰��Ѵ�.
		    	System.out.println("# --add Omok_Thread--");
		      super.add(ot);
		    }
		    
		    void remove(Omok_Thread ot){        // �����带 �����Ѵ�.
		    	System.out.println("# --remove Omok_Thread--");
		       super.remove(ot);
		    }
		    
		    Omok_Thread getOT(int i){            // i��° �����带 ��ȯ�Ѵ�.
		    	System.out.println("# --getOT i:"+i);
		      return (Omok_Thread)elementAt(i);
		    }
		    Socket getSocket(int i){              // i��° �������� ������ ��ȯ�Ѵ�.
		    	System.out.println("# --getSocket i:"+i);
		      return getOT(i).getSocket();
		    }
		 
		    // i��° ������� ����� Ŭ���̾�Ʈ���� �޽����� �����Ѵ�.
		    void sendTo(int i, String msg){
		    	System.out.println("# --sendTo i:"+i+" msg:"+msg);
		      try{
		        PrintWriter pw= new PrintWriter(getSocket(i).getOutputStream(), true);
		        pw.println(msg);
		      }catch(Exception e){}  
		    }
		    
		    int getRoomNumber(int i){            // i��° �������� �� ��ȣ�� ��ȯ�Ѵ�.
		    	System.out.println("# getRoomNumber() i:"+i);
		      return getOT(i).getRoomNumber();
		    }
		    
		    synchronized boolean isFull(int roomNum){    // ���� á���� �˾ƺ���.
		    	System.out.println("# isFull() roomNum:"+roomNum);
		      if(roomNum==0)return false;                 // ������ ���� �ʴ´�.
		 
		      // �ٸ� ���� 2�� �̻� ������ �� ����.
		      int count=0;
		      for(int i=0;i<size();i++)
		        if(roomNum==getRoomNumber(i))count++;
		      if(count>=2)return true;
		      return false;
		    }
		 
		    // roomNum �濡 msg�� �����Ѵ�.
		    void sendToRoom(int roomNum, String msg){
		      System.out.println("# sendToRoom() roomNum:"+roomNum+" msg:"+msg);
		      
		      for(int i=0;i<size();i++)
		        if(roomNum==getRoomNumber(i))
		          sendTo(i, msg);
		    }
		 
		    // ot�� ���� �濡 �ִ� �ٸ� ����ڿ��� msg�� �����Ѵ�.
		    void sendToOthers(Omok_Thread ot, String msg){
		    System.out.println("# sendToOthers() roomNum:"+ot.getRoomNumber()+" msg:"+msg);
		      for(int i=0;i<size();i++)
		        if(getRoomNumber(i)==ot.getRoomNumber() && getOT(i)!=ot)
		          sendTo(i, msg);
		    }
		 
		    // ������ ������ �غ� �Ǿ��°��� ��ȯ�Ѵ�.
		    // �� ���� ����� ��� �غ�� �����̸� true�� ��ȯ�Ѵ�.
		    synchronized boolean isReady(int roomNum){
		      int count=0;
		      System.out.println("# isReady() roomNum:"+roomNum);
		      
		      for(int i=0;i<size();i++)
		        if(roomNum==getRoomNumber(i) && getOT(i).isReady())
		          count++;
		      if(count==2)return true;
		      return false;
		    }
		 
		    // roomNum�濡 �ִ� ����ڵ��� �̸��� ��ȯ�Ѵ�.
		    String getNamesInRoom(int roomNum){
		    	System.out.println("# getNamesInRoom() roomNum:"+roomNum);
		    	
		      StringBuffer sb=new StringBuffer("[PLAYERS]");
		      for(int i=0;i<size();i++)
		        if(roomNum==getRoomNumber(i))
		          sb.append(getOT(i).getUserName()+"\t");
		      return sb.toString();
		    }
		  }	// class BManager extends Vector{

}	//public class Server {







