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
    private final int[] depth = { 2, 4, 4 };
    private final int[] countStart = { 6, 8, 10};

    @Override
    public void think() {
        if (gray) {
            randomThink();
        }
        else {
            Search s = new Search(stone);
            Point point;
            if (BoardHelper.countStone(Stone.Empty, board) > countStart[difficulty])
                point = s.minMax(depth[difficulty], Evaluation.EvaluationType.SQUARE, stone, board);
            else
                point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
            row = point.getRow();
            column = point.getColumn();
        }
    }

    public OmegaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}