package jp.ac.ynu.pl2017.gg.reversi.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Serializable{

    private static int maxConnection = 100;// 最大接続数
    private static Socket[] incoming;// 受付用のソケット
    private static boolean[] flag;// 接続中かどうかのフラグ
    //private static InputStreamReader[] isr;// 入力ストリーム用の配列
    //private static BufferedReader[] in;// バッファリングをによりテキスト読み込み用の配列
    //private static PrintWriter[] out;// 出力ストリーム用の配列
    //private static ObjectInputStream[] ois;// オブジェクト受け渡し用の配列
    //private static ObjectOutputStream[] oos;
    
    private static InputStream[] is;
    private static OutputStream[] os;
    
    private static ClientProcThread[] myClientProcThread;// スレッド用の配列
    private static int member;// 接続しているメンバーの数

    /*
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
    */

    // フラグの設定を行う
    public static void SetFlag(int n, boolean value) {
        flag[n] = value;
    }

    // mainプログラム
    public static void main(String[] args) {
        // 必要な配列を確保する
        incoming = new Socket[maxConnection];
        flag = new boolean[maxConnection];
        //isr = new InputStreamReader[maxConnection];
        //in = new BufferedReader[maxConnection];
        //out = new PrintWriter[maxConnection];
        //ois = new ObjectInputStream[maxConnection];
        //oos = new ObjectOutputStream[maxConnection];
        
        is = new InputStream[maxConnection];
        os = new OutputStream[maxConnection];
        
        myClientProcThread = new ClientProcThread[maxConnection];

        int n = 1;
        member = 0;// 誰も接続していないのでメンバー数は０

        try {
            System.out.println("サーバが起動しました");
            ServerSocket server = new ServerSocket(50000);// 50000番ポートを利用する
            
            while (true) {
                incoming[n] = server.accept();
                flag[n] = true;
                System.out.println("Accept client No." + n);
                // 必要な入出力ストリームを作成する
                //isr[n] = new InputStreamReader(incoming[n].getInputStream());
                
                is[n] = incoming[n].getInputStream();
                os[n] = incoming[n].getOutputStream();
                
                /*
                in[n] = new BufferedReader(isr[n]);
                out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
                
                ois[n] = new ObjectInputStream(incoming[n].getInputStream());
				oos[n] = new ObjectOutputStream(incoming[n].getOutputStream());
				*/
                
                myClientProcThread[n] = new ClientProcThread(n, incoming[n], is[n], os[n]);
                myClientProcThread[n].start();// スレッドを開始する
                member = n;// メンバーの数を更新する
                n++;
            }
        } catch (Exception e) {
            System.err.println("ソケット作成時にエラーが発生しました: " + e);
            e.printStackTrace();
        }
    }
}

