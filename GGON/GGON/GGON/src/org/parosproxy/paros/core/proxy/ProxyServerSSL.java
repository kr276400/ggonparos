package org.parosproxy.paros.core.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.parosproxy.paros.network.*;

public class ProxyServerSSL extends ProxyServer {
	
	private static SSLConnector ssl = HttpSender.getSSLConnector();
    
	public ProxyServerSSL() {
		super();
	}
	
	
	protected ServerSocket createServerSocket(String ip, int port) throws UnknownHostException, IOException {
		
		ServerSocket socket = ssl.listen(port, 300, InetAddress.getByName(ip));

		return socket;
	}
	
	protected ProxyThread createProxyProcess(Socket clientSocket) {
		ProxyThreadSSL process = new ProxyThreadSSL(this, clientSocket);
		return process;
	}
}
