import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class CommunicationClient extends JFrame implements ActionListener {
	static String name = "0";
	static String pass = "0";
	static String enemy = "0";
	static boolean turn = false; // true:���,false:���

	static int coordinate = 11;
	static int Ecoordinate = 11; // �G�̒u�������W
	static boolean log = false;
	static boolean mat = false;
	static Socket socket;
	Communication cn = new Communication(socket); // �ʐM�p

	JLabel Llabel = new JLabel("�����O�C��");
	JLabel Mlabel = new JLabel("�}�b�`���O��");
	JLabel Tlabel = new JLabel("");
	static JLabel Clabel = new JLabel("����̒u�������W:");
	JTextField text1;
	JPasswordField text2;
	JTextField textm;
	JTextField textc;

	JButton send = new JButton("���O�C��");
	JButton match = new JButton("OK");
	JButton rmatch = new JButton("�����_���}�b�`");
	JButton put = new JButton("�΂�u��");
	JButton start = new JButton("�J�n");

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

		text1 = new JTextField("���[�U��", 15);
		text2 = new JPasswordField("pass", 15);
		textm = new JTextField("�ΐ푊��̃��[�U��", 15);
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

		if (cmd.equals("���O�C��")) {

			name = text1.getText();
			pass = text2.getText();

			if (cn.login(name, pass)) {
				Llabel.setText("���O�C����");
				send.setEnabled(false);
				match.setEnabled(true);
				rmatch.setEnabled(true);
			} else {
				Llabel.setText("�p�X���[�h���Ⴂ�܂�");
			}
		}

		if (cmd.equals("OK")) {
			enemy = textm.getText();
			System.out.println(enemy+";"+name);
			if (!enemy.equals(name)) {
				match.setEnabled(false);
				rmatch.setEnabled(false);

				turn = cn.match(false, enemy);

				Mlabel.setText("�}�b�`���O����");
				start.setEnabled(true);

				if (turn) {
					Tlabel.setText("���Ȃ��͐��ł�");
				} else {
					Tlabel.setText("���Ȃ��͌��ł�");
				}
			}else{
				Mlabel.setText("�����̖��O�ł�");
			}
		}

		if (cmd.equals("�����_���}�b�`")) {
			
			match.setEnabled(false);
			rmatch.setEnabled(false);
			
			turn = cn.match(true, null);

			Mlabel.setText("�}�b�`���O����");
			start.setEnabled(true);

			if (turn) {
				Tlabel.setText("���Ȃ��͐��ł�");
			} else {
				Tlabel.setText("���Ȃ��͌��ł�");
			}
		}

		if (cmd.equals("�J�n")) {

			if (turn) {
				put.setEnabled(true);
			} else {
				Clabel.setText(Integer.toString(cn.recieve()));
				put.setEnabled(true);
			}
		}

		if (cmd.equals("�΂�u��")) {
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
