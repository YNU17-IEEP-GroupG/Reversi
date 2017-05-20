package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/05/17.
 * MinMax法のみを使用するAI
 */
public class GammaAI extends BaseAI {
    private final int[] depth = { 2, 2, 4 };

    @Override
    public void think() {
        Search s = new Search(stone);
        Point point;
        if (difficulty == 0 && new Random().nextBoolean())
            point = hint.get(new Random().nextInt(hint.size()));
        else
            point = s.minMax(depth[difficulty], Evaluation.EvaluationType.SQUARE, stone, board);
        row = point.getRow();
        column = point.getColumn();
    }

    public GammaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}