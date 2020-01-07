package org.parosproxy.paros.core.scanner;

import java.util.Vector;

import org.parosproxy.paros.network.HttpMessage;

public interface Variant {

    public void setMessage(HttpMessage msg);
    public Vector getParamList();
    public String setParameter(HttpMessage msg, NameValuePair originalPair, String param, String value);
    
}
