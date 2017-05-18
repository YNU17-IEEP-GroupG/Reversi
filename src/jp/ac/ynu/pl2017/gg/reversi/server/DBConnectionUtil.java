package jp.ac.ynu.pl2017.gg.reversi.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by shiita on 2017/05/18.
 * ThreadLocalを使いスレッドごとにコネクションを管理できるようにしたユーティリティクラス。
 */
public class DBConnectionUtil {
    private static final ThreadLocal session = new ThreadLocal();
    private static final String URL = "jdbc:ucanaccess://OthelloDB.accdb";

    public static Connection getConnection() {

        Connection con = (Connection) session.get();

        // まだこのThreadに存在しなければ、新しくConnectionをオープンする
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            session.set(con);
        }
        return con;
    }

    public static void closeConnection() {
        Connection con = (Connection) session.get();
        session.set(null);
        if (con != null){
            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
