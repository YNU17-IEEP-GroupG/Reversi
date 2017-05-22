package jp.ac.ynu.pl2017.gg.reversi.ai;

import java.util.List;

import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

public class OnlineDummyAI extends BaseAI {

	public OnlineDummyAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
		super(hint, stone, board, difficulty);
		row = -1;
		column = -1;
	}

	@Override
	public void think() {
		// 教訓：下手に別スレッドで待ってはいけない
		int pos[] = ClientConnection.receivePutStone();
		if (pos == null) {
			row = -1;
			column = -1;
		}
		row = pos[0];
		column = pos[1];
	}
	
	@Override
	public void randomThink() {
	}

}
