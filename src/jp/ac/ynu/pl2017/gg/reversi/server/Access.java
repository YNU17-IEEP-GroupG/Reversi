package jp.ac.ynu.pl2017.gg.reversi.server;

import jp.ac.ynu.pl2017.gg.reversi.util.User;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shiita on 2017/05/18.
 * MSAccessとのデータのやり取りを仲介するクラス。DBへのアクセスには必ずこのクラスを通す必要がある。
 * 必ずスレッドの終了時にcloseConnection()メソッドでコネクションを切断しなければならない。
 */
public class Access {

    /**
     * ログイン
     * @param name ユーザネーム
     * @param pass パスワード
     * @return ログインの可否
     */
    public static boolean login(String name, String pass) {
        String sql = "SELECT * FROM user WHERE user_name = " + name + ", password = " + pass;
        System.out.println("login: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            return result.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 勝ち負けを更新する
     * @param id ユーザID
     * @param type 更新する種類。α:0 β:1 γ:2 ω:3 オンライン:4
     * @param difficulty 更新する難易度。easy:0 normal:1 hard:2 オンライン:4
     * @param win 更新する勝敗。負け:0 勝ち:1
     * @param difference 更新する差分。
     * @return 更新の可否
     */
    public static boolean updateResult(int id, int type, int difficulty, int win, int difference) {
        String table = "online";
        String diffString = "";
        String winString = "win";
        switch (type) {
            case 0:
                table = "offline_alpha";
                break;
            case 1:
                table = "offline_beta";
                break;
            case 2:
                table = "offline_gamma";
                break;
            case 3:
                table = "offline_omega";
                break;
            case 4:
                table = "online";
                break;
        }
        switch (difficulty) {
            case 0:
                diffString = "easy_";
                break;
            case 1:
                diffString = "normal_";
                break;
            case 2:
                diffString = "hard_";
                break;
            case 3:
                diffString = "";
                break;
        }
        if (win == 1)      winString = "win";
        else if (win == 0) winString = "lose";
        String field = diffString + winString;
        String sql = "UPDATE " + table + "SET " + field + " = " + field + " + " + difference + " WHERE id = " + id;
        System.out.println("updateResult: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
        ) {
            int ret = stmt.executeUpdate(sql);
            if (ret != 0)
                return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ユーザデータを更新する
     * @param oldName 更新前のユーザネーム
     * @param newName 更新後のユーザネーム
     * @param newPass 更新後のパスワード
     * @return 更新の可否
     */
    public static boolean updateUser(String oldName, String newName, String newPass) {
        String sql = "UPDATE user SET user_name = " + newName + ", password = " + newPass + " WHERE user_name = " + oldName;
        System.out.println("updateUser: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
        ) {
            int ret = stmt.executeUpdate(sql);
            if (ret != 0)
                return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * アイコンを更新する
     * @param id ユーザID
     * @param icon パスワード
     * @return 更新の可否
     */
    public static boolean updateIcon(int id, int icon) {
        String sql = "UPDATE user SET icon = " + icon + " WHERE id = " + id;
        System.out.println("updateIcon: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
        ) {
            int ret = stmt.executeUpdate(sql);
            if (ret != 0)
                return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 背景を更新する
     * @param id ユーザID
     * @param back パスワード
     * @return 更新の可否
     */
    public static boolean updateBack(int id, int back) {
        String sql = "UPDATE user SET background = " + back + " WHERE id = " + id;
        System.out.println("updateBack: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
        ) {
            int ret = stmt.executeUpdate(sql);
            if (ret != 0)
                return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * ユーザデータの一部を取得。オンライン対戦の相手のデータ取得などに利用
     * @param name ユーザ名
     * @return userName,icon,onlineWin,onlineLoseのみのUserクラスのインスタンス
     */
    public static User requestUserData(String name) {
        String sql = "SELECT * FROM user U, online O WHERE U.user_name = " + name + " AND U.id = O.user_id";
        System.out.println("requestUserData: sql = " + sql);

        User user = new User();
        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            result.next();
            user.setUserName(result.getString("user_name"));
            user.setIcon(result.getInt("icon"));
            user.setOnlineWin(result.getInt("win"));
            user.setOnlineLose(result.getInt("lose"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 全てのユーザーデータの取得。設定画面のデータ取得などに利用
     * @param name ユーザ名
     * @return 情報が完全なUserクラスのインスタンス
     */
    public static User requestFullUserData(String name) {
        User user = requestUserData(name);
        int id = user.getId();
        // TODO: このクエリで何が返ってくるかよくわからないので、テストしてから書き直す
        String sql = "SELECT * FROM alpha A, beta B, gamma C, omega Z WHERE A.user_id = " + id +
                " AND B.user_id = " + id +
                " AND C.user_id = " + id +
                " AND Z.user_id = " + id;
        System.out.println("requestFullUserData: sql = " + sql);

        Connection con = DBConnectionUtil.getConnection();
        try (
                Statement stmt = con.createStatement();
                ResultSet result = stmt.executeQuery(sql);
        ) {
            result.next();
            // あとで書く
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 利用しているスレッドのDBへのコネクションを切断する
     * @throws Exception
     */
    public static void closeConnection() throws Exception {
        DBConnectionUtil.closeConnection();
    }
}
