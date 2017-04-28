import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JFrame {

    private enum Stone { Empty, Black, White }
    private static ImageIcon greenFrame = new ImageIcon("image/GreenFrame.jpg");
    private static ImageIcon blackIcon = new ImageIcon("image/Black.jpg");
    private static ImageIcon whiteIcon = new ImageIcon("image/White.jpg");
    private JButton[][] buttonBoard = new JButton[8][8];
    private Stone[][] board = new Stone[8][8];

    public Othello() {
        setTitle("Othello");
        getContentPane().setPreferredSize(new Dimension(320,320));
        pack();

        initBoard();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void putStone(int r, int c, Stone stone) {
//        if (board[r][c] != Stone.Empty) return;
        if (!canPut(r, c, stone)) return;
        JButton target = buttonBoard[r][c];
        switch (stone) {
            case Empty: target.setIcon(greenFrame); break;
            case Black: target.setIcon(blackIcon); break;
            case White: target.setIcon(whiteIcon); break;
        }
        board[r][c] = stone;
//        printBoard();
    }

    private boolean canPut(int r, int c, Stone stone) {
        boolean b1 = checkLine(r, c , stone, -1, 0);
        boolean b2 = checkLine(r, c , stone, -1, 1);
        boolean b3 = checkLine(r, c , stone, 0 , 1);
        boolean b4 = checkLine(r, c , stone, 1 , 1);
        boolean b5 = checkLine(r, c , stone, 1 , 0);
        boolean b6 = checkLine(r, c , stone, 1 , -1);
        boolean b7 = checkLine(r, c , stone, 0 , -1);
        boolean b8 = checkLine(r, c , stone, -1, -1);
        System.out.println(b1 + "," + b2 + "," + b3 + "," + b4 + "," + b5 + "," + b6 + "," + b7 + "," + b8);
        return b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8;
    }

    private boolean checkLine (int r, int c, Stone stone, int dr, int dc) {
        int i = r + dr;
        int j = c + dc;
        while (0 <= i && i <= 8 && 0 <= j && j <= 8) {
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
        setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton box = new JButton(greenFrame);
                box.setBorder(new EmptyBorder(0,0,0,0));
                box.setActionCommand(i + "," + j);
                box.addActionListener(e -> {
                    String[] position = e.getActionCommand().split(",");
                    int r = Integer.parseInt(position[0]); int c = Integer.parseInt(position[1]);
                    putStone(r, c, Stone.Black);
                });
                add(box);
                buttonBoard[i][j] = box;
                board[i][j] = Stone.Empty;
            }
        }
//        board[3][3] = Stone.Black; board[3][4] = Stone.White;
//        board[4][3] = Stone.White; board[4][4] = Stone.Black;
//        buttonBoard[3][3].setIcon(blackIcon); buttonBoard[3][4].setIcon(whiteIcon);
//        buttonBoard[4][3].setIcon(whiteIcon); buttonBoard[4][4].setIcon(blackIcon);
        // テスト用配置
        board[3][3] = Stone.Black; board[3][4] = Stone.Black; board[3][5] = Stone.Black;
        board[4][3] = Stone.Black; board[4][4] = Stone.White; board[4][5] = Stone.Black;
        board[5][3] = Stone.Black; board[5][4] = Stone.Black; board[5][5] = Stone.Black;
        buttonBoard[3][3].setIcon(blackIcon); buttonBoard[3][4].setIcon(blackIcon); buttonBoard[3][5].setIcon(blackIcon);
        buttonBoard[4][3].setIcon(blackIcon); buttonBoard[4][4].setIcon(whiteIcon); buttonBoard[4][5].setIcon(blackIcon);
        buttonBoard[5][3].setIcon(blackIcon); buttonBoard[5][4].setIcon(blackIcon); buttonBoard[5][5].setIcon(blackIcon);
//        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new Othello();
    }
}