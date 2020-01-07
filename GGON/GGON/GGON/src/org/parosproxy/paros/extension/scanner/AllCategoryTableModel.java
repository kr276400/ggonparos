package org.parosproxy.paros.extension.scanner;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.parosproxy.paros.core.scanner.Category;
import org.parosproxy.paros.core.scanner.Plugin;

public class AllCategoryTableModel extends DefaultTableModel {

    private static final String[] columnNames = {"카테고리", "가능 여부"};
    private List allPlugins = new Vector();

    public void setAllPlugins(List allPlugins) {
        this.allPlugins = allPlugins;
    }

    public AllCategoryTableModel() {
    }
    
    public void setTable(List allPlugins) {
        setAllPlugins(allPlugins);
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
        
        Object result = null;
        switch (col) {
        	case 0:	break;
        	case 1: setPluginCategoryEnabled(row, ((Boolean) value).booleanValue());
        			break;
        }
        fireTableCellUpdated(row, col);
    }
    
    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return Category.length();
    }

    public Object getValueAt(int row, int col) {
        Object result = null;
        switch (col) {
        	case 0:	result = Category.getName(row);
        			break;
        	case 1: result = new Boolean(isPluginCategoryEnabled(row));
        			break;
        	default: result = "";
        }
        return result;
    }

    private boolean isPluginCategoryEnabled(int category) {
        for (int i=0; i<allPlugins.size(); i++) {
            Plugin plugin = (Plugin) allPlugins.get(i);
            if (plugin.getCategory() != category) {
                continue;
            }
            if (!plugin.isEnabled()) {
                return false;
            }
        }
        return true;
    }
    
    private void setPluginCategoryEnabled(int category, boolean enabled) {
        for (int i=0; i<allPlugins.size(); i++) {
            Plugin plugin = (Plugin) allPlugins.get(i);
            if (plugin.getCategory() != category) {
                continue;
            }
            plugin.setEnabled(enabled);
        }
        
    }
    
    void setAllCategoryEnabled(boolean enabled) {
        for (int i=0; i<allPlugins.size(); i++) {
            Plugin plugin = (Plugin) allPlugins.get(i);
            plugin.setEnabled(enabled);            
        }
        fireTableDataChanged();        

    }
    
    boolean isAllCategoryEnabled() {
        for (int i=0; i<allPlugins.size(); i++) {
            Plugin plugin = (Plugin) allPlugins.get(i);
            if (!plugin.isEnabled()) {
                return false;
            }
        }
        return true;
        
    }
}
