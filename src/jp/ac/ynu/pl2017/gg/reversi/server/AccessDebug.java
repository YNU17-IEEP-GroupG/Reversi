package jp.ac.ynu.pl2017.gg.reversi.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by shiita on 2017/05/20.
 */
public class AccessDebug extends JFrame implements ActionListener {

    private static final String USER = "user";
    private static final String ALPHA = "alpha";
    private static final String BETA = "beta";
    private static final String GAMMA = "gamma";
    private static final String OMEGA = "omega";
    private static final String ONLINE = "online";
    private static final String[] COMMANDS = { USER, ALPHA, BETA, GAMMA, OMEGA, ONLINE };
    private JTextArea area;

    public AccessDebug() {
        setSize(500, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());
        for (String command : COMMANDS) {
            JButton button = new JButton(command);
            button.setActionCommand(command);
            button.addActionListener(this);
            add(button);
        }
        area = new JTextArea(23, 40);
        JScrollPane scroll = new JScrollPane(area,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = "";
        switch (e.getActionCommand()) {
            case USER:
                text = Access.getAllUserString();
                break;
            case ALPHA:
                System.out.println(Access.updateBack("日本", 1));
                text = Access.getAllOfflineString("alpha");
                break;
            case BETA:
                text = Access.getAllOfflineString("beta");
                break;
            case GAMMA:
                text = Access.getAllOfflineString("gamma");
                break;
            case OMEGA:
                text = Access.getAllOfflineString("omega");
                break;
            case ONLINE:
                text = Access.getAllOnlineString();
                break;
        }
        area.setText(text);
    }

    public static void main(String[] args) {
        new AccessDebug();
    }
}
