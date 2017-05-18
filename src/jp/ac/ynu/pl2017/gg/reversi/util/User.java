package jp.ac.ynu.pl2017.gg.reversi.util;

/**
 * Created by shiita on 2017/05/18.
 * ユーザデータのクラス。無効なデータには-1かnullが格納されている。
 */
public class User {
    private int id = -1;
    private String userName = null;
    private int icon = -1;

    private int onlineWin = -1;
    private int onlineLose = -1;

    private Offline alpha = null;
    private Offline beta = null;
    private Offline gamma = null;
    private Offline omega = null;

    public class Offline {
        private int hardWin;
        private int hardLose;
        private int normalWin;
        private int normalLose;
        private int easyWin;
        private int easyLose;

        public int getHardWin() {
            return hardWin;
        }

        public void setHardWin(int hardWin) {
            this.hardWin = hardWin;
        }

        public int getHardLose() {
            return hardLose;
        }

        public void setHardLose(int hardLose) {
            this.hardLose = hardLose;
        }

        public int getNormalWin() {
            return normalWin;
        }

        public void setNormalWin(int normalWin) {
            this.normalWin = normalWin;
        }

        public int getNormalLose() {
            return normalLose;
        }

        public void setNormalLose(int normalLose) {
            this.normalLose = normalLose;
        }

        public int getEasyWin() {
            return easyWin;
        }

        public void setEasyWin(int easyWin) {
            this.easyWin = easyWin;
        }

        public int getEasyLose() {
            return easyLose;
        }

        public void setEasyLose(int easyLose) {
            this.easyLose = easyLose;
        }
    }

    public User() {
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
