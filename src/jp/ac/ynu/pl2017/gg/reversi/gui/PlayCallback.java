package jp.ac.ynu.pl2017.gg.reversi.gui;

public interface PlayCallback {
	
	/**
	 * アイテム取得
	 */
	void onGainItem();
	
	/**
	 * 対局終了
	 */
	void onGameOver();
	
	/**
	 * 対局が途中で終了した
	 */
	void onGameAborted();

}
