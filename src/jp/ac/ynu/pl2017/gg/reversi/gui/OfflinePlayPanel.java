package jp.ac.ynu.pl2017.gg.reversi.gui;

import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.ynu.pl2017.gg.reversi.ai.AlphaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.BetaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.GammaAI;
import jp.ac.ynu.pl2017.gg.reversi.ai.OmegaAI;
import jp.ac.ynu.pl2017.gg.reversi.gui.TitlePanel.Transition;
import jp.ac.ynu.pl2017.gg.reversi.util.Offline;

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
		JLabel lCap2 = new JLabel("Type α");
		JLabel lCap3 = new JLabel("Type β");
		JLabel lCap4 = new JLabel("Type γ");
		JLabel lCap5 = new JLabel("Type ω");

		lDifficultyPanel.add(lCap1);
		lDifficultyPanel.add(lCap2);
		lDifficultyPanel.add(lCap3);
		lDifficultyPanel.add(lCap4);
		lDifficultyPanel.add(lCap5);

		// 各難易度選択
		String ROW[] = {
				"弱い",
				"普通",
				"強い" };
		for (int tLevel = 2; tLevel >= 0; tLevel--) {
			for (int tCPUType = -1; tCPUType < 4; tCPUType++) {
				if (tCPUType == -1) {
					JLabel tLvLabel = new JLabel(ROW[tLevel]);
					lDifficultyPanel.add(tLvLabel);
				} else {
					JButton tPlayButton = new JButton(new ImageIcon("image/unlock.png"));
					final int tAiType = tCPUType;
					final int tAiDiff = tLevel;
					// 戦績条件を満たさない場合は無効化する
					if (tAiDiff > 0) {
						Offline tOffline = callback.getUserData().getOfflines()[tAiType];
						if (tOffline.getWLLists()[tAiDiff - 1][0] <= 0) {
							tPlayButton.setIcon(new ImageIcon("image/lock.png"));
							tPlayButton.setEnabled(false);
						}
					}
					tPlayButton.addActionListener(e ->
							callback.changePlayPanel(
									aiList[tAiType], tAiDiff, "CPU",
									callback.getUserData().getIcon(), 0, callback.getUserData().getBackground(),
									new Random().nextBoolean()
									));
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
