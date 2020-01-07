package org.parosproxy.paros.view;

import java.awt.Frame;
import java.awt.HeadlessException;


import javax.swing.JPanel;

import org.parosproxy.paros.extension.AbstractDialog;

import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class AboutDialog extends AbstractDialog {

	private JPanel jPanel = null;
	private AboutPanel aboutPanel = null;
	private JButton btnOK = null;

    public AboutDialog() throws HeadlessException {
        super();
 		initialize();
    }

    public AboutDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
 		initialize();
    }

	private void initialize() {
        this.setContentPane(getJPanel());
        this.pack();
			
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.weighty = 1.0D;
			gridBagConstraints5.ipady = 2;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			jPanel.add(getAboutPanel(), gridBagConstraints5);
			jPanel.add(getBtnOK(), gridBagConstraints6);
		}
		return jPanel;
	}

	private AboutPanel getAboutPanel() {
		if (aboutPanel == null) {
			aboutPanel = new AboutPanel();
		}
		return aboutPanel;
	}
  
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText("»Æ¿Œ");
			btnOK.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    AboutDialog.this.dispose();

				}
			});

		}
		return btnOK;
	}
   }  
