package org.parosproxy.paros.model;


import javax.swing.DefaultListModel;

public class HistoryList extends DefaultListModel {
    
    public void addElement(final Object obj) {

        super.addElement(obj);
        
    }
    
    public synchronized void notifyItemChanged(Object obj) {
        int i = indexOf(obj);
        if (i >= 0) {
            fireContentsChanged(this, i, i);
        }
    }
    
}