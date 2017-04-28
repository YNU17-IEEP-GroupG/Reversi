import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JFrame {

    private enum Stone { Empty, Black, White }
    private static ImageIcon greenFrame = new ImageIcon("image/GreenFrame.jpg");
    private static ImageIcon black = new ImageIcon("image/Black.jpg");
    private static ImageIcon white = new ImageIcon("image/White.jpg");
    private JButton[][] buttonBoard = new JButton[8][8];
    private Stone[][] board = new Stone[8][8];

    public Othello() {
        setTitle("Othello");
        getContentPane().setPreferredSize(new Dimension(320,320));
        pack();

        setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton box = new JButton(greenFrame);
                box.setBorder(new EmptyBorder(0,0,0,0));
                box.setActionCommand(i + "," + j);
                box.addActionListener(e -> {
                    String[] position = e.getActionCommand().split(",");
                    int x = Integer.parseInt(position[0]); int y = Integer.parseInt(position[1]);
                    putStone(x, y, Stone.Black);
                });
                add(box);
                buttonBoard[i][j] = box;
                board[i][j] = Stone.Empty;
            }
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void putStone(int x, int y, Stone stone) {
        if (board[x][y] == stone) return;
        JButton target = buttonBoard[x][y];
        switch (stone) {
            case Empty: target.setIcon(greenFrame); break;
            case Black: target.setIcon(black); break;
            case White: target.setIcon(white); break;
        }
        board[x][y] = stone;
        printBoard();
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