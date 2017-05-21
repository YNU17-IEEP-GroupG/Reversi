package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/05/17.
 * ビームサーチとMinMax法を両方使う
 */
public class BetaAI extends BaseAI {
    private final int[] beamDepth = { 4, 6, 10 };
    private final int[] countStart = { 2, 6, 8 };
    private final int[] limit = { 4, 10, 20 };
    private final int[] minMaxDepth = { 2, 2, 4 };

    @Override
    public void think() {
        if (gray) {
            randomThink();
        }
        else {
            Search s = new Search(stone);
            Point point;
            if (BoardHelper.countStone(Stone.Empty, board) > countStart[difficulty]) {
                if (new Random().nextBoolean())
                    point = s.minMax(minMaxDepth[difficulty], Evaluation.EvaluationType.SQUARE, stone, board);
                else
                    point = s.beamSearch(Evaluation.EvaluationType.SQUARE, board, limit[difficulty], beamDepth[difficulty]);
            }
            else {
                point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
            }
            row = point.getRow();
            column = point.getColumn();
        }
    }

    public BetaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}
