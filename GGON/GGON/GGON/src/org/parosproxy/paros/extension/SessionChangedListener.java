package org.parosproxy.paros.extension;

import org.parosproxy.paros.model.Session;

public interface SessionChangedListener {

    public void sessionChanged(Session session);
    
}
