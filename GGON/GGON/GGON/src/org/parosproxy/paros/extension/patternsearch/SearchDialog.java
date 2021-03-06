package org.parosproxy.paros.extension.patternsearch;

import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.extension.ViewDelegate;

public class SearchDialog extends AbstractDialog {

    private ExtensionPatternSearch ext = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JScrollPane jScrollPane = null;

    private JScrollPane jScrollPane1 = null;

    private JTextArea txtPattern = null;

    private JTextArea txtResult = null;

    private JButton btnSearch = null;

    private JPanel jPanel3 = null;

    private JPanel jPanel4 = null;

    private ViewDelegate view = null;

	private JPanel jPanel5 = null;
	private Choice choice = null;
	private JRadioButton jRadioButton = null;
	private JRadioButton jRadioButton1 = null;
	private JPanel jPanel6 = null;

    public SearchDialog() {
        super();
        initialize();
    }

    public SearchDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    private void initialize() {
        this.setTitle("세션에서 패턴 얻기");
        this.setContentPane(getJPanel());
        this.setSize(561, 515);

    }

    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

            java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();

            gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.weighty = 0.1D;
            gridBagConstraints1.gridheight = 1;
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.weighty = 0.9D;
            gridBagConstraints2.gridheight = 1;
            jPanel.add(getJPanel1(), gridBagConstraints1);
            jPanel.add(getJPanel2(), gridBagConstraints2);
            jPanel.add(getJPanel6(), gridBagConstraints5);

        }
        return jPanel;
    }

    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null,
                    "암호화/해쉬화할 부분을 아래에 적어주세요.",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null);
            java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();

            java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();

            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.setPreferredSize(new java.awt.Dimension(135, 100));
            jPanel1.setBorder(titledBorder1);
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints3.gridheight = 1;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
            titledBorder1.setTitle("패턴 찾기");
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.gridy = 0;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = -1;
            gridBagConstraints4.gridy = -1;
            gridBagConstraints4.weightx = 0.5D;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
            jPanel1.add(getJScrollPane(), gridBagConstraints3);
            jPanel1.add(getJPanel3(), gridBagConstraints7);
        }
        return jPanel1;
    }

    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            TitledBorder titledBorder2 = javax.swing.BorderFactory
                    .createTitledBorder(
                            null,
                            "패턴 찾기 결과",
                            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                            javax.swing.border.TitledBorder.DEFAULT_POSITION,
                            new java.awt.Font("MS Sans Serif",
                                    java.awt.Font.PLAIN, 11),
                            java.awt.Color.black);
            java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();

            java.awt.GridBagConstraints gridBagConstraints8 = new GridBagConstraints();

            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.setPreferredSize(new java.awt.Dimension(135, 120));
            jPanel2.setBorder(titledBorder2);
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.weighty = 1.0;
            gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints8.gridheight = 1;
            gridBagConstraints9.gridx = 1;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
            titledBorder2.setTitle("찾기 결과");
            jPanel2.add(getJScrollPane1(), gridBagConstraints8);
            jPanel2.add(getJPanel4(), gridBagConstraints9);
        }
        return jPanel2;
    }

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTxtPattern());
            jScrollPane
                    .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return jScrollPane;
    }

    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setViewportView(getTxtResult());
        }
        return jScrollPane1;
    }

    private JTextArea getTxtPattern() {
        if (txtPattern == null) {
            txtPattern = new JTextArea();
            txtPattern.setLineWrap(true);
            txtPattern.setFont(new java.awt.Font("Courier New",
                    java.awt.Font.PLAIN, 12));
            txtPattern.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {                                                                            
                        view.getPopupMenu().show(e.getComponent(), e.getX(),
                                e.getY());
                    }
                }

            });

        }
        return txtPattern;
    }

    private JTextArea getTxtResult() {
        if (txtResult == null) {
            txtResult = new JTextArea();
            txtResult.setLineWrap(true);
            txtResult.setFont(new java.awt.Font("Courier New",
                    java.awt.Font.PLAIN, 12));
        }
        return txtResult;
    }

    private JButton getBtnSearch() {
        if (btnSearch == null) {
            btnSearch = new JButton();
            btnSearch.setText("찾기");
            btnSearch.setActionCommand("찾기");
            btnSearch.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    txtResult.setText("");
                    if (txtPattern.getText()!=null && !txtPattern.getText().equals(""))
                        txtResult.setText(ext.search(txtPattern.getText(),getJRadioButton().isSelected()));

                }
            });

        }
        return btnSearch;
    }

    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            java.awt.GridLayout gridLayout6 = new GridLayout();

            jPanel3 = new JPanel();
            jPanel3.setLayout(gridLayout6);
            gridLayout6.setRows(3);
            gridLayout6.setColumns(1);
            gridLayout6.setVgap(3);
            gridLayout6.setHgap(3);
            jPanel3.add(getJRadioButton(), null);
            jPanel3.add(getJRadioButton1(), null);
            jPanel3.add(getBtnSearch(), null);
        }
        return jPanel3;
    }

    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            java.awt.GridLayout gridLayout11 = new GridLayout();

            jPanel4 = new JPanel();
            jPanel4.setLayout(gridLayout11);
            gridLayout11.setRows(2);
            gridLayout11.setColumns(1);
            gridLayout11.setHgap(2);
            gridLayout11.setVgap(3);
        }
        return jPanel4;
    }

    void setView(ViewDelegate view) {
        this.view = view;
    }

    public ExtensionPatternSearch getExt() {
        return ext;
    }

    public void setExt(ExtensionPatternSearch ext) {
        this.ext = ext;
    }
  
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new GridBagLayout());
			jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "패턴 샘플(예시)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return jPanel5;
	}

	private Choice getChoice() {
		if (choice == null) {
			choice = new Choice();
			choice.addItem("--------");  // 1st one is blank, don't change

			choice.addItem("Cookie:.*?\\r\\n..........get all cookies from REQUEST");
			choice.addItem("Server:.*?\\r\\n..........get all server banners from RESPONSE");
			choice.addItem("http.*?[ ]..........get all requested URLs from REQUEST");
			choice.addItem("http.*?[.]php..........get all requested php URLs from REQUEST");
			choice.addItem("POST[ ]http.*?[ ]..........get all POST requests from REQUEST");

			choice.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {
				    if (getChoice().getSelectedIndex()==0){ 
				        getTxtPattern().setText("");
				    }
				    else{
				        String txt = getChoice().getSelectedItem();
				        getTxtPattern().setText(txt.substring(0,txt.indexOf("..........")));
				        if (txt.endsWith("REQUEST")){
				            if (!getJRadioButton().isSelected())
				                getJRadioButton().doClick();
				        }
				        else{
				            if (!getJRadioButton1().isSelected())
				                getJRadioButton1().doClick();
				        }
				        
				         
				    }
				}
			});
		}
		return choice;
	}

	private JRadioButton getJRadioButton() {
		if (jRadioButton == null) {
			jRadioButton = new JRadioButton();
			jRadioButton.setText("요청(Request)");
			jRadioButton.setSelected(true);
			jRadioButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getJRadioButton().setSelected(true);
					getJRadioButton1().setSelected(false);
				}
			});

		}
		return jRadioButton;
	}

	private JRadioButton getJRadioButton1() {
		if (jRadioButton1 == null) {
			jRadioButton1 = new JRadioButton();
			jRadioButton1.setText("응답(Response)");
			jRadioButton1.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getJRadioButton().setSelected(false);
					getJRadioButton1().setSelected(true);
				}
			});

		}
		return jRadioButton1;
	}
  
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			GridLayout gridLayout7 = new GridLayout();
			jPanel6 = new JPanel();
			jPanel6.setLayout(gridLayout7);
			jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "패턴 샘플(예시)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			gridLayout7.setRows(1);
			jPanel6.add(getChoice(), null);
		}
		return jPanel6;
	}
   } 
