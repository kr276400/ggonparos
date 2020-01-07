package org.parosproxy.paros.view;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;

public class PopupDeleteMenu extends ExtensionPopupMenu {

    
    private Component invoker = null;

	public PopupDeleteMenu() {
		super();
		initialize();
	}

	private void initialize() {
        this.setText("보기에서 삭제");
        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {    
        	    if (invoker.getName().equals("treeSite")) {
        	        JTree tree = (JTree) invoker;
                    TreePath[] paths = tree.getSelectionPaths();
                    SiteMap map = (SiteMap) tree.getModel();
                    for (int i=0; i<paths.length;i++) {
                        SiteNode node = (SiteNode) paths[i].getLastPathComponent(); 
                        delete(map, node);
                    }
                }
 
        	    
        	}
        });

			
	}
	
	
    public boolean isEnableForComponent(Component invoker) {
        if (invoker.getName() != null && invoker.getName().equals("treeSite")) {
            this.invoker = invoker;

            return true;
        } else {
            this.invoker = null;
            return false;
        }

    }
    
   public static void delete(SiteMap map, SiteNode node) {
        SiteNode child = null;
        synchronized(map) {
            while (node.getChildCount() > 0) {
                try {
                    child = (SiteNode) node.getChildAt(0);
                    delete(map, child);
                } catch (Exception e) {}
            }
            
            if (node.isRoot()) {
                return;
            }

            
            ExtensionHistory ext = (ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension("ExtensionHistory");
            ext.getHistoryList().removeElement(node.getHistoryReference());

            while (node.getPastHistoryReference().size() > 0) {
                HistoryReference ref = (HistoryReference) node.getPastHistoryReference().get(0);
                ext.getHistoryList().removeElement(ref);
                node.getPastHistoryReference().remove(0);
            }
            
            map.removeNodeFromParent(node);
        }
    }

    
}
