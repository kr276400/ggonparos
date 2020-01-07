package org.parosproxy.paros.extension.scanner;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.extension.history.ManualRequestEditorDialog;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

public class PopupMenuResend extends ExtensionPopupMenu {

    private ExtensionScanner extension = null;
    private JTree treeSite = null;
    private HttpSender httpSender = null;

    public PopupMenuResend() {
        super();
 		initialize();
    }

    public PopupMenuResend(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("다시 보내기");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
        	    
        	    ManualRequestEditorDialog dialog = extension.getManualRequestEditorDialog();
        	    
        	    HttpMessage msg = null;
        	    Object obj = null;
        	    obj = extension.getAlertPanel().getTreeAlert().getLastSelectedPathComponent();
        	    if (obj == null) {
        	        return;
        	    }
        	    AlertNode node = (AlertNode) obj;
        	    if (node.getUserObject() != null) {
        	        obj = node.getUserObject();
        	        if (obj instanceof Alert) {
        	            Alert alert = (Alert) obj;
        	            msg = alert.getMessage();
        	            
        	        } else {
        	            return;
        	        }
        	    }

        	    dialog.setMessage(msg);
        	    dialog.setVisible(true);
    
        	}
        });

			
	}
	
    public boolean isEnableForComponent(Component invoker) {
        if (invoker.getName() != null && invoker.getName().equals("treeAlert")) {
            try {
                JTree tree = (JTree) invoker;
                if (tree.getLastSelectedPathComponent() != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (!node.isRoot() && node.getUserObject() != null) {
                        return true;
                    }
                }
            } catch (Exception e) {}
            
        }
        return false;
    }
    
    void setExtension(ExtensionScanner extension) {
        this.extension = extension;
    }
 
}
