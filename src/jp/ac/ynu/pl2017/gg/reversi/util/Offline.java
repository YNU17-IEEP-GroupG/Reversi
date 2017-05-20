package jp.ac.ynu.pl2017.gg.reversi.util;

/**
 * Created by shiita on 2017/05/20.
 * オフラインでの戦績データのクラス
 */
public class Offline {
    private int hardWin = -1;
    private int hardLose = -1;
    private int normalWin = -1;
    private int normalLose = -1;
    private int easyWin = -1;
    private int easyLose = -1;

    public Offline() {
    }

    @Override
    public String toString() {
        return "hardWin = " + hardWin + ", hardLose = " + hardLose + ", normalWin = " + normalWin + ", normalLose = " + normalLose + ", easyWin = " + easyWin + ", easyLose = " + easyLose;
    }

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
