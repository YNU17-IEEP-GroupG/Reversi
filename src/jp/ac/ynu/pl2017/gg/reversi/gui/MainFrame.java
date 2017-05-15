package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.*;
import java.awt.Dialog.ModalityType;

import javax.swing.*;

public class MainFrame extends JFrame implements TitlePanel.Transition{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 248580253941254005L;
	public static final int panelW = 854;
	public static final int panelH = 480;
	private static final String TITLE = "title";
	private static final String ONLINE = "online";
	private static final String OFFLINE = "offline";
	private static final String SETTINGS = "settings";
	private CardLayout layout;

	public MainFrame() {
		super();
		setTitle("オセロ対戦ゲーム");
		getContentPane().setPreferredSize(new Dimension(panelW, panelH));
		setResizable(false);
		pack();

		// メモリを食うようならCardLayoutはやめたほうが良いかも
		// TODO setContentPane() -> validate()では駄目?
		layout = new CardLayout();
		setLayout(layout);
		TitlePanel titleCard = new TitlePanel(this);
		OfflinePlayPanel offlineCard = new OfflinePlayPanel(this);
		OnlinePlayPanel onlineCard = new OnlinePlayPanel(this);
		SettingsPanel settingsCard = new SettingsPanel();

		add(titleCard, TITLE);
		add(offlineCard, OFFLINE);
		add(onlineCard, ONLINE);
		add(settingsCard, SETTINGS);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void showLoginPopup() {
		JDialog lLoginDialog = new JDialog();
		lLoginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		lLoginDialog.setSize(400, 300);

		JPanel lLDialogPanel = new JPanel(new GridLayout(4, 1));

		JPanel lIDPanel = new JPanel(new FlowLayout());
		JLabel lIDLabel = new JLabel("ユーザID");
		JTextField lIDField = new JTextField(10);
		lIDPanel.add(lIDLabel);
		lIDPanel.add(lIDField);
		lLDialogPanel.add(lIDPanel);

		JPanel lPassPanel = new JPanel(new FlowLayout());

	}

	@Override
	public void changeOfflinePlayPanel() {
		layout.show(getContentPane(), OFFLINE);
	}

	@Override
	public void changeOnlinePlayPanel() {
		layout.show(getContentPane(), ONLINE);
	}

	@Override
	public void changeSettingsPanel() {
		layout.show(getContentPane(), SETTINGS);
	}

	@Override
	public void returnTitlePanel() {
		layout.show(getContentPane(), TITLE);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
