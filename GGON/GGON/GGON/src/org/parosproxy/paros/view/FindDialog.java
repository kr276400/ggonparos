package org.parosproxy.paros.view;

import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import org.parosproxy.paros.extension.AbstractDialog;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class FindDialog extends AbstractDialog {

	private JPanel jPanel = null;
	private JButton btnFind = null;
	private JButton btnCancel = null;
	private JTextField txtFind = null;
	private JPanel jPanel1 = null;
    private JTextComponent lastInvoker = null;

    public void setLastInvoker(JTextComponent lastInvoker) {
        this.lastInvoker = lastInvoker;
    }

    public FindDialog() throws HeadlessException {
        super();
 		initialize();
    }

    public FindDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

	private void initialize() {
        this.setVisible(false);
        this.setResizable(false);
        this.setTitle("찾기");
        this.setContentPane(getJPanel());
        this.setSize(500, 300);
        centreDialog();
        txtFind.requestFocus();
        this.getRootPane().setDefaultButton(btnFind);
        
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        AbstractAction escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                FindDialog.this.setVisible(false);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE",escapeAction);
        pack();
	}
	
	private void find() {
	    JTextComponent txtComp = lastInvoker;
	    if (txtComp == null) {
	        JFrame parent = (JFrame) (this.getParent());
	        Component c = parent.getMostRecentFocusOwner();
	        if (c instanceof JTextComponent) {
	            txtComp = (JTextComponent) c;
            }
        }
            
		try {
		    String findText = txtFind.getText().toLowerCase();
		    String txt = txtComp.getText().toLowerCase();
		    int startPos = txt.indexOf(findText, txtComp.getCaretPosition());
		    int length = findText.length();
		    if (startPos > -1) {
		        txtComp.select(startPos,startPos+length);
		        txtComp.requestFocus();
                txtFind.requestFocus();
		    } else {
		        Toolkit.getDefaultToolkit().beep();
		    }
		} catch (Exception e) {
		}
	}
  
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			javax.swing.JLabel jLabel = new JLabel();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jLabel.setText("찾을 단어 :");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,10,2,10);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(12,2,8,10);
			gridBagConstraints5.ipadx = 50;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			jPanel.add(jLabel, gridBagConstraints1);
			jPanel.add(getTxtFind(), gridBagConstraints5);
			jPanel.add(getJPanel1(), gridBagConstraints6);
		}
		return jPanel;
	}
  
	private JButton getBtnFind() {
		if (btnFind == null) {
			btnFind = new JButton();
			btnFind.setText("찾기");
			btnFind.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					find();
					
				}
			});

		}
		return btnFind;
	}
 
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("취소");
			btnCancel.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {

				    FindDialog.this.setVisible(false);
				}
			});

		}
		return btnCancel;
	}
 
	private JTextField getTxtFind() {
		if (txtFind == null) {
			txtFind = new JTextField();
			txtFind.setMinimumSize(new java.awt.Dimension(120,24));
			txtFind.setPreferredSize(new java.awt.Dimension(120,24));
		}
		return txtFind;
	}
  
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setMinimumSize(new java.awt.Dimension(155,35));
			jPanel1.add(getBtnFind(), null);
			jPanel1.add(getBtnCancel(), null);
		}
		return jPanel1;
	}
	

      }  
