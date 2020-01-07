package org.parosproxy.paros.extension.filter;

import javax.swing.JOptionPane;

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;

public class FilterChangeUserAgent extends FilterAdaptor {
    static final String[] userAgentName = {
            "Firefox 1.0.1 Windows XP",
            "Firefox 1.0 Windows 2000",
            "Internet Explorer 6.0 Windows XP",
            "Internet Explorer 6.0 Windows 2000",
            "Internet Explorer 5.5 Windows XP",
            "Internet Explorer 5.5 Windows 2000",
            "Internet Explorer 5.0 Windows XP",
            "Internet Explorer 5.0 Windows 2000",
            "Netscape 7.2 Widows XP",
            "Safari Apple Mac OS X ",
            "Opera 7.0 Windows XP English",
            "Opera 6.0 Windows XP English"
    };
    
    private static final String[] userAgentHeader = {
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6) Gecko/20050223 Firefox/1.0.1",
            "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.7.5) Gecko/20041107 Firefox/1.0",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)",
            "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.1)",
            "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)",
            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT 5.1)",
            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT 5.0)",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.2) Gecko/20040804 Netscape/7.2 (ax)",
            "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/125.5.6 (KHTML, like Gecko) Safari/125.12",
            "Opera/7.0 (Windows NT 5.1; U) [en]",
            "Opera/6.0 (Windows NT 5.1; U) [en]"

    };
    
    private FilterChangeUserAgentDialog filterChangeUserAgentDialog = null;
	private String userAgent = "";

    public int getId() {
        return 120;
    }

    public String getName() {
        return "Change user agent to other browsers.";
    }
 
	private FilterChangeUserAgentDialog getFilterChangeUserAgentDialog() {
		if (filterChangeUserAgentDialog == null) {
		    filterChangeUserAgentDialog  = new FilterChangeUserAgentDialog(getView().getMainFrame(), true);
		}
		return filterChangeUserAgentDialog ;
	}
	
	public boolean isPropertyExists() {
	    return true;
	}
	
	public void editProperty() {
	    FilterChangeUserAgentDialog  dialog = getFilterChangeUserAgentDialog();
	    dialog.setView(getView());
	    int result = dialog.showDialog();
	    if (result == JOptionPane.CANCEL_OPTION) {
	        return;
	    }
	    
	    userAgent = userAgentHeader[dialog.getUserAgentItem()];
	        
	    
	}
	
    public void onHttpRequestSend(HttpMessage msg) {

        if (userAgent.equals("") || msg.getRequestHeader().isEmpty()) {
            return;
        }
 
        msg.getRequestHeader().setHeader(HttpHeader.USER_AGENT, userAgent);            
        
        
    }

    public void onHttpResponseReceive(HttpMessage msg) {
            
    }
	
}
