package org.parosproxy.paros.extension.history;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;

public class PopupMenuEmbeddedBrowser extends ExtensionPopupMenu {

    private ExtensionHistory extension = null;
    private Component lastInvoker = null;

    public PopupMenuEmbeddedBrowser() {
        super();
 		initialize();
    }

    public PopupMenuEmbeddedBrowser(String label) {
        super(label);
        initialize();
    }

	private void initialize() {
        this.setText("브라우저에서 보기");
        if (!ExtensionHistory.isEnableForNativePlatform()) {
            this.setEnabled(false);
        }

        this.setActionCommand("");
        
        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
                HistoryReference ref = null;
                if (lastInvoker == null) {
                    return;
                }
                if (lastInvoker.getName().equalsIgnoreCase("ListLog")) {
                    JList listLog = extension.getLogPanel().getListLog();
                    if (listLog.getSelectedValues().length != 1) {
                        extension.getView().showWarningDialog("기록 패널에서 메세지를 먼저 선택해주세요.");
                        return;
                    }
                    
                    ref = (HistoryReference) listLog.getSelectedValue();
                    showBrowser(ref);                                   
                        

                } else if (lastInvoker.getName().equals("treeSite")) {
                    JTree tree = (JTree) lastInvoker;
                    SiteNode node = (SiteNode) tree.getLastSelectedPathComponent();
                    ref = node.getHistoryReference();
                    showBrowser(ref);
                }
        	}
        });

			
	}
	
    private void showBrowser(HistoryReference ref) {
        HttpMessage msg = null;
        try {
            msg = ref.getHttpMessage();
            if (!extension.browserDisplay(ref, msg)) {
                extension.getView().showWarningDialog("보이지 않는 HTTP 메세지의 타입을 선택해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    public boolean isEnableForComponent(Component invoker) {
        lastInvoker = null;
        if (!ExtensionHistory.isEnableForNativePlatform()) {
            return false;
        }
        
        if (invoker.getName() == null) {
            return false;
        }
        
        if (invoker.getName().equalsIgnoreCase("ListLog")) {
            try {
                JList list = (JList) invoker;
                if (list.getSelectedIndex() >= 0) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
                lastInvoker = invoker;
            } catch (Exception e) {
                
            }
            return true;
        } else if (invoker.getName().equals("treeSite")) {
                JTree tree = (JTree) invoker;
                lastInvoker = tree;
                return true;
        }
        return false;
    }
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
    
	
}
