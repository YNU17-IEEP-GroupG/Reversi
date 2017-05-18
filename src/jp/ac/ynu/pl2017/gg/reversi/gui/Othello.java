package jp.ac.ynu.pl2017.gg.reversi.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.Evaluation;
import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;
import jp.ac.ynu.pl2017.gg.reversi.util.Direction;
import jp.ac.ynu.pl2017.gg.reversi.util.FinishListenedThread;
import jp.ac.ynu.pl2017.gg.reversi.util.FinishListenedThread.ThreadFinishListener;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JPanel implements ActionListener, ThreadFinishListener {

	public static final int			BOARD_SIZE		= 8;
	public static final int			IMAGE_ICON_SIZE	= 40;
	public static final int         ITEM_COUNT      = 3;
	public static final ImageIcon	emptyIcon		= new ImageIcon("image/board/Empty.png");
	public static final ImageIcon	blackIcon		= new ImageIcon("image/board/black.png");
	public static final ImageIcon	whiteIcon		= new ImageIcon("image/board/white.png");
    public static final ImageIcon	tripleBlackIcon	= new ImageIcon("image/board/tripleB.png");
    public static final ImageIcon	tripleWhiteIcon = new ImageIcon("image/board/tripleW.png");
	public static final ImageIcon	rolloverIcon	    = new ImageIcon("image/board/Rollover.png");
	public static final ImageIcon	canPutIcon		= new ImageIcon("image/board/CanPut.png");
	public static final ImageIcon	grayIcon		    = new ImageIcon("image/board/graypanel.png");
	public static final ImageIcon	cannotPutIcon	= new ImageIcon("image/board/bannedpanel.png");
	public static final ImageIcon	turn1Icon		= new ImageIcon("image/board/45degree.png");
	public static final ImageIcon	turn2Icon		= new ImageIcon("image/board/90degree.png");
	public static final ImageIcon	turn3Icon		= new ImageIcon("image/board/135degree.png");
	public static final ImageIcon[]	turnBtoW		    = {turn1Icon, turn2Icon, turn3Icon, whiteIcon};
	public static final ImageIcon[]	turnWtoB		    = {turn3Icon, turn2Icon, turn1Icon, blackIcon};
	public static final ImageIcon   itemIcon        = new ImageIcon("image/board/item.png");
	public static final ImageIcon   itemCanPutIcon  = new ImageIcon("image/board/itemCanPut.png");
	
	private JButton[][]				buttonBoard		= new JButton[BOARD_SIZE][BOARD_SIZE];
	private Stone[][]				board			= new Stone[BOARD_SIZE][BOARD_SIZE];
	private List<Point>             itemPoints      = new ArrayList<>();
	private Stone					myStone;												// actionEventで使うため、仕方なくフィールドに
	private boolean					myTurn;
	private boolean					passFlag		= false;
	private boolean                 dropFlag        = false;
	private int                     grayTurn        = 0;
	private boolean                 tripleFlag      = false;
	private List<Point>             triplePoints    = new ArrayList<>();

	private Class<BaseAI>			selectedAI;
	private int						selectedDifficulty;
	
	private PlayEndCallback			callback;

	public Othello(PlayEndCallback pCallback, Class<BaseAI> pAi, int pDifficulty) {
		Dimension lDimension = new Dimension(BOARD_SIZE * IMAGE_ICON_SIZE, BOARD_SIZE * IMAGE_ICON_SIZE);
		setSize(lDimension);
		setPreferredSize(lDimension);
		// setResizable() -> pack()の順でないと大きさがずれる
		// setResizable(false);
		// pack();

		Random random = new Random();
		selectItemPoints();
		myStone = random.nextBoolean() ? Stone.Black : Stone.White;
		myTurn = random.nextBoolean();
		
		callback = pCallback;

		selectedAI = pAi;
		selectedDifficulty = pDifficulty;
		
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
		if (dropFlag) {
            drop(r, c);
		    return;
        }
//		 //デバッグに使用
//        if (r == BOARD_SIZE - 1 && c == BOARD_SIZE -1) {
//            useTriple();
//        }
//		removeAllListener();
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
		removeAllListener();
		
		EnumSet<Direction> directions = selectDirections(r, c, stone);
		if (directions.isEmpty())
			return;

		hideHint();
		if (tripleFlag) {
            buttonBoard[r][c].setIcon(stone.getTripleImageIcon());
            triplePoints.add(new Point(r, c));
            tripleFlag = false;
        }
        else {
            buttonBoard[r][c].setIcon(stone.getImageIcon());
        }
		buttonBoard[r][c].setRolloverIcon(null);
		board[r][c] = stone;
		Point point = new Point(r, c);
		if (itemPoints.contains(point)) {
		    gainItem(point);
        }
		reverseStone(r, c, stone, directions);
		// printBoard();
		new FinishListenedThread(this) {
			
			@Override
			public void doRun() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	@Override
	public void onThreadFinish() {
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
			if (board[i][j] == Stone.Empty || board[i][j] == Stone.Ban)
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
        if (grayTurn < 0) undoGray();
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
				addAllListener();
				grayTurn--;
			} else {
//				removeAllListener();
//				OmegaAI ai = new OmegaAI(hint, myStone, board, selectedDifficulty);
//				ai.think();
//				putStone(ai.getRow(), ai.getColumn(), myStone);
				
				// Create AI Instance
				try {
					Constructor<BaseAI> tConstructor =
							selectedAI.getConstructor(List.class, Stone.class, Stone[][].class, int.class);
					Object[] tArgs = {hint, myStone, board, selectedDifficulty};
					BaseAI ai = (BaseAI) tConstructor.newInstance(tArgs);
					ai.think();
					putStone(ai.getRow(), ai.getColumn(), myStone);
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
						IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
//				addAllListener();
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

				// 3倍石の反映
				String[] position = pTargetButton.getActionCommand().split(",");
				int r = Integer.parseInt(position[0]);
				int c = Integer.parseInt(position[1]);
				if (triplePoints.contains(new Point(r, c)))
					pTargetButton.setIcon(pDestStone.getTripleImageIcon());
			}
		}.start();
	}

	private void displayHint(
			List<Point> hint) {
		hint.forEach(p ->  {
            if (itemPoints.contains(p))
                buttonBoard[p.getRow()][p.getColumn()].setIcon(itemCanPutIcon);
            else
                buttonBoard[p.getRow()][p.getColumn()].setIcon(canPutIcon);
        });
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
		int count = (int) Arrays.stream(board).mapToLong(ss -> Arrays.stream(ss).filter(s -> s == stone).count()).sum();
        // 三倍石の反映
        for (Point p : triplePoints) {
            if (board[p.getRow()][p.getColumn()] == stone)
                count += 2;
        }
		return count;
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

		buttonBoard[itemPoints.get(0).getRow()][itemPoints.get(0).getColumn()].setIcon(itemIcon);
		buttonBoard[itemPoints.get(1).getRow()][itemPoints.get(1).getColumn()].setIcon(itemIcon);
		buttonBoard[itemPoints.get(2).getRow()][itemPoints.get(2).getColumn()].setIcon(itemIcon);
		buttonBoard[itemPoints.get(0).getRow()][itemPoints.get(0).getColumn()].setRolloverIcon(null);
		buttonBoard[itemPoints.get(1).getRow()][itemPoints.get(1).getColumn()].setRolloverIcon(null);
		buttonBoard[itemPoints.get(2).getRow()][itemPoints.get(2).getColumn()].setRolloverIcon(null);
	}

	private void selectItemPoints() {
	    Random random = new Random();
	    while (itemPoints.size() < ITEM_COUNT) {
	        int r = random.nextInt(8);
	        int c = random.nextInt(8);
	        if (2 <= r && r <= 5 && 2 <= c && c <= 5)
	            continue;
	        Point point = new Point(r, c);
            if (!itemPoints.contains(point)) {
                itemPoints.add(point);
                Evaluation.updateSquare(point);
            }
        }
    }

    private void gainItem(Point point) {
	    itemPoints.remove(point);
        // TODO: アイテムをPlayPanelにランダムにセットするメソッドを使用
    }

    public void useItem(Item item) {
        switch (item) {
            case BAN:
                useBan();
                break;
            case DROP:
                break;
            case GRAY:
                useGray();
                break;
            case TRIPLE:
                break;
            case CONTROLER:
                break;
        }
    }

    private void useBan() {
        Random random = new Random();
        List<Point> emptyPoints = BoardHelper.getPoints(Stone.Empty, board);
        int count = Math.min(3, emptyPoints.size());
        for (int i = 0; i < count; i++) {
            Point p = emptyPoints.get(random.nextInt(emptyPoints.size()));
            board[p.getRow()][p.getColumn()] = Stone.Ban;
            buttonBoard[p.getRow()][p.getColumn()].setIcon(cannotPutIcon);
            buttonBoard[p.getRow()][p.getColumn()].setRolloverIcon(null);
        }
    }

    private void useDrop() {
	    dropFlag = true;
	    hideHint();
	    removeAllListener();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == myStone.getReverse()) {
                    buttonBoard[i][j].addActionListener(this);
                }
            }
        }
    }

    private void drop(int r, int c) {
        board[r][c] = Stone.Empty;
        buttonBoard[r][c].setIcon(emptyIcon);
        buttonBoard[r][c].setRolloverIcon(rolloverIcon);
        dropFlag = false;
        // TODO: ここに落ちるアニメーションを追加してください
        // リスナがdropだけでなく普通に配置することにも反応してしまうため、遅延させる
        // TODO: 要修正、連続して置けるバグと同じことが起こる
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            addAllListener();
            List<Point> hint = makeHint(myStone);
            displayHint(hint);
        }).start();
    }

    private void useGray() {
	    grayTurn = 1;
	    for (JButton[] buttons : buttonBoard)
	        for (JButton button : buttons)
	            if (button.getIcon() == blackIcon || button.getIcon() == whiteIcon)
	                button.setIcon(grayIcon);
    }

    private void undoGray() {
        for (int i = 0; i < BOARD_SIZE; i++)
	        for (int j = 0; j < BOARD_SIZE; j++)
	            if (buttonBoard[i][j].getIcon() == grayIcon)
                    buttonBoard[i][j].setIcon(board[i][j].getImageIcon());
    }

    private void useTriple() {
	    tripleFlag = true;
    }
}