package com.bit.Nine.Client;

import java.awt.Choice;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Client_Listener implements MouseListener,ActionListener, KeyListener {
	
	private Socket socket;
	private PrintWriter writer;
	private TextArea textarea;
	String fieldID;
	private TextField fieldCHAT;
	private JScrollPane jsp;
	private JButton btnID;
	private String name;
	private TextArea cho;
	private JButton korean;
	private JButton english;
	Receiver rThread;
	public Client_Listener(Socket socket, TextArea textarea, String fieldID, TextField fieldCHAT, JButton btnID,JScrollPane jsp,  TextArea cho,JButton korean,JButton english){
		this.socket = socket;
		this.textarea = textarea;
		this.fieldID = fieldID;
		this.fieldCHAT = fieldCHAT;
		this.btnID = btnID;
		this.jsp = jsp;
		this.cho = cho;
		this.korean=korean;
		this.english=english;
		
		System.out.println("--Client_Listener--trans--");
		try{
			writer = new PrintWriter(socket.getOutputStream());
			
			rThread = new Receiver(socket, textarea, jsp, cho,korean,english);
			korean.addMouseListener(rThread);
			english.addMouseListener(rThread);
			rThread.start();
			
			writer.println(fieldID.trim());
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Client_Listener(Socket socket, TextArea textarea, String fieldID, TextField fieldCHAT, JButton btnID,JScrollPane jsp, TextArea cho){
		this.socket = socket;
		this.textarea = textarea;
		this.fieldID = fieldID;
		this.fieldCHAT = fieldCHAT;
		this.btnID = btnID;
		this.jsp = jsp;
		this.cho = cho;
		
		System.out.println("--Client_Listener--game--");
		try{
			writer = new PrintWriter(socket.getOutputStream());
			
			rThread = new Receiver(socket, textarea, jsp, cho);
			rThread.start();
			
			writer.println(fieldID.trim());
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	//public Client_Listener
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnID & !fieldID.trim().equals("")){
			try{
				
				fieldCHAT.setEditable(true);
//				btnID.setVisible(false);
				name = fieldID.trim();
				
				writer.println(name);
				writer.flush();
			}catch(Exception e){
				System.out.println(e);
			}
			
		}
		
	}

	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == 10 & !fieldCHAT.getText().equals("")){
			try{
				writer.println(fieldCHAT.getText());
				writer.flush();
				fieldCHAT.setText("");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public void keyReleased(KeyEvent ke) {}

	public void keyTyped(KeyEvent ke) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}