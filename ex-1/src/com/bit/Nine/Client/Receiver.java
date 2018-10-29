package com.bit.Nine.Client;

import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JScrollPane;

class Receiver extends Thread implements MouseListener{
	
	String type;
	
	private Socket socket;
	private TextArea textarea;
	private JScrollPane jsp;
	private TextArea cho;
	private JButton korean;
	private JButton english;
	private Boolean translate=false;
	String postParams;
	
	public Receiver(Socket socket, TextArea textarea, JScrollPane jsp, TextArea cho,JButton korean,JButton english){
		System.out.println("--Receiver--trans");
		this.socket = socket;
		this.textarea = textarea;
		this.jsp = jsp;
		this.cho = cho;
		this.korean=korean;
		this.english=english;
		type ="TRANS";
	}
	
	public Receiver(Socket socket, TextArea textarea, JScrollPane jsp, TextArea cho){
		System.out.println("--Receiver--game--");
		this.socket = socket;
		this.textarea = textarea;
		this.jsp = jsp;
		this.cho = cho;
		type ="GAME01";
	}
	
	public void run(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				String str = reader.readLine();
				cho.setText("");
				String temp = reader.readLine();
				String temp2 = "";
				for(int i = 1;i < temp.length() - 1;i++){
					temp2 += temp.charAt(i);
				}
				StringTokenizer st = new StringTokenizer(temp2,",");
				for(;st.hasMoreTokens();){
					cho.append(st.nextToken().trim()+"\n");
				}
				if(str == null){
					break;
				}
				
				if(this.type=="TRANS"){
					String clientId = "OoFEn1ZnOZuHpNnRhUAP";
					String clientSecret = "dU4Ean5xoj";
					try {
						String text = URLEncoder.encode(str, "UTF-8");
						String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
						URL url = new URL(apiURL);
						HttpURLConnection con = (HttpURLConnection)url.openConnection();
						con.setRequestMethod("POST");
						con.setRequestProperty("X-Naver-Client-Id", clientId);
						con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
						// post request
						if(translate==false) {
							postParams= "source=ko&target=en&text=" + text;
						}else if(translate==true) {
							postParams= "source=en&target=ko&text=" + text;
						}
						con.setDoOutput(true);
						DataOutputStream wr = new DataOutputStream(con.getOutputStream());
						wr.writeBytes(postParams);
						wr.flush();
						wr.close();
						int responseCode = con.getResponseCode();
						BufferedReader br;
						if(responseCode==200) {
							br = new BufferedReader(new InputStreamReader(con.getInputStream()));
						} else { 
							br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
						}
						String inputLine;
						StringBuffer response = new StringBuffer();
						
						while ((inputLine = br.readLine()) != null) {
							response.append(inputLine);
						}
						br.close();
						         
						String [] msg= response.toString().split(",");
						         
						int index=0;
						for(int i=0;i<msg.length;i++) {
							if(msg[i].indexOf("translatedText")!= -1) {
								index=i;
							}
						}	//for(int i=0;i<msg.length;i++) {
						msg[index]=msg[index].substring(0, msg[index].length()-3);
						if(translate==false) {
							textarea.append(str + "\n"+msg[index].replaceAll("translatedText", "번역")+"\n");
						}else {
							textarea.append(str + "\n"+msg[index]+"\n");
						}
					} catch (Exception e) {
						System.out.println(e);
					}	// try {
				}else if(this.type.equals("GAME01")){
					System.out.println("Receiver -GAME :"+str);
					textarea.append(str + "\n");
				}
				jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
				jsp.validate();
			}	// while
		}catch(Exception e){
			
		}finally{
			textarea.append("님이 나가셨습니다.\n");
			jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
			jsp.validate();
		}	//}finally{
	}	// public void run(){

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton mouse = (JButton)e.getSource();
		System.out.println(mouse.getLabel());
		if(mouse.getLabel()=="한국어번역") {

			translate=true;
			korean.setEnabled(true);
			english.setEnabled(false);
			 
		}else if(mouse.getLabel()=="영어번역") {

			translate=false;
			korean.setEnabled(false);
			english.setEnabled(true);
		
		}
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