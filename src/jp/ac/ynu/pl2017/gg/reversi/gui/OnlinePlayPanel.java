package jp.ac.ynu.pl2017.gg.reversi.gui;

import javax.swing.*;

import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelH;
import static jp.ac.ynu.pl2017.gg.reversi.gui.MainFrame.panelW;

/**
 * Created by shiita on 2017/05/13.
 */
public class OnlinePlayPanel extends JPanel {
    public OnlinePlayPanel(TitlePanel.Transition callback) {
        setSize(panelW, panelH);
        add(new JLabel("オンライン対戦画面"));
        JButton returnButton = new JButton("タイトルに戻る");
        returnButton.addActionListener(e -> callback.returnTitlePanel());
        add(returnButton);
    }
}
