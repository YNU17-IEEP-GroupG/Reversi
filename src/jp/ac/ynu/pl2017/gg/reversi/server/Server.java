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
    
    private static InputStream[] is;
    private static OutputStream[] os;
    
    private static ClientProcThread[] myClientProcThread;// スレッド用の配列
    private static int member;// 接続しているメンバーの数

    // フラグの設定を行う
    public static void SetFlag(int n, boolean value) {
        flag[n] = value;
    }
    
    public static void main(String[] args) {
        incoming = new Socket[maxConnection];
        flag = new boolean[maxConnection];
        
        is = new InputStream[maxConnection];
        os = new OutputStream[maxConnection];
        
        myClientProcThread = new ClientProcThread[maxConnection];

        int n = 1;
        member = 0;

        try {
            System.out.println("サーバが起動しました");
            ServerSocket server = new ServerSocket(50000);// 50000番ポートを利用する
            
            while (true) {
                incoming[n] = server.accept();
                flag[n] = true;
                System.out.println("Accept client No." + n);
                
                is[n] = incoming[n].getInputStream();
                os[n] = incoming[n].getOutputStream();
                
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

