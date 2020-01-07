package org.parosproxy.paros.extension.filter;

import java.util.Hashtable;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class FilterLogPostQuery extends FilterLogGetQuery {

    public int getId() {
        return 30;
    }

    public String getName() {
        return "Log unique POST queries into file (" + getLogFileName() + ")";
        
    }

    protected String getLogFileName() {
        return "filter/post.xls";
    }

    public void onHttpRequestSend(HttpMessage httpMessage) {

        HttpRequestHeader reqHeader = httpMessage.getRequestHeader();
        
        if (reqHeader != null && reqHeader.isText() && !reqHeader.isImage()){
            if (reqHeader.getMethod().equalsIgnoreCase(HttpRequestHeader.POST)){
                try{
                    
                    URI uri = reqHeader.getURI();
                    
                    int pos;
                    
                    String firstline;
                    
                    URI newURI = (URI) uri.clone();
                    String query = httpMessage.getRequestBody().toString();
                    if (query != null) {
                        newURI.setQuery(null);
                        firstline = newURI.toString();
                        Hashtable param = parseParameter(query);
                        writeLogFile(firstline,param);
                    } else {
                        firstline = uri.toString();
                        writeLogFile(firstline,null);				
                    }
   
                }catch(Exception aa){
                    aa.printStackTrace();
                }
            }
            
        }
    }
    
}
