package org.parosproxy.paros.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.parosproxy.paros.Constant;

public abstract class AbstractFrame extends JFrame {

	public AbstractFrame() {
		super();
		initialize();
	}

	private void initialize() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/p_gon_16_1.png")));
		this.setVisible(false);
		this.setTitle(Constant.PROGRAM_NAME);
		this.setSize(800, 600);
	    this.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC | java.awt.Font.BOLD, 11));
	    centerFrame();
	}

	public void centerFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
	    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
}  
