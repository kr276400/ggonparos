package org.parosproxy.paros.extension.spider;

import java.awt.Component;

import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;

public class PopupMenuSpider extends ExtensionPopupMenu {

    private ExtensionSpider extension = null;
    private JTree treeSite = null;

    public PopupMenuSpider() {
        super();
 		initialize();
    }

    public PopupMenuSpider(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("스파이더");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {    
        		if (treeSite != null) {
        		    SiteNode node = (SiteNode) treeSite.getLastSelectedPathComponent();
        		    extension.setStartNode(node);
	                if (node.isRoot()) {
	                    extension.showDialog("사이트들이 전부 크롤링 됩니다");
	                } else {
	                    try {
	                        HttpMessage msg = node.getHistoryReference().getHttpMessage();
	                        if (msg != null) {
	                            String tmp = msg.getRequestHeader().getURI().toString();
	                            extension.showDialog(tmp);
	                        }
	                    } catch (Exception e1) {
	                        
	                    }
	                }
        		}

        	}
        });
	
	}
	
    public boolean isEnableForComponent(Component invoker) {
        treeSite = getTree(invoker);
        if (treeSite != null) {
		    SiteNode node = (SiteNode) treeSite.getLastSelectedPathComponent();
		    if (node != null) {
		        this.setEnabled(true);
		    } else {
		        this.setEnabled(false);
		    }
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
    
    void setExtension(ExtensionSpider extension) {
        this.extension = extension;
    }
	
}
