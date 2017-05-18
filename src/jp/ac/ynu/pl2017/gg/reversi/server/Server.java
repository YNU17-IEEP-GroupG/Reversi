//package jp.ac.ynu.pl2017.gg.reversi.server;
//
//import java.net.Socket;
//import java.net.ServerSocket;
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.IOException;
//import java.util.*;
//
//public class Server {
//
//    public static final int PORT = 4231;
//
//    public static void main(String args[]) {
//        ServerSocket ss = null;
//
//        try {
//
//            ss = new ServerSocket(PORT);
//            System.out.println("Serverが起動しました(port=" + ss.getLocalPort() + ")");
//
//            while (true) {
//                Socket socket = ss.accept();
//                new EchoThread(socket).start();
//            }
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        } finally {
//
//            try {
//                if (ss != null) {
//                    ss.close();
//                }
//            } catch (IOException e) {
//            }
//
//        }
//
//    }
//
//}
//
//class EchoThread extends Thread {
//
//    private Socket socket;
//    private Socket Esocket; // 対戦相手のソケット
//    String mode = "match";
//    boolean turn = false; // true:先手,false:後手
//
//    public static final String NAME = "taro";
//    public static final String PASS = "pass";
//    static HashMap<String, Match> map = new HashMap<String, Match>(); // ユーザ名をキーにする
//    static Socket prerandom = null; // ランダムマッチ用のソケット
//    static Socket latrandom = null;
//    static String prename;
//    static String latname;
//
//    public EchoThread(Socket socket) {
//
//        try {
//            this.socket = socket;
//            System.out.println("接続されました " + socket.getRemoteSocketAddress());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//
//        try {
//            InputStream is = socket.getInputStream();
//            DataInputStream dis = new DataInputStream(is);
//
//            OutputStream os = socket.getOutputStream();
//            DataOutputStream dos = new DataOutputStream(os);
//
//			/* ログイン */
//            String name = dis.readUTF();
//            String pass = dis.readUTF();
//
//            if (pass.equals(PASS)) {
//                System.out.println(name + "がログインしました");
//                dos.writeBoolean(true);
//            } else {
//                System.out.println("パスワードが違います");
//                dos.writeBoolean(false);
//                socket.close();
//                System.out.println("切断されました : "
//                        + socket.getRemoteSocketAddress());
//            }
//
//			/* マッチング */
//            while (mode.equals("match")) {
//                String enemy = dis.readUTF();
//                if (enemy.equals("RANDOM_MATCH")) {
//                    if (prerandom == null) {
//                        turn = true; // 相手がいなかったら先手へ
//                        prerandom = socket;
//                        prename = name;
//                        while (Esocket==null) {
//                            Esocket = latrandom;
//                        }
//
//
//                    } else {
//                        latrandom = socket;
//                        latname = name;
//                        Esocket = prerandom;
//
//                    }
//
//                    dos.writeBoolean(turn);
//
//
//                } else {
//
//                    Match match = new Match(socket, enemy);
//                    System.out.println(name + "　は　" + enemy + "　に対戦申し込み");
//                    map.put(name, match);
//                    while (true) {
//                        if (map.containsKey(enemy)) {
//                            if (((map.get(enemy)).getEnemy()).equals(name)) {
//                                System.out.println("***マッチング成功***");
//                                Esocket = (map.get(enemy)).getSocket();
//                                map.remove(name);
//                                map.remove(enemy);
//                                dos.writeBoolean(turn);
//                                break;
//                            }
//                        } else {
//                            turn = true; // 相手が待っていなかったら先手へ
//                        }
//                    }
//
//                }
//
//                if (turn) {
//                    System.out.println(name + "は先手");
//                } else {
//                    System.out.println(name + "は後手");
//                }
//                mode = "battle";
//            }
//
//			/* 対局 */
//            int coordinate;
//            OutputStream eos = Esocket.getOutputStream();
//            DataOutputStream edos = new DataOutputStream(eos);
//            while (mode.equals("battle")) {
//
//                if (turn) {
//                    coordinate = dis.readInt();
//                    System.out.println(name + "は  " + coordinate + "  に置いた");
//                    edos.writeInt(coordinate);
//                    turn = false;
//                } else {
//                    coordinate = dis.readInt();
//                    turn = true;
//                }
//
//            }
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        } finally {
//
//            try {
//
//                if (socket != null) {
//                    socket.close();
//                    System.out.println("切断されました : "
//                            + socket.getRemoteSocketAddress());
//                }
//
//            } catch (IOException e) {
//            }
//
//        }
//
//    }
//}