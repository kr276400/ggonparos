package org.parosproxy.paros.core.proxy;

import java.net.Socket;

public class ProxyThreadSSL extends ProxyThread {
	ProxyThreadSSL(ProxyServerSSL server, Socket socket) {
		super(server, socket);
		/*
		 * ���� �ڽ� Ŭ������ �ڽ��� ������ �� �θ� Ŭ������ �����ڸ� �ҷ��� �ѹ� �ʱ�ȭ ���ְ�, 
		 * �ڽ��� �ʱ�ȭ�ϴ� �����ε�.
		 * 
		 * �׳� super���� �ٸ���. super�� �θ�Ŭ������ �ʵ尪�̳� �޼ҵ带 ���� �θ��°Ű�
		 * super()�� �ҷ��� �ʱ�ȭ ���ְ�, �ڱ⵵ �ʱ�ȭ�ϴ� ���̴�.
		 */
	}

	protected void disconnect() {

		super.disconnect();
	}
}
