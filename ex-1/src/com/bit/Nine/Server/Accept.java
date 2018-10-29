package com.bit.Nine.Server;

import java.awt.Choice;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class Accept extends Thread {
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	static List<String> nameList = Collections.synchronizedList(new ArrayList<String>());

	private PrintWriter writer;
	private Socket socket;

	
	public Accept(Socket socket){
		this.socket = socket;
		
		try{
			writer = new PrintWriter(socket.getOutputStream());
			list.add(writer);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void run(){
		String name = null;
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			name = reader.readLine();
			nameList.add(name);
//			choList.addItem(name);
			sendAll("<" + name + "님이 입장하였습니다.");
//			textarea.append(socket.getInetAddress() + "(ID: " + name + ") ����\n");
//			jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
//			jsp.validate();
			while(true){
				String str = reader.readLine();
				if(str == null){
					break;
				}
				sendAll(name + ": " + str);
			}
		}catch(Exception e){}
		finally{
			list.remove(writer);
			nameList.remove(name);
//			choList.remove(name);
			sendAll("<" + name + ">님이 퇴장하셨습니다.");
//			textarea.append(socket.getInetAddress() + "(ID: " + name + ") ����\n");
//			jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
//			jsp.validate();
			try{
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
	
	private void sendAll(String str) {
		int count = 0;
		for(PrintWriter writer:list){
			writer.println(str);
			writer.println(nameList);
			writer.flush();
			count++;
		}
	}
}