package org.parosproxy.paros.network;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestHeader extends HttpHeader {

	// 메소드 대한 부분이야
	public final static String OPTIONS	= "OPTIONS";
	public final static String GET 		= "GET";
	public final static String HEAD		= "HEAD";
	public final static String POST		= "POST";
	public final static String PUT		= "PUT";
	public final static String DELETE	= "DELETE";
	public final static String TRACE	= "TRACE";
	public final static String CONNECT	= "CONNECT";

	public final static String HOST = "Host";

	private static final Pattern patternRequestLine
		= Pattern.compile(p_METHOD + p_SP + p_URI + p_SP + p_VERSION, Pattern.CASE_INSENSITIVE);
	private static final Pattern patternHostHeader
		= Pattern.compile("([^:]+)\\s*?:?\\s*?(\\d*?)");
	private static final Pattern patternImage
		= Pattern.compile("\\.(jpg|jpeg|gif|tiff|tif|png)\\z", Pattern.CASE_INSENSITIVE);
	private static final Pattern patternPartialRequestLine
		= Pattern.compile("\\A *(OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE|CONNECT)\\b", Pattern.CASE_INSENSITIVE);
	
	private String	mMethod = "";
	private URI		mUri = null;
	private String	mHostName = "";
	private int		mHostPort = 80;
	private boolean	mIsSecure = false;

	public HttpRequestHeader() {
	    clear();
	}

    public HttpRequestHeader(String data, boolean isSecure) throws HttpMalformedHeaderException {
		this();
		setMessage(data, isSecure);
    }

    public HttpRequestHeader(String data) throws HttpMalformedHeaderException {
    	this();
    	setMessage(data);
    }
    
    public void clear() {
        super.clear();

		mMethod = "";
		mUri = null;
		mHostName = "";
		mHostPort = 80;
		mMsgHeader = "";
        
    }
    
    public HttpRequestHeader(String method, URI uri, String version) throws HttpMalformedHeaderException {
        this(method + " " + uri.toString() + " " + version.toUpperCase() + CRLF + CRLF);
        try {
            setHeader(HOST, uri.getHost() + (uri.getPort() > 0? ":" + Integer.toString(uri.getPort()):""));
        } catch (URIException e) {
            e.printStackTrace();
        }
		setHeader(USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0;)");
		setHeader(PRAGMA,"no-cache");
		setHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");
		setHeader(ACCEPT_ENCODING,null);
		if (method.equalsIgnoreCase(HTTP11)) {
		    setContentLength(0);
		}
		
    }

    public void setMessage(String data, boolean isSecure) throws HttpMalformedHeaderException {
		super.setMessage(data);
		try {
        	if (!parse(isSecure)) {
        		mMalformedHeader = true;
        	}
    	} catch (Exception e) {
    		mMalformedHeader = true;
    	}

    	if (mMalformedHeader) {
    		throw new HttpMalformedHeaderException();
    	}

    }

    public void setMessage(String data) throws HttpMalformedHeaderException {
    	this.setMessage(data, false);
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
    	mMethod = method.toUpperCase();
    }

	public URI getURI() {
		return mUri;
	}

	public void setURI(URI uri) throws URIException, NullPointerException {

		if (uri.getScheme() == null || uri.getScheme().equals("")) {
			mUri = new URI(HTTP + "://" + getHeader(HOST) + "/" + mUri.toString(), true);
		} else {
		    mUri = uri;
		}
		
		if (uri.getScheme().equalsIgnoreCase(HTTPS)) {
			mIsSecure = true;
		} else {
			mIsSecure = false;
		}
	}

	public boolean getSecure() {
		return mIsSecure;
	}

	public void setSecure(boolean isSecure) throws URIException, NullPointerException {
		mIsSecure = isSecure;

		if (mUri == null) {
			return;
		}
		
		// 만약에 URI가 똑같으면 체크하는 부분이여
		if (getSecure()&& mUri.getScheme().equalsIgnoreCase(HTTP)) {
			mUri = new URI(mUri.toString().replaceFirst(HTTP, HTTPS), true);
			return;
		}

		if (!getSecure()&& mUri.getScheme().equalsIgnoreCase(HTTPS)) {
			mUri = new URI(mUri.toString().replaceFirst(HTTPS, HTTP), true);
			return;
		}


	}

	public void setVersion(String version) {
		mVersion = version.toUpperCase();
	}

	public int getContentLength() {
		if (mContentLength == -1) {
			return 0;
		}
		return mContentLength;
	}

    protected boolean parse(boolean isSecure) throws URIException, NullPointerException {
    	    			
    	mIsSecure = isSecure;
    	Matcher matcher = patternRequestLine.matcher(mStartLine);
		if (!matcher.find()) {
			mMalformedHeader = true;
			return false;
		}
		
		mMethod = matcher.group(1);
		String sUri	= matcher.group(2);
		mVersion	= matcher.group(3);
		
		
        if (!mVersion.equalsIgnoreCase(HTTP09) && !mVersion.equalsIgnoreCase(HTTP10) && !mVersion.equalsIgnoreCase(HTTP11)) {
        	mMalformedHeader = true;
        	return false;
        }

        mUri		= parseURI(sUri);
        
		if (mUri.getScheme() == null || mUri.getScheme().equals("")) {
			mUri = new URI(HTTP + "://" + getHeader(HOST) + mUri.toString(), true);
		}

		if (getSecure() && mUri.getScheme().equalsIgnoreCase(HTTP)) {
			mUri = new URI(mUri.toString().replaceFirst(HTTP, HTTPS), true);
		}
		
		if (mUri.getScheme().equalsIgnoreCase(HTTPS)) {
			setSecure(true);
		}
		
		String hostHeader = null;
		if (mMethod.equalsIgnoreCase(CONNECT)) {
			hostHeader = sUri;
			parseHostName(hostHeader);
		} else {
			mHostName = mUri.getHost();
			mHostPort = mUri.getPort();
		}
		return true;
	}
    
	private void parseHostName(String hostHeader) {
		if (hostHeader == null) {
			return;
		}
		int pos = 0;
		if ((pos = hostHeader.indexOf(':', 2)) > -1) {
			mHostName = hostHeader.substring(0,pos).trim();
			try {
				mHostPort = Integer.parseInt(hostHeader.substring(pos+1));
			} catch (NumberFormatException e) {
			}
		} else {
			mHostName = hostHeader.trim();
		}

	}

	public String getHostName() {
		String hostName = mHostName;
		try {
			hostName = mUri.getHost();
		} catch (URIException e) {
			e.printStackTrace();
		}
		return hostName;
	}

	public int getHostPort() {
		int port = mUri.getPort();
		return port;
	}	

	public boolean isImage() {
		try {
			return (patternImage.matcher(getURI().getPath()).find());
		} catch (URIException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isRequestLine(String data) {
		return patternPartialRequestLine.matcher(data).find();	
	}

	public String getPrimeHeader() {
		return getMethod() + " " + getURI().toString() + " " + getVersion();
	}
	
	private static final char[] DELIM_UNWISE_CHAR = {
	        '<', '>', '#', '"', ' ',
	        '{', '}', '|', '\\', '^', '[', ']', '`'
	};

	private static final String DELIM = "<>#\"";
	private static final String UNWISE = "{}|\\^[]`";
	private static final String DELIM_UNWISE = DELIM + UNWISE;
	
	public static URI parseURI(String sUri) throws URIException {
	    URI uri = null;
	    
	    int len = sUri.length();
	    StringBuffer sb = new StringBuffer(len);
        char[] charray = new char[1];
        String s = null;

	    for (int i=0; i<len; i++) {
	        char ch = sUri.charAt(i);
	        if (DELIM_UNWISE.indexOf(ch) >= 0) {
	            charray[0] = ch;
	            s = new String(charray);
	            try {
	                s = URLEncoder.encode(s, "UTF8");
	            } catch (UnsupportedEncodingException e1) {
	            }
	            sb.append(s);
	        } else if (ch == '%') {

	            try {
		            String hex = sUri.substring(i+1,i+3);
	                int parsed = Integer.parseInt(hex, 16);
	                sb.append(ch);
	            } catch (Exception e) {
		            charray[0] = ch;
		            s = new String(charray);
		            try {
		                s = URLEncoder.encode(s, "UTF8");
		            } catch (UnsupportedEncodingException e1) {
		            }
		            sb.append(s);
	            }
	        } else if (ch == ' ') {
	            sb.append("%20");
	        } else {
	            sb.append(ch);
	        }
	    }
	    uri = new URI(sb.toString(), true);
		return uri;
	}


}