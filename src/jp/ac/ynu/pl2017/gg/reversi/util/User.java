package jp.ac.ynu.pl2017.gg.reversi.util;

/**
 * Created by shiita on 2017/05/18.
 * ユーザデータのクラス
 */
public class User {
    private int id = -1;
    private String userName = "";
    private int item = -1;
    private int icon = -1;
    private int background = -1;

    private int onlineWin = -1;
    private int onlineLose = -1;

    private Offline alpha = new Offline();
    private Offline beta = new Offline();
    private Offline gamma = new Offline();
    private Offline omega = new Offline();

    public User() {
    }

    @Override
    public String toString() {
        return "id = " + id + ", userName = " + userName + ", item =" + item + ", icon = " + icon + ", background = " + background + ", onlineWin = " + onlineWin + ", onlineLose = " + onlineLose + "\nalpha:" + alpha.toString() + "\nbeta:" + beta.toString() + "\ngamma:" + gamma.toString() + "\nomega:" + omega.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getOnlineWin() {
        return onlineWin;
    }

    public void setOnlineWin(int onlineWin) {
        this.onlineWin = onlineWin;
    }

    public int getOnlineLose() {
        return onlineLose;
    }

    public void setOnlineLose(int onlineLose) {
        this.onlineLose = onlineLose;
    }

    public Offline getAlpha() {
        return alpha;
    }

    public void setAlpha(Offline alpha) {
        this.alpha = alpha;
    }

    public Offline getBeta() {
        return beta;
    }

    public void setBeta(Offline beta) {
        this.beta = beta;
    }

    public Offline getGamma() {
        return gamma;
    }

    public void setGamma(Offline gamma) {
        this.gamma = gamma;
    }

    public Offline getOmega() {
        return omega;
    }

    public void setOmega(Offline omega) {
        this.omega = omega;
    }
}
