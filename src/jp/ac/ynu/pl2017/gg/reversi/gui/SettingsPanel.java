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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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

public class SettingsPanel extends BackgroundedPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1395349167588587208L;
	
	// ユーザーデータ保管場所
	public static String	username	= "6s";
	// オンライン勝敗
	public static int		onlineWL;
	// CPU勝敗
	public static int		CPUWL[][][];
	// アイコン
	public static int		iconIndex	= 0;
	// 背景
	public static int		backIndex	= 0;

	public static int		temp		= 0;

	private	JLabel			iconLabel;

	private	JLabel			onlineWLLabel;

	private JLabel onlineItemLabel;

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
		lReturnPanel.setOpaque(false);
		lReturnPanel.setPreferredSize(new Dimension(MainFrame.panelW, 40));
		lReturnPanel.setLayout(new BorderLayout());
		lReturnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> callback.returnTitlePanel());
		lReturnPanel.add(returnButton, BorderLayout.EAST);
//		lReturnPanel.add(returnButton);
		add(lReturnPanel);
		
		JPanel lIconNamePanel = new JPanel(new BorderLayout());
		lIconNamePanel.setPreferredSize(new Dimension(MainFrame.panelW - 20, 220));
		lIconNamePanel.setOpaque(false);

		/*
		 * アイコン
		 */
		JPanel lIconPanel = new JPanel(new BorderLayout());
		lIconPanel.setSize(90, 90);
		iconLabel = new JLabel(new ImageIcon("image/icon/icon"+(iconIndex+1)+".png"));
		iconLabel.setSize(new Dimension(90, 90));
		lIconPanel.add(iconLabel, BorderLayout.CENTER);
		JButton lIconChanger = new JButton("アイコンの変更");
		lIconChanger.addActionListener(e -> showImageSelectDialog(0));
		lIconPanel.add(lIconChanger, BorderLayout.SOUTH);

		lIconNamePanel.add(lIconPanel, BorderLayout.WEST);

		/*
		 * 名前
		 */
		FlowLayout fl1 = new FlowLayout();
		fl1.setAlignment(FlowLayout.LEFT);

		JPanel lNamePanel = new JPanel(new FlowLayout());
		lNamePanel.setOpaque(false);
		lNamePanel.setLayout(fl1);

		JPanel lNameWrapperPanel = new JPanel();
		lNameWrapperPanel.setLayout(fl1);
		lNameWrapperPanel.setPreferredSize(new Dimension(MainFrame.panelW - 200, 70));
		JLabel lNameLabel = new JLabel("肋助", SwingConstants.LEFT);
		Font lNameFont = new Font("Serif", Font.BOLD, 48);
		lNameLabel.setFont(lNameFont);
		lNameWrapperPanel.add(lNameLabel);
		
		// 変更ボタン
		JButton lInfoChangeButton = new JButton("アカウント情報変更");
		lInfoChangeButton.addActionListener(e -> showChangeInfoDialog());
		JButton lBackChangeButton = new JButton("背景変更");
		lBackChangeButton.addActionListener(e -> showImageSelectDialog(1));
		
		lNamePanel.add(lNameWrapperPanel);
		lNamePanel.add(lInfoChangeButton);
		lNamePanel.add(lBackChangeButton);
		
		/*
		 * オンライン情報
		 */
		JPanel lOnlineInfoPanel = new JPanel(new FlowLayout());
		lOnlineInfoPanel.setPreferredSize(new Dimension(MainFrame.panelW, 40));
		JLabel lLabel1 = new JLabel("オンライン");
		
		onlineWLLabel = new JLabel("0勝0敗");
		Font lOnlineWLFont = new Font("Serif", Font.BOLD, 24);
		onlineWLLabel.setFont(lOnlineWLFont);
		
		onlineItemLabel = new JLabel("アイテム使用回数 0回");
		
		lOnlineInfoPanel.add(lLabel1);
		lOnlineInfoPanel.add(onlineWLLabel);
		lOnlineInfoPanel.add(onlineItemLabel);
		
		/*
		 * オフライン情報
		 */
		JPanel lOfflineInfoPanel = new JPanel(new GridLayout(4, 5));
		lOfflineInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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

		BufferedImage lBufferedImage = null;
		try {
			InputStream lInputStream =
					MainFrame.class.getClassLoader().getResourceAsStream("background/back"+(backIndex+1)+".png");
			lBufferedImage = ImageIO.read(lInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBackground(lBufferedImage);
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
			String tImagePath = "image/" + (mode == 0 ? "icon/icon" : "background/backThumb") + (i+1) + ".png";
			lSelectButtons[i] = new JButton(new ImageIcon(tImagePath));
			lSelectButtons[i].setPreferredSize(new Dimension(90, 90));
			lSelectButtons[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (mode == 0) {
						iconLabel.setIcon(new ImageIcon("image/icon/icon"+(ii+1)+".png"));
					} else {
						BufferedImage lBufferedImage = null;
						InputStream lInputStream =
								MainFrame.class.getClassLoader().getResourceAsStream("background/back"+(ii+1)+".png");
						try {
							lBufferedImage = ImageIO.read(lInputStream);
						} catch (IOException e) {
							e.printStackTrace();
						}
						setBackground(lBufferedImage);
						SettingsPanel.this.repaint();
					}
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
