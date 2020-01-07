package org.parosproxy.paros.extension.scanner;

import javax.swing.tree.DefaultTreeModel;

import org.parosproxy.paros.core.scanner.Alert;

class AlertTreeModel extends DefaultTreeModel {

    AlertTreeModel() {
        super(new AlertNode("°æ°í"));
        
    }

    synchronized void addPath(Alert alert) {
        
        AlertNode parent = (AlertNode) getRoot();
        
        try {
            parent = findAndAddChild(parent, Alert.MSG_RISK[alert.getRisk()], null);
            parent = findAndAddChild(parent, alert.getAlert(), null);
            parent = findAndAddLeaf(parent, alert.getUri().toString(), alert);
        } catch (Exception e) {
            
        }
        
    }   
    
    private AlertNode findAndAddChild(AlertNode parent, String nodeName, Alert alert) {
        AlertNode result = findChild(parent, nodeName);
        if (result == null) {
            AlertNode newNode = new AlertNode(nodeName);
            int pos = parent.getChildCount();
            for (int i=0; i< parent.getChildCount(); i++) {
                if (nodeName.compareToIgnoreCase(parent.getChildAt(i).toString()) <= 0) {
                    pos = i;
                    break;
                }
            }
            insertNodeInto(newNode, parent, pos);
            result = newNode;
            result.setUserObject(alert);
        }
        return result;
    }

    private AlertNode findAndAddLeaf(AlertNode parent, String nodeName, Alert alert) {
        AlertNode result = findLeaf(parent, nodeName, alert);
        
        if (result == null) {
            AlertNode newNode = new AlertNode(nodeName);
            int pos = parent.getChildCount();
            for (int i=0; i< parent.getChildCount(); i++) {
                if (nodeName.compareToIgnoreCase(parent.getChildAt(i).toString()) <= 0) {
                    pos = i;
                    break;
                    
                }
            }
            insertNodeInto(newNode, parent, pos);
            result = newNode;
            result.setUserObject(alert);
        }
        return result;
    }

    
    private AlertNode findChild(AlertNode parent, String nodeName) {
        for (int i=0; i<parent.getChildCount(); i++) {
            AlertNode child = (AlertNode) parent.getChildAt(i);
            if (child.toString().equals(nodeName)) {
                return child;
            }
        }
        return null;
    }

    private AlertNode findLeaf(AlertNode parent, String nodeName, Alert alert) {
        for (int i=0; i<parent.getChildCount(); i++) {
            AlertNode child = (AlertNode) parent.getChildAt(i);
            if (child.toString().equals(nodeName)) {
                if (child.getUserObject() == null) {
                    return null;
                }
                
                Alert tmp = (Alert) child.getUserObject();

                if (tmp.getParam().equals(alert.getParam())) {;
                	return child;
                }
            }
        }
        return null;
    }

    
}
