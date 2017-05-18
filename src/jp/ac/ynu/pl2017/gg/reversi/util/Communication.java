package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// TODO: ClientConnection.javaに統合してください
public class Communication {
	public Socket socket;
	public static String SERVER;
	public static int PORT;

	Communication(Socket s) {
		this.socket = s;
	}
	

	public boolean login(String name, String pass) {
		boolean log = false;// ログインできたかどうか

		try {
			socket = new Socket(SERVER, PORT);
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			dos.writeUTF(name);
			dos.writeUTF(pass);

			log = dis.readBoolean();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return log;
	}

	public boolean match(boolean random, String enemy) { // random,true:ランダムマッチ,false:特定の人と
		boolean turn=true; //true:先手,false:後手
		
		try {
			
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			if (random) {
				dos.writeUTF("RANDOM_MATCH");
				turn = dis.readBoolean();
			} else {
				dos.writeUTF(enemy);
				turn = dis.readBoolean();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return turn;
	}
	
	public void send(int coordinate){
		
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			
			dos.writeInt(coordinate);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int recieve(){
		int Ecoordinate=0;
		
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			
			Ecoordinate = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Ecoordinate;
	}
	
	

}
