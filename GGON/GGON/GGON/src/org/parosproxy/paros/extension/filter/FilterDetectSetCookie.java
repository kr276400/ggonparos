package org.parosproxy.paros.extension.filter;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpResponseHeader;

public class FilterDetectSetCookie extends FilterAdaptor {

    private static final String DELIM = "\t";   
    private static final String CRLF = "\r\n";
    
    private	Pattern pattern = Pattern.compile("^ *"+ "Set-[Cc]ookie" + " *: *([^\\r\\n]*)" + "\\r\\n", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private Matcher matcher = null;
    private Vector result = null; 

    public int getId() {
        return 110;
    }

    public String getName() {
        return "Detect and alert 'Set-cookie' attempt in HTTP response for modification.";
        
    }

    public void init() {
     	
    }

    public void onHttpRequestSend(HttpMessage msg) {
       }

    public void onHttpResponseReceive(HttpMessage msg) {
        HttpResponseHeader resHeader = msg.getResponseHeader();
        if (resHeader == null || resHeader.isEmpty()) {
            return;
        }
        
        if (resHeader.getHeader("Set-cookie") != null) {
            String content = resHeader.toString();
            matcher = pattern.matcher(content);
            result = new Vector();
            while (matcher.find()){
                String cookie = matcher.group(1);
                if (cookie != null){
                    
                    getView().getMainFrame().toFront();
                    String text = JOptionPane.showInputDialog(getView().getMainFrame(), "Accept the following cookie (Ok=Accept, Cancel=Reject)?", cookie);
                    
                    if ((text != null && !text.equals(""))){            
                        resHeader.setHeader("Set-cookie", null);          
                        result.add(text);
                    } else if (text==null) {
                        resHeader.setHeader("Set-cookie", null);
                    }
                }
            }
            
            if (result.size() >0){
                content = matcher.replaceAll("");
                
                for (int i = 0;i< result.size(); i++){
                    content += "Set-Cookie: " + result.get(i).toString() + "\r\n";
                }
                try{
                    resHeader.setMessage(content);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            
            result.clear();  		
            
        }
    }
}
    

