package org.parosproxy.paros.extension.filter;

import java.util.Vector;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class FilterLogCookie extends FilterAdaptor {

    private static final String DELIM = "\t";   
    private static final String CRLF = "\r\n";
    private Vector cookieList = null;

    public int getId() {
        return 100;
    }

    public String getName() {
        return "Log cookies sent by browser.";
        
    }

    public void init() {
		cookieList = new Vector();
     	
    }

    public void onHttpRequestSend(HttpMessage msg) {
        HttpRequestHeader header = msg.getRequestHeader();
        
        if (header != null ) {
            String cookie = header.getHeader("Cookie");
            synchronized (cookieList){
                if (cookie != null && cookieList.indexOf(cookie)==-1){           		
                    URI uri = (URI) header.getURI().clone();
                    try {
                        uri.setQuery(null);
                        String sUri = uri.toString();
                        cookieList.add(cookie);

                    } catch (URIException e) {
                        e.printStackTrace();
                    }
                }
			}
		}
    }

    public void onHttpResponseReceive(HttpMessage httpMessage) {
        
    }
  }

