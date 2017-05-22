package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class ClientConnection implements Serializable {

	/**
	 * 通信用.すべての通信はここを通す
	 */
	private static Socket theSocket;
//	public static String SERVER = "133.34.236.151";
	public static int PORT = 50000;
	static PrintWriter out;
	static InputStreamReader sisr;
	static BufferedReader br;

	static OutputStream os;
	static InputStream is;

	/**
	 * 初期化
	 */
	public static void init(String serverIP) {
		try {
			theSocket = new Socket(serverIP, PORT);
			out = new PrintWriter(theSocket.getOutputStream(), true);
			sisr = new InputStreamReader(theSocket.getInputStream());
			br = new BufferedReader(sisr);

			os = theSocket.getOutputStream();
			// oos = new ObjectOutputStream(os);

			is = theSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ユーザ作成
	 * 
	 * @param pUsername
	 * @param pPassword
	 * @return 作成可否
	 */
	public static boolean createUser(String pUsername, String pPassword) {
		boolean create = false;

		try {
			out.println("CREATE");// コマンドの送信
			out.println(pUsername);
			out.println(pPassword);

			// 作成後にログイン処理を行うため、2回受信する必要がある
			if ((br.readLine()).equals("TRUE") && (br.readLine()).equals("TRUE")) {
				create = true;
			} else {
				create = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return create;
	}

	/**
	 * ログイン
	 * 
	 * @param myName
	 * @param pass
	 * @return ログイン可否
	 */
	public static boolean login(String myName, String pass) {
		boolean log = false;// ログインできたかどうか

		try {

			out.println("LOGIN");// コマンドの送信
			out.println(myName);
			out.println(pass);

			if ((br.readLine()).equals("TRUE")) {
				log = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return log;
	}

	/**
	 * パスワード変更
	 * 
	 * @param pUsername
	 * @param pNewPassword
	 * @return 変更可否
	 */
	public static boolean changePassword(String pUsername, String pNewPassword) {
		boolean update = false;

		try {
			out.println("UPDATE");// コマンドの送信
			out.println(pUsername);
			out.println(pNewPassword);

			if ((br.readLine()).equals("TRUE")) {
				update = true;
			} else {
				update = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return update;
	}

	/**
	 * ユーザ情報取得(すべて
	 * 
	 * @return User
	 */
	public static User getFullUserData() {
		User user = null;

		try {
			out.println("FULLUSER");// コマンドの送信

			ObjectInputStream ois = new ObjectInputStream(is);

			user = (User) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return user;
	}

	/**
	 * ユーザ情報取得(一部:オンラインの相手のアイコンを読み込むときなど
	 * @param　enemyName情報の欲しいユーザ名
	 * @return User
	 */
	public static User getUserData(String enemyName) {
		User user = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(is);

			out.println("USER");// コマンドの送信
			out.println(enemyName);// 情報の欲しいユーザ名を送信
			user = (User) ois.readObject();//受け取り
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return user;
	}
	
	/**
	 * マッチング
	 * 
	 * @return "ユーザ名/TURN"
	 */
	public static String match(String enemyName) {
		String info=null;
		
		try {
			out.println("MATCH");// コマンドの送信
			out.println(enemyName);

			info = br.readLine();
				

		} catch (IOException e) {
			e.printStackTrace();
		}

		return info;
	}
	
	/**
	 * ランダムマッチング
	 * 
	 * @return "ユーザ名/TURN"
	 */
	public static String randomMatch() {
		String info = null;

		try {
			out.println("RANDOM");// コマンドの送信
			info = br.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 自分の置き石送信
	 * 
	 * @param pPlace
	 * @return 通信可否(相手のターンに送信するとfalseを返す
	 */
	public static boolean sendPutStone(int[] pPlace) {
		boolean send = false;

		try {
			out.println("WRITE");// コマンドの送信

			if ((br.readLine()).equals("TRUE")) {
				send = true;
				out.println(pPlace[0]);
				out.println(pPlace[1]);
			} else {
				send = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return send;
	}

	/**
	 * 相手の置き石を受信
	 * 
	 * @return 相手が石を置く座標.nullだと通信失敗
	 */
	public static int[] receivePutStone() {
		int[] coordinate = new int[2];

		try {
			out.println("READ");// コマンドの送信

			coordinate[0] = Integer.parseInt(br.readLine());
			coordinate[1] = Integer.parseInt(br.readLine());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return coordinate;
	}

	/**
	 * 現在,自分のターンか相手のターンかを取得する(先手後手はここで確認
	 * 
	 * @return true:自分のターン,false:相手のターン
	 */
	public static boolean getTurn() {
		boolean turn = false;

		try {
			out.println("TURN");// コマンドの送信
			if ((br.readLine()).equals("TRUE")) {
				turn = true;
			} else {
				turn = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return turn;
	}

	/**
	 * 再戦の処理
	 * 
	 * @param request
	 *            true:再戦申し込み,false:対戦終了
	 * @return true:再戦開始,false:終了
	 */
	public static boolean rematch(boolean request) {
		boolean req = false;
		try {
			out.println("REMATCH");// コマンドの送信
			if (request) {
				out.println("1");
				if ((br.readLine()).equals("TRUE")) {
					req = true;
				} else {
					req = false;
				}
			} else {
				out.println("2");
				if ((br.readLine()).equals("TRUE")) {
					req = true;
				} else {
					req = false;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return req;
	}

	/**
	 * キャンセル マッチング中や再戦処理中にキャンセルできる
	 */
	public static void cansel() {
		out.println("CANSEL");
	}

	/**
	 * 更新ボタン マッチング中や再戦処理中に更新するとキャンセル処理をスキップして先に進める
	 */
	public static void reload() {
		out.println("r");
	}

	/**
	 * アイテムの使用を送信
	 * 
	 * @param pItem
	 *            アイテム種類
	 * @param pItemData
	 *            アイテムの効果データ
	 * @return　通信可否
	 */
	public static boolean sendItemUse(String pItem, Object pItemData) {
		boolean item = false;

		try {
			ObjectOutputStream oos = new ObjectOutputStream(os);

			out.println("ITEM_SEND");// コマンドの送信
			out.println(pItem);// アイテム種類の送信
			oos.writeObject(pItemData);

			if ((br.readLine()).equals("TRUE")) {
				item = true;
			} else {
				item = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return item;
	}

	/**
	 * アイテムの使用を受信
	 * 
	 * @return {アイテムの種類, アイテムの効果データ}からなる.nullの場合,相手はアイテムを使用していない
	 */
	public static Object receiveItemUse() {
		Object itemData = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(is);

			out.println("ITEM_RECIEVE");// コマンドの送信
			itemData = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return itemData;
	}

	/**
	 * 背景の更新
	 * 
	 * @param
	 * @return 通信可否
	 */
	public static boolean updateBack(int back) {
		boolean updateBack = false;

		try {
			DataOutputStream dos = new DataOutputStream(os);
			out.println("BACK");// コマンドの送信
			dos.writeInt(back);

			if ((br.readLine()).equals("TRUE")) {
				updateBack = true;
			} else {
				updateBack = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return updateBack;
	}

	/**
	 * アイコンの更新
	 * @param icon アイコンのID
	 * @return 通信可否
	 */
	public static boolean updateIcon(int icon) {
		boolean updateIcon = false;
		try (DataOutputStream dos = new DataOutputStream(os)){
			out.println("ICON");// コマンドの送信
			dos.writeInt(icon);
			if ((br.readLine()).equals("TRUE")) {
				updateIcon = true;
			} else {
				updateIcon = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return updateIcon;
	}


	// TODO 座標どう送る?

	/*
	 * public static void send(int coordinate){
	 * 
	 * try { OutputStream os = theSocket.getOutputStream(); DataOutputStream dos
	 * = new DataOutputStream(os);
	 * 
	 * InputStream is = theSocket.getInputStream(); DataInputStream dis = new
	 * DataInputStream(is);
	 * 
	 * 
	 * dos.writeInt(coordinate);
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * public static int recieve(){ int Ecoordinate=0;
	 * 
	 * try{ OutputStream os = theSocket.getOutputStream(); DataOutputStream dos
	 * = new DataOutputStream(os);
	 * 
	 * InputStream is = theSocket.getInputStream(); DataInputStream dis = new
	 * DataInputStream(is);
	 * 
	 * 
	 * Ecoordinate = dis.readInt(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * return Ecoordinate; }
	 */
}
