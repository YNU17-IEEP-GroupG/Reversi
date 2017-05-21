package jp.ac.ynu.pl2017.gg.reversi.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

class ClientProcThread extends Thread {
	private int number;// 自分の番号
	private Socket incoming;// 自分のソケット
	private Socket Eincoming; // 対戦相手のソケット
	private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private String myName;// 接続者の名前
	private String pass;// 接続者のパスワード
	private boolean battle = false;// 対局可能かどうか
	private boolean turn = false;// true:先手/false:後手
	private int myRoom = 0;

	public static final int MAX_ROOM = 10;
	static int room = 0; // 対局が行われている数
	static boolean[] change = new boolean[MAX_ROOM];
	static String[][] coordinate = new String[MAX_ROOM][2];// 通信路
	static int[] rematch = new int[MAX_ROOM];// 再戦用0:default,1:再戦,2:拒否,3:再戦受付

	static HashMap<String, Match> MatchMap = new HashMap<String, Match>(); // ユーザ名をキーにする
	static ArrayList<String> randomList = new ArrayList<String>(); 
	
	public static final String PASS = "pass";// テスト用

	public ClientProcThread(int n, Socket i, InputStreamReader isr,
			BufferedReader in, PrintWriter out) {
		number = n;
		incoming = i;
		myIsr = isr;
		myIn = in;
		myOut = out;
	}

	public void run() {
		try {
			/*
			myOut.println("Hello, client No."
					+ number
					+ "¥nコマンド一覧\n"
					+ "*******************************\n"
					+ "END:終了　MATCH:マッチング　CANSEL:キャンセル　WRITE:座標送信　READ:座標読み込み　TURN:ターン確認　r:更新\n"
					+ "*******************************");// 初回だけ呼ばれる
			*/
			
			myName = myIn.readLine();
			pass = myIn.readLine();

			if (pass.equals(PASS)) {
				System.out.println(myName + ": ログイン完了");
				myOut.println("TRUE");//ログイン成功のコマンド送信

				while (true) {// 無限ループで，ソケットへの入力を監視する
					String cmd = myIn.readLine();
					
					if (cmd != null) {// このソケット（バッファ）に入力があるかをチェック
						
						System.out.println("Received from client No." + number+ "(" + myName + "), Messages: " + cmd);
						
						if (cmd.equals("END")) {
						//	myOut.println("終了します");
							break;
						}
					

						if (cmd.equals("MATCH")) {
							System.out.println(myName + ": マッチング準備");

						//	myOut.println("対戦相手の名前を入力して下さい");
							String enemyName = myIn.readLine();
							if (!enemyName.equals("CANSEL")
									&& !enemyName.equals(myName)) {// 自分の名前をいれたらキャンセルされる

								myRoom = room;// ルーム番号をセット
								Match myMatch = new Match(room, myName,
										enemyName);
								MatchMap.put(myName, myMatch);
								System.out.println(myName + ": " + enemyName
										+ "に対戦申し込み");
							//	myOut.println(enemyName
							//			+ " に対戦を申し込みしました。しばらくお待ちください");

								while (!battle) {// マッチング待ち

									if (MatchMap.containsKey(enemyName)) {// 対戦相手がMatchMapに登録されているかチェック

										if (((MatchMap.get(enemyName)).getEnemyName()).equals(myName)) { // 対戦相手が自分を指名しているかチェック
											myRoom = (MatchMap.get(enemyName)).getRoom();// 対戦相手のルーム番号を取得
										//	myOut.printf(enemyName
										//			+ " とマッチングしました!　　あなたは");

											battle = true;

										} else {
											// !!!!「別の人をリクエストしています」の処理!!!!
											myOut.println("FALSE");
											break;
										}
									} else {// 先に接続しているので先手にする
										turn = true;
									}
									
									String matching = myIn.readLine();

									if (matching != null) { // マッチングキャンセル
										if (matching.equals("CANSEL")) {
											MatchMap.remove(myName);
											System.out.println(myName
													+ ": マッチングキャンセル");
										//	myOut.println("キャンセルしました");
											break;
										}
									}
								}// マッチング待ち終了

								room++;// room番号をインクリメント
								change[myRoom] = true;
								rematch[myRoom] = 0;
								System.out.println(myName + ": マッチング完了");
								myOut.println(enemyName);

							} else {
								System.out.println(myName + ": マッチングキャンセル");
							//	myOut.println("キャンセルしました");
								myOut.println("FALSE");
							}
							
						}
						
						if(cmd.equals("RANDOM")){
							System.out.println(myName + ": ランダムマッチング");
							String enemyName=null;
							
							if(randomList.size()==0){
								randomList.add(myName);
								turn = true;
								while(true){
									String matching = myIn.readLine();
									
									if(randomList.size()==2){
										enemyName = randomList.get(1);
										randomList.remove(0);
										randomList.remove(0);
										
										myRoom = room;
										room++;// room番号をインクリメント
										break;
									}
									
									if (matching != null) { // マッチングキャンセル
										if (matching.equals("CANSEL")) {
											randomList.remove(0);
											System.out.println(myName+ ": マッチングキャンセル");
										//	myOut.println("キャンセルしました");
											break;
										}
									}
								}
								
							}else{
								myRoom = room;
								enemyName = randomList.get(0);
								randomList.add(myName);
							}
							
							battle = true;
							change[myRoom] = true;
							rematch[myRoom] = 0;
							System.out.println(myName + ": マッチング完了");
							myOut.println(enemyName);
							
							
						}
						
						if (cmd.equals("WRITE")) {
							if (battle) {
								System.out.println(myName + ": 座標の更新");
								
								if (change[myRoom] == turn) {
								//	myOut.println("座標を入力して下さい");
									coordinate[myRoom][0] = myIn.readLine();
									coordinate[myRoom][1] = myIn.readLine();
									
								//	myOut.println(coordinate[myRoom]+ " におきました");
									myOut.println("TRUE");
									
									System.out.println(myName+" が "+coordinate[myRoom][0]+","+coordinate[myRoom][1]+"におきました");
									
									change[myRoom] = !change[myRoom];// ターンを切り替える
								} else {
								//	myOut.println("相手のターンです");
									myOut.println("FALSE");
								}
							} else {
							//	myOut.println("対局が始まってません");
								myOut.println("FALSE");
							}

						}

						if (cmd.equals("READ")) {// 最後に置いた座標をいつでも読み込める
							System.out.println(myName + ": 座標の読み込み");
							myOut.println(coordinate[myRoom][0]);
							myOut.println(coordinate[myRoom][1]);
						}

						if (cmd.equals("TURN")) {// 自分のターンかどうかを確かめる
							if (change[myRoom] == turn) {
							//	myOut.println("あなたのターンです");
								myOut.println("TRUE");
							} else {
							//	myOut.println("相手のターンです");
								myOut.println("FALSE");
							}
						}

						if (cmd.equals("REMATCH")) {
							if (rematch[myRoom] == 0) {// 相手が未応答
							//	myOut.println("再戦:1,終了:2");
								rematch[myRoom] = Integer.parseInt(myIn.readLine());

								if (rematch[myRoom] == 1) {
									myOut.println("対戦相手を待っています");
									while (true) {// 相手待ち
										
										
										String cansel = myIn.readLine();
										if (cansel.equals("CANSEL")) {
											myOut.println("キャンセルしました");
											rematch[myRoom] = 2;
											break;
										}
										
										
										if (rematch[myRoom] == 2) {
										//	myOut.println("再戦が拒否されました");
											myOut.println("FALSE");
											break;
										} else if (rematch[myRoom] == 3) {
										//	myOut.println("再戦を行います");
											myOut.println("TRUE");
											break;
										}
									}
								} else if (rematch[myRoom] == 2) {
								//	myOut.println("終了します");
									myOut.println("FALSE");
								} else {
								//	myOut.println("不正な値です");
									rematch[myRoom] = 0;
								}
							} else {// 相手が応答済
								if (rematch[myRoom] == 2) {
								//	myOut.println("再戦が拒否されました");
									rematch[myRoom] = Integer.parseInt(myIn.readLine());
									rematch[myRoom] = 0;
									myOut.println("FALSE");
									
								} else if (rematch[myRoom] == 1) {
								//	myOut.println("再戦:1,終了:2");
									rematch[myRoom] = Integer.parseInt(myIn.readLine());
									if (rematch[myRoom] == 1) {
								//		myOut.println("再戦を行います");
										myOut.println("TRUE");
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

						// MyServer.SendAll(str, myName);//
						// サーバに来たメッセージは接続しているクライアント全員に配る
					}
				}

			} else {
				System.out.println(myName + ": パスワードが違います");
				myOut.println("FALSE");
			}
		} catch (Exception e) {
			// ここにプログラムが到達するときは，接続が切れたとき
			System.out.println("Disconnect from client No." + number + "("
					+ myName + ")");
			Server.SetFlag(number, false);// 接続が切れたのでフラグを下げる
		}
	}
}

