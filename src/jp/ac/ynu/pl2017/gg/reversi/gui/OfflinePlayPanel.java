package jp.ac.ynu.pl2017.gg.reversi.gui;

import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by shiita on 2017/05/13.
 */
public class OfflinePlayPanel extends JPanel {
	public OfflinePlayPanel(TitlePanel.Transition callback) {
		setSize(panelW, panelH);
		setLayout(new BorderLayout());
		
		JLabel lLocalLabel = new JLabel("オフライン対戦画面");
//		lLocalLabel.setPreferredSize(new Dimension(MainFrame.panelW, 60));
//		lLocalLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		add(lLocalLabel, BorderLayout.NORTH);
		
		Othello othelloPanel = new Othello();
		JPanel lCoverPanel = new JPanel();
		lCoverPanel.setPreferredSize(othelloPanel.getSize());
		lCoverPanel.add(othelloPanel);
		add(lCoverPanel, BorderLayout.CENTER);
		
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> callback.returnTitlePanel());
		add(returnButton, BorderLayout.SOUTH);
	}
}
