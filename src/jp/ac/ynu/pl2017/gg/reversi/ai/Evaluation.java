package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import static jp.ac.ynu.pl2017.gg.reversi.gui.Othello.BOARD_SIZE;

/**
 * 自分と相手の局面の評価の差分を返すstaticメソッドを持つ
 * Created by shiita on 2017/05/10.
 */
public class Evaluation {
    public enum EvaluationType { SQUARE, COUNT }
    // TODO: 複数の評価配列を作成する。
    private static final int[][] square =
            {{ 30, -12,  0, -1, -1,  0, -12,  30},
                    {-12, -15, -3, -3, -3, -3, -15, -12},
                    {  0,  -3,  0, -1, -1,  0,  -3,   0},
                    { -1,  -3, -1,  0,  0, -1,  -3,  -1},
                    { -1,  -3, -1,  0,  0, -1,  -3,  -1},
                    {  0,  -3,  0, -1, -1,  0,  -3,   0},
                    {-12, -15, -3, -3, -3, -3, -15, -12},
                    { 30, -12,  0, -1, -1,  0, -12,  30},};

    public static int squareEvaluation(Stone stone, Stone[][] board) {
        Stone reverse = stone.getReverse();
        int value = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == stone)
                    value += square[i][j];
                else if (board[i][j] == reverse)
                    value -= square[i][j];
            }
        }

        // 勝敗が決まった場合には、絶対的に大きい(小さい)評価値を返す
        if (BoardHelper.countStone(stone, board) == 0) {
            return -(1 << 29);
        }
        else if (BoardHelper.countStone(reverse, board) == 0) {
            return (1 << 29);
        }

        return value;
    }

    public static int countEvaluation(Stone stone, Stone[][] board) {
        return BoardHelper.countStone(stone, board) - BoardHelper.countStone(stone.getReverse(), board);
    }
}
