package org.parosproxy.paros.network;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

/*
 * http 메세지의 request랑 response(헤더랑 바디 둘다)의 표현하는 부분이라고 생각하면 됨.
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
     * 비어있는 http 메세지 때문에 만듬
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
	 * 입력받은 req헤더, req바디, res헤더, res바디를 같이 이용해서, http 메세지가 주어진 request랑 response 둘다 주어져 있는 http 메세지를 위해서 만든 부분이여
	 */
	public HttpMessage(HttpRequestHeader reqHeader, HttpBody reqBody,
			HttpResponseHeader resHeader, HttpBody resBody) {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
		setResponseHeader(resHeader);
		setResponseBody(resBody);		
		// 보면 위에 말한 바디, 헤더 둘다 여기안에서 쓰려고 this-> 하듯이 세팅 하잖슴
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
	 * 메세지의 request 헤더 부분을 얻어와서 리턴함
	 */
	public HttpRequestHeader getRequestHeader() {
		return mReqHeader;
	}
	
	public void setRequestHeader(HttpRequestHeader reqHeader) {
		mReqHeader = reqHeader;
	}

	/*
	 * 위에 적은거처럼 response헤더를 얻어오는 건데, response를 반환혀, 근데 만약에 getResponseHeader를 문자열로 반환하는 부분,
	 * getResponseHeader().isString() 안에 값이 혹은 문자열로 반환된 값이 비어있으면, 아직 읽지 못했다는 겨
	 */
	public HttpResponseHeader getResponseHeader() {
		return mResHeader;
	}

	public void setResponseHeader(HttpResponseHeader resHeader) {
		mResHeader = resHeader;
	}

	/*
	 * 메세지의 request 바디를 얻어오는 부분인데, 반환하는 값이 null값이면, response 헤더가 존재하지 않는다는 말이여
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
	 * 2개의 메세지가 같을 경우에 둘이 비교하는데,
	 * 2개의 메세지가 같다는 말은, 호스트, 포트, 경로, 입력받은 쿼리, value(값들)이 모두 똑같다는 거여. 아마, 포스트 방식으로 request가 진행하려면, 바디가 꼭 같아야한데
	 * 여기에서는 입력 받은 msg를 이용 혀
	 */
	public boolean equals(Object object) {

	    if (!(object instanceof HttpMessage)) {
	        return false;
	    }
	    
	    HttpMessage msg = (HttpMessage) object;
	    boolean result = false;
	    
	    // method를 비교하는 부분
	    if (!this.getRequestHeader().getMethod().equalsIgnoreCase(msg.getRequestHeader().getMethod())) {
	        return false;
	    }
	    
	    // 호스트, 포트, uri를 비교하는 부분이여
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
	 * 만약에 호스트, 포트, 패스(경로)랑 쿼리 이름이 모두 같으면, 2개의 메세지가 동등한 타입? 같은 타입이라고 보면 됨.
	 * 만약에 쿼리가 value(값)이 다를지라도,
	 */
	public boolean equalType(HttpMessage msg) {
	    boolean result = false;

	    // method 비교
	    if (!this.getRequestHeader().getMethod().equalsIgnoreCase(msg.getRequestHeader().getMethod())) {
	        return false;
	    }
	    
	    // 호스트, 포트, uri를 비교함
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

	    // uri 쿼리 부분을 비교하는데, 메세지가 같은 입력 값이 여기에 세팅을 하는지, 되어있는지 고민 좀 해볼겨
        if (uri1.getQuery() != null) query1 = uri1.getQuery();
        if (uri2.getQuery() != null) query2 = uri2.getQuery();

        set1 = getParamNameSet(query1);
	    set2 = getParamNameSet(query2);

	    if (!set1.equals(set2)) {
	        return false;
	    }

	    // 포스트방식이면 여기서 비교를 할겨,
	    // 포스트 방식의 바디 부분이 같은 똑같이 세팅이 되어있어야되, 둘다
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
					// param 찾음

					key = keyValue[i].substring(0,pos);
					value = keyValue[i].substring(pos+1);

					//!!! note: this means param not separated by & and = is not parsed
					// !!! note <- 이거는 param이 아직 안 나뉘어져 있다는 말이여 &로, = <- 이놈은 파싱 하지 않았다는 말이여
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
     * request가 보내지고, response가 받는 사이에 생긴 경과 시간을 얻어와서 리턴 하는 부분이여 참고로 이건, millis안에 있음
     * 리스폰스가 못 받으면, 값은 0으로 나옴
     */
    public int getTimeElapsedMillis() {
        return timeElapsed;
    }

    /*
     * 위에 처럼 둘 사이에 생긴 얻어온 경과시간을 세팅하는 부분이여
     */
    public void setTimeElapsedMillis(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    /*
     * request가 보내지려고 할때, 시작 시간을 얻어오는겨, 메세지가 보내지기 전에, System안에currentTimeMillis를 통해서 시간을 얻어올겨
     * request가 안보내졌다면, 값은 0이여
     */
    public long getTimeSentMillis() {
        return timeSent;
    }
    /*
     * request가 보내졌다면, 시간을 세팅하는 부분이여,
     * 세팅하려고 얻어온 timeSent를 이용함
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
