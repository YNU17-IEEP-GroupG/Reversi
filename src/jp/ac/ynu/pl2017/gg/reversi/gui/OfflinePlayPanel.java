package jp.ac.ynu.pl2017.gg.reversi.gui;

import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.ynu.pl2017.gg.reversi.ai.AlphaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.BetaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.GammaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.OmegaAI;
import jp.ac.ynu.pl2017.gg.reversi.gui.TitlePanel.Transition;

/**
 * 難易度選択からのプレイ画面遷移.
 * 
 * Created by shiita on 2017/05/13.
 */
public class OfflinePlayPanel extends JPanel {
	
	private	static final Class[]	aiList = {AlphaAI.class, BetaAI.class, GammaAI.class, OmegaAI.class};
	
	private	Transition	callback;
	
	public OfflinePlayPanel(TitlePanel.Transition pCallback) {
		callback = pCallback;

		setSize(panelW, panelH);
		setLayout(new BorderLayout());
					
		JPanel lTopPanel = new JPanel();
		lTopPanel.setLayout(new BorderLayout());
		lTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		JLabel lLocalLabel = new JLabel("難易度選択");
		// lLocalLabel.setPreferredSize(new Dimension(MainFrame.panelW, 60));
		// lLocalLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lTopPanel.add(lLocalLabel);
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> pCallback.returnTitlePanel());
		lTopPanel.add(returnButton, BorderLayout.EAST);
		
		add(lTopPanel, BorderLayout.NORTH);

		JPanel lDifficultyPanel = new JPanel();
		lDifficultyPanel.setLayout(new GridLayout(4, 5));

		// ラベルキャプション
		JLabel lCap1 = new JLabel("");
		JLabel lCap2 = new JLabel("α");
		JLabel lCap3 = new JLabel("β");
		JLabel lCap4 = new JLabel("γ");
		JLabel lCap5 = new JLabel("ω");

		lDifficultyPanel.add(lCap1);
		lDifficultyPanel.add(lCap2);
		lDifficultyPanel.add(lCap3);
		lDifficultyPanel.add(lCap4);
		lDifficultyPanel.add(lCap5);

		// 各難易度選択
		String ROW[] = {
				"強い",
				"普通",
				"弱い" };
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 5; c++) {
				if (c == 0) {
					JLabel tLvLabel = new JLabel(ROW[r]);
					lDifficultyPanel.add(tLvLabel);
				} else {
					JButton tPlayButton = new JButton("PLAY");
					
					final int tAiType = c-1;
					final int tAiDiff = 2-r;
					tPlayButton.addActionListener(e ->
							callback.changePlayPanel(
									aiList[tAiType], tAiDiff,
									callback.getUserData().getIcon(), 0, callback.getUserData().getBackground()));
					lDifficultyPanel.add(tPlayButton);
				}
			}
		}

		add(lDifficultyPanel, BorderLayout.CENTER);

		/*
		 * Othello othelloPanel = new Othello(); JPanel lCoverPanel = new
		 * JPanel(); lCoverPanel.setPreferredSize(othelloPanel.getSize());
		 * lCoverPanel.add(othelloPanel); add(lCoverPanel, BorderLayout.CENTER);
		 */

	}
}
