package jp.ac.ynu.pl2017.gg.reversi.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class BackgroundedPanel extends JPanel {
	
	private	Image	image;
	
	public BackgroundedPanel() {
		this(null);
	}
	
	public BackgroundedPanel(Image pImage) {
		super();
		setBackground(pImage);
		setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (g != null && image != null) {
			g.drawImage(image, 0, 0, this);
			super.paintComponent(g);
		}
	}
	
	public void setBackground(Image pImage) {
		image = pImage;
	}

}
