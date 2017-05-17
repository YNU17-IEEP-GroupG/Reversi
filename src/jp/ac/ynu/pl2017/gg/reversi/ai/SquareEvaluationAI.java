package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;
import java.util.Random;

import static jp.ac.ynu.pl2017.gg.reversi.gui.Othello.BOARD_SIZE;

/**
 * Created by shiita on 2017/05/10.
 */
public class SquareEvaluationAI implements BaseAI {
    private Stone[][] board = new Stone[BOARD_SIZE][BOARD_SIZE];
    private Stone stone;
    private int depth;
    private List<Point> hint;
    private int row;
    private int column;
    private int countStart;

    public SquareEvaluationAI(Stone[][] board, Stone stone, int depth, int countStart, List<Point> hint) {
        this.board = board;
        this.stone = stone;
        this.hint = hint;
        this.depth = depth;
        this.countStart = countStart;
        row = hint.get(0).getRow();
        column = hint.get(0).getColumn();
    }

    @Override
    public void think() {
        Search s = new Search(stone);
        Point point;
        if (BoardHelper.countStone(Stone.Empty, board) > countStart)
            point = s.minMax(depth, Evaluation.EvaluationType.SQUARE, stone, board);
        else
            point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
        row = point.getRow();
        column = point.getColumn();
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

