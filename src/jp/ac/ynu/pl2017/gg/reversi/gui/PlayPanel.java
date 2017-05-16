package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ゲーム画面を構築するJPanel. 対人・対CPU問わずここに飛んでくる発想.
 *
 */
public class PlayPanel extends JPanel {

	private boolean	isCPU	= false;
	/**
	 * α～ωに対応,4種
	 */
	private int		cpuType	= 0;
	/**
	 * 0:弱い; 2:強い
	 */
	private int		cpuDiff	= 0;

	public Othello	lOthelloPanel;
	private JPanel	lCoverPanel;

	public PlayPanel(TitlePanel.Transition callback) {
		setPreferredSize(new Dimension(MainFrame.panelW, MainFrame.panelH));
		setLayout(new BorderLayout());

		lOthelloPanel = new Othello();
		lCoverPanel = new JPanel();
		lCoverPanel.setLayout(new FlowLayout());

		JPanel lEmptyPanel = new JPanel();
		lEmptyPanel.setPreferredSize(new Dimension(MainFrame.panelW, (MainFrame.panelH - lOthelloPanel.getHeight()) / 2));
		// FlowLayout flow1 = new FlowLayout();
		// flow1.setAlignment(FlowLayout.CENTER);
		// lCoverPanel.setLayout(flow1);
		lCoverPanel.setPreferredSize(new Dimension(lOthelloPanel.getWidth() + 50, lOthelloPanel.getHeight()));

		lCoverPanel.add(lEmptyPanel);
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
		lOpponentPanel.add(lOpponentIcon, BorderLayout.EAST);

		// 名前
		JPanel lOpponentNIPanel = new JPanel();
		lOpponentNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 80));
		lOpponentNIPanel.setLayout(new GridLayout(2, 1));
		JLabel lOpponentNameLabel = new JLabel("CPU");
		lOpponentNIPanel.add(lOpponentNameLabel);
		// 石数とアイテム
		JPanel lOpponentSIPanel = new JPanel();
		lOpponentSIPanel.setLayout(new FlowLayout());
		JLabel lStoneLabel = new JLabel("2");
		JLabel lItemLabel = new JLabel();
		lOpponentSIPanel.add(lStoneLabel);
		lOpponentSIPanel.add(lItemLabel);
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
		lPlayerPanel.add(lPlayerIcon, BorderLayout.WEST);

		// 名前
		JPanel lPlayerNIPanel = new JPanel();
		lPlayerNIPanel.setPreferredSize(new Dimension(MainFrame.panelW - lOthelloPanel.getWidth(), 80));
		lPlayerNIPanel.setLayout(new GridLayout(2, 1));
		JLabel lPlayerNameLabel = new JLabel("Player");

		// 石数とアイテム
		JPanel lPlayerSIPanel = new JPanel();
		lPlayerSIPanel.setLayout(new FlowLayout());
		JLabel lPStoneLabel = new JLabel("2");
		JLabel lPItemLabel = new JLabel();
		lPlayerSIPanel.add(lPStoneLabel);
		lPlayerSIPanel.add(lPItemLabel);
		lPlayerNIPanel.add(lPlayerSIPanel);

		lPlayerPanel.add(lPlayerNIPanel, BorderLayout.CENTER);
		lPlayerNIPanel.add(lPlayerNameLabel);

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

}
