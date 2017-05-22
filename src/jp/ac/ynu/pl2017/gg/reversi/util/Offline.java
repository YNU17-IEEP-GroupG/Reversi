package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.Serializable;

/**
 * Created by shiita on 2017/05/20.
 * オフラインでの戦績データのクラス
 */
public class Offline implements Serializable {
	private static final long	serialVersionUID	= 30474520392481L;

	private	int	hardWin		= 0;
	private	int	hardLose	= 0;
	private	int	normalWin	= 0;
	private	int	normalLose	= 0;
	private	int	easyWin		= 0;
	private	int	easyLose	= 0;

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

	public void hardWinInc() {
		hardWin++;
	}

	public void hardLoseInc() {
		hardLose++;
	}

	public void normalWinInc() {
		normalWin++;
	}

	public void normalLoseInc() {
		normalLose++;
	}

	public void easyWinInc() {
		easyWin++;
	}

	public void easyLoseInc() {
		easyLose++;
	}

	/**
	 * オフライン成績をまとめて取得
	 * @return １次元は難易度(0:弱～2:強); 2次元は勝敗
	 */
	public int[][] getWLLists() {
		return new int[][]{
				{getEasyWin(), getEasyLose()},
				{getNormalWin(), getNormalLose()},
				{getHardWin(), getHardLose()}
				};
	}
}
