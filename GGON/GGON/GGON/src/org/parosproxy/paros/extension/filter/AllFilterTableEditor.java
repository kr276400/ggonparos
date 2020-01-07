package org.parosproxy.paros.extension.filter;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

class AllFilterTableEditor extends AbstractCellEditor implements TableCellEditor {
    
    private JButton button = null;
    private int row = 0;
    private AllFilterTableModel model = null;
    AllFilterTableEditor(AllFilterTableModel model) {
        this.model = model;
        button = new JButton();
		button.addActionListener(new java.awt.event.ActionListener() { 

			public void actionPerformed(java.awt.event.ActionEvent e) {    

				Filter filter = (Filter) AllFilterTableEditor.this.model.getAllFilters().get(row);
				filter.editProperty();
			}
		});
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {
        
        if (isSelected) {
        }
        
        button.setText((String)value);

        row = rowIndex;
        
        return button;
    }
    
    public Object getCellEditorValue() {
        return button.getText();
    }
}
