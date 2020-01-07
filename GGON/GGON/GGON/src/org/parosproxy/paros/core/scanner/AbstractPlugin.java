package org.parosproxy.paros.core.scanner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;

abstract public class AbstractPlugin implements Plugin, Comparable {

    protected static final int PATTERN_PARAM = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    // CRLF 문자열
    protected static final String CRLF = "\r\n";
    
    private HostProcess parent = null;
    private HttpMessage msg = null;
    private Log log = LogFactory.getLog(this.getClass());
    private Configuration config = null;
    
    public AbstractPlugin() {
    }

    abstract public int getId();

    abstract public String getName();
    
    public String getCodeName() {
        String result = getClass().getName();
        int pos = getClass().getName().lastIndexOf(".");
        if (pos > -1) {
            result = result.substring(pos+1);
        }
        return result;
    }

    abstract public String[] getDependency();

    abstract public String getDescription();

    abstract public int getCategory();

    abstract public String getSolution();

    abstract public String getReference();

    public void init(HttpMessage msg, HostProcess parent) {
        this.msg = msg.cloneAll();
        this.parent = parent;
        init();
    }
    
    abstract public void init();

    protected HttpMessage getNewMsg() {
        return msg.cloneRequest();
    }

    protected HttpMessage getBaseMsg() {
        return msg;
    }

    protected void sendAndReceive(HttpMessage msg) throws HttpException, IOException {
        
        sendAndReceive(msg, true);
    }

    protected void sendAndReceive(HttpMessage msg, boolean isFollowRedirect) throws HttpException, IOException {

      msg.getRequestHeader().setHeader(HttpHeader.IF_MODIFIED_SINCE, null);
      msg.getRequestHeader().setHeader(HttpHeader.IF_NONE_MATCH, null);
      msg.getRequestHeader().setContentLength(msg.getRequestBody().length());

        parent.getHttpSender().sendAndReceive(msg, isFollowRedirect);
        return;

    }
    
    public void run() {
        try {
            if (!isStop()) {
                scan();
            }
        } catch (Exception e) {
            getLog().warn(e.getMessage());
        }
        notifyPluginCompleted(getParent());
    }

    abstract public void scan();

	protected void bingo(int risk, int reliability, String uri, String param, String otherInfo, HttpMessage msg) {
	    bingo(risk, reliability, this.getName(), this.getDescription(), uri, param, otherInfo, this.getSolution(), msg);
	}

	protected void bingo(int risk, int reliability, String name, String description, String uri, String param, String otherInfo, String solution, HttpMessage msg) {
	    Alert alert = new Alert(this.getId(), risk, reliability, name);
	    if (uri == null || uri.equals("")) {
	        uri = msg.getRequestHeader().getURI().toString();
	    }
	    if (param == null) {
	        param = "";
	    }
	    alert.setDetail(description, uri, param, otherInfo, solution, this.getReference(), msg);
	    parent.alertFound(alert);
	}

	protected boolean isFileExist(HttpMessage msg) {
	    return parent.getAnalyser().isFileExist(msg);	    
	}

	protected boolean isStop() {
	    return parent.isStop();
	}

    public boolean isEnabled() {
        return getProperty("enabled").equals("1");            

    }
    
    public boolean isVisible() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            setProperty("enabled", "1");
        } else {
            setProperty("enabled", "0");
        }
    }

    // 플러그인 두개가 같으면 비교할거야
    public int compareTo(Object obj) {
        
        int result = -1;
        if (obj instanceof AbstractPlugin) {
            AbstractPlugin test = (AbstractPlugin) obj;
            if (getId() < test.getId()) {
                result = -1;
            } else if (getId() > test.getId()) {
                result = 1;
            } else {
                result = 0;
            }
        }
        return result;
    }
    
    public boolean equals(Object obj) {

        if (compareTo(obj) == 0) {
            return true;
        }
        
        return false;

    }

	protected boolean matchHeaderPattern(HttpMessage msg, String header, Pattern pattern) {
		if (msg.getResponseHeader().isEmpty()) {
		    return false;
		}
		
	    String val = msg.getResponseHeader().getHeader(header);
		if (val == null) {
			return false;
		}
			
		Matcher matcher = pattern.matcher(val);
		return matcher.find();
	}

	protected boolean matchBodyPattern(HttpMessage msg, Pattern pattern, StringBuffer sb) {
		Matcher matcher = pattern.matcher(msg.getResponseBody().toString());
		boolean result = matcher.find();
		if (result) {
			if (sb != null) {
				sb.append(matcher.group());
			}
		}
		return result;
	}

	protected void writeProgress(String msg) {
	}

	protected  HostProcess getParent() {
	    return parent;
	}

    abstract public void notifyPluginCompleted(HostProcess parent);

	protected String stripOff(String body, String pattern) {
	    String urlEncodePattern = getURLEncode(pattern);
	    String urlDecodePattern = getURLDecode(pattern);
	    String htmlEncodePattern1 = getHTMLEncode(pattern);
	    String htmlEncodePattern2 = getHTMLEncode(urlEncodePattern);
	    String htmlEncodePattern3 = getHTMLEncode(urlDecodePattern);
	    String result = body.replaceAll("\\Q" + pattern + "\\E", "").replaceAll("\\Q" + urlEncodePattern + "\\E", "").replaceAll("\\Q" + urlDecodePattern + "\\E", "");
	    result = result.replaceAll("\\Q" + htmlEncodePattern1 + "\\E", "").replaceAll("\\Q" + htmlEncodePattern2 + "\\E", "").replaceAll("\\Q" + htmlEncodePattern3 + "\\E", "");
	    return result;
	}
	
    public static String getURLEncode(String msg) {
        String result = "";
        try {
            result = URLEncoder.encode(msg, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
	public static String getURLDecode(String msg) {
	    String result = "";
        try {
            result = URLDecoder.decode(msg, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
	}
	
	public static String getHTMLEncode(String msg) {
	    String result = msg.replaceAll("<", "&#60;");
	    result = result.replaceAll(">", "&#62;");
	    return result;
	}
	
	protected Kb getKb() {
	    return getParent().getKb();
	}
	
	protected Log getLog() {
	    return log;
	}
	
	public String getProperty(String key) {	    
	    return config.getString("plugins." + "p" + getId() + "." + key);
	}

	public void setProperty(String key, String value) {	    
	    config.setProperty("plugins." + "p" + getId() + "." + key, value);
	}
		
	public void setConfig(Configuration config) {
	    this.config = config;
	}
	
	public Configuration getConfig() {
	    return config;
	}

	public void createParamIfNotExist() {
        if (getProperty("enabled") == null) {
            setProperty("enabled", "1");        
        }
	}
}
