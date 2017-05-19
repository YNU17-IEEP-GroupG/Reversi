package jp.ac.ynu.pl2017.gg.reversi.gui;

import javax.swing.*;

import jp.ac.ynu.pl2017.gg.reversi.util.ClientConnection;

import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

/**
 * Created by shiita on 2017/05/13.
 */
public class OnlinePlayPanel extends JPanel {
	private	TitlePanel.Transition	callback;
	
	public OnlinePlayPanel(TitlePanel.Transition pCallback) {
		setSize(panelW, panelH);
		add(new JLabel("オンライン対戦画面"));
		JButton returnButton = new JButton("タイトルに戻る");
		returnButton.addActionListener(e -> callback.returnTitlePanel());
		add(returnButton);
		
		callback = pCallback;
		
		showRoomSearchDialog();
	}
	
	private void showRoomSearchDialog() {
		String tResult = JOptionPane.showInputDialog(this, "対戦相手名を入力.空欄でランダムマッチングになります", "対戦相手入力", JOptionPane.PLAIN_MESSAGE);
		if (tResult == null) {
			// キャンセル
			callback.returnTitlePanel();
		}
		ClientConnection.match(tResult.isEmpty(), tResult);
	}
}
