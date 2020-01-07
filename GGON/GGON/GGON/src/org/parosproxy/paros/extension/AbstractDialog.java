package org.parosproxy.paros.extension;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JDialog;

import org.parosproxy.paros.Constant;

abstract public class AbstractDialog extends JDialog {
 
    protected AbstractDialog thisDialog = null;
	public AbstractDialog() throws HeadlessException {
		super();
		initialize();
	}

	public AbstractDialog(Frame arg0, boolean arg1) throws HeadlessException {
		super(arg0, arg1);
		initialize();
	}

	private void initialize() {
		this.setVisible(false);
		this.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(1000,600);
		this.setTitle(Constant.PROGRAM_NAME);
	}

	public void centreDialog() {
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
	
	public void setVisible(boolean show) {
	    if (show) {
	        centreDialog();
	    }
	    super.setVisible(show);
	}
}
