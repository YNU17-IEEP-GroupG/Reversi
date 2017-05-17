package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;

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
	
	private TitlePanel			titleCard;
	private OfflinePlayPanel	offlineCard;
	private OnlinePlayPanel		onlineCard;
	private SettingsPanel		settingsCard;

	public MainFrame() {
		super();
		setTitle("オセロ対戦ゲーム");
		/*getContentPane().*/setPreferredSize(new Dimension(panelW, panelH));
		setResizable(false);
		pack();

		layout = new CardLayout();
		setLayout(layout);
		titleCard = new TitlePanel(this);
		offlineCard = new OfflinePlayPanel(this);
		onlineCard = new OnlinePlayPanel(this);
		settingsCard = new SettingsPanel(this);

		/*
		add(titleCard, TITLE);
		add(offlineCard, OFFLINE);
		add(onlineCard, ONLINE);
		add(settingsCard, SETTINGS);
		*/
		setContentPane(titleCard);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		showLoginDialog();
	}

	@Override
	public void changeOfflinePlayPanel() {
		setContentPane(offlineCard);
		validate();
	}

	@Override
	public void changeOnlinePlayPanel() {
		setContentPane(onlineCard);
		validate();
	}

	@Override
	public void changeSettingsPanel() {
		setContentPane(settingsCard);
		validate();
	}

	@Override
	public void returnTitlePanel() {
		setContentPane(titleCard);
		validate();
	}

	public static void main(String[] args) {
		new MainFrame();
	}

	@Override
	public void changePlayPanel(Class<BaseAI> pAi, int pDifficulty) {
		setContentPane(new PlayPanel(this, pAi, pDifficulty));
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
