package org.parosproxy.paros.network;

import java.io.IOException;

public class HttpMalformedHeaderException extends IOException {
	public HttpMalformedHeaderException() {
		super();
	}

	public HttpMalformedHeaderException(String msg) {
		super(msg);
	}
}
