package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.OnlineDummyAI;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;

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

	private	Item	havingItem;
	
	private TitlePanel.Transition	callback;
	
	private	Class<BaseAI>	selectedAI;
	private	int				selectedDifficulty;
	private	JButton			playerItemButton;
	
	/**
	 * 0:自分 1:相手
	 */
	private JLabel	turnIcon[];
	private JLabel	playerStoneLabel[];

	private JLabel	lOpponentIcon;
	private JLabel	lPlayerIcon;
	
	private	BasicPlayer	player;
	
	public PlayPanel(TitlePanel.Transition pCallback, Class<BaseAI> pAi, int pDifficulty,
			int pPIcon, int pOIcon, Image pImage) {
		super(pImage);
		backImage = pImage;
		setOpaque(false);
		callback = pCallback;
		
		setPreferredSize(new Dimension(MainFrame.panelW, MainFrame.panelH));
		setLayout(new BorderLayout());
		
		isCPU = !pAi.equals(OnlineDummyAI.class);
		
		turnIcon = new JLabel[2];
		playerStoneLabel = new JLabel[2];
		
		for (int i = 0; i < 2; i++){
			turnIcon[i] = new JLabel();
			turnIcon[i].setPreferredSize(new Dimension(60, 60));
			turnIcon[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));

			playerStoneLabel[i] = new JLabel("2");
			playerStoneLabel[i].setPreferredSize(new Dimension(60, 60));
			playerStoneLabel[i].setSize(new Dimension(60, 60));
			playerStoneLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			playerStoneLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			playerStoneLabel[i].setFont(new Font("monospace", Font.BOLD, 16));
			playerStoneLabel[i].setHorizontalAlignment(SwingConstants.RIGHT);
		}
		
		playerIconNum = pPIcon;
		opponentIconNum = pOIcon;
		lOpponentIcon = new JLabel(new ImageIcon("image/icon/icon" + (pOIcon+1) + ".png"));
		lOpponentIcon.setPreferredSize(new Dimension(90, 90));
		lOpponentIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		lPlayerIcon = new JLabel(new ImageIcon("image/icon/icon" + (pPIcon+1) + ".png"));
		lPlayerIcon.setPreferredSize(new Dimension(90, 90));
		lPlayerIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		lOthelloPanel = new Othello(this, pAi, pDifficulty);
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
		JLabel lOpponentNameLabel = new JLabel("CPU");
		lOpponentNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		lOpponentNIPanel.add(lOpponentNameLabel, BorderLayout.NORTH);
		// 石数とアイテム
		JPanel lOpponentSIPanel = new JPanel();
		FlowLayout lOpponentSILayout = new FlowLayout();
		lOpponentSILayout.setAlignment(FlowLayout.LEFT);
		lOpponentSIPanel.setLayout(lOpponentSILayout);
		JButton lOppponentItemButton = new JButton();
		lOppponentItemButton.setEnabled(false);
		lOpponentSIPanel.add(playerStoneLabel[1]);
		lOpponentSIPanel.add(lOppponentItemButton);
		lOppponentItemButton.setPreferredSize(new Dimension(60, 60));
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
		JLabel lPlayerNameLabel = new JLabel(SettingsPanel.username);
		lPlayerNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		lPlayerNIPanel.add(lPlayerNameLabel, BorderLayout.SOUTH);
		// 石数とアイテム
		JPanel lPlayerSIPanel = new JPanel();
		FlowLayout lPlayerSILayout = new FlowLayout();
		lPlayerSILayout.setAlignment(FlowLayout.RIGHT);
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
		lOpponentTurnWrapPanel.setPreferredSize(new Dimension(lPlayerPanel.getPreferredSize().width, 65));
		FlowLayout fl1 = new FlowLayout();
		fl1.setAlignment(FlowLayout.RIGHT);
		lOpponentTurnWrapPanel.setLayout(fl1);
		lOpponentTurnWrapPanel.add(turnIcon[1]);
		JPanel lPlayerTurnWrapPanel = new JPanel();
		lPlayerTurnWrapPanel.setOpaque(false);
		lPlayerTurnWrapPanel.setPreferredSize(new Dimension(lPlayerPanel.getPreferredSize().width, 65));
		FlowLayout fl2 = new FlowLayout();
		fl2.setAlignment(FlowLayout.LEFT);
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
			playerStoneLabel[i].setText("" + pCountStones[i]);
	}

	@Override
	public void onGameOver() {
		int lDialogResult = JOptionPane.showConfirmDialog(null, "再戦しますか？", "Retry?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lDialogResult == JOptionPane.YES_OPTION) {
			if (isCPU) {
				// 対CPU戦は同じ難易度でもう一度
				callback.changePlayPanel(selectedAI, selectedDifficulty,
						playerIconNum, opponentIconNum, backImage);
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
	public void onGainItem() {
		int tR = new Random().nextInt(Item.values().length - 1);
		havingItem = Item.values()[tR + 1];
		playerItemButton.setEnabled(true);
		playerItemButton.setIcon(new ImageIcon("image/item/"+havingItem.name().toLowerCase()+".png"));
		playerItemButton.addActionListener(e -> {
			if(havingItem != null && !havingItem.equals(Item.NONE)) lOthelloPanel.useItem(havingItem);
			havingItem = Item.NONE;
			playerItemButton.setIcon(null);
			playerItemButton.setEnabled(false);
		});
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
		if (arg0.getValue() == BasicPlayerEvent.EOM) {
			try {
				player.seek(0);
				player.play();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
	}

}
