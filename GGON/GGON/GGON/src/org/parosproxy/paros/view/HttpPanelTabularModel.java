package org.parosproxy.paros.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;

public class HttpPanelTabularModel extends AbstractTableModel {

    private static final String[] columnNames = {"Parameter Name", "Value"};
    private static final Pattern pSeparator	= Pattern.compile("([^=&]+)[=]([^=&]*)"); 
    private Vector listPair = new Vector();
    private boolean editable = true;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public HttpPanelTabularModel() {
        super();
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return listPair.size();
    }

    public Object getValueAt(int row, int col) {
        String[] cell = (String[]) listPair.get(row);
        return cell[col];
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public synchronized void setText(String body) {
        listPair.clear();
        String name = null;
        String value = null;
        Matcher matcher = pSeparator.matcher(body);
        int row = 0;
  	  	while (matcher.find()){
  	  	    String[] cell = new String[2];
  	  	    try {
                name = URLDecoder.decode(matcher.group(1),"8859_1");
      	  	    value = URLDecoder.decode(matcher.group(2),"8859_1");
      	  	    cell[0] = name;
      	  	    cell[1] = value;
      	  	    listPair.add(cell);
            } catch (UnsupportedEncodingException e) {
            } catch (IllegalArgumentException e) {
            }
  	  	}

  	  	fireTableDataChanged();
    }
    
    public synchronized String getText() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<listPair.size(); i++) {
            if (i > 0) sb.append('&');
            String[] cell = (String[]) listPair.get(i);
            try {
                sb.append(URLEncoder.encode(cell[0],"UTF8") + "=" + URLEncoder.encode(cell[1],"UTF8"));
            } catch (UnsupportedEncodingException e) {
            } catch (IllegalArgumentException e) {
            }
        }
        return sb.toString();
            
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        
        return isEditable();
        
    }
    
    public void setValueAt(Object value, int row, int col) {
        String[] cell = null;
        while (row >= listPair.size()) {
            cell = new String[2];
            cell[0] = "";
            cell[1] = "";
            listPair.add(cell);
        }
        
        cell = (String[]) listPair.get(row);
        cell[col] = (String) value;
        fireTableCellUpdated(row, col);
    }
}