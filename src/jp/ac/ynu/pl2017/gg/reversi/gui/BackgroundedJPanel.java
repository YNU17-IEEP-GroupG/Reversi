package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundedJPanel extends JPanel {
	private	String	imagePath;
	
	public BackgroundedJPanel(String path) {
		super();
		path = imagePath;
	}
	
	@Override
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
			arg0.drawImage(new ImageIcon(imagePath).getImage(),
					0, 0, MainFrame.panelW, MainFrame.panelH, this);
	}

}
