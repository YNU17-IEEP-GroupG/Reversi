package jp.ac.ynu.pl2017.gg.reversi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static jp.ac.ynu.pl2017.gg.reversi.gui.Othello.BOARD_SIZE;
/**
 * Created by shiita on 2017/05/17.
 * AIの先読みに利用
 */
public class BoardHelper {
    public static List<Point> makeHint(Stone stone, Stone[][] board) {
        List<Point> hint = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (!selectDirections(i, j, stone, board).isEmpty())
                    hint.add(new Point(i, j));
        return hint;
    }

    public static EnumSet<Direction> selectDirections(int r, int c, Stone stone, Stone[][] board) {
        EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        if (board[r][c] != Stone.Empty) return directions;
        EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> checkLine(r, c, stone, d, board))
                .forEach(directions::add);
        return directions;
    }

    public static boolean checkLine (int r, int c, Stone stone, Direction direction, Stone[][] board) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if      (board[i][j] == Stone.Empty || board[i][j] == Stone.Ban) break;
            else if (board[i][j] == stone){
                if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1) return true;
                else break;
            }
            i += dr; j += dc;
        }
        return false;
    }

    public static List<Point> putStone(int r, int c, Stone stone, Stone[][] board) {
        EnumSet<Direction> directions = BoardHelper.selectDirections(r, c, stone, board);
        if (directions.isEmpty()) return new ArrayList<>();
        board[r][c] = stone;
        return reverseStone(r, c, stone, directions, board);
    }

    public static List<Point> reverseStone(int r, int c, Stone stone, EnumSet<Direction> directions, Stone[][] board) {
        return directions.stream()
                .map(d -> reverseLine(r, c, stone, d, board))
                .flatMap(pl -> pl.stream())
                .collect(Collectors.toList());
    }

    public static List<Point> reverseLine(int r, int c, Stone stone, Direction direction, Stone[][] board) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;
        Stone reverse = stone.getReverse();
        List<Point> pList = new ArrayList<>();

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if (board[i][j] == reverse) {
                board[i][j] = stone;
                pList.add(new Point(i, j));
            }
            else {
                break;
            }
            i += dr; j += dc;
        }
        return pList;
    }

    public static void undo(int r, int c, Stone stone, List<Point> pList, Stone[][] board) {
        Stone reverse = stone.getReverse();
        pList.stream().forEach(p -> board[p.row][p.column] = reverse);
        board[r][c] = Stone.Empty;
    }

    public static int countStone(Stone stone, Stone[][] board) {
        return (int) Arrays.stream(board)
                .mapToLong(ss -> Arrays.stream(ss)
                        .filter(s -> s == stone)
                        .count())
                .sum();
    }

    public static Stone[][] cloneBoard(Stone[][] board) {
        return Arrays.stream(board)
                .map(ss -> ss.clone())
                .toArray(Stone[][]::new);
    }

    public static List<Point> getPoints(Stone stone, Stone[][] board) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == stone) {
                    points.add(new Point(i, j));
                }
            }
        }
        return  points;
    }

    public static void printBoard(Stone[][] board) {
        for (Stone[] stones : board) {
            for (Stone stone : stones) System.out.print(stone + " ");
            System.out.println();
        }
        System.out.println();
    }
}
