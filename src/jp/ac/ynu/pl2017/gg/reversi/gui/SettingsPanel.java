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
import javax.swing.*;
import javax.swing.border.LineBorder;

import jp.ac.ynu.pl2017.gg.reversi.gui.TitlePanel.Transition;
import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.Offline;

public class SettingsPanel extends BackgroundedPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1395349167588587208L;
	
	public static int		temp		= 0;

	private	JLabel			iconLabel;

	private	JLabel			onlineWLLabel;

	private JLabel			onlineItemLabel;

	private JLabel			nameLabel;
	
	private	Transition		callback;

	public SettingsPanel(TitlePanel.Transition pCallback) {
		super();
		callback = pCallback;

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
		iconLabel = new JLabel(new ImageIcon(
				"image/icon/icon" + (callback.getUserData().getIcon()+1) + ".png"));
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
		nameLabel = new JLabel(callback.getUserData().getUserName(), SwingConstants.LEFT);
		Font lNameFont = new Font("Serif", Font.BOLD, 48);
		nameLabel.setFont(lNameFont);
		lNameWrapperPanel.add(nameLabel);
		
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
		
		onlineWLLabel = new JLabel(""+callback.getUserData().getOnlineWin()+"勝"+callback.getUserData().getOnlineLose()+"敗");
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
		String ROW[] = {"弱い", "普通", "強い"};
		
		for (int tLevel = 3; tLevel >= 0; tLevel--) {
			for (int tCPUType = -1; tCPUType < 4; tCPUType ++) {
				if (tLevel == 3) {
					JLabel tLabel = new JLabel(CAP[tCPUType + 1]);
					lOfflineInfoPanel.add(tLabel);
				} else {
					if (tCPUType == -1) {
						JLabel tLabel = new JLabel(ROW[tLevel]);
						lOfflineInfoPanel.add(tLabel);
					} else {
						// オフライン戦績
						Offline tOffline = callback.getUserData().getOfflines()[tCPUType];
						int tWL[] = tOffline.getWLLists()[tLevel];
						JLabel tLabel = new JLabel("" + tWL[0] + "勝" + tWL[1] + "敗");
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
					MainFrame.class.getClassLoader().getResourceAsStream(
							"background/back" + (callback.getUserData().getBackground() + 1) + ".png");
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
		
		JTextField lNameField = new JTextField(callback.getUserData().getUserName());
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
				String password = String.valueOf(lPassField1.getPassword());
				String subPassword = String.valueOf(lPassField2.getPassword());
				if (!password.equals(subPassword)) {
					JOptionPane.showMessageDialog(lChangeDialog, "同じパスワードを入力してください", "エラー", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String newName = lNameField.getText();
					if (ClientConnection.updateNamePass(newName, password)) {
						JOptionPane.showMessageDialog(lChangeDialog, "アカウント情報を変更しました", "変更完了", JOptionPane.INFORMATION_MESSAGE);
						callback.getUserData().setUserName(newName);
						nameLabel.setText(newName);
					}
					else {
						JOptionPane.showMessageDialog(lChangeDialog, "アカウント情報を変更できませんでした", "エラー", JOptionPane.ERROR_MESSAGE);
					}
				}
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
		
		temp = mode == 0 ? callback.getUserData().getIcon() : callback.getUserData().getBackground();
		
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
				if (mode == 0) {
					callback.getUserData().setIcon(temp);
					ClientConnection.updateIcon(temp);
				} else {
					callback.getUserData().setBackground(temp);
					ClientConnection.updateBack(temp);
				}
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
