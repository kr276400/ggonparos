package org.parosproxy.paros.network;

import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpMethodHelper {

	private static final String OPTIONS	= "OPTIONS";
	private static final String GET		= "GET";
	private static final String HEAD	= "HEAD";
	private static final String POST	= "POST";
	private static final String PUT		= "PUT";
	private static final String DELETE	= "DELETE";
	private static final String TRACE	= "TRACE";
	private static final String CONNECT	= "CONNECT";
	private static final String HTTP11	= "HTTP/1.1";
	private static final String HTTP10	= "HTTP/1.0";
	private static final String HTTP	= "http";
	private static final String HTTPS	= "https";
	
	private static final String CRLF	=	"\r\n";
	private static final String LF		=	"\n";
	private static final Pattern patternCRLF	= Pattern.compile("\\r\\n", Pattern.MULTILINE);
	private static final Pattern patternLF		= Pattern.compile("\\n", Pattern.MULTILINE);
	
	private static final String p_TEXT		= "[^\\x00-\\x1f\\r\\n]*"; 
	private static final String p_METHOD	= "(\\w+)";
	private static final String p_SP		= " +";
	private static final String p_URI		= "(\\S+)";
	private static final String p_VERSION	= "(HTTP/\\d+\\.\\d+)";
	
	private static final Pattern patternRequestLine = Pattern.compile(p_METHOD + p_SP + p_URI + p_SP + p_VERSION, Pattern.CASE_INSENSITIVE);
	private static final Pattern patternHostHeader = Pattern.compile("([^:]+)\\s*:?\\s*(\\d*)");

	private static final String HEADER_HOST = "Host";
	
	private String mUserAgent = "";
	public void setUserAgent(String userAgent) {
		mUserAgent = userAgent;
	}

	public HttpMethod createRequestMethodNew(HttpRequestHeader header, HttpBody body) throws URIException {
		HttpMethod httpMethod = null;
		
		String method = header.getMethod();
		URI uri	= header.getURI();
		String version = header.getVersion();
		
		httpMethod = new GenericMethod(method);

		httpMethod.setURI(uri);
		HttpMethodParams httpParams = httpMethod.getParams();
		httpParams.setVersion(HttpVersion.HTTP_1_0);
		if (version.equalsIgnoreCase(HttpHeader.HTTP11)) {
			httpParams.setVersion(HttpVersion.HTTP_1_1);
		}
		
		int pos = 0;
		Pattern pattern = null;
		String delimiter = CRLF;
		
		String msg = header.getHeadersAsString();
		if ((pos = msg.indexOf(CRLF)) < 0) {
			if ((pos = msg.indexOf(LF)) < 0) {
				delimiter = LF;
				pattern = patternLF;
			}
		} else {
			delimiter = CRLF;
			pattern = patternCRLF;
		}
	        
		String[] split = pattern.split(msg);
		String token = null;
		String name = null;
		String value = null;
		String host = null;
		
		for (int i=0; i<split.length; i++) {
			token = split[i];
			if (token.equals("")) {
				continue;
			}
			
			if ((pos = token.indexOf(":")) < 0) {
				return null;
			}
			name  = token.substring(0, pos).trim();
			value = token.substring(pos +1).trim();			
			httpMethod.addRequestHeader(name, value);

		}

		if (body != null && body.length() > 0) {
			EntityEnclosingMethod generic = (EntityEnclosingMethod) httpMethod;
            generic.setRequestEntity(new ByteArrayRequestEntity(body.getBytes()));

        }

		httpMethod.setFollowRedirects(false);
		return httpMethod;

	}
	public HttpMethod createRequestMethod(HttpRequestHeader header, HttpBody body) throws URIException {
		HttpMethod httpMethod = null;
		
		String method = header.getMethod();
		URI uri	= header.getURI();
		String version = header.getVersion();
		
		if (method.equalsIgnoreCase(GET)) {
			httpMethod = new GetMethod();
		} else if (method.equalsIgnoreCase(POST)) {
			httpMethod = new PostMethod();
		} else if (method.equalsIgnoreCase(DELETE)) {
			httpMethod = new DeleteMethod();
		} else if (method.equalsIgnoreCase(PUT)) {
			httpMethod = new PutMethod();
		} else if (method.equalsIgnoreCase(HEAD)) {
			httpMethod = new HeadMethod();
		} else if (method.equalsIgnoreCase(OPTIONS)) {
			httpMethod = new OptionsMethod();
		} else if (method.equalsIgnoreCase(TRACE)) {
			httpMethod = new TraceMethod(uri.toString());
		} else {
			httpMethod = new GenericMethod(method);
		}

		httpMethod.setURI(uri);
		HttpMethodParams httpParams = httpMethod.getParams();
		httpParams.setVersion(HttpVersion.HTTP_1_0);
		if (version.equalsIgnoreCase(HttpHeader.HTTP11)) {
			httpParams.setVersion(HttpVersion.HTTP_1_1);
		}
		
		int pos = 0;
		Pattern pattern = null;
		String delimiter = CRLF;
		
		String msg = header.getHeadersAsString();
		if ((pos = msg.indexOf(CRLF)) < 0) {
			if ((pos = msg.indexOf(LF)) < 0) {
				delimiter = LF;
				pattern = patternLF;
			}
		} else {
			delimiter = CRLF;
			pattern = patternCRLF;
		}
	        
		String[] split = pattern.split(msg);
		String token = null;
		String name = null;
		String value = null;
		
		for (int i=0; i<split.length; i++) {
			token = split[i];
			if (token.equals("")) {
				continue;
			}
			
			if ((pos = token.indexOf(":")) < 0) {
				return null;
			}
			name  = token.substring(0, pos).trim();
			value = token.substring(pos +1).trim();			
			httpMethod.addRequestHeader(name, value);

		}

		if (body != null && body.length() > 0 && (httpMethod instanceof PostMethod || httpMethod instanceof PutMethod)) {
			EntityEnclosingMethod post = (EntityEnclosingMethod) httpMethod;
            post.setRequestEntity(new ByteArrayRequestEntity(body.getBytes()));

		}

		httpMethod.setFollowRedirects(false);
		return httpMethod;

	}

	public static void updateHttpRequestHeaderSent(HttpRequestHeader req, HttpMethod httpMethodSent) {
		StringBuffer sb = new StringBuffer(200);
		String name = null;
		String value = null;

		if (!httpMethodSent.hasBeenUsed()) {
		    return;
		}
		
		sb.append(req.getPrimeHeader() + CRLF);

		Header[] header = httpMethodSent.getRequestHeaders();
		for (int i=0; i<header.length; i++) {
			name = header[i].getName();
			value = header[i].getValue();
			sb.append(name + ": " + value + CRLF);
		}
		
		sb.append(CRLF);
	    try {
            req.setMessage(sb.toString());
        } catch (HttpMalformedHeaderException e) {
            e.printStackTrace();
        }
	}
	
	private static String getHttpResponseHeaderAsString(HttpMethod httpMethod) {
		StringBuffer sb = new StringBuffer(200);
		String name = null;
		String value = null;

		sb.append(httpMethod.getStatusLine().toString() + CRLF);

		Header[] header = httpMethod.getResponseHeaders();
		for (int i=0; i<header.length; i++) {
			name = header[i].getName();
			value = header[i].getValue();
			sb.append(name + ": " + value + CRLF);
		}
		
		sb.append(CRLF);
		return sb.toString();
	}

	
	public static HttpResponseHeader getHttpResponseHeader(HttpMethod httpMethod) throws HttpMalformedHeaderException {
		return new HttpResponseHeader(getHttpResponseHeaderAsString(httpMethod));
	}

}
