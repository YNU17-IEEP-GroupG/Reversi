package jp.ac.ynu.pl2017.gg.reversi.gui;

public interface PlayCallback {
	/**
	 * ターン変更
	 */
	void onTurnChange(boolean isMyTurn, int[] pCountStones);
	
	/**
	 * アイテム取得
	 */
	void onGainItem(boolean playerTurn);
	
	/**
	 * アイテム使用可能
	 */
	void enableItem();
	
	/**アイテム使用不可
	 * 
	 */
	void disableItem();

	/**
	 * 対局相手がアイテムを使用した
	 */
	void onOpponentUseItem();

	/**
	 * 対局終了
	 */
	void onGameOver();
	
	/**
	 * 対局が途中で終了した
	 */
	void onGameAborted();

}
