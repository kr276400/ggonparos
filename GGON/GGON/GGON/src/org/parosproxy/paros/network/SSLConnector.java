package org.parosproxy.paros.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.ReflectionSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;


public class SSLConnector implements SecureProtocolSocketFactory {

	private static final String SSL = "SSL";
	
	private static SSLSocketFactory clientSSLSockFactory = null;
	private static SSLSocketFactory clientSSLSockCertFactory = null;
    
	private static ServerSocketFactory serverSSLSockFactory = null;
    private static SSLSocketFactory tunnelSSLFactory = null;
    
    
	public SSLConnector() {
		if (clientSSLSockFactory == null) {
			clientSSLSockFactory = getClientSocketFactory(SSL);
		}
		if (serverSSLSockFactory == null) {
			serverSSLSockFactory = getServerSocketFactory(SSL);
		}

	}

	public SSLSocket client(String hostName, int hostPort, boolean useClientCert) throws IOException {

		SSLSocket socket = null;

		socket = clientNoHandshake(hostName, hostPort, useClientCert);

		socket.startHandshake();

		return socket;
	}

	public SSLSocket clientNoHandshake(String hostName, int hostPort, boolean useClientCert) throws IOException {

		SSLSocket socket = null;

		if (useClientCert) {
			socket = (SSLSocket) clientSSLSockCertFactory.createSocket(hostName, hostPort);
		} else {
			socket = (SSLSocket) clientSSLSockFactory.createSocket(hostName, hostPort);
		}

		return socket;
	}

	public void setClientCert(File keyFile, char[] passPhrase) throws Exception {
	
		clientSSLSockCertFactory = null;
		TrustManager[] trustMgr = {new RelaxedX509TrustManager()};	

		SSLContext ctx;
		KeyManagerFactory kmf;
		KeyStore ks;

		ctx = SSLContext.getInstance(SSL);
		kmf = KeyManagerFactory.getInstance("SunX509");
		ks = KeyStore.getInstance("pkcs12");

		ks.load(new FileInputStream(keyFile), passPhrase);
		java.security.SecureRandom x = new java.security.SecureRandom();
		x.setSeed(System.currentTimeMillis());
		kmf.init(ks, passPhrase);
		ctx.init(kmf.getKeyManagers(), trustMgr, x);

		clientSSLSockCertFactory = ctx.getSocketFactory();

	}
	
	public void setEnableClientCert(boolean enabled) {
	    if (enabled) {
	        clientSSLSockFactory = clientSSLSockCertFactory;
	    } else {
	        clientSSLSockFactory = getClientSocketFactory(SSL);
	    }
	}

	public ServerSocket listen(int portNum) throws IOException {
		ServerSocket sslServerPort = null;
	    sslServerPort = serverSSLSockFactory.createServerSocket(portNum);
		return sslServerPort;
	}

	public ServerSocket listen() throws IOException {
		ServerSocket sslServerPort = null;
	    sslServerPort = serverSSLSockFactory.createServerSocket();
		return sslServerPort;
	}

	public ServerSocket listen(int portNum, int maxConnection) throws IOException {
		ServerSocket sslServerPort = null;
		sslServerPort = serverSSLSockFactory.createServerSocket(portNum, maxConnection);
		return sslServerPort;
	}

	public ServerSocket listen(int paramPortNum, int maxConnection, InetAddress ip ) throws IOException {

      	ServerSocket sslServerPort = serverSSLSockFactory.createServerSocket(paramPortNum, maxConnection, ip);
		return sslServerPort;
	}

	public SSLSocketFactory getClientSocketFactory(String type) {
		TrustManager[] trustMgr = new TrustManager[]{new RelaxedX509TrustManager()};	

		try {
			SSLContext sslContext = SSLContext.getInstance(type);
			java.security.SecureRandom x = new java.security.SecureRandom();
			x.setSeed(System.currentTimeMillis());
			sslContext.init(null, trustMgr, x);
			clientSSLSockFactory = sslContext.getSocketFactory();
			HttpsURLConnection.setDefaultSSLSocketFactory(clientSSLSockFactory);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientSSLSockFactory;

	}

    public ServerSocketFactory getServerSocketFactory(String type) {
    	
		if (type.equals("SSL") || type.equals("SSLv3")) {
		    SSLServerSocketFactory ssf = null;
		    try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;
				char[] passphrase = "!@#$%^&*()".toCharArray();

				ctx = SSLContext.getInstance(type);
				kmf = KeyManagerFactory.getInstance("SunX509");
				ks = KeyStore.getInstance("JKS");

				java.security.SecureRandom x = new java.security.SecureRandom();
				x.setSeed(System.currentTimeMillis());

				ks.load(this.getClass().getClassLoader().getResourceAsStream("resource/paroskey"), passphrase);
				
				kmf.init(ks, passphrase);
				ctx.init(kmf.getKeyManagers(), null, x);

				ssf = ctx.getServerSocketFactory();

                tunnelSSLFactory = ctx.getSocketFactory();
                
				return ssf;
		    } catch (Exception e) {
				e.printStackTrace();
		    }
		} else {
		    return ServerSocketFactory.getDefault();
		}
		return null;

	}

    public Socket createSocket(
        String host,
        int port,
        InetAddress clientHost,
        int clientPort)
        throws IOException, UnknownHostException {

        Socket socket = clientSSLSockFactory.createSocket(
            host,
            port,
            clientHost,
            clientPort
        );
        return socket;
    }

    public Socket createSocket(
        final String host,
        final int port,
        final InetAddress localAddress,
        final int localPort,
        final HttpConnectionParams params
    ) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = ReflectionSocketFactory.createSocket(
                "javax.net.ssl.SSLSocketFactory", host, port, localAddress, localPort, timeout);
            if (socket == null) {
                socket = ControllerThreadSocketFactory.createSocket(
                    this, host, port, localAddress, localPort, timeout);
            }
            return socket;
        }
    }

    public Socket createSocket(String host, int port)
        throws IOException, UnknownHostException {
        return clientSSLSockFactory.createSocket(
            host,
            port
        );
    }

    public Socket createSocket(
        Socket socket,
        String host,
        int port,
        boolean autoClose)
        throws IOException, UnknownHostException {
        return clientSSLSockFactory.createSocket(
            socket,
            host,
            port,
            autoClose
        );
    }

    public Socket createTunnelServerSocket(Socket socket) throws IOException {
        SSLSocket s = (SSLSocket) tunnelSSLFactory.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        s.setUseClientMode(false);
        s.startHandshake();
        return s;
    }

}

class RelaxedX509TrustManager implements X509TrustManager {
	public boolean checkClientTrusted(java.security.cert.X509Certificate[] chain){
		return true;
	}

	public boolean isServerTrusted(java.security.cert.X509Certificate[] chain){
		return true;
	}

	public boolean isClientTrusted(java.security.cert.X509Certificate[] chain){
		return true;
	}


	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
	{}

	public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
	{}
}
