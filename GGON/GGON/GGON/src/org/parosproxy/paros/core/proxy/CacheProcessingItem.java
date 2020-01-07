package org.parosproxy.paros.core.proxy;

import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMessage;

public class CacheProcessingItem {

    public CacheProcessingItem(HistoryReference ref, HttpMessage msg) {
        reference = ref;
        message = msg;
    }
    
    HistoryReference reference = null;
    HttpMessage message = null;
    
}
