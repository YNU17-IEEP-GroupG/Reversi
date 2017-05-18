package jp.ac.ynu.pl2017.gg.reversi.util;

import java.net.Socket;

// TODO: ClientConnection.javaに統合してください
public class Communication {
	public Socket socket;
	public static String SERVER;
	public static int PORT;

	Communication(Socket s) {
		this.socket = s;
	}
	
	

}
