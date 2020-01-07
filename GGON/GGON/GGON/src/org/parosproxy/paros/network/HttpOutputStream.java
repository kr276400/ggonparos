package org.parosproxy.paros.network;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpOutputStream extends BufferedOutputStream {

    private static final int	BUFFER_SIZE = 4096;
	private static final String CODEPAGE = "8859_1";
	private static final String CRLF2 = "\r\n\r\n";
	private static final String LF2 = "\n\n";
		
	public HttpOutputStream(OutputStream out) {
	    super(out, BUFFER_SIZE);
	}
		

	public void write(String data) throws IOException {
		super.write(data.getBytes(CODEPAGE));
		flush();
	}
	
	public void write(HttpBody body) throws IOException {
		if (body != null && body.length() > 0) {
			write(body.toString().getBytes(CODEPAGE));
		}
	}
	
	public void write(HttpResponseHeader resHeader) throws IOException {
		write(resHeader.toString());
	}


	public void write(byte[] buf) throws IOException {
		if (buf == null) return;
		write(buf, 0, buf.length);
	}

	public void write(byte[] buf, int len) throws IOException {
		write(buf, 0, len);
	}
		
	public void write(byte[] buf, int off, int len) throws IOException {
		if (buf == null) return;
		super.write(buf, off, len);
		flush();
	}

	public void close() {
		try {

		    super.close();
		} catch (Exception e) {
		}
	}

	public void flush() throws IOException {
		super.flush();	
	}
}
