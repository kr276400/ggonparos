package org.parosproxy.paros.extension.filter;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class AllFilterTableRenderer extends JComponent implements TableCellRenderer {

    JComponent button = new JButton();
    JComponent label = new JLabel();
    
    AllFilterTableRenderer() {
        super();
    }

     public Component getTableCellRendererComponent(JTable table, Object value,
             boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

         if (isSelected) {
         }

         if (hasFocus) {
         }

         JComponent result = label;
         if (!value.toString().equals("")) {
             result = button;
             ((JButton) button).setText(value.toString());
         }

         return result;
     }

     public void validate() {}
     public void revalidate() {}
     protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
     public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

}
