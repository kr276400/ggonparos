package org.parosproxy.paros.extension.history;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.view.PopupDeleteMenu;

public class PopupMenuDeleteHistory extends ExtensionPopupMenu {

    private ExtensionHistory extension = null;
    private JTree treeSite = null;
 
    public PopupMenuDeleteHistory() {
        super();
 		initialize();
    }

    public PopupMenuDeleteHistory(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("'View'에서 삭제");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {    
        	    JList listLog = extension.getLogPanel().getListLog();
        	    Object[] obj = listLog.getSelectedValues();
        	    for (int i=0; i<obj.length; i++) {
        	        HistoryReference ref = (HistoryReference) obj[i];
        	        deleteHistory(ref);
        	    }
        	}
        });

			
	}
	
    public boolean isEnableForComponent(Component invoker) {
        
        if (invoker.getName() != null && invoker.getName().equals("ListLog")) {
            try {
                JList list = (JList) invoker;
                if (list.getSelectedIndex() >= 0) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
            } catch (Exception e) {}
            return true;
            
            
        }
        return false;
    }

    private JTree getTree(Component invoker) {
        if (invoker instanceof JTree) {
            JTree tree = (JTree) invoker;
            if (tree.getName().equals("treeSite")) {
                return tree;
            }
        }

        return null;
    }
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
    	
    private void deleteHistory(HistoryReference ref) {

        if (ref == null) {
            return;
        }
        extension.getHistoryList().removeElement(ref);

        SiteNode node = ref.getSiteNode();
        if (node == null) {
            return;
        }

        Session session = Model.getSingleton().getSession();
        SiteMap map = session.getSiteTree();

        if (node.getHistoryReference() == ref) {
            // same active Node
            PopupDeleteMenu.delete(map, node);
        } else {
            node.getPastHistoryReference().remove(ref);
        }        

    }
}
