package org.parosproxy.paros.core.spider;

import java.sql.SQLException;

import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class QueueItem {
    private int depth = 0;
    private HistoryReference ref = null;

    QueueItem() {
        super();
    }
    
    
    QueueItem(Session session, int historyType, HttpMessage msg) throws HttpMalformedHeaderException, SQLException {
        ref = new HistoryReference(session, historyType, msg);
    }
    
    void setDepth(int depth) {
        this.depth = depth;
    }
    int getDepth() {
        return depth;
    }
    
    HttpMessage getMessage() {
        HttpMessage msg = null;
        try {
            msg = ref.getHttpMessage();
        } catch (Exception e) {}
        
        return msg;
    }
    
    HistoryReference getHistoryReference() {
        return ref;
    }
}
