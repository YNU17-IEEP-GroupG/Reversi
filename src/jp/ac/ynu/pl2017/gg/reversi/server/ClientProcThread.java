package jp.ac.ynu.pl2017.gg.reversi.server;

import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.User;

import java.io.BufferedReader;
import java.io.DataInputStream;
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
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.ITEM_POSITION_S;
import static jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection.ITEM_POSITION_R;
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

import static jp.ac.ynu.pl2017.gg.reversi.gui.Othello.ITEM_COUNT;

class ClientProcThread extends Thread implements Serializable{
	private int number;// 自分の番号
	private Socket incoming;// 自分のソケット
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
	static boolean randomflag = false;
	static int room = 0; // 対局が行われている数
	static boolean[] change = new boolean[MAX_ROOM];
	static Item[] items = new Item[MAX_ROOM];// アイテムオブジェクトを格納
	static String[][] itemPlacer = new String[MAX_ROOM][ITEM_COUNT];
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
	}

	public void run() {
		try {
		
			while (true) {
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
					break;
				}
				else {
					System.out.println(myName + ": パスワードが違います");
					myOut.println(FALSE);
				}
			}

			while (true) {// 無限ループで，ソケットへの入力を監視する
				String cmd = myIn.readLine();

				if (cmd != null) {// このソケット（バッファ）に入力があるかをチェック

					System.out.println("Received from client No." + number
							+ "(" + myName + "), Messages: " + cmd);

					if (cmd.equals(END)) {
						break;
					}

					if (cmd.equals(MATCH)) {
						System.out.println(myName + ": マッチング準備");

						String enemyName = myIn.readLine();
						if (!enemyName.equals(CANCEL)
								&& !enemyName.equals(myName) && Access.exists(enemyName)) {// 自分の名前をいれたらキャンセルされる,また,存在しないユーザ名もキャンセル

							myRoom = room;// ルーム番号をセット
							Match myMatch = new Match(room, myName,
									enemyName);
							MatchMap.put(myName, myMatch);
							System.out.println(myName + ": " + enemyName
									+ "に対戦申し込み");

							while (!battle) {// マッチング待ち

								if (MatchMap.containsKey(enemyName)) {// 対戦相手がMatchMapに登録されているかチェック

									if (((MatchMap.get(enemyName))
											.getEnemyName()).equals(myName)) { // 対戦相手が自分を指名しているかチェック
										myRoom = (MatchMap.get(enemyName))
												.getRoom();// 対戦相手のルーム番号を取得

										battle = true;

									} else {
										// !!!!「別の人をリクエストしています」の処理!!!!
										myOut.println(FALSE);
										break;
									}
								} else {// 先に接続しているので先手にする
									turn = true;
								}

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

								if (randomflag) {
									enemyName = randomList.get(1);
									randomList.remove(0);
									randomList.remove(0);
									System.out.println(myName + "OKOKOKOK");
									myRoom = room;
									room++;// room番号をインクリメント
									
									randomflag = false;
									break;
								}
								
								Thread.sleep(500);
							}

						} else {
							myRoom = room;
							randomList.add(myName);
							enemyName = randomList.get(0);
							randomflag = true;
						}

						battle = true;
						change[myRoom] = true;
						itemPlacer[myRoom] = null;
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
							System.out.println(myName + ": 座標の更新(Current="+change[myRoom]+"%" + turn);
							boolean disconnect = false;
							if (items[myRoom] != null) {
								disconnect = items[myRoom].getPos().equals(new int[] {-1, -1});
							}

							if (change[myRoom] == turn) {
								
								myOut.println(TRUE);

								ObjectInputStream ois = new ObjectInputStream(myIs);
								Item item = (Item)ois.readObject();
								int[] pos = (int[]) ois.readObject();
								int[] coordinate = (int[]) ois.readObject();
								item.setPos(pos);
								item.setCoordinate(coordinate);

								if (!disconnect)
									items[myRoom] = item;


								System.out.println(myName + " が "
										+ coordinate[0] + ","
										+ coordinate[1] + "におきました");

								myOut.println();

								if (!disconnect)
									change[myRoom] = !change[myRoom];// ターンを切り替える
							} else {
								myOut.println(FALSE);
							}
						} else {
							myOut.println(FALSE);
						}

					}

					if (cmd.equals(READ)) {// 最後に置いた座標をいつでも読み込める
						System.out.println(myName + ": 座標の読み込み(Current="+change[myRoom]);
						while (change[myRoom] != turn) {
							Thread.sleep(500);
						}
						ObjectOutputStream oos = new ObjectOutputStream(myOs);
						oos.writeObject(items[myRoom]);
						oos.writeObject(items[myRoom].getPos());
						oos.writeObject(items[myRoom].getCoordinate());
					}

					if (cmd.equals(TURN)) {// 自分のターンかどうかを確かめる
						if (change[myRoom] == turn) {
							myOut.println(TRUE);
						} else {
							myOut.println(FALSE);
						}
					}

					if (cmd.equals(ITEM_POSITION_S)) {
						String temp[] = new String[ITEM_COUNT];
						for (int i = 0; i < ITEM_COUNT; i++) {
							temp[i] = myIn.readLine();
						}
						itemPlacer[myRoom] = temp;
					}

					if (cmd.equals(ITEM_POSITION_R)) {
						while (itemPlacer[myRoom] == null || itemPlacer[myRoom][0] == null) {
							Thread.sleep(500);
						}
						for (int i = 0; i < ITEM_COUNT; i++) {
							myOut.println(itemPlacer[myRoom][i]);
						}
					}

					if (cmd.equals(REMATCH)) {
						if (rematch[myRoom] == 0) {// 相手が未応答
							rematch[myRoom] = Integer.parseInt(myIn
									.readLine());

							if (rematch[myRoom] == 1) {
								System.out.println(myName+": 再戦申し込み");
								while (true) {// 相手待ち

									if (rematch[myRoom] == 2) {
										myOut.println(FALSE);
										break;
									} else if (rematch[myRoom] == 3) {
										myOut.println(TRUE);
										break;
									}
								}
							} else if (rematch[myRoom] == 2) {
								myOut.println(FALSE);
							} else {
								rematch[myRoom] = 0;
							}
						} else {// 相手が応答済
							if (rematch[myRoom] == 2) {
								rematch[myRoom] = Integer.parseInt(myIn
										.readLine());
								rematch[myRoom] = 0;
								myOut.println(FALSE);

							} else if (rematch[myRoom] == 1) {
								
								rematch[myRoom] = Integer.parseInt(myIn
										.readLine());
								if (rematch[myRoom] == 1) {
									
									myOut.println(TRUE);
									rematch[myRoom] = 3;
								} else if (rematch[myRoom] == 2) {
									myOut.println(FALSE);
									rematch[myRoom] = 0;
								} else {
									myOut.println(FALSE);
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
						boolean exist = Access.exists(newName);
						if (!exist || (exist && newName.equals(myName))) {// 登録済またはパスワードのみの変更かどうか判定
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
							System.out.println(myName+": 背景を設定しました");
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
							System.out.println(myName+": アイコンを設定しました");
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
				}
			}
		} catch (Exception e) {
			
			System.out.println("Disconnect from client No." + number + "("
					+ myName + ")");
			Server.SetFlag(number, false);
			if (change[myRoom]) {
				Item item = Item.NONE;
				item.setCoordinate(new int[] {-1, -1});
				items[myRoom] = item;
				change[myRoom] = false;
			}
		} finally {
			Access.closeConnection();
			if(MatchMap.containsKey(myName)){
				MatchMap.remove(myName);
			}
		}
	}
}
