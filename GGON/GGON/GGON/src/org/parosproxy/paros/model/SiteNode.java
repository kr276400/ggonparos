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
    
    // 현재 노드 레퍼런스를 세팅하는데, 만약에 존재하는 레퍼런스면, 스파이더에 기록되있는거 삭제할거야, 그러고, 예전 기록 리스트에 추가할거야
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
