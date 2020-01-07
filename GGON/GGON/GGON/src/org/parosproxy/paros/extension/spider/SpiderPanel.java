package org.parosproxy.paros.extension.spider;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.view.View;

public class SpiderPanel extends AbstractPanel {

	private JSplitPane splitPane = null;
	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private JLabel jLabel = null;
	private JTextArea txtURIFound = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel1 = null;
	private JTextArea txtURISkip = null;
	private JScrollPane jScrollPane1 = null;

    public SpiderPanel() {
        super();
 		initialize();
    }

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setSize(700, 214);
        this.setName("스파이더 부분");
        this.add(getSplitPane(), getSplitPane().getName());
			
	}

	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setName("splitPane");
			splitPane.setDividerSize(3);
			splitPane.setDividerLocation(120);
			splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			splitPane.setTopComponent(getLeftPanel());
			splitPane.setBottomComponent(getRightPanel());
			splitPane.setResizeWeight(0.5D);
		}
		return splitPane;
	}

	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			jLabel = new JLabel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			leftPanel = new JPanel();
			leftPanel.setLayout(new GridBagLayout());
			jLabel.setText("크롤링 중에 찾은 URI :");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(0,2,0,2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			leftPanel.add(jLabel, gridBagConstraints1);
			leftPanel.add(getJScrollPane(), gridBagConstraints2);
		}
		return leftPanel;
	}

	private JPanel getRightPanel() {
		if (rightPanel == null) {
			jLabel1 = new JLabel();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			rightPanel = new JPanel();
			rightPanel.setLayout(new GridBagLayout());
			jLabel1.setText("크롤링 범위를 벗어난 URI :");
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new java.awt.Insets(0,2,0,2);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.ipady = 24;
			rightPanel.add(jLabel1, gridBagConstraints3);
			rightPanel.add(getJScrollPane1(), gridBagConstraints4);
		}
		return rightPanel;
	}

	JTextArea getTxtURIFound() {
		if (txtURIFound == null) {
			txtURIFound = new JTextArea();
			txtURIFound.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			txtURIFound.setEditable(false);
			txtURIFound.setLineWrap(true);
			txtURIFound.addMouseListener(new java.awt.event.MouseAdapter() { 
			    public void mousePressed(java.awt.event.MouseEvent e) {    
			        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
			            View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			        }			    	
			    }
			    
			});
		}
		return txtURIFound;
	}
 
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtURIFound());
			jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane;
	}

	JTextArea getTxtURISkip() {
		if (txtURISkip == null) {
			txtURISkip = new JTextArea();
			txtURISkip.setEditable(false);
			txtURISkip.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			txtURISkip.setLineWrap(true);
			txtURISkip.addMouseListener(new java.awt.event.MouseAdapter() { 
			    public void mousePressed(java.awt.event.MouseEvent e) {    
			        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
			            View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			        }			    	
			    }
			});
		}
		return txtURISkip;
	}
 
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getTxtURISkip());
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane1;
	}
	
	void appendFound(final String s) {
		if (EventQueue.isDispatchThread()) {
			getTxtURIFound().append(s);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					getTxtURIFound().append(s);
				}
			});
		} catch (Exception e) {
		}
	    
	}
	
	void appendFoundButSkip(final String s) {
		if (EventQueue.isDispatchThread()) {
			getTxtURISkip().append(s);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					getTxtURISkip().append(s);
				}
			});
		} catch (Exception e) {
		}
	}
	    
	void clear() {
	    getTxtURIFound().setText("");
	    getTxtURISkip().setText("");
	}
	
        }  