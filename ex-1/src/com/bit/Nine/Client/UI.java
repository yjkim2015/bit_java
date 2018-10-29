package com.bit.Nine.Client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;



public class UI extends Frame implements Runnable,WindowListener,KeyListener,MouseListener{

	public static String item=null;
	public static JTextField nick;
	int count=0;
	Socket sock = null;
	InputStream in=null;
	OutputStream out=null;
	PrintStream ps=null;
	DataInputStream dis=null;
	Panel p;
	ArrayList<String> arr= new ArrayList<String>();
	BufferedReader br = null;
	JButton jbt;
	JButton jbt3;
	
	public UI() {
		
		
		addWindowListener(this);
		p = new Panel(new BorderLayout());
//		p.setBackground(new Color(230,230,230));
		p.setBackground(Color.WHITE);
//		ImageIcon img= new ImageIcon("nine.png");
		ImageIcon img= new ImageIcon("NINE_FRONT.png");
		JLabel l1= new JLabel(img);
		Panel p2=new Panel(new GridLayout(3,1));
		p2.add(new JLabel(""));
		p2.add(l1);
		p.add(p2,BorderLayout.NORTH);
		Panel p3= new Panel(new FlowLayout());
		ImageIcon img2=new ImageIcon("game.png");
		 jbt= new JButton(img2);
		jbt.addMouseListener(this); //���� �̹��� ���콺 �̺�Ʈ
//		jbt.setBackground(new Color(230,230,230));
		jbt.setBackground(Color.WHITE);
		
		ImageIcon img4=new ImageIcon("chat.png");
		 jbt3= new JButton(img4);
		jbt3.addMouseListener(this); //���� �̹��� ���콺 �̺�Ʈ
		
//		jbt3.setBackground(new Color(230,230,230));
		jbt3.setBackground(Color.WHITE);
		jbt3.setEnabled(false);

		Panel jp= new Panel(new FlowLayout());
		jp.setPreferredSize(new Dimension(500,50));
		
		p3.add(jbt);
		p3.add(jbt3);
		p3.add(jp);
		
		
		Panel bottom= new Panel(new GridLayout(2,1));
		Panel flow_bot= new Panel(new FlowLayout());
		
		nick= new JTextField("닉네임을 입력해주세요");
		nick.setHorizontalAlignment(JTextField.CENTER);
		nick.setPreferredSize(new Dimension(200,50));
		nick.addKeyListener(this);
		
		flow_bot.add(new JLabel(""));
		flow_bot.add(nick);
		flow_bot.add(new JLabel(""));
		bottom.add(flow_bot);
		
		Panel flow_bot2=new Panel(new FlowLayout());
		JButton btn1= new JButton("Sign in");
		btn1.addMouseListener(this);
		btn1.setPreferredSize(new Dimension(200,50));
		
		flow_bot2.add(new JLabel(""));
		flow_bot2.add(btn1);
		flow_bot2.add(new JLabel(""));
		bottom.add(flow_bot2);

		p3.add(bottom);
		p.add(p3,BorderLayout.CENTER);

//			p.add(bottom,BorderLayout.SOUTH);
//			

		
		add(p);
		setBounds(100,100,500,645);
		setVisible(true);
		setResizable(false);

		

	
}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		UI me = new UI();
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(count==0) {nick.setText(""); count=1;}
		JTextField connect=(JTextField)e.getSource();
		nick.setText(connect.getText());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		if(((JButton)e.getSource()).getLabel()=="Sign in" 
			&& nick.getText()!="" && count!=0 && item!=null) 
		{
				switch(item) {
					case "유튜브":
						break;
					case "게임": 
						new Game01("NINE OMOK",nick.getText());

						break;
					case "번역":
						new ChatClientA(nick.getText()).ModeA();
						break;
				}
				dispose();
				
		}else if(((JButton)e.getSource()).getIcon().toString()=="game.png")
		{
			System.out.println("게임");
			item="게임";
			jbt.setEnabled(true);
			jbt3.setEnabled(false);
		}else if(((JButton)e.getSource()).getIcon().toString()=="chat.png")
		{
			System.out.println("번역");
			item="번역";
			jbt.setEnabled(false);
			jbt3.setEnabled(true);
		}
	
	}	// public void mouseClicked(MouseEvent e) {

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

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
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
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
