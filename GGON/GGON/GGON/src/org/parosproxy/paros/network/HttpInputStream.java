package org.parosproxy.paros.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLSocket;

public class HttpInputStream extends BufferedInputStream {

	private static final int	BUFFER_SIZE = 4096;
	private static final String CRLF = "\r\n";
	private static final String CRLF2 = CRLF + CRLF;
	private static final String LF = "\n";
	private static final String LF2 = LF + LF;

	private byte[] mBuffer = new byte[BUFFER_SIZE];
	private Socket mSocket = null;
	
	public HttpInputStream(Socket socket) throws IOException {
	    super(socket.getInputStream(), BUFFER_SIZE);
		setSocket(socket);
	}
	
	public HttpRequestHeader readRequestHeader(boolean isSecure) throws HttpMalformedHeaderException, IOException {
		return new HttpRequestHeader(readHeader(), isSecure);
	}

    
	public synchronized String readHeader() throws IOException {
		String msg = "";
        int		oneByte = -1;
        boolean eoh = false;
        boolean neverReadOnce = true;
        StringBuffer sb = new StringBuffer(200);
        
        do {
            oneByte = super.read();
        	
        	if (oneByte == -1) {
        		eoh = true;
        		if (neverReadOnce) {
        			HttpUtil.sleep(50);
        			continue;
        		}
				break;
        	} else {
        		neverReadOnce = false;
        	}
            sb.append((char) oneByte);

            if (((char) oneByte) == '\n' && isHeaderEnd(sb)) {
                eoh = true;
                msg = sb.toString();
            }
		} while (!eoh || neverReadOnce);

        return msg;

	}

	private boolean isHeaderEnd(StringBuffer sb) {
		int len = sb.length();
		if (len > 2) {
			if (LF2.equals(sb.substring(len-2))) {
				return true;
			}
		}
	
		if (len > 4) {
			if (CRLF2.equals(sb.substring(len-4))) {
				return true;
			}
		}
	
		return false;
	}

	public synchronized HttpBody readBody(HttpHeader httpHeader) {

		int contentLength = httpHeader.getContentLength();	
		int readBodyLength = 0;
		int len = 0;
		
		HttpBody body = (contentLength > 0) ? new HttpBody(contentLength) : new HttpBody();
		
		try {
			while (contentLength == -1 || readBodyLength < contentLength) {
				len = readBody(contentLength, readBodyLength, mBuffer);	
                if (len > 0) {
					readBodyLength += len;
				}
				body.append(mBuffer, len);
			}
		} catch (IOException e) {
		}
		
        
		return body;
	}

	private int readBody(int contentLength, int readBodyLength, byte[] buffer) throws IOException {

		int len = 0;
		int remainingLen = 0;

		if (contentLength == -1) {
			len = super.read(buffer);

		} else {
			remainingLen = contentLength - readBodyLength;
			if (remainingLen < buffer.length && remainingLen > 0) {
				len = super.read(buffer,0,remainingLen);

			} else if (remainingLen > buffer.length) {
				len = super.read(buffer);

			}

		}

		return len;
	}
	
	public void setSocket(Socket socket) {
		mSocket = socket;
	}

	public int available() throws IOException {
		int avail = 0;
		int timeout = 0;

		avail = super.available();

		if (avail == 0 && mSocket != null && mSocket instanceof SSLSocket) {
			try {
				timeout = mSocket.getSoTimeout();
				mSocket.setSoTimeout(1);
				super.mark(256);
				super.read();
				super.reset();
				avail = super.available();
			} catch (SocketTimeoutException e) {
				avail = 0;
			} finally {
				mSocket.setSoTimeout(timeout);
			}
		}
		
		return avail;
	}
	
	public int read() throws IOException {
		return super.read();

	}
	public int read(byte[] b) throws IOException {

		return super.read(b);

	}
	
	public int read(byte[] b, int off, int len) throws IOException {

		return super.read(b, off, len);

	}

	public void close() {
		try {

		    super.close();
		} catch (Exception e) {
			
		}
	}
}
