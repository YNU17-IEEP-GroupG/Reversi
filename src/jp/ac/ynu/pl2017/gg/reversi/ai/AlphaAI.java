package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/17.
 */
public class AlphaAI extends BaseAI {

    @Override
    public void think() {
        int limit = 2;
        int countStart = 2;
        Search s = new Search(stone);
        switch (difficulty) {
            case 0:
                limit = 2;
                countStart = 0;
                break;
            case 1:
                limit = 3;
                countStart = 4;
                break;
            case 2:
                limit = 5;
                countStart = 8;
                break;
        }
        Point point;
        if (BoardHelper.countStone(Stone.Empty, board) > countStart)
            point = s.beamSearch(Evaluation.EvaluationType.COUNT, board, limit);
        else
            point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
        row = point.getRow();
        column = point.getColumn();
    }

    public AlphaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}
