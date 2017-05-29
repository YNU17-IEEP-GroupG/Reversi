package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.Evaluation;
import jp.ac.ynu.pl2017.gg.reversi.ai.OnlineDummyAI;
import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Direction;
import jp.ac.ynu.pl2017.gg.reversi.util.FinishListenedThread;
import jp.ac.ynu.pl2017.gg.reversi.util.FinishListenedThread.ThreadFinishListener;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

/**
 * Created by shiita on 2017/04/29.
 */
public class Othello extends JPanel implements ActionListener, ThreadFinishListener, OnlineDummyAI.OnlineItem {

	public static final int			BOARD_SIZE		= 8;
	public static final int			IMAGE_ICON_SIZE	= 40;
	public static final int			ITEM_COUNT	  = 3;
	public static final ImageIcon	emptyIcon		= new ImageIcon("image/board/Empty.png");
	public static final ImageIcon	blackIcon		= new ImageIcon("image/board/black.png");
	public static final ImageIcon	whiteIcon		= new ImageIcon("image/board/white.png");
	public static final ImageIcon	tripleBlackIcon	= new ImageIcon("image/board/tripleB.png");
	public static final ImageIcon	tripleWhiteIcon = new ImageIcon("image/board/tripleW.png");
	public static final ImageIcon	rolloverIcon		= new ImageIcon("image/board/Rollover.png");
	public static final ImageIcon	canPutIcon		= new ImageIcon("image/board/CanPut.png");
	public static final ImageIcon	grayIcon			= new ImageIcon("image/board/graypanel.png");
	public static final ImageIcon	cannotPutIcon	= new ImageIcon("image/board/bannedpanel.png");
	public static final ImageIcon	turn1Icon		= new ImageIcon("image/board/45degree.png");
	public static final ImageIcon	turn2Icon		= new ImageIcon("image/board/90degree.png");
	public static final ImageIcon	turn3Icon		= new ImageIcon("image/board/135degree.png");
	public static final ImageIcon	dropB1Icon		= new ImageIcon("image/board/dropB1.png");
	public static final ImageIcon	dropB2Icon		= new ImageIcon("image/board/dropB2.png");
	public static final ImageIcon	dropB3Icon		= new ImageIcon("image/board/dropB3.png");
	public static final ImageIcon	dropW1Icon		= new ImageIcon("image/board/dropW1.png");
	public static final ImageIcon	dropW2Icon		= new ImageIcon("image/board/dropW2.png");
	public static final ImageIcon	dropW3Icon		= new ImageIcon("image/board/dropW3.png");
	public static final ImageIcon	dropK1Icon		= new ImageIcon("image/board/dropback1.png");
	
	public static final ImageIcon[]	turnBtoW			= {turn1Icon, turn2Icon, turn3Icon, whiteIcon};
	public static final ImageIcon[]	turnWtoB			= {turn3Icon, turn2Icon, turn1Icon, blackIcon};
	public static final ImageIcon[]	turnWtoE			= {dropW1Icon, dropW2Icon, dropW3Icon, dropK1Icon, emptyIcon};
	public static final ImageIcon[]	turnBtoE			= {dropB1Icon, dropB2Icon, dropB3Icon, dropK1Icon, emptyIcon};
	public static final ImageIcon   itemIcon		= new ImageIcon("image/board/item.png");
	public static final ImageIcon   itemCanPutIcon  = new ImageIcon("image/board/itemCanPut.png");
	public static final ImageIcon	itemRollOver  	= new ImageIcon("image/board/itemRollover.png");
	
	private JButton[][]				buttonBoard		= new JButton[BOARD_SIZE][BOARD_SIZE];
	private Stone[][]				board			= new Stone[BOARD_SIZE][BOARD_SIZE];
	private List<Point>				itemPoints		= new ArrayList<>();
	private Stone					myStone;												// actionEventで使うため、仕方なくフィールドに
	private boolean					myTurn;
	private boolean					passFlag		= false;
	private boolean				 dropFlag		= false;
	private int					 grayTurn		= 0;
	private int					 grayTurnCPU    = 0;
	private boolean				 tripleFlag	  = false;
	private List<Point>			 triplePoints	= new ArrayList<>();
	private Item                 usedItem           = Item.NONE;

	private Class<? extends BaseAI>			selectedAI;
	private int						selectedDifficulty;
	/**
	 * CPU戦かどうかの判定
	 */
	private boolean 				isCPU		= false;
	// CPUでの使用
	private boolean				 CPUItemFlag = false;

	private PlayCallback			callback;

	public Othello(PlayCallback pCallback, Class<? extends BaseAI> pAi, int pDifficulty, boolean pMyTurn) {
		Dimension lDimension = new Dimension(BOARD_SIZE * IMAGE_ICON_SIZE, BOARD_SIZE * IMAGE_ICON_SIZE);
		setSize(lDimension);
		setPreferredSize(lDimension);
		// setResizable() -> pack()の順でないと大きさがずれる
		// setResizable(false);
		// pack();

		Random random = new Random();
		myStone = random.nextBoolean() ? Stone.Black : Stone.White;
		myTurn = !pMyTurn;
		callback = pCallback;

		selectedAI = pAi;
		selectedDifficulty = pDifficulty;
		isCPU = !pAi.equals(OnlineDummyAI.class);
//		System.err.println("CPU?"+isCPU);
		
		if (!isCPU) {
			if (!pMyTurn) {
				// 先攻の場合はアイテム位置を選定
				selectItemPoints();
				ClientConnection.sendSelectItemPos(itemPoints);
			} else {
				// 後攻の場合はアイテム位置を受信する.
				itemPoints = ClientConnection.receiveSelectItemPos();
			}
		} else {
			selectItemPoints();
		}

		
		initBoard();

		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		
		new FinishListenedThread(this) {
			
			@Override
			public Object doRun() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
				return null;
			}
		}.start();
	}

	@Override
	public void actionPerformed(
			ActionEvent e) {
		String[] position = e.getActionCommand().split(",");
		int r = Integer.parseInt(position[0]);
		int c = Integer.parseInt(position[1]);
		if (dropFlag) {
			drop(r, c);
			int[] pos = {-1, -1, -1, -1, -1, -1};
			pos[0] = r; pos[1] = c;
			if (!isCPU) {
			    usedItem = Item.DROP;
			    usedItem.setPos(pos);
			}
			return;
		}
		 //デバッグに使用
//		if (r == BOARD_SIZE - 1 && c == BOARD_SIZE -1) {
//			useDrop();
//		}
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
		
		EnumSet<Direction> directions = selectDirections(r, c, stone);
		if (directions.isEmpty())
			return;

		removeAllListener();

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
		
		// 置き石送信
		if (!isCPU && myTurn) {
		    int[] coordinate = {r, c};
		    usedItem.setCoordinate(coordinate);
			if (!ClientConnection.sendPutStone(usedItem)) {
				gameOver();
			}
			usedItem = Item.NONE;
		}
		
		Point point = new Point(r, c);
		if (itemPoints.contains(point)) {
			gainItem(point);
		}
		reverseStone(r, c, stone, directions);
		// printBoard();
		new FinishListenedThread(this) {
			
			@Override
			public Object doRun() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		}.start();
	}

	@Override
	public void onThreadFinish(Object pCallback) {
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
		Stone playersStone = myTurn ? myStone : myStone.getReverse();
		callback.onTurnChange(myTurn, new int[]{countStone(playersStone), countStone(playersStone.getReverse())});
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
//				// デバッグ用
//				CPUItemFlag = true;
				if (isCPU && CPUItemFlag) {
					// 確率でアイテムを使用にする
					useItemCPU();
					callback.onOpponentUseItem();
					// アイテム使用で状況が変わる可能性があるため
					hint = makeHint(myStone);
					CPUItemFlag = false;
					if (hint.isEmpty()) {
						nextTurn();
						return;
					}

					final List<Point> tHint = hint;
					new FinishListenedThread(new ThreadFinishListener() {
						@Override
						public void onThreadFinish(Object pCB) {
							cpuAction(tHint);
						}
					}) {
						
						@Override
						public Object doRun() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}
							return null;
						}
					}.start();
				} else {
					cpuAction(hint);
				}
//				removeAllListener();
//				OmegaAI ai = new OmegaAI(hint, myStone, board, selectedDifficulty);
//				ai.think();
//				putStone(ai.getRow(), ai.getColumn(), myStone);
				
//				addAllListener();
			}
		}
	}
	
	private void cpuAction(List<Point> hint) {
		try {
			// AI restrict
			Constructor<? extends BaseAI> tConstructor =
					selectedAI.getConstructor(List.class, Stone.class, Stone[][].class, int.class);
			Object[] tArgs = {hint, myStone, board, selectedDifficulty};
			BaseAI ai = (BaseAI) tConstructor.newInstance(tArgs);
			if (!myTurn && grayTurnCPU > 0) {
				ai.setGray();
				grayTurnCPU--;
			}
			if (!isCPU) {
				((OnlineDummyAI)ai).setCallback(this);
			}
			ai.think();
			putStone(ai.getRow(), ai.getColumn(), myStone);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void removeAllListener() {
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				button.removeActionListener(this);
		callback.disableItem();
	}

	private void addAllListener() {
		for (JButton[] buttons : buttonBoard)
			for (JButton button : buttons)
				button.addActionListener(this);
		callback.enableItem();
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
				showAnimation(buttonBoard[i][j], stone, false);
//				buttonBoard[i][j].setIcon(stone.getImageIcon());
			} else {
				break;
			}
			i += dr;
			j += dc;
		}
	}
	
	private void showAnimation(final JButton pTargetButton, final Stone pStone, boolean pIsDrop) {
		new Thread(){
			@Override
			public void run() {
				ImageIcon[] targetIcons = null;
				if (pIsDrop) {
					//ドロップ
					targetIcons = pStone.equals(Stone.Black) ? turnBtoE : turnWtoE;
				} else {
					if (pStone.equals(Stone.White)) {
						// 黒→白
						targetIcons = turnBtoW;
					} else if (pStone.equals(Stone.Black)) {
						targetIcons = turnWtoB;
					}
				}
				for (int i = 0; i < targetIcons.length; i++) {
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
					pTargetButton.setIcon(pStone.getTripleImageIcon());
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
		        else if (button.getIcon().equals(itemCanPutIcon))
		            button.setIcon(itemIcon);
	}

	private void gameOver() {
		int black = countStone(Stone.Black); int white = countStone(Stone.White);
		String result = String.format("黒：%d  白：%d", black, white);
		JOptionPane.showMessageDialog(this, new JLabel(result), "ゲームセット", JOptionPane.INFORMATION_MESSAGE);
		// 灰色の石を元に戻す
		undoGray();
		// 全てのボタンを無効化。ImageIconは見えるように
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				buttonBoard[i][j].setDisabledIcon(board[i][j].getImageIcon());
				buttonBoard[i][j].setEnabled(false);
			}
		}

		int ret = 0;
		Stone myResultStone = myTurn ? myStone : myStone.getReverse();
		if (myResultStone == Stone.Black) {
			ret = black > white ? 1 : 0;
		}
		else {
			ret = white > black ? 1 : 0;
		}
		System.out.println(ret);
		// PlayPanelにコールバック
		callback.onGameOver(ret);
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

		for (int i = 0; i < itemPoints.size(); i++) {
            buttonBoard[itemPoints.get(i).getRow()][itemPoints.get(i).getColumn()].setIcon(itemIcon);
            buttonBoard[itemPoints.get(i).getRow()][itemPoints.get(i).getColumn()].setRolloverIcon(itemRollOver);
        }
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
		callback.onGainItem(myTurn);
		if (!myTurn) {
			CPUItemFlag = true;
		}
	}

	public void useItem(Item item) {
		switch (item) {
			case BAN:
				useBan();
				break;
			case DROP:
				useDrop();
				break;
			case GRAY:
				grayTurnCPU = 3;
				if (!isCPU) {
					// これだけメソッドがないので、ここでアイテム情報セット
                    usedItem = Item.GRAY;
				}
				break;
			case TRIPLE:
				useTriple();
				break;
//			case CONTROLER:
//				break;
		}
	}

	private void useBan() {
	    // 先手で1ターン目に使用するとこのメソッドが２回呼ばれてしまうバグあり
		// 特に重要でも無いので一旦保留
	    hideHint();
	    List<Point> banPoints = makeBanPoints();
	    if (!isCPU) {
			int[] pos = new int[6];
			for (int i = 0; i < 3; i++) {
				pos[i * 2]     = banPoints.get(i).getRow();
				pos[i * 2 + 1] = banPoints.get(i).getColumn();
			}
			// アイテム使用データ送信
//			ClientConnection.sendItemUse(Item.BAN, pos);
            usedItem = Item.BAN;
			usedItem.setPos(pos);
		}
		reflectBan(banPoints);		// ヒントの再表示とパス処理
		List<Point> hint = makeHint(myStone);
		if (hint.isEmpty())
			nextTurn();
		else
			displayHint(hint);
	}

	private List<Point> makeBanPoints() {
		Random random = new Random();
		List<Point> emptyPoints = BoardHelper.getPoints(Stone.Empty, board);
		int count = Math.min(3, emptyPoints.size());
		List<Point> banPoints = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			banPoints.add(emptyPoints.get(random.nextInt(emptyPoints.size())));
		}
		return banPoints;
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
		// アイテム使用情報はアクションが起こった場所で送信する
	}

	private void drop(int r, int c) {
		removeAllListener();
		
		Stone lbkStone = board[r][c];
		board[r][c] = Stone.Empty;
		// 三倍の石のリストからの削除
        triplePoints.remove(new Point(r, c));
		dropFlag = false;
		
		showAnimation(buttonBoard[r][c], lbkStone, true);

		new FinishListenedThread(new ThreadFinishListener() {
			@Override
			public void onThreadFinish(Object pCB) {
				addAllListener();
				List<Point> hint = makeHint(myStone);
				displayHint(hint);
			}
		}) {
			
			@Override
			public Object doRun() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
				}
				return null;
			}
		}.start();
		
		/*
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
		*/
	}

	// 相手側のクラスで呼び出す
	private void useGray() {
		grayTurn = 1;
		for (int i = 0; i < BOARD_SIZE; i++)
			for (int j = 0; j < BOARD_SIZE; j++)
				if (board[i][j] == Stone.Black || board[i][j] == Stone.White)
					buttonBoard[i][j].setIcon(grayIcon);
	}

	// 相手側のクラスで呼び出す
	private void undoGray() {
		for (int i = 0; i < BOARD_SIZE; i++)
			for (int j = 0; j < BOARD_SIZE; j++)
				if (buttonBoard[i][j].getIcon() == grayIcon)
					buttonBoard[i][j].setIcon(board[i][j].getImageIcon());
		triplePoints.forEach(p -> buttonBoard[p.getRow()][p.getColumn()].setIcon(board[p.getRow()][p.getColumn()].getTripleImageIcon()));
	}

	private void useTriple() {
		tripleFlag = true;
		if (!isCPU) {
		    usedItem = Item.TRIPLE;
		}
	}

	private void useItemCPU() {
		// controlが無いので今は4
		int select = new Random().nextInt(4);
		switch (select) {
			case 0:
				reflectBan(makeBanPoints());
				break;
			case 1:
				reflectDrop(Evaluation.getMaxEvaluatedPoint(BoardHelper.getPoints(myStone.getReverse(), board)));
				break;
			case 2:
				reflectGray();
				break;
			case 3:
				reflectTriple();
				break;
		}
	}

	/*=============== CPU用に作成したが、オンライン対戦でも使えるように作った ===============*/

	/**
	 * 石が置けないマスを引数で示された場所に設置
	 * @param banPoints 石が置けないマスの座標
	 */
	@Override
	public void reflectBan(List<Point> banPoints) {
		banPoints.forEach(p -> {
			board[p.getRow()][p.getColumn()] = Stone.Ban;
			buttonBoard[p.getRow()][p.getColumn()].setIcon(cannotPutIcon);
			buttonBoard[p.getRow()][p.getColumn()].setRolloverIcon(null);
		});
		callback.onOpponentUseItem();
	}

	@Override
	public void reflectDrop(Point point) {
		drop(point.getRow(), point.getColumn());
		callback.onOpponentUseItem();
	}

	@Override
	public void reflectGray() {
		useGray();
		callback.onOpponentUseItem();
	}

	@Override
	public void reflectTriple() {
		tripleFlag = true;
		callback.onOpponentUseItem();
	}
}
