package org.parosproxy.paros.core.scanner;

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;

public class VariantFormQuery extends VariantAbstractQuery {

    private static final String WWW_FORM_URLENCODED = "www-form-urlencoded";

    public VariantFormQuery() {
        super();
    }
    
    public void setMessage(HttpMessage msg) {
        parse(msg.getRequestBody().toString());
    }
        
    
    protected void buildMessage(HttpMessage msg, String query) {
        msg.getRequestBody().setBody(query);
    }
    
    protected String getEncodedValue(HttpMessage msg, String value) {
        String contentType = null;
        String encoded = "";
        
        contentType = msg.getRequestHeader().getHeader(HttpHeader.CONTENT_TYPE);
        if (value != null) {
            if (contentType != null && contentType.toLowerCase().endsWith(WWW_FORM_URLENCODED)) {
                encoded = AbstractPlugin.getURLEncode(value);
            }
        }
        return encoded;
    }

}
