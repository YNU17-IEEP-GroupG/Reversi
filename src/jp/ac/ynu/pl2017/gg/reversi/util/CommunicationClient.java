import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class CommunicationClient extends JFrame implements ActionListener {
	static String name = "0";
	static String pass = "0";
	static String enemy = "0";
	static boolean turn = false; // true:先手,false:後手

	static int coordinate = 11;
	static int Ecoordinate = 11; // 敵の置いた座標
	static boolean log = false;
	static boolean mat = false;
	static Socket socket;
	Communication cn = new Communication(socket); // 通信用

	JLabel Llabel = new JLabel("未ログイン");
	JLabel Mlabel = new JLabel("マッチング中");
	JLabel Tlabel = new JLabel("");
	static JLabel Clabel = new JLabel("相手の置いた座標:");
	JTextField text1;
	JPasswordField text2;
	JTextField textm;
	JTextField textc;

	JButton send = new JButton("ログイン");
	JButton match = new JButton("OK");
	JButton rmatch = new JButton("ランダムマッチ");
	JButton put = new JButton("石を置く");
	JButton start = new JButton("開始");

	CommunicationClient() {
		send.addActionListener(this);
		match.addActionListener(this);
		rmatch.addActionListener(this);
		put.addActionListener(this);
		start.addActionListener(this);

		match.setEnabled(false);
		rmatch.setEnabled(false);
		put.setEnabled(false);
		start.setEnabled(false);

		text1 = new JTextField("ユーザ名", 15);
		text2 = new JPasswordField("pass", 15);
		textm = new JTextField("対戦相手のユーザ名", 15);
		textc = new JTextField("11", 15);

		JPanel pn = new JPanel();
		pn.setLayout(new GridLayout(4, 2));
		pn.add(text1);
		pn.add(text2);
		pn.add(send);
		pn.add(Llabel);

		pn.add(textm);
		pn.add(Mlabel);
		pn.add(match);
		pn.add(rmatch);
		add(pn, "North");

		JPanel pc = new JPanel();
		pc.setLayout(new GridLayout(2, 1));

		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(2, 2));
		jp.add(Tlabel);
		jp.add(Clabel);
		jp.add(textc);
		jp.add(put);

		pc.add(start);
		pc.add(jp);
		add(pc, "Center");
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals("ログイン")) {

			name = text1.getText();
			pass = text2.getText();

			if (cn.login(name, pass)) {
				Llabel.setText("ログイン中");
				send.setEnabled(false);
				match.setEnabled(true);
				rmatch.setEnabled(true);
			} else {
				Llabel.setText("パスワードが違います");
			}
		}

		if (cmd.equals("OK")) {
			enemy = textm.getText();
			System.out.println(enemy+";"+name);
			if (!enemy.equals(name)) {
				match.setEnabled(false);
				rmatch.setEnabled(false);

				turn = cn.match(false, enemy);

				Mlabel.setText("マッチング成功");
				start.setEnabled(true);

				if (turn) {
					Tlabel.setText("あなたは先手です");
				} else {
					Tlabel.setText("あなたは後手です");
				}
			}else{
				Mlabel.setText("自分の名前です");
			}
		}

		if (cmd.equals("ランダムマッチ")) {
			
			match.setEnabled(false);
			rmatch.setEnabled(false);
			
			turn = cn.match(true, null);

			Mlabel.setText("マッチング成功");
			start.setEnabled(true);

			if (turn) {
				Tlabel.setText("あなたは先手です");
			} else {
				Tlabel.setText("あなたは後手です");
			}
		}

		if (cmd.equals("開始")) {

			if (turn) {
				put.setEnabled(true);
			} else {
				Clabel.setText(Integer.toString(cn.recieve()));
				put.setEnabled(true);
			}
		}

		if (cmd.equals("石を置く")) {
			cn.send(Integer.parseInt(textc.getText()));
			put.setEnabled(false);
			Clabel.setText(Integer.toString(cn.recieve()));
		}
	}

	public static void main(String args[]) {

		CommunicationClient frame = new CommunicationClient();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setTitle("Test");
		frame.setVisible(true);

	}

}
