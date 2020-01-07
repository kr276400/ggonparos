package org.parosproxy.paros.extension.trap;

import javax.swing.JPanel;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.parosproxy.paros.extension.AbstractPanel;

import javax.swing.JScrollPane;

public class TrapFilterPanel extends AbstractPanel {

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JCheckBox chkEnableInclusiveFilter = null;
	private JLabel jLabel = null;
	private JTextArea txtInclusiveFilter = null;
	private JLabel jLabel1 = null;
	private JCheckBox chkEnableExclusiveFilter = null;
	private JTextArea txtExclusiveFilter = null;
	private JLabel jLabel2 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
    public TrapFilterPanel() {
        super();
		initialize();
    }

	private void initialize() {
        jLabel = new JLabel();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.setSize(405, 297);
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 0.5D;
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints2.weighty = 0.5D;
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jLabel.setText(" ");
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.weightx = 1.0D;
        gridBagConstraints3.weighty = 1.0D;
        this.add(getJPanel(), gridBagConstraints1);
        this.add(getJPanel1(), gridBagConstraints2);
        this.add(jLabel, gridBagConstraints3);
			
	}
  
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new JLabel();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jLabel1.setText("여러 필터도 가능합니다. 임의 문자기호는 *''를 사용하고, 데이터의 시작과 끝을 나타낼 때는 ';'를 사용합니다.");
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.gridwidth = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 0.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.ipady = 15;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "URI가 폭 넓은 필터와 연결되었을 때 트랩을 사용하세요", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11), java.awt.Color.black));
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.weighty = 0.0D;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.ipady = 15;
			jPanel.add(getChkEnableInclusiveFilter(), gridBagConstraints4);
			jPanel.add(getJScrollPane(), gridBagConstraints10);
			jPanel.add(jLabel1, gridBagConstraints6);
		}
		return jPanel;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel2 = new JLabel();
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jLabel2.setText("여러 필터도 가능합니다. 임의 문자기호는 *''를 사용하고, 데이터의 시작과 끝을 나타낼 때는 ';'를 사용합니다.");
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.gridwidth = 1;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weightx = 1.0D;
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "URI가 (전용 필터)와 연결하는 중에는 트랩을 하지마세요 ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11), java.awt.Color.black));
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 0.0D;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.ipady = 15;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			jPanel1.add(getChkEnableExclusiveFilter(), gridBagConstraints7);
			jPanel1.add(getJScrollPane1(), gridBagConstraints11);
			jPanel1.add(jLabel2, gridBagConstraints9);
		}
		return jPanel1;
	}

	JCheckBox getChkEnableInclusiveFilter() {
		if (chkEnableInclusiveFilter == null) {
			chkEnableInclusiveFilter = new JCheckBox();
			chkEnableInclusiveFilter.setText("폭 넓은 필터 사용(포괄적)");
			chkEnableInclusiveFilter.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
				    setInclusiveFilter(chkEnableInclusiveFilter.isSelected());
				}
			});
		}
		return chkEnableInclusiveFilter;
	}

	JTextArea getTxtInclusiveFilter() {
		if (txtInclusiveFilter == null) {
			txtInclusiveFilter = new JTextArea();
			txtInclusiveFilter.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			txtInclusiveFilter.setLineWrap(true);
			txtInclusiveFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			txtInclusiveFilter.setRows(3);
		}
		return txtInclusiveFilter;
	}

	JCheckBox getChkEnableExclusiveFilter() {
		if (chkEnableExclusiveFilter == null) {
			chkEnableExclusiveFilter = new JCheckBox();
			chkEnableExclusiveFilter.setText("전용 필터 사용(독점적인)");
			chkEnableExclusiveFilter.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
				    setExclusiveFilter(chkEnableExclusiveFilter.isSelected());
				}
			});
		}
		return chkEnableExclusiveFilter;
	}

	JTextArea getTxtExclusiveFilter() {
		if (txtExclusiveFilter == null) {
			txtExclusiveFilter = new JTextArea();
			txtExclusiveFilter.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			txtExclusiveFilter.setLineWrap(true);
			txtExclusiveFilter.setRows(3);
			txtExclusiveFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		return txtExclusiveFilter;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtInclusiveFilter());
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane;
	}
   
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getTxtExclusiveFilter());
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane1;
	}
	
	void setInclusiveFilter(boolean isEnabled) {
	    txtInclusiveFilter.setEnabled(isEnabled);
	    Color color = Color.WHITE;
	    if (!isEnabled) {
	        txtInclusiveFilter.setText("");
	        color = this.getBackground();
	    }
	    
	    txtInclusiveFilter.setBackground(color);

	}
	
	void setExclusiveFilter(boolean isEnabled) {
	    txtExclusiveFilter.setEnabled(isEnabled);
	    Color color = Color.WHITE;
	    if (!isEnabled) {
	        txtExclusiveFilter.setText("");
	        color = this.getBackground();
	    }
	    
	    txtExclusiveFilter.setBackground(color);

	}
	
        } 
