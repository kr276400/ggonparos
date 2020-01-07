package org.parosproxy.paros.extension.history;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.view.PopupPurgeMenu;

public class PopupMenuPurgeHistory extends ExtensionPopupMenu {

    private ExtensionHistory extension = null;
    private JTree treeSite = null;

    public PopupMenuPurgeHistory() {
        super();
 		initialize();
    }

    public PopupMenuPurgeHistory(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("데이터베이스로 부터 제거");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {    
        	    JList listLog = extension.getLogPanel().getListLog();
        	    Object[] obj = listLog.getSelectedValues();
        	    if (obj.length > 1) {
        	        int result = extension.getView().showConfirmDialog("이 기록은 데이터에서 제거 됩니다.  진행 하시겠습니까?");
        	        if (result != JOptionPane.YES_OPTION) {
        	            return;
        	        }
        	    }
        	    synchronized(extension.getHistoryList()) {
        	        
        	        for (int i=0; i<obj.length; i++) {
        	            HistoryReference ref = (HistoryReference) obj[i];
        	            purgeHistory(ref);
        	        }
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
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
    
    private void purgeHistory(HistoryReference ref) {

        if (ref == null) {
            return;
        }
        extension.getHistoryList().removeElement(ref);
        ref.delete();

        SiteNode node = ref.getSiteNode();
        if (node == null) {
            return;
        }

        Session session = Model.getSingleton().getSession();
        SiteMap map = session.getSiteTree();

        if (node.getHistoryReference() == ref) {
            PopupPurgeMenu.purge(map, node);
        } else {
            node.getPastHistoryReference().remove(ref);
        }        

    }
	
}
