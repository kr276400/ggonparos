package org.parosproxy.paros.extension.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.parosproxy.paros.view.AbstractParamPanel;

public class AllFilterPanel extends AbstractParamPanel {

	private JTable tableFilter = null;
	private JScrollPane jScrollPane = null;

    public AllFilterPanel() {
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
	private static final int width[] = {400,50, 20};
	private JButton btnEnableAll = null;
	private JButton btnDisableAll = null;
	private AllFilterTableModel allFilterTableModel = null;  

	private JTable getTableFilter() {
		if (tableFilter == null) {
			tableFilter = new JTable();
			tableFilter.setRowHeight(18);
			tableFilter.setIntercellSpacing(new java.awt.Dimension(1,1));
			tableFilter.setModel(getAllFilterTableModel());
	        for (int i = 0; i < width.length; i++) {
	            TableColumn column = tableFilter.getColumnModel().getColumn(i);
	            column.setPreferredWidth(width[i]);
	        }
	        TableColumn col = tableFilter.getColumnModel().getColumn(2);
	        col.setCellRenderer(new AllFilterTableRenderer());
	        col.setCellEditor(new AllFilterTableEditor(getAllFilterTableModel()));
		}
		return tableFilter;
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
			jScrollPane.setViewportView(getTableFilter());
			jScrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
			jScrollPane.setEnabled(false);
		}
		return jScrollPane;
	}
 
	private JButton getBtnEnableAll() {
		if (btnEnableAll == null) {
			btnEnableAll = new JButton();
			btnEnableAll.setText("전체 가능");
			btnEnableAll.setEnabled(false);
			btnEnableAll.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    


				}
			});

		}
		return btnEnableAll;
	}
  
	private JButton getBtnDisableAll() {
		if (btnDisableAll == null) {
			btnDisableAll = new JButton();
			btnDisableAll.setText("전체 불가");
			btnDisableAll.setEnabled(false);
			btnDisableAll.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					
				}
			});

		}
		return btnDisableAll;
	}
  
	AllFilterTableModel getAllFilterTableModel() {
		if (allFilterTableModel == null) {
			allFilterTableModel = new AllFilterTableModel();
		}
		return allFilterTableModel;
	}
       }  
