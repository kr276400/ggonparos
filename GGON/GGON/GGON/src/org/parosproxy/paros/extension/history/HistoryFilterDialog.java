package org.parosproxy.paros.extension.history;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.view.View;

public class HistoryFilterDialog extends AbstractDialog {

	private JPanel jPanel = null;
	private JButton btnApply = null;
	private JButton btnCancel = null;
	private JTextField txtPattern = null;
	private JPanel jPanel1 = null;
	private int exitResult = JOptionPane.CANCEL_OPTION;

	
	private JButton btnReset = null;
	private JPanel jPanel2 = null;
	private JRadioButton radioExact = null;
	private JRadioButton radioRegex = null;

    public HistoryFilterDialog() throws HeadlessException {
        super();
 		initialize();
    }

    public HistoryFilterDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

	private void initialize() {
        this.setContentPane(getJPanel());
        this.setVisible(false);
        this.setResizable(false);
        this.setTitle("필터 기록");
        centreDialog();
        txtPattern.requestFocus();
        this.getRootPane().setDefaultButton(btnApply);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        AbstractAction escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                HistoryFilterDialog.this.dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE",escapeAction);
        this.pack();
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();

			javax.swing.JLabel jLabel1 = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			javax.swing.JLabel jLabel = new JLabel();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jLabel.setText("패턴:");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,10,5,10);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.ipady = 1;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,10);
			gridBagConstraints5.ipadx = 100;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.insets = new java.awt.Insets(5,2,5,2);
			gridBagConstraints6.ipadx = 3;
			gridBagConstraints6.ipady = 3;
			jLabel1.setText("<html><p>찾으려고 하는 문자열이나 패턴을 'Requests'/'Responses' 필터나 기록에서 찾을 수 있게, 아래에 적어주세요. 정규 표현도 할 수 있습니다.</p></html>");
			jLabel1.setMaximumSize(new java.awt.Dimension(2147483647,80));
			jLabel1.setMinimumSize(new java.awt.Dimension(350,24));
			jLabel1.setPreferredSize(new java.awt.Dimension(350,50));
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.insets = new java.awt.Insets(5,10,5,10);
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.ipadx = 3;
			gridBagConstraints11.ipady = 3;
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridwidth = 3;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.insets = new java.awt.Insets(2,10,2,10);
			gridBagConstraints12.ipadx = 0;
			gridBagConstraints12.ipady = 1;
			jPanel.add(jLabel1, gridBagConstraints11);
			jPanel.add(jLabel, gridBagConstraints1);
			jPanel.add(getTxtPattern(), gridBagConstraints5);
			jPanel.add(getJPanel2(), gridBagConstraints12);
			jPanel.add(getJPanel1(), gridBagConstraints6);
		}
		return jPanel;
	}
 
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText("적용");

			btnApply.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    try {
				        Pattern pattern = Pattern.compile(getPattern());
				    } catch (Exception e1) {
				        View.getSingleton().showWarningDialog("불가능한 패턴입니다.");
				        return;
				    }
				    exitResult = JOptionPane.OK_OPTION;
				    HistoryFilterDialog.this.dispose();
					
				}
			});

		}
		return btnApply;
	}
  
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("취소");
			btnCancel.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {

				    exitResult = JOptionPane.CANCEL_OPTION;
				    HistoryFilterDialog.this.dispose();

				}
			});

		}
		return btnCancel;
	}

	private JTextField getTxtPattern() {
		if (txtPattern == null) {
			txtPattern = new JTextField();
		}
		return txtPattern;
	}
  
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.add(getBtnApply(), null);
			jPanel1.add(getBtnReset(), null);
			jPanel1.add(getBtnCancel(), null);
		}
		return jPanel1;
	}
	public int showDialog() {
	    this.setVisible(true);
	    return exitResult;
	}
	
	public String getPattern() {
	    String result = "";
	    if (getRadioRegex().isSelected()) {
	        result = getTxtPattern().getText();
	    } else if (getRadioExact().isSelected()) {
	        result = "\\Q" + getTxtPattern().getText() + "\\E";
	        
	    }
	    return result;
	}
  
	private JButton getBtnReset() {
		if (btnReset == null) {
			btnReset = new JButton();
			btnReset.setText("필터 리셋");
			btnReset.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					exitResult = JOptionPane.NO_OPTION;
					txtPattern.setText("");
					HistoryFilterDialog.this.dispose();
				}
			});

		}
		return btnReset;
	}
 
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridLayout gridLayout3 = new GridLayout();
			jPanel2 = new JPanel();
			jPanel2.setLayout(gridLayout3);
			gridLayout3.setRows(1);
			jPanel2.add(getRadioExact(), null);
			jPanel2.add(getRadioRegex(), null);
			ButtonGroup group = new ButtonGroup();
			group.add(getRadioExact());
			group.add(getRadioRegex());
		}
		return jPanel2;
	}

	private JRadioButton getRadioExact() {
		if (radioExact == null) {
			radioExact = new JRadioButton();
			radioExact.setText("정밀하게 (확실하지 않은 경우)");
			radioExact.setSelected(true);
		}
		return radioExact;
	}
  
	private JRadioButton getRadioRegex() {
		if (radioRegex == null) {
			radioRegex = new JRadioButton();
			radioRegex.setText("정규 표현");
		}
		return radioRegex;
	}
          } 
