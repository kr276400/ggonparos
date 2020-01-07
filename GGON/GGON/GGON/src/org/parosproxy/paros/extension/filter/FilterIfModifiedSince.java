package org.parosproxy.paros.extension.filter;

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class FilterIfModifiedSince extends FilterAdaptor {

    public int getId() {
        return 10;
    }

    public String getName() {
        return "Avoid browser cache (strip off IfModifiedSince)";
        
    }

    public void onHttpRequestSend(HttpMessage httpMessage) {
        HttpRequestHeader reqHeader = httpMessage.getRequestHeader();
      	if (!reqHeader.isEmpty() && reqHeader.isText()){
      		String ifModifed = reqHeader.getHeader(HttpHeader.IF_MODIFIED_SINCE);
      		if (ifModifed != null){    
      			reqHeader.setHeader(HttpHeader.IF_MODIFIED_SINCE, null);                   
      		}
      		String ifNoneMatch = reqHeader.getHeader(HttpHeader.IF_NONE_MATCH);
      		if (ifNoneMatch != null){    
      			reqHeader.setHeader(HttpHeader.IF_NONE_MATCH, null);                   
      		}
      		
      	}

    }

    public void onHttpResponseReceive(HttpMessage httpMessage) {

    }

}
