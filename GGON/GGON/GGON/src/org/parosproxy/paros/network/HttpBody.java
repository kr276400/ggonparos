package org.parosproxy.paros.network;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * request �� response �޼��� �ȿ� http �ٵ� �߻���? �̶�� �����ϼ�.
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
	 * http�ٵ��� ����� Ȯ���ϰ� �̸� �Ҵ��س��´ٰ� ���� ��, �ƽø� ������� 256k �̰�, �Ѿ�°� ���ϵ��� �����Ѱ���, ���� ���� content ���̸�.
	 */
	public HttpBody(int capacity) {
	    
	    if (capacity>0) {
	    	//capacity = ������, �� �뷮�̶�� ����.
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
	 * ���� ������ (���ڿ� ������)�κ��� httpbody�� ����ٰ� �����ϸ� �Ǵµ�, �����Ѵٰ� ����.
	 */
	public HttpBody(String data) {
	    this();
		if (data == null)
			return;
		setBody(data);
        cacheString = null;
        /*
         * ��ü �ڵ� ������ ���ڿ��� �̷���� data���� �ް�,
         * ���࿡ data�� null ���̸� �׳� �����ϰ�, setBody ��� �ؿ� �ٵ� �����ϴ� �Լ��� data���� �Ѱ�
         * �׸��� ���ڿ��� �Ǿ��ִ� cacheString�� null ���� �ִ°���.
         */
	}

	public void setBody(byte[] buf) {
	    body = new StringBuffer(buf.length);
	    append(buf);
        cacheString = null;

	}
	
	/*
	 * ���� �� data�� �����ϱ� ���ؼ� �ռ� ����� httpbody�� �����Ѵٰ� ������
	 * ���� ó���� UnsupportedEncodingException���� ó����.
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
	 * �� body�� Ȯ���� ���̰� ������������, byte�迭�� ÷���Ѵٴ� ���̿�,
	 * �����, buf�� length �Ѵ� ���� ���̿�.
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
	 * body�� byte array �迭�� ÷���Ѵٰ� ���� ��.
	 * ����������, buf�� ���� ������.
	 */
	public void append(byte[] buf) {
		if (buf == null)
			return;
		append(buf, buf.length);
        cacheString = null;

	}

	/*
	 * body�� ���ڿ��� �߰��ϴ� �κ���.
	 * �Լ� �ȿ� ���� buf�� ���� �κ�.
	 */
	public void append(String buf) {
		if (buf == null)
			return;

		body.append(buf);
        cacheString = null;

	}

	/*
	 * body�ȿ� ���빰�� ���ڿ��� ������ �κ���
	 */
	public String toString() {

        if (cacheString != null) {
            return cacheString;
        }
        
	    if (isChangedCharset) {
            cacheString = toString(getCharset());
            // ���⼭ ���ڿ��� ��ȯ�ؼ� ������, charset�� character�� ������ ���� ���°� �ϰ���.
            isChangedCharset = false;
            return cacheString;
	    }
	    
	    // Ȯ�ε��� ���� charset ���� �κ���.
	    String html = body.toString();

	    Matcher matcher = patternCharset.matcher(html);
		if (matcher.find()) {
			setCharset(matcher.group(1));
		} else {
		    String utf8 = toUTF8();
		    if (utf8 != null) {
		    	// UTF8�� �Ǿ��ִٰ� ��������
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
	 * ���� �ٵ��� ���̸� ������
	 */
	public int length() {
		return body.length();
	}
	/*
	 * �ٵ��� ���� ���̸� �����ϰ�, ���࿡ ���� ���빰�� ���̰� �� ��ų�, �ʹ� ����ġ�� ��� ������ ���̸� ���ϰſ�.
	 * ����� ���̴� ���� ������ ������.
	 */
	public void setLength(int length) {
		body.setLength(length);
        cacheString = null;

	}
	
    public String getCharset() {
        return charset;
    }
    // ���� charset�� ���� �ϱ� ���� �κ���
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