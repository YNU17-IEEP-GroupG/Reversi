package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;





import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;





import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.User;

public class MainFrame extends JFrame implements TitlePanel.Transition {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 248580253941254005L;
	public static final int		panelW				= 854;
	public static final int		panelH				= 480;
	private CardLayout			layout;
	
	private TitlePanel			titleCard;
	private OfflinePlayPanel	offlineCard;
	
	private	User				userData;

	public MainFrame() {
		super();
		setTitle("Reversi × Treasure");
		/*getContentPane().*/setPreferredSize(new Dimension(panelW, panelH));
		setResizable(false);
		pack();
		
		// 仮ユーザデータ作成
		userData = new User();
		userData.setUserName("6s");
		userData.setIcon(0);
		userData.setBackground(0);

		layout = new CardLayout();
		setLayout(layout);
		
		BufferedImage lBufferedImage = null;
		try {
			InputStream lInputStream = getClass().getClassLoader().getResourceAsStream("titleBack.png");
			lBufferedImage = ImageIO.read(lInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		titleCard = new TitlePanel(this, lBufferedImage);
		offlineCard = new OfflinePlayPanel(this);

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
		setContentPane(new OnlinePlayPanel(this));
		validate();
	}

	@Override
	public void changeSettingsPanel() {
		setContentPane(new SettingsPanel(this));
		validate();
	}

	@Override
	public void returnTitlePanel() {
		setContentPane(titleCard);
		validate();
	}

	public static void main(String[] args) {
		ClientConnection.init();
		new MainFrame();
	}

	@Override
	public void changePlayPanel(Class<BaseAI> pAi, int pDifficulty,
			int pPIcon, int pOIcon, int pImage) {
		BufferedImage bufferedImage = null;
		try {
			InputStream lInputStream =
					getClass().getClassLoader().getResourceAsStream("background/back"+(pImage+1)+".png");
			bufferedImage = ImageIO.read(lInputStream);
		} catch (IOException e) {
		}
		setContentPane(new PlayPanel(this, pAi, pDifficulty, pPIcon, pOIcon, bufferedImage));
		validate();
	}

	@Override
	public void changePlayPanel(Class<BaseAI> pAi, int pDifficulty, int pPIcon, int pOIcon, Image pImage) {
		setContentPane(new PlayPanel(this, pAi, pDifficulty, pPIcon, pOIcon, pImage));
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
				login = ClientConnection.login(userInput.getText(), new String(passInput.getPassword()));
				if (!login) {
					JOptionPane.showMessageDialog(this, "ログインに失敗しました", "エラー", JOptionPane.ERROR_MESSAGE);
				} else {
					// TODO 本実装待ち
//					ClientConnection.getUserData();
				}
				dispose();
			});
			createButton.addActionListener(e -> {
				login = ClientConnection.createUser(userInput.getText(), new String(passInput.getPassword()));
				if (!login) {
					JOptionPane.showMessageDialog(this, "ユーザ作成に失敗しました", "エラー", JOptionPane.ERROR_MESSAGE);
				} else {
					// TODO 本実装待ち
//					ClientConnection.getUserData();
				}
				dispose();
			});

			setLayout(new FlowLayout());
			add(inputPanel);
			add(loginButton);
			add(createButton);

			setVisible(true);
			setDefaultCloseOperation(WindowConstants	.DISPOSE_ON_CLOSE);
		}
	}

	@Override
	public User getUserData() {
		return userData;
	}
}
