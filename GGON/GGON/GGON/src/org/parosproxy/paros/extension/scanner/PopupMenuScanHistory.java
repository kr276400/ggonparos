package org.parosproxy.paros.extension.scanner;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteNode;

public class PopupMenuScanHistory extends ExtensionPopupMenu {

    private ExtensionScanner extension = null;
    private JList listLog = null;

    public PopupMenuScanHistory() {
        super();
 		initialize();
    }

    public PopupMenuScanHistory(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("이 기록 스캔하기");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
                
                Object[] obj = listLog.getSelectedValues();
                if (obj.length != 1) {
                    return;
                }
                
                try {
                    HistoryReference ref = (HistoryReference) obj[0];
                    SiteNode siteNode = ref.getSiteNode();
                    extension.startScan(siteNode);
                } catch (Exception e1) {
                    extension.getView().showWarningDialog("기록을 가져오는데 오류가 발생하였습니다.");
                }
        	}
        });

			
	}
	
    public boolean isEnableForComponent(Component invoker) {
        
        if (invoker.getName() != null && invoker.getName().equals("ListLog")) {
            try {
                JList list = (JList) invoker;
                listLog = list;
                Object[] obj = listLog.getSelectedValues();

                if (obj.length == 1 && extension.getScanner().isStop()) {
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
        
    void setExtension(ExtensionScanner extension) {
        this.extension = extension;
    }

}
