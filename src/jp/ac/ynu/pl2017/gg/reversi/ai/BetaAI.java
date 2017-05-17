package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/05/17.
 * ビームサーチとMinMax法を両方使う
 */
public class BetaAI extends BaseAI {
    Random random;

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
        Point point = hint.get(0);
        int select = random.nextInt(2);
        if (select == 0)
            point = s.minMax(depth, Evaluation.EvaluationType.SQUARE, stone, board);
        else if (select == 1)
            point = s.beamSearch(Evaluation.EvaluationType.SQUARE, board, 20);
        row = point.getRow();
        column = point.getColumn();
    }

    public BetaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
        random = new Random();
    }
}
