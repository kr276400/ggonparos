package org.parosproxy.paros.extension.filter;

import java.util.regex.Matcher;

import org.parosproxy.paros.network.HttpMessage;

public class FilterReplaceResponseBody extends FilterAbstractReplace {

    public int getId() {
        return 80;
    }

    public String getName() {
        return "Replace HTTP response body using defined pattern.";
    }

    public void onHttpRequestSend(HttpMessage httpMessage) {
        
    }

    public void onHttpResponseReceive(HttpMessage msg) {
        if (getPattern() == null) {
            return;
        } else if (msg.getResponseHeader().isEmpty() || msg.getResponseHeader().isImage() || msg.getResponseBody().length() == 0) {
            return;
        }
        
        Matcher matcher = getPattern().matcher(msg.getResponseBody().toString());
        String result = matcher.replaceAll(getReplaceText());
        msg.getResponseBody().setBody(result);
        msg.getResponseHeader().setContentLength(msg.getResponseBody().length());
            
    }
	
 }
