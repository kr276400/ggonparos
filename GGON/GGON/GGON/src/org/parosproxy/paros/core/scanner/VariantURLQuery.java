package org.parosproxy.paros.core.scanner;

import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpMessage;

public class VariantURLQuery extends VariantAbstractQuery {

    public VariantURLQuery() {
        super();
    }
    
    public void setMessage(HttpMessage msg) {
        try {
            parse(msg.getRequestHeader().getURI().getQuery());
        } catch (URIException e) {
            e.printStackTrace();
        }
    }
    
    protected void buildMessage(HttpMessage msg, String query) {
        try {
            msg.getRequestHeader().getURI().setQuery(query);
        } catch (URIException e) {
            e.printStackTrace();
        }
    }

}
