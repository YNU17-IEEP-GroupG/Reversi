package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1395349167588587208L;

	public SettingsPanel(TitlePanel.Transition callback) {
		super();

		setSize(854, 480);

		setLayout(new FlowLayout());

		/*
		 * 画面上部
		 */
		JPanel lIconNamePanel = new JPanel(new BorderLayout());
		lIconNamePanel.setPreferredSize(new Dimension(840, 220));

		/*
		 * アイコン
		 */
		JPanel lIconPanel = new JPanel(new BorderLayout());
		lIconPanel.setSize(200, 220);
		JLabel lIconLabel = new JLabel(new ImageIcon("./200.png"));
		lIconLabel.setSize(new Dimension(200, 200));
		lIconPanel.add(lIconLabel, BorderLayout.CENTER);
		JButton lIconChanger = new JButton("アイコンの変更");
		lIconPanel.add(lIconChanger, BorderLayout.SOUTH);

		lIconNamePanel.add(lIconPanel, BorderLayout.WEST);

		/*
		 * 名前
		 */
		JPanel lNamePanel = new JPanel(new FlowLayout());
		JLabel lNameLabel = new JLabel("肋助", SwingConstants.LEFT);
		Font lNameFont = new Font("Serif", Font.BOLD, 48);
		lNameLabel.setFont(lNameFont);
		lNameLabel.setPreferredSize(new Dimension(600, 70));
		lNamePanel.add(lNameLabel, BorderLayout.NORTH);

		lIconNamePanel.add(lNamePanel, BorderLayout.CENTER);

		add(lIconNamePanel);

		/*
		 * 戻る
		 */
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> callback.returnTitlePanel());
		add(returnButton, BorderLayout.SOUTH);
	}

	@Override
	public void validate() {
		super.validate();

		if (getParent() != null) {
			setSize(getParent().getSize());
		}
	}

}
