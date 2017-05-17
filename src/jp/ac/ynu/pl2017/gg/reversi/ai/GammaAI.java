package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/17.
 * MinMax法のみを使用するAI
 */
public class GammaAI extends BaseAI {

    @Override
    public void think() {
        int depth = 2;
        Search s = new Search(stone);
        switch (difficulty) {
            case 0:
                depth = 2;
                break;
            case 1:
                depth = 2;
                break;
            case 2:
                depth = 4;
                break;
        }
        Point point;
        point = s.minMax(depth, Evaluation.EvaluationType.SQUARE, stone, board);
        row = point.getRow();
        column = point.getColumn();
    }

    public GammaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}
