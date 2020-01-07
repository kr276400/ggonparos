package org.parosproxy.paros.extension.filter;

import java.util.regex.Matcher;

import org.parosproxy.paros.network.HttpMessage;

public class FilterReplaceRequestBody extends FilterAbstractReplace {

    public int getId() {
        return 60;
    }

    public String getName() {
        return "Replace HTTP request body using defined pattern.";
    }

    public void onHttpRequestSend(HttpMessage msg) {

        if (getPattern() == null) {
            return;
        } else if (msg.getRequestHeader().isEmpty() || msg.getRequestBody().length() == 0) {
            return;
        }
        
        Matcher matcher = getPattern().matcher(msg.getRequestBody().toString());
        String result = matcher.replaceAll(getReplaceText());
        msg.getRequestBody().setBody(result);
        msg.getRequestHeader().setContentLength(msg.getRequestBody().length());
                   
    }

    public void onHttpResponseReceive(HttpMessage msg) {
 
    } 
}
