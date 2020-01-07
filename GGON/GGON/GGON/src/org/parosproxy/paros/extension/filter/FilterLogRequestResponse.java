package org.parosproxy.paros.extension.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.parosproxy.paros.network.HttpMessage;

public class FilterLogRequestResponse extends FilterAdaptor {

    private static final String logFile = "filter/message.txt";
    private static final String delim = "====================================";   
    private static final String CRLF = "\r\n";
    private File outFile = new File(logFile);
    private BufferedWriter writer = null;
    private long lastWriteTime = System.currentTimeMillis();
    private int counter = 1;

    public int getId() {
        return 40;
    }

    public String getName() {
        return "Log request and response into file (" + logFile + ")";
    }

    public void onHttpRequestSend(HttpMessage httpMessage) {

    }

    public void onHttpResponseReceive(HttpMessage httpMessage) {

        if (!httpMessage.getRequestHeader().isText() || httpMessage.getRequestHeader().isImage() || httpMessage.getResponseHeader().isImage()) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(httpMessage.getRequestHeader().toString());
        sb.append(httpMessage.getRequestBody().toString() + CRLF);
        
        if (!httpMessage.getResponseHeader().isEmpty()) {
            sb.append(httpMessage.getResponseHeader().toString());
            sb.append(httpMessage.getResponseBody().toString() + CRLF);
        }
        
        writeLogFile(sb.toString());
    }
    
    private synchronized void writeLogFile(String line) {
        try{
            
            if (getWriter() != null) {
                getWriter().write("===== " + counter + " " + delim + CRLF);
                getWriter().write(line + CRLF);
                counter++;
            }

        }catch(Exception e){
        }
        
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
