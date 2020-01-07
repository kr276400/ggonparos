package org.parosproxy.paros.network;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * request 나 response 메세지 안에 http 바디가 추상적? 이라고 생각하셈.
 */
public class HttpBody {

	private StringBuffer body = null;
	public static final String STORAGE_CHARSET = "8859_1";
	public static final String DEFAULT_CHARSET = "8859_1";
	private String charset = DEFAULT_CHARSET;
	private boolean isChangedCharset = false;
    private String cacheString = null;
	private static final Pattern patternCharset = Pattern.compile("<META +[^>]+charset *= *['\\x22]?([^>'\\x22;]+)['\\x22]? *>", Pattern.CASE_INSENSITIVE);
	
	public HttpBody() {
	    body = new StringBuffer();
        cacheString = null;
	}
	/*
	 * http바디의 사이즈를 확실하게 미리 할당해놓는다고 보면 됨, 맥시멈 사이즈는 256k 이고, 넘어가는거 피하도록 설정한거임, 맞지 않은 content 길이를.
	 */
	public HttpBody(int capacity) {
	    
	    if (capacity>0) {
	    	//capacity = 사이즈, 즉 용량이라는 뜻임.
	        if (capacity > 128000) {
	            capacity = 128000;
	        }
	        body = new StringBuffer(capacity);
	    } else {
	        body = new StringBuffer();
	    }
        cacheString = null;

	}

	/*
	 * 얻어온 데이터 (문자열 형식임)로부터 httpbody를 만든다고 생각하면 되는데, 설정한다고 보셈.
	 */
	public HttpBody(String data) {
	    this();
		if (data == null)
			return;
		setBody(data);
        cacheString = null;
        /*
         * 전체 코드 내용이 문자열로 이루어진 data값을 받고,
         * 만약에 data가 null 값이면 그냥 리턴하고, setBody 라고 밑에 바디 세팅하는 함수에 data값을 넘겨
         * 그리고 문자열로 되어있는 cacheString에 null 값을 주는거임.
         */
	}

	public void setBody(byte[] buf) {
	    body = new StringBuffer(buf.length);
	    append(buf);
        cacheString = null;

	}
	
	/*
	 * 제공 된 data를 저장하기 위해서 앞서 진행된 httpbody를 세팅한다고 생각혀
	 * 예외 처리는 UnsupportedEncodingException으로 처리함.
	 */
	public void setBody(String data)  {
	    byte[] buf = null;
	    try {
		    buf = data.getBytes(getCharset());

		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		setBody(buf);
        cacheString = null;

	}

	/*
	 * 이 body에 확실한 길이가 정해져있으면, byte배열을 첨부한다는 말이여,
	 * 참고로, buf나 length 둘다 얻어온 값이여.
	 */
	public void append(byte[] buf, int len) {
		if (buf == null)
			return;
		
		String temp = null;
		try {
			temp = new String(buf, 0, len, STORAGE_CHARSET);
		} catch (Exception e) {
			temp = new String(buf, 0, len);
		}
		body.append(temp);
        cacheString = null;

	}
	
	/*
	 * body에 byte array 배열을 첨부한다고 보면 됨.
	 * 마찬가지로, buf도 얻어온 값들임.
	 */
	public void append(byte[] buf) {
		if (buf == null)
			return;
		append(buf, buf.length);
        cacheString = null;

	}

	/*
	 * body에 문자열을 추가하는 부분임.
	 * 함수 안에 들어가는 buf도 얻어온 부분.
	 */
	public void append(String buf) {
		if (buf == null)
			return;

		body.append(buf);
        cacheString = null;

	}

	/*
	 * body안에 내용물을 문자열로 얻어오는 부분임
	 */
	public String toString() {

        if (cacheString != null) {
            return cacheString;
        }
        
	    if (isChangedCharset) {
            cacheString = toString(getCharset());
            // 여기서 문자열로 변환해서 저장함, charset은 character를 세팅한 것을 얻어온거 일거임.
            isChangedCharset = false;
            return cacheString;
	    }
	    
	    // 확인되지 않은 charset 관련 부분임.
	    String html = body.toString();

	    Matcher matcher = patternCharset.matcher(html);
		if (matcher.find()) {
			setCharset(matcher.group(1));
		} else {
		    String utf8 = toUTF8();
		    if (utf8 != null) {
		    	// UTF8로 되어있다고 추정했음
		        setCharset("UTF8");
                isChangedCharset = false;
                cacheString = utf8;
                return cacheString;
		    }
		    
		}
	    cacheString = toString(getCharset());
		return cacheString;
	}
	
	public String toString(String charset) {
	    String result = "";
		try {
		    if (charset.equalsIgnoreCase(STORAGE_CHARSET)) {
		        result = body.toString();
		    } else {
		        result = new String(getBytes(), charset);
		    }
		} catch (UnsupportedEncodingException e) {
		    result = body.toString();
		}
		return result;
	    
	}
	
	public byte[] getBytes() {
		byte[] result = null;
		try {
			result = body.toString().getBytes(STORAGE_CHARSET);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * 현재 바디의 길이를 리턴함
	 */
	public int length() {
		return body.length();
	}
	/*
	 * 바디의 현재 길이를 세팅하고, 만약에 현재 내용물의 길이가 더 길거나, 너무 지나치게 길면 데이터 길이를 줄일거여.
	 * 참고로 길이는 얻어온 길이임 위에서.
	 */
	public void setLength(int length) {
		body.setLength(length);
        cacheString = null;

	}
	
    public String getCharset() {
        return charset;
    }
    // 얻어온 charset을 세팅 하기 위한 부분임
    public void setCharset(String charset) {
        if (charset == null || charset.equals("")) {
            return;
        }
        this.charset = charset;
        isChangedCharset = true;
    }
    
    private String toUTF8() {
        byte[] buf1 = getBytes();
        String utf8 = null;
        byte[] buf2 = null;
        try {
            utf8 = new String(buf1, "UTF8");
            buf2 = utf8.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("UTF8 not supported.  Using 8859_1 instead.");
            return body.toString();
        }

        if (buf1.length != buf2.length) {
            return null;
        }
        return utf8;
    }
    
    
}