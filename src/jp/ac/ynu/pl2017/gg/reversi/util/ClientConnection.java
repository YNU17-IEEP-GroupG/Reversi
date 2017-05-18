package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientConnection {
	
	/**
	 * 通信用.すべての通信はここを通す
	 */
	private	static Socket	theSocket;
	public	static String	SERVER;
	public	static int		PORT;
	
	/**
	 * 初期化
	 */
	public static void init() {
		try {
			theSocket = new Socket(SERVER, PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ユーザ作成
	 * @param pUsername
	 * @param pPassword
	 * @return 作成可否
	 */
	public static boolean createUser(String pUsername, String pPassword) {
		return false;
	}
	
	/**
	 * ログイン
	 * @param pUsername
	 * @param pPassword
	 * @return ログイン可否
	 */
	public static boolean login(String name, String pass) {
		boolean log = false;// ログインできたかどうか

		try {
//			theSocket = new Socket(SERVER, PORT);
			OutputStream os = theSocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = theSocket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			dos.writeUTF(name);
			dos.writeUTF(pass);

			log = dis.readBoolean();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return log;
	}

	/**
	 * パスワード変更
	 * @param pUsername
	 * @param pNewPassword
	 * @return 変更可否
	 */
	public static boolean changePassword(String pUsername, String pNewPassword) {
		return false;
	}
	
	/**
	 * 自分の置き石送信
	 * @param pPlace
	 * @return 通信可否
	 */
	public static boolean sendPutStone(int[] pPlace) {
		return false;
	}
	
	/**
	 * 相手の置き石を受信
	 * @return 相手が石を置く座標.nullだと通信失敗
	 */
	public static int[] receivePutStone() {
		return null;
	}
	
	/**
	 * アイテムの使用を送信
	 * @param pItem アイテム種類
	 * @param pItemData アイテムの効果データ
	 * @return　通信可否
	 */
	public static boolean sendItemUse(String pItem, Object pItemData) {
		return false;
	}
	
	/**
	 * アイテムの使用を受信
	 * @return {アイテムの種類, アイテムの効果データ}からなる.nullの場合,相手はアイテムを使用していない
	 */
	public static Object[] receiveItemUse() {
		return null;
	}

	public static boolean match(boolean random, String enemy) { // random,true:ランダムマッチ,false:特定の人と
		boolean turn=true; //true:先手,false:後手
		
		try {
			
			OutputStream os = theSocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = theSocket.getInputStream();
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

	// TODO 座標どう送る?
	public static void send(int coordinate){
		
		try {
			OutputStream os = theSocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = theSocket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			
			dos.writeInt(coordinate);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static int recieve(){
		int Ecoordinate=0;
		
		try{
			OutputStream os = theSocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			InputStream is = theSocket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			
			Ecoordinate = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Ecoordinate;
	}

}
