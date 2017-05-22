package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
	
	/**
	 * 通信用.すべての通信はここを通す
	 */
	private	static Socket	theSocket;
	public	static String	SERVER;
	public	static int		PORT;
	static PrintWriter out;
	static InputStreamReader sisr;
	static BufferedReader br;
	
	/**
	 * 初期化
	 */
	public static void init() {
		try {
			theSocket = new Socket(SERVER, PORT);
			out = new PrintWriter(theSocket.getOutputStream(), true);
			sisr = new InputStreamReader(
					theSocket.getInputStream());
			br = new BufferedReader(sisr);
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
		boolean create = false;
		
		try {
			out.println("CREATE");//コマンドの送信
			out.println(pUsername);
			out.println(pPassword);
			
			if((br.readLine()).equals("TRUE")){
				create = true;
			}else{
				create = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return create;
	}
	
	/**
	 * ログイン
	 * @param pUsername
	 * @param pPassword
	 * @return ログイン可否
	 */
	public static boolean login(String myName, String pass) {
		boolean log = false;// ログインできたかどうか

		try {
			
			out.println("LOGIN");//コマンドの送信
			out.println(myName);
			out.println(pass);
			
			if((br.readLine()).equals("TRUE")){
				log = true;
			}

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
		boolean update = false;
		
		try{
			out.println("UPDATE");//コマンドの送信
			out.println(pUsername);
			out.println(pNewPassword);
			
			if((br.readLine()).equals("TRUE")){
				update = true;
			}else{
				update = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return update;
	}
	
	/**
	 * ユーザ情報取得
	 * @return User
	 */
//	public static User getUserData(){
//		User user;
//		
//		return user;
//	}
	
	
	public static String match(String enemyName) {
		String eName=null;
		
		try{
			out.println("MATCH");//コマンドの送信
			out.println(enemyName);
			
			eName = br.readLine();
			
			if(eName.equals("FALSE")){
				eName=null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return eName;
	}
	
	public static String randomMatch() {
		String eName=null;
		
		try{
			out.println("RANDOM");//コマンドの送信
			eName = br.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return eName;
	}

	/**
	 * 自分の置き石送信
	 * @param pPlace
	 * @return 通信可否(相手のターンに送信するとfalseを返す
	 */
	public static boolean sendPutStone(int[] pPlace) {
		boolean send = false;
		
		try{
			out.println("WRITE");//コマンドの送信
			
			if((br.readLine()).equals("TRUE")){
				send = true;
				out.println(pPlace[0]);
				out.println(pPlace[1]);
			}else{
				send = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return send;
	}
	
	/**
	 * 相手の置き石を受信
	 * @return 相手が石を置く座標.nullだと通信失敗
	 */
	public static int[] receivePutStone() {
		int[] coordinate = new int[2];
		
		coordinate = null;
		
		try{
			out.println("READ");//コマンドの送信
			
			coordinate[0] = Integer.parseInt(br.readLine());
			coordinate[1] = Integer.parseInt(br.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return coordinate;
	}
	
	/**
	 * 現在,自分のターンか相手のターンかを取得する(先手後手はここで確認
	 * @return true:自分のターン,false:相手のターン
	 */
	public static boolean getTurn(){
		boolean turn=false;
		
		try{
			out.println("TURN");//コマンドの送信
			if((br.readLine()).equals("TRUE")){
				turn = true;
			}else{
				turn = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return turn;
	}
	
	/**
	 * 再戦の処理
	 * @param request true:再戦申し込み,false:対戦終了
	 * @return true:再戦開始,false:終了
	 */
	public static boolean rematch(boolean request){
		boolean req = false;
		try{
			out.println("REMATCH");//コマンドの送信
			if(request){
				out.println("1");
				if((br.readLine()).equals("TRUE")){
					req = true;
				}else{
					req = false;
				}
			}else{
				out.println("2");
				if((br.readLine()).equals("TRUE")){
					req = true;
				}else{
					req = false;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return req;
	}
	
	/**
	 * キャンセル
	 * マッチング中や再戦処理中にキャンセルできる
	 */
	public static void cansel(){
		out.println("CANSEL");
	}
	
	/**
	 * 更新ボタン
	 * マッチング中や再戦処理中に更新するとキャンセル処理をスキップして先に進める
	 */
	public static void reload(){
		out.println("r");
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
	
	

	// TODO 座標どう送る?
	
	/*
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
	
	*/
}
