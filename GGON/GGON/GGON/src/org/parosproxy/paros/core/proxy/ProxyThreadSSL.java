package org.parosproxy.paros.core.proxy;

import java.net.Socket;

public class ProxyThreadSSL extends ProxyThread {
	ProxyThreadSSL(ProxyServerSSL server, Socket socket) {
		super(server, socket);
		/*
		 * 현재 자식 클래스가 자신을 생성할 때 부모 클래스의 생성자를 불러서 한번 초기화 해주고, 
		 * 자신을 초기화하는 새끼인데.
		 * 
		 * 그냥 super랑은 다르다. super는 부모클래스의 필드값이나 메소드를 직접 부르는거고
		 * super()는 불러서 초기화 해주고, 자기도 초기화하는 놈이다.
		 */
	}

	protected void disconnect() {

		super.disconnect();
	}
}
