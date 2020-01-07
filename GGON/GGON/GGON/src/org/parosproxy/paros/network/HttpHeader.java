package org.parosproxy.paros.network;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


abstract public class HttpHeader implements java.io.Serializable{


	public static final String CRLF 			= "\r\n";
	public static final String LF 				= "\n";
	public static final String CONTENT_LENGTH 	= "Content-length";
	public static final String TRANSFER_ENCODING = "Transfer-encoding";
	public static final String CONTENT_ENCODING = "Content-encoding";
	public static final String CONTENT_TYPE 	= "Content-Type";
	public static final String PROXY_CONNECTION = "Proxy-Connection";
	public static final String PROXY_AUTHENTICATE = "Proxy-authenticate";
	public static final String CONNECTION		= "Connection";
	public static final String AUTHORIZATION	= "Authorization";
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	public static final String LOCATION				= "Location";
	public static final String IF_MODIFIED_SINCE	= "If-Modified-Since";
	public static final String IF_NONE_MATCH		= "If-None-Match";
	public static final String USER_AGENT		= "User-Agent";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CACHE_CONTROL	= "Cache-control";
	public static final String PRAGMA			= "Pragma";
	public static final String REFERER			= "Referer";
	
	public static final String HTTP09 	= "HTTP/0.9";
	public static final String HTTP10 	= "HTTP/1.0";
	public static final String HTTP11 	= "HTTP/1.1";
	public static final String _CLOSE 			= "Close";
	public static final String _KEEP_ALIVE 		= "Keep-alive";
	public static final String _CHUNKED			= "Chunked";
	
	public static final String SCHEME_HTTP		= "http://";
	public static final String SCHEME_HTTPS		= "https://";
	public static final String HTTP				= "http";
	public static final String HTTPS			= "https";

	public static final Pattern patternCRLF			= Pattern.compile("\\r\\n", Pattern.MULTILINE);
	public static final Pattern patternLF				= Pattern.compile("\\n", Pattern.MULTILINE);
	
	private static final Pattern patternCharset = Pattern.compile("charset *= *([^;\\s]+)", Pattern.CASE_INSENSITIVE);

	protected static final String p_TEXT		= "[^\\x00-\\x1f\\r\\n]*"; 
	protected static final String p_METHOD		= "(\\w+)";
	protected static final String p_SP			= " +";

	protected static final String p_URI			= "([^\\r\\n]+)";
	protected static final String p_VERSION		= "(HTTP/\\d+\\.\\d+)";
	protected static final String p_STATUS_CODE	= "(\\d{3})";
	protected static final String p_REASON_PHRASE = "(" + p_TEXT + ")";
	
	protected String mStartLine = "";
	protected String mMsgHeader = "";
	protected boolean mMalformedHeader = false;
	protected Hashtable mHeaderFields = new Hashtable();
	protected int mContentLength = -1;
	protected String mLineDelimiter = CRLF;
	protected String mVersion = "";
	
	public HttpHeader() {
		init();
	}

	public HttpHeader(String data) throws HttpMalformedHeaderException {
		this();
		setMessage(data);
	}

	private void init() {
		mHeaderFields = new Hashtable();
		mStartLine = "";
		mMsgHeader = "";
		mMalformedHeader = false;
		mContentLength = -1;
		mLineDelimiter = CRLF;
		mVersion = "";
	}

	public void setMessage(String data) throws HttpMalformedHeaderException {
		init(); 

		mMsgHeader = data;
		try {
			if (!this.parse(data)) {
				mMalformedHeader = true;
			}
		} catch (Exception e) {
			mMalformedHeader = true;
		}

		if (mMalformedHeader) {
			throw new HttpMalformedHeaderException();
		}

	}
	
	public void clear() {
	    init();
	}

	public String getHeader(String name) {
		Vector v = getHeaders(name);
		if (v == null) {
			return null;
		}
		
		return (String) (v.firstElement());
	}
 
    public Vector getHeaders(String name) {
    	Vector v = (Vector) mHeaderFields.get(name.toUpperCase());
    	return v;
    }

	public void addHeader(String name, String val) {
		mMsgHeader = mMsgHeader + name + ": " + val + mLineDelimiter;
		addInternalHeaderFields(name, val);
	}

    public void setHeader(String name, String value) {

		Pattern pattern = null;

		if (getHeaders(name) == null && value != null) {
			addHeader(name, value);
		} else {
			try {
				pattern = getHeaderRegex(name);
				Matcher matcher = pattern.matcher(mMsgHeader);
				if (value == null) {
					mMsgHeader = matcher.replaceAll("");
				} else {
					String newString = name + ": " + value + mLineDelimiter;
					mMsgHeader = matcher.replaceAll(newString);
				}

				replaceInternalHeaderFields(name, value);
				
			}
			catch (Exception e) {
			}
			
		}
    }
    
    private Pattern getHeaderRegex(String name) throws PatternSyntaxException
	{
		return Pattern.compile("^ *"+ name + " *: *[^\\r\\n]*" + mLineDelimiter, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	}

	public String getVersion() {
		return mVersion;
	}

	abstract public void setVersion(String version);

	public int getContentLength() {
        return mContentLength;
    }

	public void setContentLength(int len) {
		if (mContentLength != len) {
			setHeader(CONTENT_LENGTH, Integer.toString(len));
			mContentLength = len;
		}
	}

   	public boolean isConnectionClose() {
		boolean result = true;
		if (mMalformedHeader) {
			return true;
		}

		if (isHttp10()) {
			result = true;
			try {
				if (getHeader(CONNECTION).equalsIgnoreCase(_KEEP_ALIVE) || getHeader(PROXY_CONNECTION).equalsIgnoreCase(_KEEP_ALIVE)) {
					return false;
				}
			} catch (NullPointerException e) {
			}

		} else if (isHttp11()) {
			result = false;
			try {
				if (getHeader(CONNECTION).equalsIgnoreCase(_CLOSE)) {
					return true;
				} else if  (getHeader(PROXY_CONNECTION).equalsIgnoreCase(_CLOSE)) {
					return true;
				}
			} catch (NullPointerException e) {
			}
		}
		return result;
	}

	public boolean isHttp10() {
		if (mVersion.equalsIgnoreCase(HTTP10)) {
			return true;
		}
		return false;
	}

	public boolean isHttp11() {
		if (mVersion.equalsIgnoreCase(HTTP11)) {
			return true;
		}
		return false;
	}

    public boolean isTransferEncodingChunked() {
    	String transferEncoding = getHeader(TRANSFER_ENCODING);
    	if (transferEncoding != null && transferEncoding.equalsIgnoreCase(_CHUNKED)) {
    		return true;
    	}
    	return false;
    }

    protected boolean parse(String data) throws Exception {

        String 	token = null,
				name = null,
				value = null;
        int pos = 0;
        Pattern pattern = null;

        if(data == null || data.equals("")) {
            return true;
        }

        if ((pos = data.indexOf(CRLF)) < 0) {
        	if ((pos = data.indexOf(LF)) < 0) {
        		return false;
        	} else {
        		mLineDelimiter = LF;
        		pattern = patternLF;
        	}
        } else {
        	mLineDelimiter = CRLF;
        	pattern = patternCRLF;
        }
        
		String[] split = pattern.split(data);
		mStartLine = split[0];

		StringBuffer sb = new StringBuffer(2048);
		for (int i=1; i<split.length; i++)
		{
			token = split[i];
			if (token.equals("")) {
				continue;
			}
			
            if((pos = token.indexOf(":")) < 0) {
				mMalformedHeader = true;
                return false;
            }
            name  = token.substring(0, pos).trim();
            value = token.substring(pos +1).trim();

            if(name.equalsIgnoreCase(CONTENT_LENGTH)) {
            	try {
                	mContentLength = Integer.parseInt(value);
            	} catch (NumberFormatException nfe){}
            }
		
			sb.append(name + ": " + value + mLineDelimiter);
		
			addInternalHeaderFields(name, value);
		}

        mMsgHeader = sb.toString();
		return true;
	}

	private void replaceInternalHeaderFields(String name, String value) {
		String key = name.toUpperCase();
		Vector v = getHeaders(key);
		if (v == null) {
			v = new Vector();
			mHeaderFields.put(key, v);
		}

		if (value != null) {
		    v.clear();
			v.add(value);
		} else {
			mHeaderFields.remove(key);
		}
	}

	private void addInternalHeaderFields(String name, String value) {
		String key = name.toUpperCase();
		Vector v = getHeaders(key);
		if (v == null) {
			v = new Vector();
			mHeaderFields.put(key, v);
		}

		if (value != null) {
			v.add(value);
		} else {
			mHeaderFields.remove(key);
		}
	}

    public boolean isMalformedHeader() {
        return mMalformedHeader;
    }

    public String toString() {
		return getPrimeHeader() + mLineDelimiter + mMsgHeader + mLineDelimiter;
    }

    abstract public String getPrimeHeader();

    public boolean isImage() {
    	return false;
    }

    public boolean isText() {
    	return true;
    }

    public String getLineDelimiter() {
    	return mLineDelimiter;
    }

    public String getHeadersAsString() {
    	return mMsgHeader;
    }

    public boolean isEmpty() {
        if (mMsgHeader == null || mMsgHeader.equals("")) {
            return true;
        }
        
        return false;
    }
    
	public String getCharset() {
	    String contentType = getHeader(CONTENT_TYPE);
	    String charset = "";
	    if (contentType == null) {
	        return null;
	    }
	    
	    Matcher matcher = patternCharset.matcher(contentType);
	    if (matcher.find()) {
	        charset = matcher.group(1);
	    }
	    return charset;
	}
}
