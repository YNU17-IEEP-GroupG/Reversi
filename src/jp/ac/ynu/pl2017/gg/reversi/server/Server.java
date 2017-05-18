package jp.ac.ynu.pl2017.gg.reversi.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;


class ClientProcThread extends Thread {
	private int number;// 自分の番号
	private Socket incoming;//自分のソケット
	private Socket Eincoming; //対戦相手のソケット
	private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private String myName;// 接続者の名前
	private String pass;// 接続者のパスワード
	private boolean battle=false;//対局可能か
	private boolean turn=false;//true:先手/false:後手
	private int myRoom = 0;
	
	static int room=0; //対局が行われている数
	static boolean[] change;
	static String[] coordinate;//通信路
	
	
	static HashMap<String, Match> MatchMap = new HashMap<String, Match>(); // ユーザ名をキーにする

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
			myOut.println("Hello, client No." + number
					+ "¥nコマンド一覧¥n"
					+ "*******************************¥n"
					+ "END:終了　MATCH:マッチング　CANSEL:キャンセル　BATTLE:座標送信　r:更新¥n"
					+ "*******************************");// 初回だけ呼ばれる

			myName = myIn.readLine();
			pass = myIn.readLine();

			if (pass.equals(PASS)) {
				System.out.println(myName + ": ログイン完了");

				while (true) {// 無限ループで，ソケットへの入力を監視する
					String cmd = myIn.readLine();
					System.out.println("Received from client No." + number
							+ "(" + myName + "), Messages: " + cmd);
					if (cmd != null) {// このソケット（バッファ）に入力があるかをチェック
						if (cmd.equals("END")) {
							myOut.println("終了します");
							break;
						}
						
						if(cmd.equals("MATCH")){
							System.out.println(myName + ": マッチング準備");
							
							myOut.println("対戦相手の名前を入力して下さい");
							String enemyName = myIn.readLine();
							if(!enemyName.equals("CANSEL")){
								
								myRoom = room;//ルーム番号をセット
								Match myMatch = new Match(room,myName,enemyName);
								MatchMap.put(myName,myMatch);
								System.out.println(myName + ": "+enemyName+"に対戦申し込み");
								myOut.println(enemyName+" に対戦を申し込みしました。しばらくお待ちください");
								
								while(!battle){//マッチング待ち
									
									if (MatchMap.containsKey(enemyName)) {//対戦相手がMatchMapに登録されているかチェック
										
										if(((MatchMap.get(enemyName)).getEnemyName()).equals(myName)){ //対戦相手が自分を指名しているかチェック
											myRoom = (MatchMap.get(enemyName)).getRoom();//対戦相手のルーム番号を取得
											myOut.printf(enemyName+" とマッチングしました!　　あなたは");
											
											if(turn){
												myOut.println("先手です");
											}else{
												myOut.println("後手です");
											}
											
											battle = true;
											
										}else{
											//!!!!「別の人をリクエストしています」の処理!!!!
										}
									}else{//先に接続しているので先手にする
										turn = true;
									}
									
									String matching = myIn.readLine();
									
									if(matching != null){ //マッチングキャンセル
										if(matching.equals("CANSEL")){
											MatchMap.remove(myName);
											System.out.println(myName + ": マッチングキャンセル");
											myOut.println("キャンセルしました");
											break;
										}
									}
								}//マッチング待ち終了
								
								room++;//room番号をインクリメント
								System.out.println(myName+": マッチング完了");
								
							}else{
								System.out.println(myName + ": マッチングキャンセル");
								myOut.println("キャンセルしました");
							}
						}
						
					if(cmd.equals("WRITE")){
						if(battle){
							
						}else{
							myOut.println("キャンセルしました");
						}
					}
						
					//	MyServer.SendAll(str, myName);// サーバに来たメッセージは接続しているクライアント全員に配る
					}
				}
				
			} else {
				System.out.println(myName + ": パスワードが違います");
			}
		} catch (Exception e) {
			// ここにプログラムが到達するときは，接続が切れたとき
			System.out.println("Disconnect from client No." + number + "("
					+ myName + ")");
			Server.SetFlag(number, false);// 接続が切れたのでフラグを下げる
		}
	}
}

class Server {

	private static int maxConnection = 100;// 最大接続数
	private static Socket[] incoming;// 受付用のソケット
	private static boolean[] flag;// 接続中かどうかのフラグ
	private static InputStreamReader[] isr;// 入力ストリーム用の配列
	private static BufferedReader[] in;// バッファリングをによりテキスト読み込み用の配列
	private static PrintWriter[] out;// 出力ストリーム用の配列
	private static ClientProcThread[] myClientProcThread;// スレッド用の配列
	private static int member;// 接続しているメンバーの数

	// 全員にメッセージを送る
	public static void SendAll(String str, String myName) {
		// 送られた来たメッセージを接続している全員に配る
		for (int i = 1; i <= member; i++) {
			if (flag[i] == true) {
				out[i].println(str);
				out[i].flush();// バッファをはき出す＝＞バッファにある全てのデータをすぐに送信する
				System.out.println("Send messages to client No." + i);
			}
		}
	}

	// フラグの設定を行う
	public static void SetFlag(int n, boolean value) {
		flag[n] = value;
	}

	// mainプログラム
	public static void main(String[] args) {
		// 必要な配列を確保する
		incoming = new Socket[maxConnection];
		flag = new boolean[maxConnection];
		isr = new InputStreamReader[maxConnection];
		in = new BufferedReader[maxConnection];
		out = new PrintWriter[maxConnection];
		myClientProcThread = new ClientProcThread[maxConnection];

		int n = 1;
		member = 0;// 誰も接続していないのでメンバー数は０

		try {
			System.out.println("サーバが起動しました");
			ServerSocket server = new ServerSocket(10000);// 10000番ポートを利用する
			while (true) {
				incoming[n] = server.accept();
				flag[n] = true;
				System.out.println("Accept client No." + n);
				// 必要な入出力ストリームを作成する
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);

				myClientProcThread[n] = new ClientProcThread(n, incoming[n],
						isr[n], in[n], out[n]);// 必要なパラメータを渡しスレッドを作成
				myClientProcThread[n].start();// スレッドを開始する
				member = n;// メンバーの数を更新する
				n++;
			}
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}
}
