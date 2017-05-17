package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection {
	
	/**
	 * 通信用.すべての通信はここを通す
	 */
	private	static Socket	theSocket;
	public	static String	SERVER_ADDRESS;
	public	static int		SERVER_PORT;
	
	/**
	 * 初期化
	 */
	public static void init() {
		try {
			theSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
		} catch (IOException e) {
			throw new RuntimeException(e);
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
	public static boolean logIn(String pUsername, String pPassword) {
		return false;
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

}
