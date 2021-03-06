package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import jp.ac.ynu.pl2017.gg.reversi.ai.AlphaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.BetaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.GammaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.OmegaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.OnlineDummyAI;
import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.Offline;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;
import jp.ac.ynu.pl2017.gg.reversi.util.User;

/**
 * ゲーム画面を構築するJPanel. 対人・対CPU問わずここに飛んでくる発想.
 *
 */
public class PlayPanel extends BackgroundedPanel implements PlayCallback, BasicPlayerListener {

	private boolean	isCPU	= false;

	private Item item = Item.NONE;

	public Othello	lOthelloPanel;
	private JPanel	lCoverPanel;
	
	private	int		playerIconNum;
	private	int		opponentIconNum;
	private	Image	backImage;

	private	Item	havingItem = Item.NONE;
	
	private TitlePanel.Transition	callback;
	
	private	Class<? extends BaseAI>	selectedAI;
	private	int				selectedDifficulty;
	private	String			opponentName;

	private JButton			opponentItemButton;
	private	JButton			playerItemButton;
	
	/**
	 * 0:自分 1:相手
	 */
	private JLabel	turnIcon[];
	private JLabel	playerStoneLabel[];

	private JLabel	lOpponentIcon;
	private JLabel	lPlayerIcon;

	
	private	BasicPlayer	player;

	public PlayPanel(TitlePanel.Transition pCallback, Class<? extends BaseAI> pAi, int pDifficulty, String pOpponentName,
			int pPIcon, int pOIcon, Image pImage, boolean pMyTurn) {
		super(pImage);
		opponentName = pOpponentName;
		backImage = pImage;
		setOpaque(false);
		callback = pCallback;
		
		setPreferredSize(new Dimension(MainFrame.panelW, MainFrame.panelH));
		setLayout(new BorderLayout());
		
		isCPU = !pAi.equals(OnlineDummyAI.class);

		// オンライン戦での切断に対応した通信処理。負け数を1増やす
		if (!isCPU) {
			ClientConnection.updateResultOnline(0, 1);
			User user = callback.getUserData();
			user.setOnlineLose(user.getOnlineLose() + 1);
		}

		turnIcon = new JLabel[2];
		playerStoneLabel = new JLabel[2];
		
		for (int i = 0; i < 2; i++){
			turnIcon[i] = new JLabel();
			turnIcon[i].setPreferredSize(new Dimension(40, 40));
            turnIcon[i].setIcon(new ImageIcon("image/turn" + (i == 0 ? "" : "Reverse") + ".png"));

			playerStoneLabel[i] = new JLabel("石の数:2");
			playerStoneLabel[i].setPreferredSize(new Dimension(100, 60));
			playerStoneLabel[i].setSize(new Dimension(100, 60));
//			playerStoneLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			playerStoneLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			playerStoneLabel[i].setFont(new Font("monospace", Font.BOLD, 16));
		}
		
		playerIconNum = pPIcon;
		opponentIconNum = pOIcon;
		lOpponentIcon = new JLabel(new ImageIcon("image/icon/icon" + (pOIcon+1) + ".png"));
		lOpponentIcon.setPreferredSize(new Dimension(100, 100));
		lOpponentIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		lPlayerIcon = new JLabel(new ImageIcon("image/icon/icon" + (pPIcon+1) + ".png"));
		lPlayerIcon.setPreferredSize(new Dimension(100, 100));
		lPlayerIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		lOthelloPanel = new Othello(this, pAi, pDifficulty, pMyTurn);
		lCoverPanel = new JPanel();
		lCoverPanel.setOpaque(false);
		lCoverPanel.setLayout(new FlowLayout());
		
		selectedAI = pAi;
		selectedDifficulty = pDifficulty;

		// FlowLayout flow1 = new FlowLayout();
		// flow1.setAlignment(FlowLayout.CENTER);
		// lCoverPanel.setLayout(flow1);
		lCoverPanel.setPreferredSize(new Dimension(lOthelloPanel.getWidth() + 50, MainFrame.panelH));
		int margin = (MainFrame.panelH - lOthelloPanel.getHeight()) / 2;
		lCoverPanel.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));

		lCoverPanel.add(lOthelloPanel);
		add(lCoverPanel, BorderLayout.CENTER);

		showInfo();

		// validate();
		player = new BasicPlayer();
		InputStream lInputStream = MainFrame.class.getClassLoader().getResourceAsStream("bgm.mp3");
		try {
			player.open(lInputStream);
			player.play();
			player.addBasicPlayerListener(this);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void showInfo() {

		/*
		 * 自分と相手
		 */
		JPanel lInfoPanel = new JPanel();
		lInfoPanel.setPreferredSize(new Dimension(MainFrame.panelW - lCoverPanel.getPreferredSize().width, MainFrame.panelH));
		lInfoPanel.setLayout(new BorderLayout());
		lInfoPanel.setOpaque(false);

		/*
		 * 相手情報
		 */
		JPanel lOpponentPanel = new JPanel();
		lOpponentPanel.setLayout(new BorderLayout());

		// アイコン
		lOpponentPanel.add(lOpponentIcon, BorderLayout.EAST);

		// 名前
		JPanel lOpponentNIPanel = new JPanel();
		lOpponentNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 100));
		lOpponentNIPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lOpponentNIPanel.setLayout(new BorderLayout());
		JLabel lOpponentNameLabel = new JLabel(opponentName);
		lOpponentNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		JPanel lPositioningPanel = new JPanel(new BorderLayout());
		lPositioningPanel.add(lOpponentNameLabel, BorderLayout.EAST);
		lOpponentNIPanel.add(lPositioningPanel, BorderLayout.NORTH);
		// 石数とアイテム
		JPanel lOpponentSIPanel = new JPanel();
		FlowLayout lOpponentSILayout = new FlowLayout(FlowLayout.LEFT, 10, 0);
		lOpponentSIPanel.setLayout(lOpponentSILayout);
		opponentItemButton = new JButton();
		opponentItemButton.setEnabled(false);
        lOpponentSIPanel.add(opponentItemButton);
        lOpponentSIPanel.add(playerStoneLabel[1]);
		opponentItemButton.setPreferredSize(new Dimension(60, 60));
		lOpponentNIPanel.add(lOpponentSIPanel);

		lOpponentPanel.add(lOpponentNIPanel, BorderLayout.CENTER);
		/*
		 * プレイヤー情報
		 */
		JPanel lPlayerPanel = new JPanel();
		lPlayerPanel.setLayout(new BorderLayout());

		// アイコン
		lPlayerPanel.add(lPlayerIcon, BorderLayout.WEST);

		// 名前
		JPanel lPlayerNIPanel = new JPanel();
		lPlayerNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 100));
		lPlayerNIPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lPlayerNIPanel.setLayout(new BorderLayout());
		JLabel lPlayerNameLabel = new JLabel(callback.getUserData().getUserName());
		lPlayerNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		lPlayerNIPanel.add(lPlayerNameLabel, BorderLayout.SOUTH);
		lPlayerNIPanel.setOpaque(true);
		// 石数とアイテム
		JPanel lPlayerSIPanel = new JPanel();
		FlowLayout lPlayerSILayout = new FlowLayout(FlowLayout.RIGHT, 10, 0);
		lPlayerSIPanel.setLayout(lPlayerSILayout);
		playerItemButton = new JButton();
		playerItemButton.setEnabled(false);
		lPlayerSIPanel.add(playerStoneLabel[0]);
		lPlayerSIPanel.add(playerItemButton);
		playerItemButton.setPreferredSize(new Dimension(60, 60));
		lPlayerNIPanel.add(lPlayerSIPanel);

		lPlayerPanel.add(lPlayerNIPanel, BorderLayout.CENTER);
		
		/*
		 * ターン情報
		 */
		JPanel lTurnPanel = new JPanel();
		lTurnPanel.setLayout(new BorderLayout());
		lTurnPanel.setOpaque(false);
		
		JPanel lOpponentTurnWrapPanel = new JPanel();
		lOpponentTurnWrapPanel.setOpaque(false);
		lOpponentTurnWrapPanel.setPreferredSize(new Dimension(lPlayerPanel.getPreferredSize().width, 40));
		FlowLayout fl1 = new FlowLayout(FlowLayout.RIGHT, 30, 0);
		lOpponentTurnWrapPanel.setLayout(fl1);
		lOpponentTurnWrapPanel.add(turnIcon[1]);
		JPanel lPlayerTurnWrapPanel = new JPanel();
		lPlayerTurnWrapPanel.setOpaque(false);
		lPlayerTurnWrapPanel.setPreferredSize(new Dimension(lPlayerPanel.getPreferredSize().width, 40));
		FlowLayout fl2 = new FlowLayout(FlowLayout.LEFT, 30, 0);
		lPlayerTurnWrapPanel.setLayout(fl2);
		lPlayerTurnWrapPanel.add(turnIcon[0]);
		
		lTurnPanel.add(lOpponentTurnWrapPanel, BorderLayout.NORTH);
		lTurnPanel.add(lPlayerTurnWrapPanel, BorderLayout.SOUTH);
		
		lInfoPanel.add(lOpponentPanel, BorderLayout.NORTH);
		lInfoPanel.add(lPlayerPanel, BorderLayout.SOUTH);
		lInfoPanel.add(lTurnPanel, BorderLayout.CENTER);

		add(lInfoPanel, BorderLayout.EAST);
	}

	@Override
	public void onTurnChange(boolean isMyTurn, int[] pCountStones) {
		turnIcon[0].setVisible(isMyTurn);
		turnIcon[1].setVisible(!isMyTurn);
		
		for (int i = 0; i < 2; i++)
			playerStoneLabel[i].setText("石の数:" + pCountStones[i]);
	}

	@Override
	public void onGameOver(int result) {
		if (isCPU) {
			int cpuID = 0;
			if 		(selectedAI.equals(AlphaAI.class)) cpuID = 0;
			else if (selectedAI.equals(BetaAI.class))  cpuID = 1;
			else if (selectedAI.equals(GammaAI.class)) cpuID = 2;
			else if (selectedAI.equals(OmegaAI.class)) cpuID = 3;
			ClientConnection.updateResultCPU(cpuID, selectedDifficulty, result);
			Offline[] offlines = callback.getUserData().getOfflines();
			// 手抜きですごめんなさい
			if (selectedDifficulty == 0) {
				if (result == 1) offlines[cpuID].easyWinInc();
				else			 offlines[cpuID].easyLoseInc();
			}
			else if (selectedDifficulty == 1) {
				if (result == 1) offlines[cpuID].normalWinInc();
				else			 offlines[cpuID].normalLoseInc();
			}
			else if (selectedDifficulty == 2) {
				if (result == 1) offlines[cpuID].hardWinInc();
				else			 offlines[cpuID].hardLoseInc();
			}
		}
		else {
			// オンライン戦は事前に負け数を増やしているので、勝ちの処理だけでいい
			if (result == 1 || result == -1) {
				ClientConnection.updateResultOnline(1, 1);
				ClientConnection.updateResultOnline(0, -1);
				User user = callback.getUserData();
				user.setOnlineWin(user.getOnlineWin() + 1);
				user.setOnlineLose(user.getOnlineLose() - 1);
			}
		}

		// 結果表示ダイアログ(モーダル)
		JDialog lResultDialog = new JDialog();
		lResultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		lResultDialog.setLocationRelativeTo(this);
		lResultDialog.setSize(400, 170);
		lResultDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		lResultDialog.setResizable(false);
		
		JPanel lResultPanel = (JPanel) lResultDialog.getContentPane();
		lResultDialog.setLayout(new FlowLayout());
		
		JPanel lWLWrapper = new JPanel();
		lWLWrapper.setPreferredSize(new Dimension(400, 55));
		
		JLabel lWLabel = new JLabel(new ImageIcon("image/res" + ((result == 1 || result == -1) ? "win": "lose") + ".png"));
		lWLabel.setFont(new Font("Serif", Font.BOLD, 36));
		lWLWrapper.add(lWLabel);
		
		JPanel lBWWrapper = new JPanel();
		lBWWrapper.setPreferredSize(new Dimension(400, 25));
		JLabel lBWLabel = new JLabel();
		if (result == -1)
			lBWLabel.setText("相手の通信が切断されました");
		else
			lBWLabel = new JLabel(String.format("黒:%2d, 白:%2d", lOthelloPanel.countStone(Stone.Black), lOthelloPanel.countStone(Stone.White)));
		lBWWrapper.add(lBWLabel);
		
		JButton lResultOK = new JButton("OK");
		lResultOK.addActionListener(e -> lResultDialog.dispose());
		
		lResultPanel.add(lWLWrapper);
		lResultPanel.add(lBWWrapper);
		lResultPanel.add(lResultOK);
		
		lResultDialog.setVisible(true);
		
		// 再戦確認
		int lDialogResult = JOptionPane.NO_OPTION;
		if (result != -1) {
			lDialogResult = JOptionPane.showConfirmDialog(this, "再戦しますか？", "Retry?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lDialogResult == JOptionPane.YES_OPTION) {
			if (isCPU) {
				// 対CPU戦は同じ難易度でもう一度
				callback.changePlayPanel(selectedAI, selectedDifficulty, opponentName,
						playerIconNum, opponentIconNum, backImage, new Random().nextBoolean());
			} else {
				// 対人戦は再戦を申し込む
			}
		} else {
			// 終了
			callback.returnTitlePanel();
		}
		
	}

	@Override
	public void onGameAborted() {
	}

	@Override
	public void onGainItem(boolean playerTurn) {
		if (playerTurn) {
			// アイテムを上書きしないように
			if (havingItem != Item.NONE) return;
			int tR = new Random().nextInt(Item.values().length - 1);
			havingItem = Item.values()[tR + 1];
			playerItemButton.setEnabled(true);
			playerItemButton.setIcon(new ImageIcon("image/item/"+havingItem.name().toLowerCase()+".png"));
			playerItemButton.setToolTipText(havingItem.toString());
			playerItemButton.addActionListener(e -> {
				if(havingItem != null && !havingItem.equals(Item.NONE)) lOthelloPanel.useItem(havingItem);
				havingItem = Item.NONE;
				playerItemButton.setIcon(null);
				playerItemButton.setEnabled(false);
				playerItemButton.setToolTipText(havingItem.toString());
			});
		}
		else {
			// setIconで通常のアイコンもセットしないと無効化されたアイコンが表示されないみたい
			opponentItemButton.setIcon(new ImageIcon("image/item/itemSecret.png"));
			opponentItemButton.setDisabledIcon(new ImageIcon("image/item/itemSecret.png"));
		}
	}

	@Override
	public void enableItem() {
		if (playerItemButton != null) {
			playerItemButton.setEnabled(true);
		}
	}

	@Override
	public void disableItem() {
		if (playerItemButton != null) {
			playerItemButton.setEnabled(false);
		}
	}

	@Override
	public void onOpponentUseItem() {
		opponentItemButton.setIcon(null);
		opponentItemButton.setIcon(null);
	}

	@Override
	public void opened(Object arg0, Map arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		System.err.println("EventCalled" + arg0.getCode());
		if (arg0.getCode() == BasicPlayerEvent.EOM) {
			try {
				player.open(MainFrame.class.getClassLoader().getResourceAsStream("bgm.mp3"));
				player.play();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
	}

}
