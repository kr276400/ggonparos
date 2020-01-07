package org.parosproxy.paros.model;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class SiteNode extends DefaultMutableTreeNode {

    private String nodeName = null;
    private HistoryReference historyReference = null;
    private Vector pastHistoryList = new Vector(10);
    
    public SiteNode(String nodeName) {
        super();
        this.nodeName = nodeName;
    }
    
    public String toString() {
        return nodeName;
    }
    
    public HistoryReference getHistoryReference() {
        return historyReference;
    }
    
    // ���� ��� ���۷����� �����ϴµ�, ���࿡ �����ϴ� ���۷�����, �����̴��� ��ϵ��ִ°� �����Ұž�, �׷���, ���� ��� ����Ʈ�� �߰��Ұž�
    public void setHistoryReference(HistoryReference historyReference) {

        if (getHistoryReference() != null) {

            if (!getPastHistoryReference().contains(historyReference)) {
                getPastHistoryReference().add(getHistoryReference());
            }
        }
        
        this.historyReference = historyReference;
        this.historyReference.setSiteNode(this);
    }    
    
    public Vector getPastHistoryReference() {
        return pastHistoryList;
    }
    
}
