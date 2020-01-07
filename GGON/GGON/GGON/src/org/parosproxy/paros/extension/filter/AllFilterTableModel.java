package org.parosproxy.paros.extension.filter;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class AllFilterTableModel extends DefaultTableModel {

    private static final String[] columnNames = {"필터 이름", "가능 여부", ""};
    private List allFilters = null;

    private void setAllFilters(List allFilters) {
        this.allFilters = allFilters;
    }

    public AllFilterTableModel() {
        allFilters = new Vector();
    }
    
    public void setTable(List allFilters) {
        setAllFilters(allFilters);
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
    
    public boolean isCellEditable(int row, int col) {
        boolean result = false;
        Filter filter = (Filter) getAllFilters().get(row);
        switch (col) {
        	case 0:	result = false;
        			break;
        	case 1: result = true;
        			break;
        	case 2: if (filter.isPropertyExists()) {
        	    		result = true;
        			}	 else {
        			    result = false;
        			}
        }
        return result;
    }
    
    public void setValueAt(Object value, int row, int col) {
        
        Object result = null;
        Filter filter = (Filter) allFilters.get(row);
        switch (col) {
        	case 0:	break;
        	case 1: filter.setEnabled(((Boolean) value).booleanValue());
        			break;
        	case 2: break;
        }
        fireTableCellUpdated(row, col);
    }
    
    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return getAllFilters().size();
    }

    public Object getValueAt(int row, int col) {
        Object result = null;
        Filter filter = (Filter) getAllFilters().get(row);
        switch (col) {
        	case 0:	result = filter.getName();
        			break;
        	case 1: result = new Boolean(filter.isEnabled());
        			break;
        	case 2: if (filter.isPropertyExists()) {
        	    		result = "...";
        			} else {
        			    result = "";
        			}
        			break;
        	default: result = "";
        }
        return result;
    }
    
    void setAllFilterEnabled(boolean enabled) {
        for (int i=0; i<getAllFilters().size(); i++) {
            Filter filter = (Filter) getAllFilters().get(i);
            filter.setEnabled(enabled);            
        }
        fireTableDataChanged();        

    }

    public List getAllFilters() {
        if (allFilters == null) {
            allFilters = new Vector();
        }
        return allFilters;
    }
}


