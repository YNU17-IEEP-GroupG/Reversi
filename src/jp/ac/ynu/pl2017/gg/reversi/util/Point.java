package jp.ac.ynu.pl2017.gg.reversi.util;

import jp.ac.ynu.pl2017.gg.reversi.gui.Othello;

/**
 * Created by shiita on 2017/04/29.
 */
public class Point {
    int row = 0;
    int column = 0;

    public Point(int row, int column) throws PointException {
        if (0 <= row && row <= Othello.BOARD_SIZE)
            this.row = row;
        else
            throw new PointException("Pointの行の値が範囲外です。");
        if (0 <= column && column <= Othello.BOARD_SIZE)
            this.column = column;
        else
            throw new PointException("Pointの列の値が範囲外です。");
    }
    
    public int getColumn() {
		return column;
	}
    
    public int getRow() {
		return row;
	}

    @Override
    public String toString() {
        return String.format("(row, column) = (%d, %d)", row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (row != point.row) return false;
        return column == point.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    private class PointException extends RuntimeException {
        public PointException(String message) {
            super(message);
        }
    }
}