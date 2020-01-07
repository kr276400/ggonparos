package org.parosproxy.paros.view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.parosproxy.paros.extension.AbstractDialog;

public class WaitMessageDialog extends AbstractDialog {

	private JPanel jPanel = null;
	private JLabel lblMessage = null;
    public WaitMessageDialog() throws HeadlessException {
        super();
		initialize();
    }

    public WaitMessageDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
		initialize();
    }

	private void initialize() {
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        this.setContentPane(getJPanel());
        this.setSize(282, 118);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
			
	}
 
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			lblMessage = new JLabel();

			lblMessage.setText(" ");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 273;
			gridBagConstraints1.ipady = 79;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.insets = new java.awt.Insets(20,20,20,20);
			jPanel.add(lblMessage, gridBagConstraints2);
		}
		return jPanel;
	}
	
	public void setText(String s) {
	    lblMessage.setText(s);
	    this.pack();
	}
	
 }  
