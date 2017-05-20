package jp.ac.ynu.pl2017.gg.reversi.server;

import jp.ac.ynu.pl2017.gg.reversi.util.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shiita on 2017/05/18.
 * MSAccessとのデータのやり取りを仲介するクラス。DBへのアクセスには必ずこのクラスを通す必要がある。
 * 必ずスレッドの終了時にcloseConnection()メソッドでコネクションを切断しなければならない。
 */
public class Access {

    private static final String[] table = { "alpha", "beta", "gamma", "omega", "online" };
    private static final String[] difficulties = { "easy_", "normal_", "hard_", "" };
    private static final String[] judgements = { "win", "lose" };

    /**
     * ログイン
     * @param name ユーザネーム
     * @param pass パスワード
     * @return ログインの可否
     */
    public static boolean login(String name, String pass) {
        String sql = "SELECT * FROM user WHERE user_name = " + q(name) + " AND password = " + q(pass);
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
     * @param name ユーザネーム
     * @param type 更新する種類。α:0 β:1 γ:2 ω:3 オンライン:4
     * @param difficulty 更新する難易度。easy:0 normal:1 hard:2 オンライン:3
     * @param judgement 更新する勝敗。負け:0 勝ち:1
     * @param difference 更新する差分。
     * @return 更新の可否
     */
    public static boolean updateResult(String name, int type, int difficulty, int judgement, int difference) {
        String field = difficulties[difficulty] + judgements[judgement];
        int userId = getUserId(name);
        if (userId == -1) return false;
        String sql = "UPDATE " + table[type] + " SET " + field + " = " + field + " + " + difference + " WHERE user_id = " + userId;
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
     * ユーザデータを更新する。変更前と変更後のユーザ名が同じ場合パスワードの更新をする。
     * 既に新しいユーザ名が使われていた場合にはfalseを返すが、呼び出し側でexists()を用いて呼び出さないようにしてほしい
     * @param oldName 更新前のユーザネーム
     * @param newName 更新後のユーザネーム
     * @param newPass 更新後のパスワード
     * @return 更新の可否
     */
    public static boolean updateUser(String oldName, String newName, String newPass) {
        int userId = getUserId(oldName);
        // 古いユーザネームが存在しない または 新しいユーザネームが既に存在する
        if (userId == -1 || (exists(newName) && !newName.equals(oldName))) return false;

        String sql = "UPDATE user SET user_name = " + q(newName) + ", password = " + q(newPass) + " WHERE id = " + userId;
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
     * @param name ユーザ名
     * @param icon パスワード
     * @return 更新の可否
     */
    public static boolean updateIcon(String name, int icon) {
        String sql = "UPDATE user SET icon = " + icon + " WHERE user_name = " + q(name);
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
     * @param name ユーザ名
     * @param back パスワード
     * @return 更新の可否
     */
    public static boolean updateBack(String name, int back) {
        String sql = "UPDATE user SET background = " + back + " WHERE user_name = " + q(name);
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
     * ユーザ名が既に登録済みかどうかを確認する
     * @param name ユーザ名
     * @return 登録済みかどうか
     */
    public static boolean exists(String name) {
        if (getUserId(name) == -1)
            return false;
        else
            return true;
    }

    /**
     * 利用しているスレッドのDBへのコネクションを切断する
     * @throws Exception
     */
    public static void closeConnection() throws Exception {
        DBConnectionUtil.closeConnection();
    }

    // 文字列を""で囲った文字列に変換。java -> "java"
    private static String q(String string) {
        return "\"" + string + "\"";
    }

    private static int getUserId(String name) {
        String sql = "SELECT * FROM user WHERE user_name = " + q(name);

        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            if (!result.next()) return -1;
            return result.getInt("id");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /*==================== 以下デバッグ用 ====================*/

    public static String getAllUserString() {
        String sql = "SELECT * FROM user";
        System.out.println("getAllUserString: sql = " + sql);

        StringBuffer sb = new StringBuffer();
        sb.append("id, user_name, password, item, icon, background\n");
        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            while (result.next()) {
                sb.append(result.getInt("id")); sb.append(", ");
                sb.append(result.getString("user_name")); sb.append(", ");
                sb.append(result.getString("password")); sb.append(", ");
                sb.append(result.getInt("item")); sb.append(", ");
                sb.append(result.getInt("icon")); sb.append(", ");
                sb.append(result.getInt("background")); sb.append("\n");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAllOfflineString(String type) {
        String sql = "SELECT * FROM " + type;
        System.out.println("getAllOfflineString: sql = " + sql);

        StringBuffer sb = new StringBuffer();
        sb.append("user_id, hard_win, hard_lose, normal_win, normal_lose, easy_win, easy_lose\n");
        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            while (result.next()) {
                sb.append(result.getInt("user_id")); sb.append(", ");
                sb.append(result.getInt("hard_win")); sb.append(", ");
                sb.append(result.getInt("hard_lose")); sb.append(", ");
                sb.append(result.getInt("normal_win")); sb.append(", ");
                sb.append(result.getInt("normal_lose")); sb.append(", ");
                sb.append(result.getInt("easy_win")); sb.append(", ");
                sb.append(result.getInt("easy_lose")); sb.append("\n");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAllOnlineString() {
        String sql = "SELECT * FROM online";
        System.out.println("getAllOnlineString: sql = " + sql);

        StringBuffer sb = new StringBuffer();
        sb.append("user_id, win, lose\n");
        Connection con = DBConnectionUtil.getConnection();
        try (
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sql);
        ) {
            while (result.next()) {
                sb.append(result.getInt("user_id")); sb.append(", ");
                sb.append(result.getInt("win")); sb.append(", ");
                sb.append(result.getInt("lose")); sb.append("\n");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
