package jp.ac.ynu.pl2017.gg.reversi.server;

import jp.ac.ynu.pl2017.gg.reversi.util.User;

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
            case BETA:
            case GAMMA:
            case OMEGA:
                text = Access.getAllOfflineString(e.getActionCommand());
                break;
            case ONLINE:
                text = Access.getAllOnlineString();
                break;
        }
        area.setText(text);
    }

    public static void main(String[] args) {
        new AccessDebug();
        // name alpha hard_win 272
//        for (int i = 0; i < 1000; i++) {
//            new Thread(() -> {
//                for (int j = 0; j < 10; j++) {
//                    System.out.println(j + ":" + Access.updateResult("name", 0, 2, 1, 1));
//                }
//                Access.closeConnection();
//            }).start();
//        }
    }
}
