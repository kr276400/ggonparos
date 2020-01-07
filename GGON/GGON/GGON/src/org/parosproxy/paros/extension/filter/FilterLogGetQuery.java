package org.parosproxy.paros.extension.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class FilterLogGetQuery extends FilterAdaptor {

    private static final String delim = "\t";   
    private static final String CRLF = "\r\n";
    private File outFile;		    
    private Pattern pSeparator	= Pattern.compile("([^=&]+)[=]([^=&]*)"); 
    private Matcher matcher2;
    private BufferedWriter writer = null;
    private long lastWriteTime = System.currentTimeMillis();

    public int getId() {
        return 20;
    }

    public String getName() {
        return "Log unique GET queries into file (" + getLogFileName() + ")";
        
    }

    public void init() {
     	outFile = new File(getLogFileName());
     	
    }

    protected String getLogFileName() {
        return "filter/get.xls";
    }
 
    public void onHttpRequestSend(HttpMessage httpMessage) {

        HttpRequestHeader reqHeader = httpMessage.getRequestHeader();
        
        if (reqHeader != null && reqHeader.isText() && !reqHeader.isImage()){
            if (reqHeader.getMethod().equalsIgnoreCase(HttpRequestHeader.GET)){
                try{
                    
                    URI uri = reqHeader.getURI();
                    
                    int pos;
                    
                    String firstline;
                    
                    URI newURI = (URI) uri.clone();
                    String query = newURI.getQuery();
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

    public void onHttpResponseReceive(HttpMessage httpMessage) {
        
    }
    
    protected synchronized void writeLogFile(String line, Hashtable param){
        try{
            
            if (getWriter() != null) {
                
                getWriter().write(line + CRLF);
            }
            
            if (param!=null){
                Enumeration v = param.keys();
                while (v.hasMoreElements()) {
                    String name = (String)v.nextElement();
                    String value = (String)param.get(name);
                    getWriter().write(delim + name + delim + value + CRLF);		        		           
                }    		
            }

            lastWriteTime = System.currentTimeMillis();
            
        }catch(IOException ae){
        }
        
    }
    
    protected Hashtable parseParameter(String param){
        Hashtable table = new Hashtable();
        
        try{	  
            matcher2 = pSeparator.matcher(param);
            while (matcher2.find()){
                table.put(matcher2.group(1), matcher2.group(2));
                
            }
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return table;
        
    }
    
    public synchronized void timer() {
        if (writer != null && System.currentTimeMillis() > lastWriteTime + 5000) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
            }            
        }
    }
    
    private synchronized BufferedWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new BufferedWriter(new FileWriter(outFile,true));            
        }
        return writer;
    }
    
}
