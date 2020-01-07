package org.parosproxy.paros.view;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.parosproxy.paros.network.HostAuthentication;

public class OptionsAuthenticationTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Host", "Port", "User name", "Password", "Realm"};
    
    private Vector listAuth = new Vector();

    public OptionsAuthenticationTableModel() {
        super();
    }

    public int getColumnCount() {
        return 5;
    }

    public int getRowCount() {
        return listAuth.size();
    }

    public Object getValueAt(int row, int col) {
        HostAuthentication auth = (HostAuthentication) listAuth.get(row);
        Object result = null;
        switch (col) {
        	case 0:	result = auth.getHostName();
        			break;
        	case 1: result = new Integer(auth.getPort());
        			break;
        	case 2: result = auth.getUserName();
        			break;
        	case 3:	result = auth.getPassword();
        			break;
        	case 4:	result = auth.getRealm();
        			break;
        	default: result = "";
        }
        return result;
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) {
        
        HostAuthentication auth = (HostAuthentication) listAuth.get(row);
        Object result = null;
        switch (col) {
        	case 0:	auth.setHostName((String) value);
        			break;
        	case 1: auth.setPort(((Integer) value).intValue());
        			break;
        	case 2: auth.setUserName((String) value);
        			break;
        	case 3:	auth.setPassword((String) value);
        			break;
        	case 4:	auth.setRealm((String) value);
        			break;
        }
        checkAndAppendNewRow();
        fireTableCellUpdated(row, col);
    }

    public Vector getListAuth() {
        HostAuthentication auth = null;
        for (int i=0; i<listAuth.size();) {
            auth = (HostAuthentication) listAuth.get(i);
            if (auth.getHostName().equals("")) {
                listAuth.remove(i);
                continue;
            }
            i++;
        }
        
        Vector newList = new Vector(listAuth);
        return newList;
    }

    public void setListAuth(Vector listAuth) {
        this.listAuth = new Vector(listAuth);
        checkAndAppendNewRow();
  	  	fireTableDataChanged();
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    private void checkAndAppendNewRow() {
        HostAuthentication auth = null;
        if (listAuth.size() > 0) {
            auth = (HostAuthentication) listAuth.get(listAuth.size()-1);
            if (!auth.getHostName().equals("")) {
                auth = new HostAuthentication();
                listAuth.add(auth);
            }
        } else {
            auth = new HostAuthentication();
            listAuth.add(auth);
        }
    }
    
    public Class getColumnClass(int c) {
        if (c == 1) {
            return Integer.class;
        }
        return String.class;
        
    }
    
}
