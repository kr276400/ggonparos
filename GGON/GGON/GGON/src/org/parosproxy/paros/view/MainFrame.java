package org.parosproxy.paros.view;

import java.awt.EventQueue;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;

public class MainFrame extends AbstractFrame {
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JPanel paneContent = null;
	private javax.swing.JLabel txtStatus = null;
	private org.parosproxy.paros.view.WorkbenchPanel paneStandard = null;
	private org.parosproxy.paros.view.MainMenuBar mainMenuBar = null;
	private JPanel paneDisplay = null;

	public MainFrame() {
		super();
		initialize();
	}

	private void initialize() {
        this.setJMenuBar(getMainMenuBar());
        this.setContentPane(getPaneContent());

        this.setSize(1000, 600);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() { 

        	public void windowClosing(java.awt.event.WindowEvent e) {    

        		getMainMenuBar().getMenuFileControl().exit();
        	}
        });

        this.setVisible(false);
	}
 
	private javax.swing.JPanel getPaneContent() {
		if (paneContent == null) {
			
			paneContent = new javax.swing.JPanel();
			paneContent.setLayout(new BoxLayout(getPaneContent(), BoxLayout.Y_AXIS));
			paneContent.setEnabled(true);
			paneContent.setPreferredSize(new java.awt.Dimension(1000,600));
			paneContent.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			paneContent.add(getPaneDisplay(), null);
			paneContent.add(getTxtStatus(), null);
		}
		return paneContent;
	}
  
	private javax.swing.JLabel getTxtStatus() {
		if (txtStatus == null) {
			txtStatus = new javax.swing.JLabel();
			txtStatus.setName("txtStatus");
			txtStatus.setText("설정");
			txtStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			txtStatus.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			txtStatus.setPreferredSize(new java.awt.Dimension(800,18));
			//history, spider, ouput 부분
		}
		return txtStatus;
	}
  
	org.parosproxy.paros.view.WorkbenchPanel getWorkbench() {
		if (paneStandard == null) {
			paneStandard = new org.parosproxy.paros.view.WorkbenchPanel();
			paneStandard.setLayout(new java.awt.CardLayout());
			paneStandard.setName("paneStandard");
		}
		return paneStandard;
	}
  
	public org.parosproxy.paros.view.MainMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new org.parosproxy.paros.view.MainMenuBar();
		}
		return mainMenuBar;
	}
	
	public void setStatus(final String msg) {
		if (EventQueue.isDispatchThread()) {
			txtStatus.setText(msg);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					txtStatus.setText(msg);
				}
			});
		} catch (Exception e) {
		}
	}
   
	public JPanel getPaneDisplay() {
		if (paneDisplay == null) {
			paneDisplay = new JPanel();
			paneDisplay.setLayout(new CardLayout());
			paneDisplay.setName("paneDisplay");
			paneDisplay.add(getWorkbench(), getWorkbench().getName());
		}
		return paneDisplay;
	}
   }
