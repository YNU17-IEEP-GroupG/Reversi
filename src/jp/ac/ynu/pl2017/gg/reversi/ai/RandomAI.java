package jp.ac.ynu.pl2017.gg.reversi.ai;
import java.util.List;
import java.util.Random;

import jp.ac.ynu.pl2017.gg.reversi.util.Point;

/**
 * Created by shiita on 2017/04/29.
 */
public class RandomAI implements BaseAI{
    private List<Point> hint;
    private int row;
    private int column;

    public RandomAI(List<Point> hint) {
        this.hint = hint;
        row = hint.get(0).getRow();
        column = hint.get(0).getColumn();
    }

    @Override
    public void think() {
        randomThink();
    }

    @Override
    public void randomThink() {
        Point point = hint.get(new Random().nextInt(hint.size()));
        row = point.getRow();
        column = point.getColumn();
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
