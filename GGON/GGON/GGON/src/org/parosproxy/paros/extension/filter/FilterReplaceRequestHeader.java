package org.parosproxy.paros.extension.filter;

import java.util.regex.Matcher;

import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class FilterReplaceRequestHeader extends FilterAbstractReplace {

    public int getId() {
        return 50;
    }

    public String getName() {
        return "Replace HTTP request header using defined pattern.";
    }

    public void onHttpRequestSend(HttpMessage msg) {

        if (getPattern() == null || msg.getRequestHeader().isEmpty()) {
            return;
        }
        
        Matcher matcher = getPattern().matcher(msg.getRequestHeader().toString());
        String result = matcher.replaceAll(getReplaceText());
        try {
            msg.getRequestHeader().setMessage(result);
        } catch (HttpMalformedHeaderException e) {

        }

    }

    public void onHttpResponseReceive(HttpMessage msg) {
            
    }
}
