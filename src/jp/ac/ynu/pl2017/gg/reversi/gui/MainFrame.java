package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 248580253941254005L;

	public MainFrame() {
		super();
		setSize(854, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		setVisible(true);
		setContentPane(new SettingsPanel());
		validate();
	}

	public void showLoginPopup() {
		JDialog lLoginDialog = new JDialog();
		lLoginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		lLoginDialog.setSize(400, 300);

		JPanel lLDialogPanel = new JPanel(new GridLayout(4, 1));

		JPanel lIDPanel = new JPanel(new FlowLayout());
		JLabel lIDLabel = new JLabel("ユーザID");
		JTextField lIDField = new JTextField(10);
		lIDPanel.add(lIDLabel);
		lIDPanel.add(lIDField);
		lLDialogPanel.add(lIDPanel);

		JPanel lPassPanel = new JPanel(new FlowLayout());

	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
