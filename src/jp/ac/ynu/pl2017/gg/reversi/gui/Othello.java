package jp.ac.ynu.pl2017.gg.reversi.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import jp.ac.ynu.pl2017.gg.reversi.ai.RandomAI;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;
import jp.ac.ynu.pl2017.gg.reversi.util.Direction;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JPanel implements ActionListener {

	public static final int			BOARD_SIZE		= 8;
	public static final int			IMAGE_ICON_SIZE	= 40;
	public static final ImageIcon	emptyIcon		= new ImageIcon("image/Empty.png");
	public static final ImageIcon	blackIcon		= new ImageIcon("image/black.png");
	public static final ImageIcon	whiteIcon		= new ImageIcon("image/white.png");
	public static final ImageIcon	rolloverIcon	= new ImageIcon("image/Rollover.png");
	public static final ImageIcon	canPutIcon		= new ImageIcon("image/CanPut.png");
	public static final ImageIcon	grayIcon		= new ImageIcon("image/graypanel.png");
	public static final ImageIcon	cannotPutIcon	= new ImageIcon("image/bannedpanel.png");
	public static final ImageIcon	turn1Icon		= new ImageIcon("image/45degree.png");
	public static final ImageIcon	turn2Icon		= new ImageIcon("image/90degree.png");
	public static final ImageIcon	turn3Icon		= new ImageIcon("image/135degree.png");
	public static final ImageIcon[]	turnBtoW		= {turn1Icon, turn2Icon, turn3Icon, whiteIcon};
	public static final ImageIcon[]	turnWtoB		= {turn3Icon, turn2Icon, turn1Icon, blackIcon};
	
	private JButton[][]				buttonBoard		= new JButton[BOARD_SIZE][BOARD_SIZE];
	private Stone[][]				board			= new Stone[BOARD_SIZE][BOARD_SIZE];
	private Stone					myStone;												// actionEventで使うため、仕方なくフィールドに
	private boolean					myTurn;
	private boolean					passFlag		= false;
	
	private PlayEndCallback			callback;

	public Othello(PlayEndCallback pCallback) {
		Dimension lDimension = new Dimension(BOARD_SIZE * IMAGE_ICON_SIZE, BOARD_SIZE * IMAGE_ICON_SIZE);
		setSize(lDimension);
		setPreferredSize(lDimension);
		// setResizable() -> pack()の順でないと大きさがずれる
		// setResizable(false);
		// pack();

		Random random = new Random();
		myStone = random.nextBoolean() ? Stone.Black : Stone.White;
		myTurn = random.nextBoolean();
		
		callback = pCallback;
		
		initBoard();

		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		nextTurn();
	}

	@Override
	public void actionPerformed(
			ActionEvent e) {
		String[] position = e.getActionCommand().split(",");
		int r = Integer.parseInt(position[0]);
		int c = Integer.parseInt(position[1]);
		putStone(r, c, myStone);
	}

	public List<Point> makeHint(
			Stone stone) {
		List<Point> hint = new ArrayList<>();
		for (int i = 0; i < BOARD_SIZE; i++)
			for (int j = 0; j < BOARD_SIZE; j++)
				if (!selectDirections(i, j, stone).isEmpty())
					hint.add(new Point(i, j));
		return hint;
	}

	private void putStone(
			int r,
			int c,
			Stone stone) {
		EnumSet<Direction> directions = selectDirections(r, c, stone);
		if (directions.isEmpty())
			return;

		buttonBoard[r][c].setIcon(stone.getImageIcon());
		buttonBoard[r][c].setRolloverIcon(null);
		board[r][c] = stone;
		reverseStone(r, c, stone, directions);
		// printBoard();
		nextTurn();
	}

	private EnumSet<Direction> selectDirections(
			int r,
			int c,
			Stone stone) {
		EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
		if (board[r][c] != Stone.Empty)
			return directions;
		EnumSet.allOf(Direction.class).stream().filter(d -> checkLine(r, c, stone, d)).forEach(directions::add);
		return directions;
	}

	private boolean checkLine(
			int r,
			int c,
			Stone stone,
			Direction direction) {
		int dr = direction.getDR();
		int dc = direction.getDC();
		int i = r + dr;
		int j = c + dc;

		while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
			if (board[i][j] == Stone.Empty)
				break;
			else if (board[i][j] == stone) {
				if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1)
					return true;
				else
					break;
			}
			i += dr;
			j += dc;
		}
		return false;
	}

	private void nextTurn() {
		myTurn = !myTurn;
		myStone = myStone.getReverse();
		hideHint();
		List<Point> hint = makeHint(myStone);
		if (hint.isEmpty()) {
			if (passFlag) {
				gameOver();
			} else {
				passFlag = true;
				nextTurn();
			}
		} else {
			passFlag = false;
			if (myTurn) {
				displayHint(hint);
			} else {
				removeAllListener();
				new Thread(() -> {
					RandomAI ai = new RandomAI(hint);
					putStone(ai.getRow(), ai.getColumn(), myStone);
					addAllListener();
				}).start();
			}
		}
	}

	private void removeAllListener() {
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				button.removeActionListener(this);
	}

	private void addAllListener() {
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				button.addActionListener(this);
	}

	private void reverseStone(
			int r,
			int c,
			Stone stone,
			EnumSet<Direction> directions) {
		directions.forEach(d -> reverseLine(r, c, stone, d));
	}

	private void reverseLine(
			int r,
			int c,
			Stone stone,
			Direction direction) {
		int dr = direction.getDR();
		int dc = direction.getDC();
		int i = r + dr;
		int j = c + dc;
		Stone reverse = stone.getReverse();

		while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
			if (board[i][j] == reverse) {
				board[i][j] = stone;
				showAnimation(buttonBoard[i][j], stone);
//				buttonBoard[i][j].setIcon(stone.getImageIcon());
			} else {
				break;
			}
			i += dr;
			j += dc;
		}
	}
	
	private void showAnimation(final JButton pTargetButton, final Stone pDestStone) {
		new Thread(){
			@Override
			public void run() {
				ImageIcon[] targetIcons;
				if (pDestStone.equals(Stone.White)) {
					// 黒→白
					targetIcons = turnBtoW;
				} else {
					targetIcons = turnWtoB;
				}
				for (int i = 0; i < 4; i++) {
					pTargetButton.setIcon(targetIcons[i]);
					try {
						Thread.sleep(120);
					} catch (InterruptedException e) {
						pTargetButton.setIcon(targetIcons[3]);
						break;
					}
				}
			}
		}.start();
	}

	private void displayHint(
			List<Point> hint) {
		hint.forEach(p -> buttonBoard[p.getRow()][p.getColumn()].setIcon(canPutIcon));
	}

	private void hideHint() {
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				if (button.getIcon().equals(canPutIcon))
					button.setIcon(emptyIcon);
	}

	private void gameOver() {
		String result = String.format("黒：%d  白：%d", countStone(Stone.Black), countStone(Stone.White));
		JOptionPane.showMessageDialog(this, new JLabel(result), "ゲームセット", JOptionPane.INFORMATION_MESSAGE);
		// 全てのボタンを無効化
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				button.setEnabled(false);
		// PlayPanelにコールバック
		callback.onGameOver();
	}

	private int countStone(
			Stone stone) {
		return (int) Arrays.stream(board).mapToLong(ss -> Arrays.stream(ss).filter(s -> s == stone).count()).sum();
	}

	private void initBoard() {
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				JButton button = new JButton(emptyIcon);
				button.setRolloverIcon(rolloverIcon);
				button.setBorder(new EmptyBorder(0, 0, 0, 0));
				button.setActionCommand(i + "," + j);
				button.addActionListener(this);
				add(button);
				buttonBoard[i][j] = button;
				board[i][j] = Stone.Empty;
			}
		}
		board[3][3] = Stone.Black;
		board[3][4] = Stone.White;
		board[4][3] = Stone.White;
		board[4][4] = Stone.Black;
		buttonBoard[3][3].setIcon(blackIcon);
		buttonBoard[3][4].setIcon(whiteIcon);
		buttonBoard[4][3].setIcon(whiteIcon);
		buttonBoard[4][4].setIcon(blackIcon);
		buttonBoard[3][3].setRolloverIcon(null);
		buttonBoard[3][4].setRolloverIcon(null);
		buttonBoard[4][3].setRolloverIcon(null);
		buttonBoard[4][4].setRolloverIcon(null);

		// // パステスト
		// board[0][0] = Stone.White; buttonBoard[0][0].setIcon(whiteIcon);
		// board[0][1] = Stone.Black; buttonBoard[0][1].setIcon(blackIcon);
	}

	private void printBoard() {
		for (Stone[] stones : board) {
			for (Stone stone : stones)
				System.out.print(stone + " ");
			System.out.println();
		}
		System.out.println();
	}
}
