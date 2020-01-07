package org.parosproxy.paros.extension.filter;

import java.util.regex.Matcher;

import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class FilterReplaceResponseHeader extends FilterAbstractReplace {

    public int getId() {
        return 70;
    }

    public String getName() {
        return "Replace HTTP response header using defined pattern.";
    }

    public void onHttpRequestSend(HttpMessage msg) {
 
    }

    public void onHttpResponseReceive(HttpMessage msg) {
        
        if (getPattern() == null) {
            return;
        } else if (msg.getResponseHeader().isEmpty()) {
            return;
        }
        
        Matcher matcher = getPattern().matcher(msg.getResponseHeader().toString());
        String result = matcher.replaceAll(getReplaceText());
        try {
            msg.getResponseHeader().setMessage(result);
        } catch (HttpMalformedHeaderException e) {

        }
            
    }
}
