package org.parosproxy.paros.extension.scanner;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parosproxy.paros.core.scanner.HostProcess;
import org.parosproxy.paros.extension.AbstractDialog;

public class ProgressDialog extends AbstractDialog {

	private JPanel jPanel = null;
	private JScrollPane paneScroll = null;
	private JButton btnStopAllHost = null;
	private JPanel paneProgress = null;
	private ExtensionScanner pluginScanner = null;

    public ProgressDialog() throws HeadlessException {
        super();
 		initialize();
    }

    public ProgressDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

	private void initialize() {
        this.setTitle("½ºÄµ");
        this.setName("ProgressDialog");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJPanel());
        this.setSize(1000, 600); // 440, 550
        this.addWindowListener(new java.awt.event.WindowAdapter() { 

        	public void windowClosing(java.awt.event.WindowEvent e) {    

        	    getBtnStopAllHost().doClick();

        	}
        });
			
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.weightx = 1.0D;
			jPanel.add(getPaneScroll(), gridBagConstraints6);
			jPanel.add(getBtnStopAllHost(), gridBagConstraints7);
		}
		return jPanel;
	}

	private JScrollPane getPaneScroll() {
		if (paneScroll == null) {
			paneScroll = new JScrollPane();
			paneScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			paneScroll.setViewportView(getPaneProgress());
		}
		return paneScroll;
	}
  
	private JButton getBtnStopAllHost() {
		if (btnStopAllHost == null) {
			btnStopAllHost = new JButton();
			btnStopAllHost.setText("ÀüÃ¼ È£½ºÆ® ¸ØÃß±â");
			btnStopAllHost.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {
				    if (pluginScanner != null) {
				        btnStopAllHost.setEnabled(false);
				        pluginScanner.getScanner().stop();
				    }

				}
			});

		}
		return btnStopAllHost;
	}
   
	private JPanel getPaneProgress() {
		if (paneProgress == null) {
			java.awt.GridLayout gridLayout8 = new GridLayout();
			gridLayout8.setColumns(1);
			gridLayout8.setRows(5);
			paneProgress = new JPanel();
			paneProgress.setLayout(gridLayout8);
		}
		return paneProgress;
	}
	
	void addHostProgress(final String hostAndPort, final HostProcess hostThread) {
		if (EventQueue.isDispatchThread()) {
			addHostProgressNonEvent(hostAndPort, hostThread);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					addHostProgressNonEvent(hostAndPort, hostThread);
				}
			});
		} catch (Exception e) {
		}
	}
	
	void removeHostProgress(final String hostAndPort) {
		if (EventQueue.isDispatchThread()) {
			removeHostProgressNonEvent(hostAndPort);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					removeHostProgressNonEvent(hostAndPort);
				}
			});
		} catch (Exception e) {
		}
	    
	}

	void updateHostProgress(final String hostAndPort, final String msg, final int percentage) {
		if (EventQueue.isDispatchThread()) {
			updateHostProgressNonEvent(hostAndPort, msg, percentage);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					updateHostProgressNonEvent(hostAndPort, msg, percentage);
				}
			});
		} catch (Exception e) {
		}
	    
	}

	private void addHostProgressNonEvent(String hostAndPort, HostProcess hostThread) {
	    HostProgressMeter meter = new HostProgressMeter();
	    meter.setHostProcess(hostThread);
	    meter.setName(hostAndPort);	    
	    meter.getTxtHost().setText(hostAndPort);
	    synchronized(getPaneProgress()) {
	        getPaneProgress().add(meter);
	        getPaneProgress().validate();
	    }
	}
	
	private void removeHostProgressNonEvent(String hostAndPort) {
	    HostProgressMeter meter = getMeter(hostAndPort);
	    if (meter == null) {
	        return;
	    }
	    synchronized(getPaneProgress()) {
	        getPaneProgress().remove(meter);
	        getPaneProgress().validate();

	    }
	}

	private void updateHostProgressNonEvent (String hostAndPort, String testName, int percentage) {
	    HostProgressMeter meter = getMeter(hostAndPort);
	    if (meter == null) {
	        return;
	    }
	    meter.setProgress(testName, percentage);
	    
	}
	
	private HostProgressMeter getMeter(String hostAndPort) {
	    synchronized(getPaneProgress()) {
	        for (int i=0; i<getPaneProgress().getComponentCount(); i++) {
	            Component c = (Component) getPaneProgress().getComponent(i);
	            if (c.getName().equals(hostAndPort)) {
	                return (HostProgressMeter) c;
	            }
	        }
	    }
	    return null;
	}

    public void setPluginScanner(ExtensionScanner pluginScanner) {
        this.pluginScanner = pluginScanner;
    }
    }  
