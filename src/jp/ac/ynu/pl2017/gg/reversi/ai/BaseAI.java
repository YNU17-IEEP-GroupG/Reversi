package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.List;
import java.util.Random;

import static jp.ac.ynu.pl2017.gg.reversi.gui.Othello.BOARD_SIZE;

/**
 * Created by shiita on 2017/05/10.
 */
public abstract class BaseAI {
    protected List<Point> hint;
    protected Stone stone;
    protected Stone[][] board = new Stone[BOARD_SIZE][BOARD_SIZE];
    protected int difficulty;
    protected int row;
    protected int column;

    public abstract void think();

    public BaseAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        this.hint = hint;
        this.stone = stone;
        this.board = board;
        this.difficulty = difficulty;
        row = hint.get(0).getRow();
        column = hint.get(0).getColumn();
    }

    public void randomThink() {
        Point point = hint.get(new Random().nextInt(hint.size()));
        row = point.getRow();
        column = point.getColumn();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
