package org.parosproxy.paros.extension.scanner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.view.AbstractParamPanel;

public class PolicyAllCategoryPanel extends AbstractParamPanel {

	private JTable tableTest = null;
	private JScrollPane jScrollPane = null;
	private AllCategoryTableModel allCategoryTableModel = null;  
    
	public PolicyAllCategoryPanel() {
        super();
 		initialize();
    }

	private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

        java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        this.setSize(375, 204);
        this.setName("categoryPanel");
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.weighty = 1.0;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 1;
        gridBagConstraints11.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints11.gridwidth = 2;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(getBtnEnableAll(), gridBagConstraints1);
        this.add(getBtnDisableAll(), gridBagConstraints2);
        this.add(getJScrollPane(), gridBagConstraints11);
			
	}
	private static final int width[] = {300,50};
	private JButton btnEnableAll = null;
	private JButton btnDisableAll = null;

	private JTable getTableTest() {
		if (tableTest == null) {
			tableTest = new JTable();
			tableTest.setModel(getAllCategoryTableModel());
			tableTest.setRowHeight(18);
			tableTest.setIntercellSpacing(new java.awt.Dimension(1,1));
	        for (int i = 0; i < 2; i++) {
	            TableColumn column = tableTest.getColumnModel().getColumn(i);
	            column.setPreferredWidth(width[i]);
	        }
		}
		return tableTest;
	}

    public void initParam(Object obj) {
        
    }

    public void validateParam(Object obj) throws Exception {
        
    }

    public void saveParam(Object obj) throws Exception {
        
    }
   
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTableTest());
			jScrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
		}
		return jScrollPane;
	}
   
	AllCategoryTableModel getAllCategoryTableModel() {
		if (allCategoryTableModel == null) {
			allCategoryTableModel = new AllCategoryTableModel();
			allCategoryTableModel.setTable(PluginFactory.getAllPlugin());
		}
		return allCategoryTableModel;
	}
  
	private JButton getBtnEnableAll() {
		if (btnEnableAll == null) {
			btnEnableAll = new JButton();
			btnEnableAll.setText("전체 가능");
			btnEnableAll.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					getAllCategoryTableModel().setAllCategoryEnabled(true);
					
				}
			});

		}
		return btnEnableAll;
	}

	private JButton getBtnDisableAll() {
		if (btnDisableAll == null) {
			btnDisableAll = new JButton();
			btnDisableAll.setText("전체 불가");
			btnDisableAll.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					getAllCategoryTableModel().setAllCategoryEnabled(false);

				}
			});

		}
		return btnDisableAll;
	}
      }  
