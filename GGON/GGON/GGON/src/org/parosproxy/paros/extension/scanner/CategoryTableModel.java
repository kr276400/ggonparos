package org.parosproxy.paros.extension.scanner;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.parosproxy.paros.core.scanner.Plugin;

public class CategoryTableModel extends DefaultTableModel {

    private static final String[] columnNames = {"테스트 이름", "가능 여부"};
    private Vector listTestCategory = new Vector();
    public CategoryTableModel() {
    }
    
    public void setTable(int category, List allTest) {
        listTestCategory.clear();
        for (int i=0; i<allTest.size(); i++) {
            Plugin test = (Plugin) allTest.get(i);
            if (test.getCategory() == category) {
                listTestCategory.add(test);
            }
        }
        fireTableDataChanged();
        
    }

    public Class getColumnClass(int c) {
        if (c == 1) {
            return Boolean.class;
        }
        return String.class;
        
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return true;
        }
        return false;
    }
    
    public void setValueAt(Object value, int row, int col) {
        
        Plugin test = (Plugin) listTestCategory.get(row);
        Object result = null;
        switch (col) {
        	case 0:	break;
        	case 1: test.setEnabled(((Boolean) value).booleanValue());
        			break;
        }
        fireTableCellUpdated(row, col);
    }
    
    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return getTestList().size();
    }

    public Object getValueAt(int row, int col) {
        Plugin test = (Plugin) listTestCategory.get(row);
        Object result = null;
        switch (col) {
        	case 0:	result = test.getName();
        			break;
        	case 1: result = new Boolean(test.isEnabled());
        			break;
        	default: result = "";
        }
        return result;
    }
    
    private List getTestList() {
        if (listTestCategory == null) {
            listTestCategory = new Vector();
        }
        return listTestCategory;
    }
    
}
