package org.parosproxy.paros.extension.scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class AlertNode extends DefaultMutableTreeNode {
    private String nodeName = null;
    
    public AlertNode(String nodeName) {
        super();
        this.nodeName = nodeName;
    }
    
    public String toString() {
        return nodeName;
    }
}
