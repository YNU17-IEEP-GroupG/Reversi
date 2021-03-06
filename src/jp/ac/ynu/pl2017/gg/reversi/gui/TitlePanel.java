package jp.ac.ynu.pl2017.gg.reversi.gui;

import javax.swing.*;

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;
import jp.ac.ynu.pl2017.gg.reversi.util.User;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

import java.awt.Image;

/**
 * Created by shiita on 2017/05/12.
 */
public class TitlePanel extends BackgroundedPanel {

	public TitlePanel(Transition callback, Image pImage) {
		super(pImage);
		setOpaque(false);
		setSize(panelW, panelH);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		ImageIcon image = new ImageIcon("image/Title.png");
		JLabel title = new JLabel(image);
		JButton offline = new JButton("CPU対戦");
		JButton online = new JButton("オンライン対戦");
		JButton option = new JButton("オプション");

		int tw = image.getIconWidth();
		int widthMargin = (panelW - tw) / 2;

		layout.putConstraint(SpringLayout.NORTH, title, 80, SpringLayout.NORTH,
				this);
		layout.putConstraint(SpringLayout.WEST, title, widthMargin,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, offline, panelH / 2 + 40,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, offline, widthMargin + 50,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, offline, -(widthMargin + 50),
				SpringLayout.EAST, this);

		layout.putConstraint(SpringLayout.NORTH, online, 40,
				SpringLayout.NORTH, offline);
		layout.putConstraint(SpringLayout.WEST, online, widthMargin + 50,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, online, -(widthMargin + 50),
				SpringLayout.EAST, this);

		layout.putConstraint(SpringLayout.NORTH, option, 10,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, option, -10, SpringLayout.EAST,
				this);

		add(title);
		add(offline);
		add(online);
		add(option);

		offline.addActionListener(e -> {
			if (!callback.isLogin()) {
				callback.showLoginDialog();
				if (!callback.isLogin()) {
					return;
				}
			}
			callback.changeOfflinePlayPanel();
		});
		online.addActionListener(e -> callback.changeOnlinePlayPanel());
		option.addActionListener(e -> callback.changeSettingsPanel());
	}

	public interface Transition {
		void changeOfflinePlayPanel();

		void changeOnlinePlayPanel();

		void changeSettingsPanel();

		void returnTitlePanel();

		void showLoginDialog();

		void changePlayPanel(Class<? extends BaseAI> pAi, int pDifficulty, String pOpponentName,
				int pPIcon, int pOIcon, int pImage, boolean pMyTurn);
		
		void changePlayPanel(Class<? extends BaseAI> pAi, int pDifficulty, String pOpponentName,
				int pPIcon, int pOIcon, Image pImage, boolean pMyTurn);
		
		User getUserData();
		
		boolean isLogin();

		void showRoomSearchDialog(); 
	}
}
