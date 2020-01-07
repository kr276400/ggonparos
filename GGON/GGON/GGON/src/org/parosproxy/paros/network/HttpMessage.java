package org.parosproxy.paros.network;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

/*
 * http �޼����� request�� response(����� �ٵ� �Ѵ�)�� ǥ���ϴ� �κ��̶�� �����ϸ� ��.
 */
public class HttpMessage {

	private static Pattern staticPatternParam = Pattern.compile("&", Pattern.CASE_INSENSITIVE);

	private HttpRequestHeader mReqHeader = new HttpRequestHeader();
	private HttpBody mReqBody = new HttpBody();
	private HttpResponseHeader mResHeader = new HttpResponseHeader();
	private HttpBody mResBody = new HttpBody();
	private Object userObject = null;
	private int timeElapsed = 0;
	private long timeSent = 0;
    private String tag = "";

    /*
     * ����ִ� http �޼��� ������ ����
     */
	public HttpMessage() {
	}
	
	public HttpMessage(URI uri) throws HttpMalformedHeaderException {
	    setRequestHeader(new HttpRequestHeader(HttpRequestHeader.GET, uri, HttpHeader.HTTP11));
	}

	public HttpMessage(HttpRequestHeader reqHeader) {
	    setRequestHeader(reqHeader);
	}
	
	public HttpMessage(HttpRequestHeader reqHeader, HttpBody reqBody) {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);	    
	}
	/*
	 * �Է¹��� req���, req�ٵ�, res���, res�ٵ� ���� �̿��ؼ�, http �޼����� �־��� request�� response �Ѵ� �־��� �ִ� http �޼����� ���ؼ� ���� �κ��̿�
	 */
	public HttpMessage(HttpRequestHeader reqHeader, HttpBody reqBody,
			HttpResponseHeader resHeader, HttpBody resBody) {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
		setResponseHeader(resHeader);
		setResponseBody(resBody);		
		// ���� ���� ���� �ٵ�, ��� �Ѵ� ����ȿ��� ������ this-> �ϵ��� ���� ���ݽ�
	}
	
	public HttpMessage(String reqHeader, String reqBody, String resHeader, String resBody) throws HttpMalformedHeaderException {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
		if (resHeader != null && !resHeader.equals("")) {
		    setResponseHeader(resHeader);
		    setResponseBody(resBody);
		}
	}

	/*
	 * �޼����� request ��� �κ��� ���ͼ� ������
	 */
	public HttpRequestHeader getRequestHeader() {
		return mReqHeader;
	}
	
	public void setRequestHeader(HttpRequestHeader reqHeader) {
		mReqHeader = reqHeader;
	}

	/*
	 * ���� ������ó�� response����� ������ �ǵ�, response�� ��ȯ��, �ٵ� ���࿡ getResponseHeader�� ���ڿ��� ��ȯ�ϴ� �κ�,
	 * getResponseHeader().isString() �ȿ� ���� Ȥ�� ���ڿ��� ��ȯ�� ���� ���������, ���� ���� ���ߴٴ� ��
	 */
	public HttpResponseHeader getResponseHeader() {
		return mResHeader;
	}

	public void setResponseHeader(HttpResponseHeader resHeader) {
		mResHeader = resHeader;
	}

	/*
	 * �޼����� request �ٵ� ������ �κ��ε�, ��ȯ�ϴ� ���� null���̸�, response ����� �������� �ʴ´ٴ� ���̿�
	 */
	public HttpBody getRequestBody() {
		return mReqBody;
	}

	public void setRequestBody(HttpBody reqBody) {
		mReqBody = reqBody;
	}

	public HttpBody getResponseBody() {
		return mResBody;
	}

	public void setResponseBody(HttpBody resBody) {
		mResBody = resBody;
	    getResponseBody().setCharset(getResponseHeader().getCharset());

	}
	
	public void setRequestHeader(String reqHeader) throws HttpMalformedHeaderException {
		HttpRequestHeader newHeader = new HttpRequestHeader(reqHeader);
		setRequestHeader(newHeader);
	}
	
	public void setResponseHeader(String resHeader) throws HttpMalformedHeaderException {
		HttpResponseHeader newHeader = new HttpResponseHeader(resHeader);
		setResponseHeader(newHeader);

	}

	public void setRequestBody(String body) {
		getRequestBody().setBody(body);
	    getRequestBody().setCharset(getRequestHeader().getCharset());

	}

	public void setResponseBody(String body) {
	    if (mReqBody == null) {
	        mReqBody = new HttpBody();
	    }
		getResponseBody().setBody(body);
	    getResponseBody().setCharset(getResponseHeader().getCharset());

	}

	/*
	 * 2���� �޼����� ���� ��쿡 ���� ���ϴµ�,
	 * 2���� �޼����� ���ٴ� ����, ȣ��Ʈ, ��Ʈ, ���, �Է¹��� ����, value(����)�� ��� �Ȱ��ٴ� �ſ�. �Ƹ�, ����Ʈ ������� request�� �����Ϸ���, �ٵ� �� ���ƾ��ѵ�
	 * ���⿡���� �Է� ���� msg�� �̿� ��
	 */
	public boolean equals(Object object) {

	    if (!(object instanceof HttpMessage)) {
	        return false;
	    }
	    
	    HttpMessage msg = (HttpMessage) object;
	    boolean result = false;
	    
	    // method�� ���ϴ� �κ�
	    if (!this.getRequestHeader().getMethod().equalsIgnoreCase(msg.getRequestHeader().getMethod())) {
	        return false;
	    }
	    
	    // ȣ��Ʈ, ��Ʈ, uri�� ���ϴ� �κ��̿�
	    URI uri1 = this.getRequestHeader().getURI();
	    URI uri2 = msg.getRequestHeader().getURI();

	    
	    try {
            if (uri1.getHost() == null || uri2.getHost() == null || !uri1.getHost().equalsIgnoreCase(uri2.getHost())) {
                return false;
            }
            
            if (uri1.getPort() != uri2.getPort()) {
                return false;
            }
            
            String pathQuery1 = uri1.getPathQuery();
            String pathQuery2 = uri2.getPathQuery();

            if (pathQuery1 == null && pathQuery2 == null) {
                return true;
            } else if (pathQuery1 != null && pathQuery2 != null) {
                return pathQuery1.equalsIgnoreCase(pathQuery2);
            } else if (pathQuery1 == null || pathQuery2 == null) {
                return false;
            }
            
            if (this.getRequestHeader().getMethod().equalsIgnoreCase(HttpRequestHeader.POST)) {
                return this.getRequestBody().toString(HttpBody.STORAGE_CHARSET).equalsIgnoreCase(msg.getRequestBody().toString(HttpBody.STORAGE_CHARSET));
            }
            
            result = true;
            
        } catch (URIException e) {
            try {
                result = this.getRequestHeader().getURI().toString().equalsIgnoreCase(msg.getRequestHeader().getURI().toString());
            } catch (Exception e1) {}
        }

        return result;
	}

	/*
	 * ���࿡ ȣ��Ʈ, ��Ʈ, �н�(���)�� ���� �̸��� ��� ������, 2���� �޼����� ������ Ÿ��? ���� Ÿ���̶�� ���� ��.
	 * ���࿡ ������ value(��)�� �ٸ�����,
	 */
	public boolean equalType(HttpMessage msg) {
	    boolean result = false;

	    // method ��
	    if (!this.getRequestHeader().getMethod().equalsIgnoreCase(msg.getRequestHeader().getMethod())) {
	        return false;
	    }
	    
	    // ȣ��Ʈ, ��Ʈ, uri�� ����
	    URI uri1 = this.getRequestHeader().getURI();
	    URI uri2 = msg.getRequestHeader().getURI();

	    
	    try {
            if (uri1.getHost() == null || uri2.getHost() == null || !uri1.getHost().equalsIgnoreCase(uri2.getHost())) {
                return false;
            }
            
            if (uri1.getPort() != uri2.getPort()) {
                return false;
            }
            
            String path1 = uri1.getPath();
            String path2 = uri2.getPath();

            if (path1 == null && path2 == null) {
                return true;
            }
            
            if (path1 != null && path2 != null && !path1.equalsIgnoreCase(path2)) {
                return false;
            } else {
                if (path1 == null || path2 == null) {
                    return false;
                }
            }

            if (!queryEquals(msg)) {
                return false;
            }
            
            result = true;
            
        } catch (URIException e) {
            e.printStackTrace();
        }
        
	    return result;
	}
	
	private boolean queryEquals(HttpMessage msg) throws URIException {
	    boolean result = false;
	    
	    URI uri1 = this.getRequestHeader().getURI();
	    URI uri2 = msg.getRequestHeader().getURI();

	    String query1 = "";
	    String query2 = "";

	    SortedSet set1 = null;
	    SortedSet set2 = null;

	    // uri ���� �κ��� ���ϴµ�, �޼����� ���� �Է� ���� ���⿡ ������ �ϴ���, �Ǿ��ִ��� ��� �� �غ���
        if (uri1.getQuery() != null) query1 = uri1.getQuery();
        if (uri2.getQuery() != null) query2 = uri2.getQuery();

        set1 = getParamNameSet(query1);
	    set2 = getParamNameSet(query2);

	    if (!set1.equals(set2)) {
	        return false;
	    }

	    // ����Ʈ����̸� ���⼭ �񱳸� �Ұ�,
	    // ����Ʈ ����� �ٵ� �κ��� ���� �Ȱ��� ������ �Ǿ��־�ߵ�, �Ѵ�
	    if (getRequestHeader().getMethod().equalsIgnoreCase(HttpRequestHeader.POST)) {
	        
	        query1 = this.getRequestBody().toString(HttpBody.STORAGE_CHARSET);
	        query2 = msg.getRequestBody().toString(HttpBody.STORAGE_CHARSET);
	        set1 = getParamNameSet(query1);
		    set2 = getParamNameSet(query2);	        

		    if (!set1.equals(set2)) {
		        return false;
		    }
	    }

	    result = true;
	    

	    
	    return result;
	}
	
	public TreeSet getParamNameSet(String params) {
	    TreeSet set = new TreeSet();
	    String[] keyValue = staticPatternParam.split(params);
		String key = null;
		String value = null;
		int pos = 0;
		for (int i=0; i<keyValue.length; i++) {
			key = null;
			value = null;
			pos = keyValue[i].indexOf('=');
			try {
				if (pos > 0) {
					// param ã��

					key = keyValue[i].substring(0,pos);
					value = keyValue[i].substring(pos+1);

					//!!! note: this means param not separated by & and = is not parsed
					// !!! note <- �̰Ŵ� param�� ���� �� �������� �ִٴ� ���̿� &��, = <- �̳��� �Ľ� ���� �ʾҴٴ� ���̿�
				}
				set.add(key);
			} catch (Exception e) {
			}
		}
		
		return set;
	}
	
    public Object getUserObject() {
        return userObject;
    }
 
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
    
    public HttpMessage cloneAll() {
        HttpMessage newMsg = cloneRequest();
        
        if (!this.getResponseHeader().isEmpty()) {
            try {
                newMsg.getResponseHeader().setMessage(this.getResponseHeader().toString());
            } catch (HttpMalformedHeaderException e) {
            }
            newMsg.setResponseBody(this.getResponseBody().toString(HttpBody.STORAGE_CHARSET));
        }

        return newMsg;
    }
    
    public HttpMessage cloneRequest() {
        HttpMessage newMsg = new HttpMessage();
        if (!this.getRequestHeader().isEmpty()) {
            try {
                newMsg.getRequestHeader().setMessage(this.getRequestHeader().toString());
            } catch (HttpMalformedHeaderException e) {
                e.printStackTrace();
            }
            newMsg.setRequestBody(this.getRequestBody().toString(HttpBody.STORAGE_CHARSET));
        }
        return newMsg;
    }
    /*
     * request�� ��������, response�� �޴� ���̿� ���� ��� �ð��� ���ͼ� ���� �ϴ� �κ��̿� ����� �̰�, millis�ȿ� ����
     * ���������� �� ������, ���� 0���� ����
     */
    public int getTimeElapsedMillis() {
        return timeElapsed;
    }

    /*
     * ���� ó�� �� ���̿� ���� ���� ����ð��� �����ϴ� �κ��̿�
     */
    public void setTimeElapsedMillis(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    /*
     * request�� ���������� �Ҷ�, ���� �ð��� �����°�, �޼����� �������� ����, System�ȿ�currentTimeMillis�� ���ؼ� �ð��� ���ð�
     * request�� �Ⱥ������ٸ�, ���� 0�̿�
     */
    public long getTimeSentMillis() {
        return timeSent;
    }
    /*
     * request�� �������ٸ�, �ð��� �����ϴ� �κ��̿�,
     * �����Ϸ��� ���� timeSent�� �̿���
     */
    public void setTimeSentMillis(long timeSent) {
        this.timeSent = timeSent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
