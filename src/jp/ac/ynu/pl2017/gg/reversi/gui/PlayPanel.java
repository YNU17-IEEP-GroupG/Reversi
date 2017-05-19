package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;

/**
 * ゲーム画面を構築するJPanel. 対人・対CPU問わずここに飛んでくる発想.
 *
 */
public class PlayPanel extends JPanel implements PlayCallback {

	private boolean	isCPU	= false;
	/**
	 * α～ωに対応,4種
	 */
	private int		cpuType	= 0;
	/**
	 * 0:弱い; 2:強い
	 */
	private int		cpuDiff	= 0;

	private Item item = Item.NONE;

	public Othello	lOthelloPanel;
	private JPanel	lCoverPanel;
	
	private	Item havingItem;
	
	private TitlePanel.Transition	callback;
	
	private	Class<BaseAI>	selectedAI;
	private	int				selectedDifficulty;
	private	JButton			playerItemButton;

	public PlayPanel(TitlePanel.Transition pCallback, Class<BaseAI> pAi, int pDifficulty) {
		callback = pCallback;
		
		setPreferredSize(new Dimension(MainFrame.panelW, MainFrame.panelH));
		setLayout(new BorderLayout());

		lOthelloPanel = new Othello(this, pAi, pDifficulty);
		lCoverPanel = new JPanel();
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
	}

	public void showInfo() {

		/*
		 * 自分と相手
		 */
		JPanel lInfoPanel = new JPanel();
		lInfoPanel.setPreferredSize(new Dimension(MainFrame.panelW - lCoverPanel.getPreferredSize().width, MainFrame.panelH));
		lInfoPanel.setLayout(new BorderLayout());

		/*
		 * 相手情報
		 */
		JPanel lOpponentPanel = new JPanel();
		lOpponentPanel.setLayout(new BorderLayout());

		// アイコン
		JLabel lOpponentIcon = new JLabel();
		lOpponentIcon.setPreferredSize(new Dimension(80, 80));
		lOpponentIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lOpponentPanel.add(lOpponentIcon, BorderLayout.EAST);

		// 名前
		JPanel lOpponentNIPanel = new JPanel();
		lOpponentNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 100));
		lOpponentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lOpponentNIPanel.setLayout(new BorderLayout());
		JLabel lOpponentNameLabel = new JLabel("CPU");
		lOpponentNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		lOpponentNIPanel.add(lOpponentNameLabel, BorderLayout.NORTH);
		// 石数とアイテム
		JPanel lOpponentSIPanel = new JPanel();
		FlowLayout lOpponentSILayout = new FlowLayout();
		lOpponentSILayout.setAlignment(FlowLayout.LEFT);
		lOpponentSIPanel.setLayout(lOpponentSILayout);
		JLabel lOpponentStoneLabel = new JLabel("2");
		lOpponentStoneLabel.setPreferredSize(new Dimension(60, 60));
		lOpponentStoneLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lOpponentStoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lOpponentStoneLabel.setFont(new Font("monospace", Font.BOLD, 16));
		lOpponentStoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JButton lOppponentItemButton = new JButton();
		lOppponentItemButton.setEnabled(false);
		lOpponentSIPanel.add(lOpponentStoneLabel);
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
		JLabel lPlayerIcon = new JLabel();
		lPlayerIcon.setPreferredSize(new Dimension(80, 80));
		lPlayerIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lPlayerPanel.add(lPlayerIcon, BorderLayout.WEST);

		// 名前
		JPanel lPlayerNIPanel = new JPanel();
		lPlayerNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 100));
		lPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lPlayerNIPanel.setLayout(new BorderLayout());
		JLabel lPlayerNameLabel = new JLabel(SettingsPanel.username);
		lPlayerNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		lPlayerNIPanel.add(lPlayerNameLabel, BorderLayout.SOUTH);
		// 石数とアイテム
		JPanel lPlayerSIPanel = new JPanel();
		FlowLayout lPlayerSILayout = new FlowLayout();
		lPlayerSILayout.setAlignment(FlowLayout.RIGHT);
		lPlayerSIPanel.setLayout(lPlayerSILayout);
		JLabel lPlayerStoneLabel = new JLabel("2");
		lPlayerStoneLabel.setPreferredSize(new Dimension(60, 60));
		lPlayerStoneLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lPlayerStoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lPlayerStoneLabel.setFont(new Font("monospace", Font.BOLD, 16));
		lPlayerStoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		playerItemButton = new JButton();
		playerItemButton.setEnabled(false);
		lPlayerSIPanel.add(lPlayerStoneLabel);
		lPlayerSIPanel.add(playerItemButton);
		playerItemButton.setPreferredSize(new Dimension(60, 60));
		lPlayerNIPanel.add(lPlayerSIPanel);

		lPlayerPanel.add(lPlayerNIPanel, BorderLayout.CENTER);

		lInfoPanel.add(lOpponentPanel, BorderLayout.NORTH);
		lInfoPanel.add(lPlayerPanel, BorderLayout.SOUTH);

		add(lInfoPanel, BorderLayout.EAST);
	}

	public void setDifficulty(int pDiff) {
		cpuDiff = pDiff;
	}

	public void setType(int pType) {
		cpuType = pType;
	}

	@Override
	public void onGameOver() {
		int lDialogResult = JOptionPane.showConfirmDialog(null, "再戦しますか？", "Retry?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (lDialogResult == JOptionPane.YES_NO_OPTION) {
			if (isCPU) {
				// 対CPU戦は同じ難易度でもう一度
				callback.changePlayPanel(selectedAI, selectedDifficulty);
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

}
