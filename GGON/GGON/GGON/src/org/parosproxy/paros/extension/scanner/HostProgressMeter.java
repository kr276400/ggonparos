package org.parosproxy.paros.extension.scanner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.parosproxy.paros.core.scanner.HostProcess;

public class HostProgressMeter extends JPanel {

	private JLabel txtHost = null;
	private JProgressBar barProgress = null;
	private JButton btnStop = null;
	private JLabel txtDisplay = null;
	private HostProcess hostProcess = null;
	
	private JScrollPane jScrollPane = null;

	public HostProgressMeter() {
		super();
		initialize();
	}

	private  void initialize() {
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

		java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();

		javax.swing.JLabel jLabel = new JLabel();

		java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

		this.setLayout(new GridBagLayout());
		this.setSize(380, 76);
		this.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.RAISED));
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(2,2,2,5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.insets = new java.awt.Insets(2,5,2,2);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints2.gridwidth = 2;
		jLabel.setText("»£Ω∫∆Æ:");
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.insets = new java.awt.Insets(2,5,2,5);
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHEAST;
		gridBagConstraints5.gridx = 2;
		gridBagConstraints5.gridy = 1;
		gridBagConstraints5.insets = new java.awt.Insets(2,2,2,5);
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.weighty = 0.0D;
		gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints12.gridwidth = 2;
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridy = 2;
		gridBagConstraints12.insets = new java.awt.Insets(2,5,2,5);
		this.add(jLabel, gridBagConstraints4);
		this.add(getTxtHost(), gridBagConstraints1);
		this.add(getBtnStop(), gridBagConstraints5);
		this.add(getBarProgress(), gridBagConstraints2);
		this.add(getJScrollPane(), gridBagConstraints12);
	}

	JLabel getTxtHost() {
		if (txtHost == null) {
			txtHost = new JLabel("    ");
		}
		return txtHost;
	}
  
	private JProgressBar getBarProgress() {
		if (barProgress == null) {
			barProgress = new JProgressBar();
			barProgress.setPreferredSize(new java.awt.Dimension(150,20));
		}
		return barProgress;
	}
   
	private JButton getBtnStop() {
		if (btnStop == null) {
			btnStop = new JButton();
			btnStop.setText("∏ÿ√„");
			btnStop.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
				    if (hostProcess != null) {
				        hostProcess.stop();
				    }
				    btnStop.setEnabled(false);

				}
			});

		}
		return btnStop;
	}
	
	void setProgress(String msg, int percentage) {
	    getBarProgress().setValue(percentage);
	    getTxtDisplay().setText(msg);
	   
	}
	
	void setHostProcess(HostProcess hostThread) {
	    this.hostProcess = hostThread;
	}
 
	private JLabel getTxtDisplay() {
		if (txtDisplay == null) {
			txtDisplay = new JLabel("    ");
		}
		return txtDisplay;
	}
  
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtDisplay());
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		return jScrollPane;
	}
     }  
