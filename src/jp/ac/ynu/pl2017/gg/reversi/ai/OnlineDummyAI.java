package jp.ac.ynu.pl2017.gg.reversi.ai;

import java.util.List;

import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

public class OnlineDummyAI extends BaseAI {

	public OnlineDummyAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
		super(hint, stone, board, difficulty);
	}

	@Override
	public void think() {
		new Thread() {
			@Override
			public void run() {
				int pos[] = ClientConnection.receivePutStone();
				if (pos == null) {
					row = -1;
					column = -1;
				}
				row = pos[0];
				column = pos[1];
			}
		}.start();
	}
	@Override
	public void randomThink() {
	}

}
