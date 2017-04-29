import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JFrame implements ActionListener {

    public static final int BOARD_SIZE = 8;
    public static final int IMAGE_ICON_SIZE = 40;
    public static final ImageIcon emptyIcon    = new ImageIcon("image/Empty.jpg");
    public static final ImageIcon blackIcon    = new ImageIcon("image/Black.jpg");
    public static final ImageIcon whiteIcon    = new ImageIcon("image/White.jpg");
    public static final ImageIcon rolloverIcon = new ImageIcon("image/Rollover.png");
    public static final ImageIcon canPutIcon   = new ImageIcon("image/CanPut.png");

    private JButton[][] buttonBoard = new JButton[BOARD_SIZE][BOARD_SIZE];
    private Stone[][] board = new Stone[BOARD_SIZE][BOARD_SIZE];
    private Stone myStone; // actionEventで使うため、仕方なくフィールドに
    private Boolean passFlag = false;

    public Othello() {
        setTitle("Othello");
        getContentPane().setPreferredSize(new Dimension(BOARD_SIZE * IMAGE_ICON_SIZE,BOARD_SIZE * IMAGE_ICON_SIZE));
        pack();

        myStone = Stone.White;
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
        EnumSet<Direction> directions = canPut(r, c, stone);
        if (directions.isEmpty()) return;
        JButton target = buttonBoard[r][c];
        target.setRolloverIcon(null);
        switch (stone) {
            case Empty: target.setIcon(emptyIcon); break;
            case Black: target.setIcon(blackIcon); break;
            case White: target.setIcon(whiteIcon); break;
        }
        board[r][c] = stone;
        reverseStone(r, c, stone, directions);

        nextTurn();
        printBoard();
    }

    private void nextTurn() {
        hideHint();
        myStone = getReverse(myStone);
        List<Point> hint = makeHint(myStone);
        if (hint.isEmpty()) {
            if (passFlag)
                gameOver();
            else {
                passFlag = true;
                nextTurn();
            }
        }
        else {
            displayHint(hint);
            passFlag = false;
        }
    }

    private void reverseStone(int r, int c, Stone stone, EnumSet<Direction> directions) {
        directions.forEach(d -> reverseLine(r, c, stone, d));
    }

    private void reverseLine(int r, int c, Stone stone, Direction direction) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;
        Stone reverse = getReverse(stone);

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if (board[i][j] == reverse) {
                board[i][j] = stone;
                buttonBoard[i][j].setIcon(stone.getImageIcon());
            }
            else {
                break;
            }
            i += dr; j += dc;
        }
    }

    private List<Point> makeHint(Stone stone) {
        List<Point> hint = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                EnumSet<Direction> directions = canPut(i, j, stone);
                if (!directions.isEmpty()) {
                    hint.add(new Point(i, j));
                }
            }
        }
        return hint;
    }

    private void displayHint(List<Point> hint) {
        hint.forEach(h -> buttonBoard[h.row][h.column].setIcon(canPutIcon));
    }

    private void hideHint() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (buttonBoard[i][j].getIcon().equals(canPutIcon))
                    buttonBoard[i][j].setIcon(emptyIcon);
    }

    private EnumSet<Direction> canPut(int r, int c, Stone stone) {
        EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        if (board[r][c] != Stone.Empty) return directions;
        EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> checkLine(r, c, stone, d))
                .forEach(directions::add);
        return directions;
    }

    private boolean checkLine (int r, int c, Stone stone, Direction direction) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if      (board[i][j] == Stone.Empty) break;
            else if (board[i][j] == stone){
                if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1) return true;
                else break;
            }
            i += dr; j += dc;
        }
        return false;
    }

    private Stone getReverse(Stone stone) {
        return stone == Stone.Black ? Stone.White : Stone.Black;
    }

    private void gameOver() {
        int black = countStone(Stone.Black); int white = countStone(Stone.White);
        JLabel result = new JLabel(String.format("黒：%d  白：%d", black, white));
        JOptionPane.showMessageDialog(this, result, "ゲームセット", JOptionPane.INFORMATION_MESSAGE);
        // 全てのボタンを無効化
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttonBoard[i][j].setEnabled(false);
            }
        }
    }

    private int countStone(Stone stone) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (board[i][j] == stone)
                    count++;
        return count;
    }

    private void initBoard() {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton box = new JButton(emptyIcon);
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