package jp.ac.ynu.pl2017.gg.reversi.gui;

import sun.applet.Main;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class MainFrame extends JFrame implements TitlePanel.Transition {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 248580253941254005L;
	public static final int		panelW				= 854;
	public static final int		panelH				= 480;
	private static final String	TITLE				= "title";
	private static final String	ONLINE				= "online";
	private static final String	OFFLINE				= "offline";
	private static final String	SETTINGS			= "settings";
	private CardLayout			layout;

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
		SettingsPanel settingsCard = new SettingsPanel(this);

		add(titleCard, TITLE);
		add(offlineCard, OFFLINE);
		add(onlineCard, ONLINE);
		add(settingsCard, SETTINGS);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		showLoginDialog();
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

	@Override
	public void changePlayPanel() {
		setContentPane(new PlayPanel(this));
		validate();
	}

	@Override
	public void showLoginDialog() {
		new LoginDialog(this);
	}

	private class LoginDialog extends JDialog {
		MainFrame mainFrame;
        boolean login = false;

		private LoginDialog(Frame owner) {
			super(owner);
			mainFrame = (MainFrame) owner;
			setTitle("ログイン");
			int width = 380;
			int height = 150;
			setSize(width, height);
			Dimension pSize = mainFrame.getSize();
			setLocation((int)(pSize.getWidth() - width) / 2, (int)(pSize.getHeight() - height) / 2);
            setModal(true);

            JLabel userLabel = new JLabel("ユーザー名");
            JTextField userInput = new JTextField(14);
            JLabel passLabel = new JLabel("パスワード");
            JPasswordField passInput = new JPasswordField(14);
            JPanel inputPanel = new JPanel();
            userInput.setHorizontalAlignment(JTextField.CENTER);
            passInput.setHorizontalAlignment(JPasswordField.CENTER);
            inputPanel.setLayout(new GridLayout(2, 2));
            inputPanel.add(userLabel);
            inputPanel.add(userInput);
            inputPanel.add(passLabel);
            inputPanel.add(passInput);

            JButton loginButton = new JButton("OK");
            JButton createButton = new JButton("新規作成");
            loginButton.addActionListener(e -> {
                // TODO: ログインの通信処理を書いてください
                // 通信クラスのメソッド呼び出しなど
                // 今はOKを押したらダイアログが消えるようにします
                login = true;
                dispose();
            });
            createButton.addActionListener(e -> {
                // TODO: アカウント新規作成の処理を書いてください
            });

            setLayout(new FlowLayout());
            add(inputPanel);
            add(loginButton);
            add(createButton);

            setVisible(true);
            setDefaultCloseOperation(WindowConstants	.DISPOSE_ON_CLOSE);
        }
    }
}










