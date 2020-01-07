package org.parosproxy.paros.extension.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.parosproxy.paros.network.HttpMessage;

public class FilterDetectMalciousContent extends FilterAdaptor {

    public int getId() {
        return 90;
    }

    public String getName() {
        return "Detect insecure or potentially malicious content in HTTP responses.";        
    }

    public void onHttpRequestSend(HttpMessage httpMessage) {

    }

    public void onHttpResponseReceive(HttpMessage msg) {

	    if (msg.getResponseHeader().isImage()) {
	        checkImage(msg);
	    }

	    if (msg.getResponseHeader().isText()) {
	        checkText(msg);
	    }

    }
  
    private void checkImage(HttpMessage msg) {
        
    }
    
	private void checkText(HttpMessage msg) {

	    try {
			checkAutocomplete(msg);
		} catch (Exception e) {
		}

		try {
			checkMaliciousCode(msg);
		} catch (Exception e) {
		}
	    
	}

	private void writeOutput(String hostPath, String msg, String match) {

		if (match != null) {
		}
		
	}
	
	private void checkAutocomplete(HttpMessage msg) {
	    
		String txtBody = msg.getResponseBody().toString();
		String txtForm = null;
		String txtInput = null;
		Matcher matcherAutocomplete = null;

	}

	private static final int DEFAULT = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;

	private static final Pattern[] patternBadHeaderList = {
			Pattern.compile("Content-type:\\s+application/hta", DEFAULT),
			Pattern.compile("Content-Disposition: attachment; filename=[^{}]+\\{[\\w\\d\\-]*\\}[^{}]+", DEFAULT),
			Pattern.compile("Location:\\s+URL:", DEFAULT)
	};

	private static final String[] patternBadHeaderDesc = {
		"Suspicious content-type header 'application/hta'",
		"MS IE Microsoft Internet Explorer CLSID File Extension Misrepresentation Vulnerability (http://www.securityfocus.com/bid/9510)",
		"Microsoft Internet Explorer URL Local Resource Access Weakness (http://www.securityfocus.com/bid/10472, http://secunia.com/advisories/11793"
	};
	
	private final static Pattern[] patternBadBodyList = {
		Pattern.compile("^.*file:javascript:eval.*$", DEFAULT),
		Pattern.compile("<[^>]*CLSID:11111111-1111-1111-1111-11111111111[^>]*>", DEFAULT),
		Pattern.compile("^.*?Scripting\\.FileSystemObject.*?$", DEFAULT),
		Pattern.compile("^.*?new\\s+ActiveXObject.*?$", DEFAULT),
		Pattern.compile("<OBJECT\\s+[^>]+>", DEFAULT),
		Pattern.compile("https?://[^\\s\"']+?@[^\\s\"']+?", DEFAULT),
		Pattern.compile("^.*?Microsoft\\.XMLHTTP.*?$", DEFAULT),		
		Pattern.compile("^.*?SaveToFile.*?$", DEFAULT),
		Pattern.compile("^.*?CreateObject\\(\\s*[\"']+Adodb.Stream[\"']\\s*\\)$", DEFAULT),
		Pattern.compile("^.*?execcommand.*?$", DEFAULT),
		Pattern.compile("(ms-its|ms-itss|mk:@MSITStore):mhtml:file://", DEFAULT),
		Pattern.compile("ms-its|ms-itss", DEFAULT),		// simplified one of the above
		Pattern.compile("<iframe[^>]+src=['\"]*shell:[^>]+>", DEFAULT),
		Pattern.compile("showModalDialog\\([^)]*\\).location\\s*?=\\s*?[\"']javascript:[\"']<SCRIPT", DEFAULT),
		Pattern.compile("^.*?execScript.*?$", DEFAULT)
	};

	private final static String[] patternBadBodyDesc = {
			"Suspcious use of javascript 'file:javascript:eval'.",
			"Suspicious ActiveX CLSID 11111111-1111-1111-1111-... being used.",
			"Attempt to access Scripting.FileSystemObject.",
			"Inline creation of ActiveX object.",
			"ActiveX object used.",
			"URL with '@' to obscure hyperlink.",
			"Suspicious use of ActiveX XMLHTTP object (http://www.securityfocus.com/bid/8577)",
			"Suspicious scripting attempt to access local file via SafeToFile.  MS IE Self Executing HTML Arbitrary Code Execution Vulnerability.  (http://www.securityfocus.com/bid/8984)",
			"MS IE ADODB.Stream Object File Installation Weakness.  (http://www.securityfocus.com/bid/10514)",
			"MS IE ExecCommand Cross-Domain Access Violation Vulnerability (http://www.securityfocus.com/bid/9015)",
			"MS IE MT-ITS Protocol Zone Bypass Vulnerability (http://www.securityfocus.com/bid/9658)",
			"MS IE MT-ITS Protocol Zone Bypass Vulnerability (http://www.securityfocus.com/bid/9658)",
			"MS IE Shell: IFrame Cross-Zone Scripting Vulnerability (http://www.securityfocus.com/bid/9628)",
			"Microsoft Internet Explorer Modal Dialog Zone Bypass Vulnerability (http://www.securityfocus.com/bid/10473)",
			"Suspicious use of IE ActiveX Control Cross-Site Scripting (http://secunia.com/advisories/13482/)",
	};
	
	private void checkMaliciousCode(HttpMessage msg) {
	    
	    Pattern bad = null;
		Matcher matcher = null;
		String txtHeader = msg.getRequestHeader().toString();
		String txtBody = msg.getResponseBody().toString();

		
		for (int i=0; i<patternBadHeaderList.length; i++){
			bad = patternBadHeaderList[i];
			matcher = bad.matcher(txtHeader);
			while (matcher.find()) {
				writeOutput(msg.getRequestHeader().getURI().toString(), patternBadHeaderDesc[i], matcher.group(0));
			}
		}
		
		for (int i=0; i<patternBadBodyList.length; i++){
			bad = patternBadBodyList[i];
			matcher = bad.matcher(txtBody);
			while (matcher.find()) {
				writeOutput(msg.getRequestHeader().getURI().toString(), patternBadBodyDesc[i], matcher.group(0));				
			}
		}
	}

    

}
