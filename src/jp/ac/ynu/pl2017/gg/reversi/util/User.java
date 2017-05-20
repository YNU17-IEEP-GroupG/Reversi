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

    // indexは0~3が順にalpha,beta,gamma,omegaに対応
    private Offline[] offlines = new Offline[4];

    public User() {
        for (int i = 0; i < offlines.length; i++)
            offlines[i] = new Offline();
    }

    @Override
    public String toString() {
        return "id = " + id + ", userName = " + userName + ", item =" + item + ", icon = " + icon + ", background = " + background + ", onlineWin = " + onlineWin + ", onlineLose = " + onlineLose + "\nalpha:" + offlines[0].toString() + "\nbeta:" + offlines[1].toString() + "\ngamma:" + offlines[2].toString() + "\nomega:" + offlines[3].toString();
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

    public Offline[] getOfflines() {
        return offlines;
    }

    public void setOfflines(Offline[] offlines) {
        this.offlines = offlines;
    }
}
