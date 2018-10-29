package com.bit.Nine.Client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ChatClientA extends Frame implements MouseListener,WindowListener{

	static final String IP_ADDRESS = "localhost";
//	static final String IP_ADDRESS = "192.168.0.7";
	static final int PORT = 9999;
	
	Panel p;
	Panel p_top;
	Panel center;
	Panel bottom;
	TextArea ta;
	TextArea ta2;
	Panel bottom_1;
	JButton korean;
	JButton english;
	TextField tf;
	Panel bottom_2;
	JButton submit;
	String name;
	JScrollPane jsp;
	
	
	int count=0;
	public ChatClientA(String name) {
		this.name=name;
	}
	public void ModeA() {
		//���� �̹��� 
		
		addWindowListener(this);
		p=new Panel(new BorderLayout());
		p_top=new Panel(new FlowLayout());

		ImageIcon img= new ImageIcon("Title_trans_495_60.png");
		JLabel jl1 = new JLabel(img);
		jl1.setPreferredSize(new Dimension(495,60));
		/*
		ImageIcon img= new ImageIcon("chat.png");
		JLabel jl1 = new JLabel(img);
		jl1.setPreferredSize(new Dimension(50,50));
		
		JButton jl2 = new JButton("������");
		
		jl2.setPreferredSize(new Dimension(370,50));
		jl2.setBackground(Color.GREEN);
		p_top.setBackground(Color.green);
		ImageIcon img2= new ImageIcon("nine3.png");
		JLabel jl3= new JLabel(img2);
		p_top.add(jl2);  
		p_top.add(jl3);
		 */
		p_top.add(jl1);
		p.add(p_top,BorderLayout.NORTH);
		//----------------------------------------------------
	
	
		//�ؽ�Ʈ ���� ��ȭâ
		ta= new TextArea();

		jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(330,400));
		jsp.getViewport().add(ta);
		
		center=new Panel(new FlowLayout());
	
		center.add(jsp);
		//�ؽ�Ʈ ���� �����ڸ���Ʈ
		ta2= new TextArea();
		ta2.setPreferredSize(new Dimension(150,400));
		center.add(ta2);
		
		
		//---------------------------------------------------------
		//�ѱ��� ���� ��ư 
		bottom= new Panel(new GridLayout(2,1));
		bottom_1= new Panel(new FlowLayout());
		korean= new JButton("한국어번역");
		english = new JButton("영어번역");
		english.setEnabled(false);

		//�ؽ�Ʈ �ʵ� (ä���Է�â)
		tf= new TextField();
		tf.setPreferredSize(new Dimension(370,50));
		bottom_1.add(korean);
		bottom_1.add(english);
		bottom.add(bottom_1);
	
		bottom_2= new Panel(new FlowLayout());
		bottom_2.add(tf);
		
		//���� ��
		submit = new JButton("전송");
		submit.setPreferredSize(new Dimension(100,50));
		bottom_2.add(submit);
		bottom.add(bottom_2);
		bottom.setBackground(new Color(230,230,230));
		center.add(bottom);
		
		p.add(center, BorderLayout.CENTER);
		
		add(p);
		setBounds(100,100,500,645);
		setVisible(true);
		setResizable(false);
		connect();
		
		
	}
	public void connect() {
		OutputStream out=null;
		try{
			Socket socket = new Socket(IP_ADDRESS,PORT);
			System.out.println("접속됨");
			//-------------------------------
			out =socket.getOutputStream();
			out.write((byte)'!');
			//-------------------------------
			Client_Listener cl = new Client_Listener(socket, ta, name, tf, submit, jsp, ta2,korean,english);
			p.revalidate();
			submit.addActionListener(cl);
			tf.addKeyListener(cl);
			korean.addMouseListener(cl);
			english.addMouseListener(cl);
		}catch(Exception e){
			
		}
	}
	public static void main(String[] args) {
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		dispose();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		dispose();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
		
