package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/17.
 * MinMax法を使い、終盤は全探索で最良の手を読むAI
 * GammaAIよりも深く読む
 */
public class OmegaAI extends BaseAI {

    @Override
    public void think() {
        int depth = 4;
        int countStart = 10;
        Search s = new Search(stone);
        switch (difficulty) {
            case 0:
                depth = 2;
                countStart = 6;
                break;
            case 1:
                depth = 4;
                countStart = 8;
                break;
            case 2:
                depth = 4;
                countStart = 10;
                break;
        }
        Point point;
        if (BoardHelper.countStone(Stone.Empty, board) > countStart)
            point = s.minMax(depth, Evaluation.EvaluationType.SQUARE, stone, board);
        else
            point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
        row = point.getRow();
        column = point.getColumn();
    }

    public OmegaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}
