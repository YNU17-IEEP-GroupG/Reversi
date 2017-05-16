package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1395349167588587208L;
	
	private String	username	= "6s";
	private int		onlineWL;
	private int		hardCPUWL[][];
	private int		mediumCPUWL[][];
	private int		basicCPUWL[][];
	private int		iconIndex	= 0;
	private int		backIndex	= 0;
	private int		temp		= 0;

	public SettingsPanel(TitlePanel.Transition callback) {
		super();

		setSize(MainFrame.panelW, MainFrame.panelH);

		setLayout(new FlowLayout());

		/*
		 * 画面上部
		 */
		/*
		 * 戻る
		 */
		JPanel lReturnPanel = new JPanel();
		lReturnPanel.setPreferredSize(new Dimension(MainFrame.panelW, 50));
		SpringLayout lRLayout = new SpringLayout();
		lReturnPanel.setLayout(lRLayout);
		
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> callback.returnTitlePanel());
//		lReturnPanel.add(returnButton, BorderLayout.EAST);
		lRLayout.putConstraint(SpringLayout.NORTH, returnButton, 10,
				SpringLayout.NORTH, lReturnPanel);
		lRLayout.putConstraint(SpringLayout.EAST, returnButton, -10, SpringLayout.EAST,
				lReturnPanel);
		lReturnPanel.add(returnButton);
		add(lReturnPanel);
		
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
		lIconChanger.addActionListener(e -> showImageSelectDialog(0));
		lIconPanel.add(lIconChanger, BorderLayout.SOUTH);

		lIconNamePanel.add(lIconPanel, BorderLayout.WEST);

		/*
		 * 名前
		 */
		JPanel lNamePanel = new JPanel(new FlowLayout());

		JLabel lNameLabel = new JLabel("肋助", SwingConstants.LEFT);
		Font lNameFont = new Font("Serif", Font.BOLD, 48);
		lNameLabel.setFont(lNameFont);
		lNameLabel.setPreferredSize(new Dimension(MainFrame.panelW - 200, 70));
		
		// 変更ボタン
		JButton lInfoChangeButton = new JButton("アカウント情報変更");
		lInfoChangeButton.addActionListener(e -> showChangeInfoDialog());
		JButton lBackChangeButton = new JButton("背景変更");
		lBackChangeButton.addActionListener(e -> showImageSelectDialog(1));
		
		lNamePanel.add(lNameLabel);
		lNamePanel.add(lInfoChangeButton);
		lNamePanel.add(lBackChangeButton);
		
		/*
		 * オンライン情報
		 */
		JPanel lOnlineInfoPanel = new JPanel(new FlowLayout());
		lOnlineInfoPanel.setPreferredSize(new Dimension(MainFrame.panelW, 60));
		JLabel lLabel1 = new JLabel("オンライン");
		
		JLabel lOnlineWLLabel = new JLabel("0勝0敗");
		Font lOnlineWLFont = new Font("Serif", Font.BOLD, 36);
		lOnlineWLLabel.setFont(lOnlineWLFont);
		
		lOnlineInfoPanel.add(lLabel1);
		lOnlineInfoPanel.add(lOnlineWLLabel);
		
		/*
		 * オフライン情報
		 */
		JPanel lOfflineInfoPanel = new JPanel(new GridLayout(4, 5));
		lOfflineInfoPanel.setPreferredSize(new Dimension(MainFrame.panelW, 120));
		String CAP[] = {"オフライン", "vs α", "vs β", "vs γ", "vs ω"};
		String ROW[] = {"強い", "普通", "弱い"};
		
		
		
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 5; c ++) {
				if (r == 0) {
					JLabel tLabel = new JLabel(CAP[c]);
					lOfflineInfoPanel.add(tLabel);
				} else {
					if (c == 0) {
						JLabel tLabel = new JLabel(ROW[r-1]);
						lOfflineInfoPanel.add(tLabel);
					} else {
						JLabel tLabel = new JLabel("0勝0敗");
						lOfflineInfoPanel.add(tLabel);
					}
				}
			}
		}
		
		lIconNamePanel.add(lNamePanel, BorderLayout.CENTER);

		add(lIconNamePanel);
		add(lOnlineInfoPanel);
		add(lOfflineInfoPanel);

	}

	public void showChangeInfoDialog() {
		JDialog lChangeDialog = new JDialog();
		lChangeDialog.setTitle("ユーザ情報変更");
		lChangeDialog.setPreferredSize(new Dimension(400, 170));
		lChangeDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		lChangeDialog.setResizable(false);
		lChangeDialog.getContentPane().setLayout(new FlowLayout());
		
		JLabel lNameLabel = new JLabel("名前");
		JLabel lPassLabel1 = new JLabel("パスワード");
		JLabel lPassLabel2 = new JLabel("パスワード（確認）");
		
		JTextField lNameField = new JTextField(username);
		JPasswordField lPassField1 = new JPasswordField();
		JPasswordField lPassField2 = new JPasswordField();
		
		JPanel lChangePanel = new JPanel();
		lChangePanel.setPreferredSize(new Dimension(360, lNameField.getPreferredSize().height * 3));
		
		GroupLayout layout = new GroupLayout(lChangePanel);
		lChangePanel.setLayout(layout);
		
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup()
				.addComponent(lNameLabel).addComponent(lNameField));
		hGroup.addGroup(layout.createParallelGroup()
				.addComponent(lPassLabel1).addComponent(lPassField1));
		hGroup.addGroup(layout.createParallelGroup()
				.addComponent(lPassLabel2).addComponent(lPassField2));
		layout.setVerticalGroup(hGroup);
		
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup()
				.addComponent(lNameLabel).addComponent(lPassLabel1).addComponent(lPassLabel2));
		vGroup.addGroup(layout.createParallelGroup()
				.addComponent(lNameField).addComponent(lPassField1).addComponent(lPassField2));
		layout.setHorizontalGroup(vGroup);
		
		lChangeDialog.getContentPane().add(lChangePanel);
		
		JButton lOKButton = new JButton("OK");
		lOKButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lChangeDialog.dispose();
			}
		});
		JButton lCancelButton = new JButton("取消");
		lCancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lChangeDialog.dispose();
			}
		});

		lChangeDialog.getContentPane().add(lOKButton);
		lChangeDialog.getContentPane().add(lCancelButton);

		lChangeDialog.pack();
		lChangeDialog.setVisible(true);
	}
	
	/**
	 * @param mode 1なら背景,0ならアイコン
	 */
	public void showImageSelectDialog(final int mode) {
		JDialog lChangeDialog = new JDialog();
		lChangeDialog.setTitle(mode == 0 ? "アイコン変更" : "背景変更");
		lChangeDialog.setSize(new Dimension(400, 170));
		lChangeDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		lChangeDialog.setResizable(false);
		lChangeDialog.getContentPane().setLayout(new FlowLayout());
		
		temp = mode == 0 ? iconIndex : backIndex;
		
		JPanel lImagePanel = new JPanel(new GridLayout(1, 4));
		lImagePanel.setPreferredSize(new Dimension(360, 90));
		JButton lSelectButtons[] = new JButton[4];
		for (int i = 0; i < 4; i++) {
			final int ii = i;
			lSelectButtons[i] = new JButton();
			lSelectButtons[i].setPreferredSize(new Dimension(90, 90));
			lSelectButtons[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectButton(lSelectButtons, ii);
					temp = ii;
				}
			});
			lImagePanel.add(lSelectButtons[i]);
		}
		selectButton(lSelectButtons, temp);
		lChangeDialog.getContentPane().add(lImagePanel);
		
		JButton lOKButton = new JButton("OK");
		lOKButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mode == 0) iconIndex = temp; else backIndex = temp;
				lChangeDialog.dispose();
			}
		});
		JButton lCancelButton = new JButton("取消");
		lCancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lChangeDialog.dispose();
			}
		});
		
		lChangeDialog.getContentPane().add(lOKButton);
		lChangeDialog.getContentPane().add(lCancelButton);

		lChangeDialog.validate();
		lChangeDialog.setVisible(true);
	}
	
	private static void selectButton(JButton[] buttons, int i) {
		for (int j = 0; j < buttons.length; j++) {
			if (j == i) {
				buttons[j].setBorder(new LineBorder(Color.BLUE, 2, true));
			} else {
				buttons[j].setBorder(new LineBorder(Color.BLACK, 1, false));
			}
		}
	}

}
