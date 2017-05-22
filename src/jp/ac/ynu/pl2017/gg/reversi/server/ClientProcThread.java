package jp.ac.ynu.pl2017.gg.reversi.server;

import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.User;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.BACK;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.CANCEL;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.CREATE;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.END;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.EXISTS;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.FULL_USER;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.ICON;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.ITEM_RECEIVE;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.ITEM_SEND;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.LOGIN;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.MATCH;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.RANDOM;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.READ;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.REMATCH;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.TRUE;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.FALSE;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.TURN;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.UPDATE_RESULT_CPU;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.UPDATE_RESULT_ONLINE;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.UPDATE_USER;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.USER;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.WRITE;

class ClientProcThread extends Thread implements Serializable{
	private int number;// 自分の番号
	private Socket incoming;// 自分のソケット
	private Socket Eincoming; // 対戦相手のソケット
	// private InputStreamReader myIsr;
	private InputStream myIs;
	private OutputStream myOs;
	private BufferedReader myIn;
	private PrintWriter myOut;

	private String myName;// 接続者の名前
	private String pass;// 接続者のパスワード
	private boolean battle = false;// 対局可能かどうか
	private boolean turn = false;// true:先手/false:後手
	private int myRoom = 0;

	public static final int MAX_ROOM = 20;
	static int room = 0; // 対局が行われている数
	static boolean[] change = new boolean[MAX_ROOM];
	static String[][] coordinate = new String[MAX_ROOM][2];// 通信路
	static Item[] item = new Item[MAX_ROOM];// アイテムオブジェクトを格納
	static int[][] itemPos = new int[6][MAX_ROOM];
//	static String itemName[] = new String[MAX_ROOM];
	static int[] rematch = new int[MAX_ROOM];// 再戦用0:default,1:再戦,2:拒否,3:再戦受付

	static HashMap<String, Match> MatchMap = new HashMap<String, Match>(); // ユーザ名をキーにする
	static ArrayList<String> randomList = new ArrayList<String>();

	public static final String PASS = "pass";// テスト用


	public ClientProcThread(int n, Socket i, InputStream is, OutputStream os) {
		number = n;
		incoming = i;
		myIs = is;
		myOs = os;
		myIn = new BufferedReader(new InputStreamReader(is));
		myOut = new PrintWriter(os, true);
		// myOis = new ObjectInputStream(is);
		// myOos = new ObjectOutputStream(os);
	}

	public void run() {
		try {
			/*
			 * myOut.println("Hello, client No." + number + "¥nコマンド一覧\n" +
			 * "*******************************\n" +
			 * "END:終了　MATCH:マッチング　CANCEL:キャンセル　WRITE:座標送信　READ:座標読み込み　TURN:ターン確認　r:更新\n"
			 * + "*******************************");// 初回だけ呼ばれる
			 */

			String firstCmd = myIn.readLine();

			if (firstCmd.equals(LOGIN)) {
				myName = myIn.readLine();
				pass = myIn.readLine();
			} else if (firstCmd.equals(CREATE)) {
				String newMyName = myIn.readLine();
				String newPass = myIn.readLine();
				if (!Access.exists(newMyName)) {// 登録済かどうか調べる
					if (Access.makeNewUser(newMyName, newPass)) {
						System.out.println("アカウント作成:" + newMyName);
						myOut.println(TRUE);
						myName = newMyName;
						pass = newPass;
					} else {
						myOut.println(FALSE);
					}
				} else {
					myOut.println(FALSE);
				}
			}

			if (Access.login(myName, pass)) {
				System.out.println(myName + ": ログイン完了");
				myOut.println(TRUE);// ログイン成功のコマンド送信

				while (true) {// 無限ループで，ソケットへの入力を監視する
					String cmd = myIn.readLine();

					if (cmd != null) {// このソケット（バッファ）に入力があるかをチェック

						System.out.println("Received from client No." + number
								+ "(" + myName + "), Messages: " + cmd);

						if (cmd.equals(END)) {
							// myOut.println("終了します");
							break;
						}

						if (cmd.equals(MATCH)) {
							System.out.println(myName + ": マッチング準備");

							// myOut.println("対戦相手の名前を入力して下さい");
							String enemyName = myIn.readLine();
							if (!enemyName.equals(CANCEL)
									&& !enemyName.equals(myName) && Access.exists(enemyName)) {// 自分の名前をいれたらキャンセルされる,また,存在しないユーザ名もキャンセル

								myRoom = room;// ルーム番号をセット
								Match myMatch = new Match(room, myName,
										enemyName);
								MatchMap.put(myName, myMatch);
								System.out.println(myName + ": " + enemyName
										+ "に対戦申し込み");
								// myOut.println(enemyName
								// + " に対戦を申し込みしました。しばらくお待ちください");

								while (!battle) {// マッチング待ち

									if (MatchMap.containsKey(enemyName)) {// 対戦相手がMatchMapに登録されているかチェック

										if (((MatchMap.get(enemyName))
												.getEnemyName()).equals(myName)) { // 対戦相手が自分を指名しているかチェック
											myRoom = (MatchMap.get(enemyName))
													.getRoom();// 対戦相手のルーム番号を取得
											// myOut.printf(enemyName
											// + " とマッチングしました!　　あなたは");

											battle = true;

										} else {
											// !!!!「別の人をリクエストしています」の処理!!!!
											myOut.println(FALSE);
											break;
										}
									} else {// 先に接続しているので先手にする
										turn = true;
									}
									
									/*
									String matching = myIn.readLine();

									if (matching != null) { // マッチングキャンセル
										if (matching.equals("CANCEL")) {
											MatchMap.remove(myName);
											System.out.println(myName
													+ ": マッチングキャンセル");
											// myOut.println("キャンセルしました");
											break;
										}
									}
									*/
									
								}// マッチング待ち終了
								
								System.out.println(myName + ": マッチング完了"+"(room:"+myRoom+"%"+turn);
								
								room++;// room番号をインクリメント
								change[myRoom] = true;
								rematch[myRoom] = 0;
								
								if(turn){
									myOut.println(enemyName+"/"+"true");
								}else{
									myOut.println(enemyName+"/"+"false");
								}

							} else {
								System.out.println(myName + ": マッチングキャンセル");
								// myOut.println("キャンセルしました");
								myOut.println(FALSE);
							}

						}

						if (cmd.equals(RANDOM)) {
							System.out.println(myName + ": ランダムマッチング");
							String enemyName = null;

							if (randomList.size() == 0) {
								randomList.add(myName);
								turn = true;
								while (true) {

									if (randomList.size() == 2) {
										enemyName = randomList.get(1);
										randomList.remove(0);
										randomList.remove(0);

										myRoom = room;
										room++;// room番号をインクリメント
										break;
									}
									
									/*
									String matching = myIn.readLine();
									if (matching != null) { // マッチングキャンセル
										if (matching.equals("CANCEL")) {
											randomList.remove(0);
											System.out.println(myName
													+ ": マッチングキャンセル");
											// myOut.println("キャンセルしました");
											break;
										}
									}
									*/
								}

							} else {
								myRoom = room;
								enemyName = randomList.get(0);
								randomList.add(myName);
							}

							battle = true;
							change[myRoom] = true;
							rematch[myRoom] = 0;
							System.out.println(myName + ": マッチング完了");
							
							if(turn){
								myOut.println(enemyName+"/"+"true");
							}else{
								myOut.println(enemyName+"/"+"false");
							}

						}

						if (cmd.equals(WRITE)) {
							if (battle) {
								System.out.println(myName + ": 座標の更新%" + turn);

								if (change[myRoom] == turn) {
									// myOut.println("座標を入力して下さい");
									coordinate[myRoom][0] = myIn.readLine();
									coordinate[myRoom][1] = myIn.readLine();

									// myOut.println(coordinate[myRoom]+
									// " におきました");
									myOut.println(TRUE);

									System.out.println(myName + " が "
											+ coordinate[myRoom][0] + ","
											+ coordinate[myRoom][1] + "におきました");

									change[myRoom] = !change[myRoom];// ターンを切り替える
								} else {
									// myOut.println("相手のターンです");
									myOut.println(FALSE);
								}
							} else {
								// myOut.println("対局が始まってません");
								myOut.println(FALSE);
							}

						}

						if (cmd.equals(READ)) {// 最後に置いた座標をいつでも読み込める
							System.out.println(myName + ": 座標の読み込み");
							myOut.println(coordinate[myRoom][0]);
							myOut.println(coordinate[myRoom][1]);
						}

						if (cmd.equals(TURN)) {// 自分のターンかどうかを確かめる
							if (change[myRoom] == turn) {
								// myOut.println("あなたのターンです");
								myOut.println(TRUE);
							} else {
								// myOut.println("相手のターンです");
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(ITEM_SEND)) {// アイテム送信
//							itemName[myRoom] = myIn.readLine();// アイテム名を受信
							ObjectInputStream myOis = new ObjectInputStream(
									myIs);
							item[myRoom] = (Item) myOis.readObject();// アイテムオブジェクトを受信
							DataInputStream myDis = new DataInputStream(myIs);
							// アイテムの影響を及ぼした座標は必ず長さ6で扱う
							for (int i = 0; i < 6; i++) {
								itemPos[i][myRoom] = myDis.readInt();
							}
							if (item[myRoom] != null) {
								myOut.println(TRUE);

								/* アイテム使用数をDBに記録 */
								Access.updateItem(myName, 1);

							} else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(ITEM_RECEIVE)) {// アイテム受信
							ObjectOutputStream myOos = new ObjectOutputStream(
									myOs);
							myOos.writeObject(item[myRoom]);// クライアントへ送信
							DataOutputStream myDos = new DataOutputStream(myOs);
							// アイテムの影響を及ぼした座標は必ず長さ6で扱う
							for (int i = 0; i < 6; i++) {
								myDos.writeInt(itemPos[i][myRoom]);
							}
							item[myRoom] = Item.NONE;// オブジェクトを初期化
						}

						if (cmd.equals(REMATCH)) {
							if (rematch[myRoom] == 0) {// 相手が未応答
								// myOut.println("再戦:1,終了:2");
								rematch[myRoom] = Integer.parseInt(myIn
										.readLine());

								if (rematch[myRoom] == 1) {
									// myOut.println("対戦相手を待っています");
									while (true) {// 相手待ち

										String cancel = myIn.readLine();
										if (cancel.equals(CANCEL)) {
											myOut.println("キャンセルしました");
											rematch[myRoom] = 2;
											break;
										}

										if (rematch[myRoom] == 2) {
											// myOut.println("再戦が拒否されました");
											myOut.println(FALSE);
											break;
										} else if (rematch[myRoom] == 3) {
											// myOut.println("再戦を行います");
											myOut.println(TRUE);
											break;
										}
									}
								} else if (rematch[myRoom] == 2) {
									// myOut.println("終了します");
									myOut.println(FALSE);
								} else {
									// myOut.println("不正な値です");
									rematch[myRoom] = 0;
								}
							} else {// 相手が応答済
								if (rematch[myRoom] == 2) {
									// myOut.println("再戦が拒否されました");
									rematch[myRoom] = Integer.parseInt(myIn
											.readLine());
									rematch[myRoom] = 0;
									myOut.println(FALSE);

								} else if (rematch[myRoom] == 1) {
									// myOut.println("再戦:1,終了:2");
									rematch[myRoom] = Integer.parseInt(myIn
											.readLine());
									if (rematch[myRoom] == 1) {
										// myOut.println("再戦を行います");
										myOut.println(TRUE);
										rematch[myRoom] = 3;
									} else if (rematch[myRoom] == 2) {
										myOut.println("終了します");
										rematch[myRoom] = 0;
									} else {
										myOut.println("不正な値です");
									}
								}
							}
						}

						if (cmd.equals(FULL_USER)) {
							User user;
							ObjectOutputStream myOos = new ObjectOutputStream(
									myOs);

							System.out.println(myName + ": ユーザ情報読み込み(すべて");
							user = Access.requestFullUserData(myName);
							myOos.writeObject(user);
						}

						if (cmd.equals(USER)) {
							User user;
							String eneName;
							
							eneName = myIn.readLine();//相手の名前を読み込み
							ObjectOutputStream myOos = new ObjectOutputStream(
									myOs);

							System.out.println(myName + ": ユーザ情報読み込み");
							user = Access.requestUserData(eneName);
							myOos.writeObject(user);
						}

						if (cmd.equals(UPDATE_USER)) {
							String newName = myIn.readLine();
							String newPass = myIn.readLine();
							if (!Access.exists(newName)) {// 登録済かどうか判定
								if (Access.updateUser(myName, newName, newPass)) {
									myOut.println(TRUE);
									System.out.println(myName + " >> "
											+ newName + "   " + pass + " >> "
											+ "newPass");
									myName = newName;
									pass = newPass;
								} else {
									myOut.println(FALSE);
								}
							} else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(EXISTS)) {
							String name = myIn.readLine();
							if (Access.exists(name)) {
								myOut.println(TRUE);
							}
							else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(BACK)) {
							int back = 0;
							DataInputStream dis = new DataInputStream(myIs);
							back = dis.readInt();
							if (Access.updateBack(myName, back)) {
								myOut.println(TRUE);
							} else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(ICON)) {
							int icon = 0;
							DataInputStream dis = new DataInputStream(myIs);
							icon = dis.readInt();
							if (Access.updateIcon(myName, icon)) {
								myOut.println(TRUE);
							} else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(UPDATE_RESULT_CPU)) {
							DataInputStream dis = new DataInputStream(myIs);
							int type = dis.readInt();
							int difficulty = dis.readInt();
							int judgement = dis.readInt();
							if (Access.updateResult(myName, type, difficulty, judgement, 1)) {
								myOut.println(TRUE);
							} else {
								myOut.println(FALSE);
							}
						}

						if (cmd.equals(UPDATE_RESULT_ONLINE)) {
							DataInputStream dis = new DataInputStream(myIs);
							int judgement = dis.readInt();
							int difference = dis.readInt();
							if (Access.updateResult(myName, 4, 3, judgement, difference)) {
								myOut.println(TRUE);
							} else {
								myOut.println(FALSE);
							}
						}
						

						// MyServer.SendAll(str, myName);//
						// サーバに来たメッセージは接続しているクライアント全員に配る
					}
				}

			} else {
				System.out.println(myName + ": パスワードが違います");
				myOut.println(FALSE);
			}
		} catch (Exception e) {
			// ここにプログラムが到達するときは，接続が切れたとき
			System.out.println("Disconnect from client No." + number + "("
					+ myName + ")");
			Server.SetFlag(number, false);// 接続が切れたのでフラグを下げる
		} finally {
			Access.closeConnection();
			if(MatchMap.containsKey(myName)){
				MatchMap.remove(myName);
			}
		}
	}
}
