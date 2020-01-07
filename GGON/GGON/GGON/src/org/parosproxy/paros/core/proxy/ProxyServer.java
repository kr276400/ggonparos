package org.parosproxy.paros.core.proxy;
 
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.parosproxy.paros.network.ConnectionParam;
import org.parosproxy.paros.network.HttpUtil;
import org.parosproxy.paros.network.SSLConnector;


public class ProxyServer implements Runnable {

	protected Thread	thread = null;

	protected final static int PORT_TIME_OUT = 0;
	protected ServerSocket proxySocket = null;
	protected boolean isProxyRunning = false;
	protected ProxyParam proxyParam = new ProxyParam();
	protected ConnectionParam connectionParam = new ConnectionParam();
	protected Vector listenerList = new Vector();
	protected boolean serialize = false;
    protected boolean enableCacheProcessing = false;
    protected Vector cacheProcessingList = new Vector();
    
    /*
     * enableCacheProcessing�� �����Ѵ�
     */
    public boolean isEnableCacheProcessing() {
        return enableCacheProcessing;
    }
    /*
     * enableCacheProcessing�̶� The enableCacheProcessing �� ������ �Ǿ��ִٸ�
     */
    public void setEnableCacheProcessing(boolean enableCacheProcessing) {
        this.enableCacheProcessing = enableCacheProcessing;
        if (!enableCacheProcessing) {
            cacheProcessingList.clear();
        }
    }
    /*
     * Serialize�� ������
     * Serialize�� ����ȭ��� �ϸ� �Ǵµ�, 
     * �̳��� ���� �ϳĸ�, ��ü�� ����ȭ�ؼ� ������ ������ ���·� ����� �ſ�
     * ����ȭ�� ���ĸ�, �����ϰ� ����ؼ� ��ü���� �����͸� �������� �����ͷ� �����ؼ� Stream�� ���ؼ� �����͸� �а� �� ���ִ� ���̶�� ����
     */
    public boolean isSerialize() {
        return serialize;
        // isSerialize�� �ȿ� �޼ҵ� �ȿ� ���� ������ ������ ���°� �Ǵ���, Ȥ�� ����ȭ�� �� �ִ��� Ȯ���ϴ°�
    }
	public ProxyServer() {
	}

	public void setProxyParam(ProxyParam param) {
		proxyParam = param;
	}

	public ProxyParam getProxyParam() {
		return proxyParam;
	}
	
	public void setConnectionParam(ConnectionParam connection) {
	    connectionParam = connection;
	}

	public ConnectionParam getConnectionParam() {
	    return connectionParam;
	}
	
	/*
	 * true�� ������ ���������� ������ �ߴٴ� �Ҹ���
	 */
	public synchronized int startServer(String ip, int port, boolean isDynamicPort) {

		if (isProxyRunning) {
			stopServer();
		}
	
		isProxyRunning	= false;

		thread = new Thread(this);
		thread.setDaemon(true);   
		// ���� �������� ������ ������ �������� ���� ���� ���� �켱���� �ִ°ſ�
   	    thread.setPriority(Thread.NORM_PRIORITY+1);

   	    proxySocket = null;
   	    for (int i=0; i<20 && proxySocket == null; i++) {
   	        try {
   	            
   	            proxySocket = createServerSocket(ip, port);
   	            proxySocket.setSoTimeout(PORT_TIME_OUT);
   	            isProxyRunning = true;
   	            
   	        } catch (Exception e) {
   	            if (!isDynamicPort) {
   	                e.printStackTrace();
   	                return -1;
   	            } else {
   	                if (port < 65535) {
   	                    port++;
   	                }
   	            }
   	        }
   	        
   	    }

   	    if (proxySocket == null) {
   	        return -1;
   	    }
   	    
		thread.start();

		return proxySocket.getLocalPort();
		
	}

	/*
	 * ���� �۵� ���ߴ°ǵ�
	 * ������ ���� �� ������ true���� �ֶ�� �ſ�
	 */
	public synchronized boolean stopServer(){

		if (!isProxyRunning) {
			return false;
		}

		isProxyRunning = false;
        HttpUtil.closeServerSocket(proxySocket);

		try {
			thread.join();   // ��Ʈ Ÿ���� ������ ��
		} catch (Exception e) {
		}

		proxySocket = null;

		return true;
	}

	public void run() {

		Socket clientSocket = null;
		ProxyThread process = null;

		while (isProxyRunning) {
			try {
				clientSocket = proxySocket.accept();
				process = createProxyProcess(clientSocket);
				process.start();
			} catch (SocketTimeoutException e) {
				// ���� �Ƴ�, ���� �ð��� �����Ѱ�, �� �����ٴ� ����
			} catch (IOException e) {
				// �˷����� ���� ����� �����ε�, ��� �۾��� �����ϴµ�, ���� ��� �ϸ�, cpu �ð��� ��� �Դ°� ���Ϸ��� ������ ��Ű�� ����, �ڷ� �̷�ٴ°ſ�
			    try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                }
			}
			
		}

	}

	protected ServerSocket createServerSocket(String ip, int port) throws UnknownHostException, IOException {
		ServerSocket socket = new ServerSocket(port, 400, InetAddress.getByName(ip));

		return socket;
	}
	
	protected ProxyThread createProxyProcess(Socket clientSocket) {
		ProxyThread process = new ProxyThread(this, clientSocket);
		return process;
	}
	
	protected void writeOutput(String s) {
	}
	
	public void addProxyListener(ProxyListener listener) {
		listenerList.add(listener);		
	}
	
	public void removeProxyListener(ProxyListener listener) {
		listenerList.remove(listener);
	}
	
	synchronized List getListenerList() {
		return listenerList;
	}

    public boolean isAnyProxyThreadRunning() {
        return ProxyThread.isAnyProxyThreadRunning();
        /*
         * ���Ͻ� �����尡 �������� ���� ����� �����ϴ°� ����
         */
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    public void addCacheProcessingList(CacheProcessingItem item) {
        cacheProcessingList.add(item);
    }
    
    Vector getCacheProcessingList() {
        return cacheProcessingList;
    }
}