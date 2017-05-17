package jp.ac.ynu.pl2017.gg.reversi.gui;

public interface PlayEndCallback {
	
	/**
	 * 対局終了
	 */
	void onGameOver();
	
	/**
	 * 対局が途中で終了した
	 */
	void onGameAborted();

}
