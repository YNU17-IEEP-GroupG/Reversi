package jp.ac.ynu.pl2017.gg.reversi.ai;

import java.util.ArrayList;
import java.util.List;

import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

public class OnlineDummyAI extends BaseAI {
	private OnlineItem callback = null;

	public OnlineDummyAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
		super(hint, stone, board, difficulty);
		row = -1;
		column = -1;
	}

	@Override
	public void think() {
		Item item = ClientConnection.receivePutStone();
		int[] pos;
		switch (item) {
			case TRIPLE:
				callback.reflectTriple();
				break;
			case GRAY:
				callback.reflectGray();
				break;
			case DROP:
				pos = item.getPos();
				callback.reflectDrop(new Point(pos[0], pos[1]));
				break;
			case BAN:
				pos = item.getPos();
				List<Point> banPoints = new ArrayList<>();
				for (int i = 0; i < 3; i++) {
					banPoints.add(new Point(pos[i * 2], pos[i * 2 + 1]));
				}
				callback.reflectBan(banPoints);
				break;
			case NONE:
				break;
		}

		// 教訓：下手に別スレッドで待ってはいけない
		int[] coordinate = item.getCoordinate();
		System.err.println("受信しました");
		row = coordinate[0];
		column = coordinate[1];
	}
	
	@Override
	public void randomThink() {
	}

	public void setCallback(OnlineItem callback) {
		this.callback = callback;
	}

	// アイテム使用をDummyAIから自身の盤面に反映するためのインターフェース
	public interface OnlineItem {
		void reflectBan(List<Point> banPoints);
		void reflectDrop(Point point);
		void reflectGray();
		void reflectTriple();
	}
}
