package org.parosproxy.paros.extension.scanner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.parosproxy.paros.view.AbstractParamPanel;

public class PolicyCategoryPanel extends AbstractParamPanel {

	private JTable tableTest = null;
	private JScrollPane jScrollPane = null;
	private CategoryTableModel categoryTableModel = null;  
    public PolicyCategoryPanel() {
        super();
 		initialize();
    }

	private void initialize() {
        java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        this.setSize(375, 204); // 375, 204
        this.setName("categoryPanel");
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.weighty = 1.0;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 1;
        gridBagConstraints11.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(getJScrollPane(), gridBagConstraints11);
			
	}
	private static final int width[] = {300,60};

	private JTable getTableTest() {
		if (tableTest == null) {
			tableTest = new JTable();
			tableTest.setModel(getCategoryTableModel());
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
   
	CategoryTableModel getCategoryTableModel() {
		if (categoryTableModel == null) {
			categoryTableModel = new CategoryTableModel();
		}
		return categoryTableModel;
	}
    }  
