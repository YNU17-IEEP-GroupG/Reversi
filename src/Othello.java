import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JFrame implements ActionListener {

    private enum Stone { Empty, Black, White }
    public static final int BOARD_SIZE = 20;
    public static final int IMAGE_ICON_SIZE = 40;
    private static ImageIcon greenFrame = new ImageIcon("image/GreenFrame.jpg");
    private static ImageIcon rolloverIcon = new ImageIcon("image/Rollover.png");
    private static ImageIcon canPutIcon = new ImageIcon("image/CanPut.png");
    private static ImageIcon blackIcon = new ImageIcon("image/Black.jpg");
    private static ImageIcon whiteIcon = new ImageIcon("image/White.jpg");
    private JButton[][] buttonBoard = new JButton[BOARD_SIZE][BOARD_SIZE];
    private Stone[][] board = new Stone[BOARD_SIZE][BOARD_SIZE];
    private Stone myStone; // actionEventで使うため、仕方なくフィールドに

    public Othello() {
        setTitle("Othello");
        getContentPane().setPreferredSize(new Dimension(BOARD_SIZE * IMAGE_ICON_SIZE,BOARD_SIZE * IMAGE_ICON_SIZE));
        pack();

        myStone = Stone.Black;
        initBoard();
        List<Point> hint = makeHint(myStone);
        displayHint(hint);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] position = e.getActionCommand().split(",");
        int r = Integer.parseInt(position[0]); int c = Integer.parseInt(position[1]);
        putStone(r, c, myStone);
    }

    private void putStone(int r, int c, Stone stone) {
        if (!canPut(r, c, stone)) return;
        JButton target = buttonBoard[r][c];
        target.setRolloverIcon(null);
        switch (stone) {
            case Empty: target.setIcon(greenFrame); break;
            case Black: target.setIcon(blackIcon); break;
            case White: target.setIcon(whiteIcon); break;
        }
        board[r][c] = stone;

        // テスト用
        hideHint();
        myStone = getReverse(myStone);
        List<Point> hint = makeHint(myStone);
        displayHint(hint);
//        printBoard();
    }

    private List<Point> makeHint(Stone stone) {
        List<Point> hint = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (canPut(i, j, stone)) {
                    hint.add(new Point(i, j));
                }
            }
        }
        return hint;
    }

    private void displayHint(List<Point> hint) {
        hint.stream().forEach(h -> buttonBoard[h.row][h.column].setIcon(canPutIcon));
    }

    private void hideHint(List<Point> hint) {
        hint.stream()
                .filter(h -> buttonBoard[h.row][h.column].getIcon().equals(canPutIcon))
                .forEach(h -> buttonBoard[h.row][h.column].setIcon(greenFrame));
    }

    private void hideHint() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (buttonBoard[i][j].getIcon().equals(canPutIcon))
                    buttonBoard[i][j].setIcon(greenFrame);
    }

    private boolean canPut(int r, int c, Stone stone) {
        if (board[r][c] != Stone.Empty) return false;
        boolean b1 = checkLine(r, c , stone, -1, 0);
        boolean b2 = checkLine(r, c , stone, -1, 1);
        boolean b3 = checkLine(r, c , stone, 0 , 1);
        boolean b4 = checkLine(r, c , stone, 1 , 1);
        boolean b5 = checkLine(r, c , stone, 1 , 0);
        boolean b6 = checkLine(r, c , stone, 1 , -1);
        boolean b7 = checkLine(r, c , stone, 0 , -1);
        boolean b8 = checkLine(r, c , stone, -1, -1);
        return b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8;
    }

    private boolean checkLine (int r, int c, Stone stone, int dr, int dc) {
        int i = r + dr;
        int j = c + dc;
        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if      (board[i][j] == Stone.Empty) break;
            else if (board[i][j] == stone){
                if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1) return true;
                else break;
            }
            i += dr;
            j += dc;
        }
        return false;
    }

    private Stone getReverse(Stone stone) {
        return stone == Stone.Black ? Stone.White : Stone.Black;
    }

    private void initBoard() {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton box = new JButton(greenFrame);
                box.setRolloverIcon(rolloverIcon);
                box.setBorder(new EmptyBorder(0,0,0,0));
                box.setActionCommand(i + "," + j);
                box.addActionListener(this);
                add(box);
                buttonBoard[i][j] = box;
                board[i][j] = Stone.Empty;
            }
        }
        board[3][3] = Stone.Black; board[3][4] = Stone.White;
        board[4][3] = Stone.White; board[4][4] = Stone.Black;
        buttonBoard[3][3].setIcon(blackIcon); buttonBoard[3][4].setIcon(whiteIcon);
        buttonBoard[4][3].setIcon(whiteIcon); buttonBoard[4][4].setIcon(blackIcon);
        buttonBoard[3][3].setRolloverIcon(null); buttonBoard[3][4].setRolloverIcon(null);
        buttonBoard[4][3].setRolloverIcon(null); buttonBoard[4][4].setRolloverIcon(null);
        // テスト用配置
//        board[3][3] = Stone.Black; board[3][4] = Stone.Black; board[3][5] = Stone.Black;
//        board[4][3] = Stone.Black; board[4][4] = Stone.White; board[4][5] = Stone.Black;
//        board[5][3] = Stone.Black; board[5][4] = Stone.Black; board[5][5] = Stone.Black;
//        buttonBoard[3][3].setIcon(blackIcon); buttonBoard[3][4].setIcon(blackIcon); buttonBoard[3][5].setIcon(blackIcon);
//        buttonBoard[4][3].setIcon(blackIcon); buttonBoard[4][4].setIcon(whiteIcon); buttonBoard[4][5].setIcon(blackIcon);
//        buttonBoard[5][3].setIcon(blackIcon); buttonBoard[5][4].setIcon(blackIcon); buttonBoard[5][5].setIcon(blackIcon);
//        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new Othello();
    }

    private class Point {
        int row = 0;
        int column = 0;

        public Point(int row, int column) throws PointException {
            if (0 <= row && row <= BOARD_SIZE)
                this.row = row;
            else
                throw new PointException("Pointの行の値が範囲外です。");
            if (0 <= column && column <= BOARD_SIZE)
                this.column = column;
            else
                throw new PointException("Pointの列の値が範囲外です。");
        }

        @Override
        public String toString() {
            return String.format("(row, column) = (%d, %d)", row, column);
        }
    }

    private class PointException extends RuntimeException {
        public PointException(String message) {
            super(message);
        }
    }
}