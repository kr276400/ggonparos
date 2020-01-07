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
     * enableCacheProcessing을 리턴한다
     */
    public boolean isEnableCacheProcessing() {
        return enableCacheProcessing;
    }
    /*
     * enableCacheProcessing이랑 The enableCacheProcessing 이 세팅이 되어있다면
     */
    public void setEnableCacheProcessing(boolean enableCacheProcessing) {
        this.enableCacheProcessing = enableCacheProcessing;
        if (!enableCacheProcessing) {
            cacheProcessingList.clear();
        }
    }
    /*
     * Serialize를 리턴함
     * Serialize는 직렬화라고 하면 되는데, 
     * 이놈이 뭔일 하냐면, 객체를 직렬화해서 전송이 가능한 형태로 만드는 거여
     * 직렬화가 뭐냐면, 간단하게 얘기해서 객체들의 데이터를 연속적인 데이터로 변형해서 Stream을 통해서 데이터를 읽게 끔 해주는 놈이라는 거지
     */
    public boolean isSerialize() {
        return serialize;
        // isSerialize는 안에 메소드 안에 값이 전송이 가능한 형태가 되는지, 혹은 직렬화할 수 있는지 확인하는겨
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
	 * true는 서버가 성공적으로 시작을 했다는 소리여
	 */
	public synchronized int startServer(String ip, int port, boolean isDynamicPort) {

		if (isProxyRunning) {
			stopServer();
		}
	
		isProxyRunning	= false;

		thread = new Thread(this);
		thread.setDaemon(true);   
		// 소켓 서버에서 빠르게 승인을 받으려고 보통 보다 높게 우선원을 주는거여
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
	 * 서버 작동 멈추는건데
	 * 서버가 멈출 수 있으면 true값을 주라는 거여
	 */
	public synchronized boolean stopServer(){

		if (!isProxyRunning) {
			return false;
		}

		isProxyRunning = false;
        HttpUtil.closeServerSocket(proxySocket);

		try {
			thread.join();   // 포트 타임이 끝났을 때
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
				// 별거 아녀, 소켓 시간만 도착한겨, 즉 끝났다는 거지
			} catch (IOException e) {
				// 알려지지 않은 입출력 예외인디, 계속 작업은 진행하는디, 만약 계속 하면, cpu 시간을 잡아 먹는걸 피하려고 지연을 시키는 거지, 뒤로 미룬다는거여
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
         * 프록시 쓰레드가 러닝하지 않은 놈들으 리턴하는거 같여
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