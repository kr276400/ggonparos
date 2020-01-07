package org.parosproxy.paros.core.proxy;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.ConnectionParam;
import org.parosproxy.paros.network.HttpBody;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpInputStream;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpOutputStream;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.network.HttpUtil;


class ProxyThread implements Runnable {
/*
 * implements는 인터페이스 안에서 상속
 * extends는 abstract라고 해서 추상클래스 에서 상속
 * 
 * 참고로, 일반적인 클래스에 사자, 독수리, 등등이 있다고 치면,
 * 추상 클래스는 일반 클래스보다 추상적이라고 보면됨, 구체적이지 않음.
 * 예를 들어 포유류, 어류, 조류, 등등 이런거 라고 생각하면됨.
 * 
 * 추상클래스 이새끼는 하나 이상의 추상 메소드를 가져야되고, 객체 생성은 안되거던? 근데 슈퍼클래스로는 사용할 수 있데.
 * 추상 메소드 사용하려면, 메소드 정의 해야한다.
 * 
 * 간단하게, 내용이 없는 비어있는 메소드라고 보면 됨. 걍 슈퍼 클래스의 용도인데, 일반 메소드도 포함가능 하니꼐, 추상 메소드를 꼭 구현해라.
 * 
 * 인터페이스는 추상메소드로만 이루어져있는데, 메소드의 선언만 가능함.
 * 그리고 인터페이스 이용하려면, implements라고 키워드를 사용해야함.
 * 걍 따른 클래스끼리 연결시켜주는 연결장치라고 보면 됨.
 * 
 * 인터페이스 같은 경우는 추상 메소드랑 상수만 가져 
 * 메소드는 무조건 public abstract으로 선언 되고,
 * 변수는 public static final로 무조건 선언됨.
 */
	private static final int		TIME_OUT = 60000;

	private static final String		CONNECT_HTTP_200 = "HTTP/1.1 200 Connection established\r\nProxy-connection: Keep-alive\r\n\r\n";

    
	private static Log log = LogFactory.getLog(ProxyThread.class);
    
	protected ProxyServer parentServer = null;
	protected ProxyParam proxyParam = null;
	protected ConnectionParam connectionParam = null;
	protected Thread thread = null;
	protected Socket inSocket	= null;
	protected Socket outSocket = null;
	protected HttpInputStream httpIn = null;
	protected HttpOutputStream httpOut = null;
	protected ProxyThread originProcess = this;
	
	private HttpSender 		httpSender = null;

	private Object semaphore = this;
	private static Object semaphoreSingleton = new Object();

    private static Vector proxyThreadList = new Vector();
    
	ProxyThread(ProxyServer server, Socket socket) {
		/*
		 * 스레드는 프로그램의 실행 흐름, 프로그램을 구성하고 있는 실행 단위라도 보면 됨.
		 */
		inSocket = socket;
    	try {
			inSocket.setTcpNoDelay(true);
    		inSocket.setSoTimeout(60000);
		} catch (SocketException e) {
		}

		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY-1);
		/* 
		 * 우선권을 주는거지.
		 * 가장 높은 우선 순위는 10, 기본 우선 순위는 5, 가장 낮은 우선 순위는 1 인데,
		 * 보통 스레드의 우선 순위는 5여.
		 * setPriority는 스레드의 우선 순위를 변경하는 메소드여.
		 * MIN_PRIORITY(1) 가장 낮은 새끼
		 * NORM_PRIORITY(5) 보통 새끼
		 * MAX_PRIORITY(10) 가장 높은 새끼
		 * 해서 이렇게 우선권을 쥐워주는건데 , NORM_PRIORITY-1은
		 * 위에 써있는 NORM_PRIORITY가 5라는 우선권을 주잖아? -1 해서 4라는 우선권을 준다고. 
		 * +1 이면 6이라는 우선권을 주는거임.
		 */
		parentServer = server;
		proxyParam = parentServer.getProxyParam();
		connectionParam = parentServer.getConnectionParam();
		
	}

	public void start() {
		thread.start();
        // 스레드 시작함
	}

	private void beginSSL() throws IOException {

        boolean isSecure = true;
        HttpRequestHeader firstHeader = null;

        inSocket = HttpSender.getSSLConnector().createTunnelServerSocket(inSocket);
        
        httpIn = new HttpInputStream(inSocket);
        httpOut = new HttpOutputStream(inSocket.getOutputStream());
        
        firstHeader = httpIn.readRequestHeader(isSecure);
        processHttp(firstHeader, isSecure);
    }
	
	public void run() {
        proxyThreadList.add(thread);
		boolean isSecure = this instanceof ProxyThreadSSL;
		HttpRequestHeader firstHeader = null;
		
		try {
			httpIn = new HttpInputStream(inSocket);
			httpOut = new HttpOutputStream(inSocket.getOutputStream());
			
			firstHeader = httpIn.readRequestHeader(isSecure);
            
			if (firstHeader.getMethod().equalsIgnoreCase(HttpRequestHeader.CONNECT)) {
				httpOut.write(CONNECT_HTTP_200);
                httpOut.flush();
				/*
				 * 뒤에 자세하게 나오는데, 버퍼 내용을 클라한테 전송하고 버퍼 내용을 비운다고 보면됨.
				 * 위에 write에서 받은 CONNECT_HTTP_200 값을 클라한테 보내주고 그 안을 비운다고.
				 */
                beginSSL();
				
			} else {
				processHttp(firstHeader, isSecure);
			}
		} catch (IOException e) {
		    log.warn(e.getMessage());
		    // 경고 날리는겨 로그에다가

		} finally {
            proxyThreadList.remove(thread);
			disconnect();
		}
	}
	
	protected void processHttp(HttpRequestHeader requestHeader, boolean isSecure) throws IOException {

		HttpBody reqBody = null;
		boolean isFirstRequest = true;
		HttpMessage msg = null;
        
        if (isRecursive(requestHeader)) {
            throw new IOException("Recursive request to proxy itself stopped.");
        }
        // 처음에 읽고 나서 소켓의 timeout 부분에 시간을 줄일겨
        inSocket.setSoTimeout(2500);

		do {

			if (isFirstRequest) {
				isFirstRequest = false;
			} else {
			    try {
			        requestHeader = httpIn.readRequestHeader(isSecure);

			    } catch (SocketTimeoutException e) {
			        return;
			    }
			}

			msg = new HttpMessage();
			msg.setRequestHeader(requestHeader);
			
			if (msg.getRequestHeader().getContentLength() > 0) {
				reqBody		= httpIn.readBody(requestHeader);
				msg.setRequestBody(reqBody);
			}
            
			modifyHeader(msg);
            
            if (isProcessCache(msg)) {
                continue;
            }
          
			if (parentServer.isSerialize()) {
			    semaphore = semaphoreSingleton;
			} else {
			    semaphore = this;
			}
			
			synchronized (semaphore) {
			    
			    notifyListenerRequestSend(msg);
			    
			    
			    try {
			    	/*
			    	 * 바이러스 새끼 때문에 진행이 안되는 response에 위치해있는 버그가 차지했을 때, 
			    	 * 첫 번쨰로, 스트리밍 하는 새끼가 일을 못해.
			    	 */

					getHttpSender().sendAndReceive(msg);
			        notifyListenerResponseReceive(msg);
			        
			        // response 의 헤더랑 바디를 적어
			        httpOut.write(msg.getResponseHeader());
		            httpOut.flush();
			        
			        if (msg.getResponseBody().length() > 0) {
			            httpOut.write(msg.getResponseBody().getBytes());
			            httpOut.flush();
			        }
			        
			    } catch (IOException e) {
			        throw e;
			    }
			}
			/*
			 * 세마포르를 줄이는 건데, 세마포르란 프로세스간의 통신 수단 중에 가장 간단한 새끼여서, 0 이나 1의 플래그만 넘겨
			 */
            

	    } while (!isConnectionClose(msg) && !inSocket.isClosed());
    }
	
	private boolean isConnectionClose(HttpMessage msg) {
		
		if (msg == null || msg.getResponseHeader().isEmpty()) {
		    return true;
		}
		
		if (msg.getRequestHeader().isConnectionClose()) {
		    return true;
		}
				
		if (msg.getResponseHeader().isConnectionClose()) {
		    return true;
		}
        
        if (msg.getResponseHeader().getContentLength() == -1 && msg.getResponseBody().length() > 0) {
            // no length and body > 0 must terminate otherwise cannot there is no way for client browser to know the length.
        	// 길이가 없거나 안되거나, 0보다 큰 바디는 제거된다고, 그 외에는 클라이언트 브라우저가 length를 알기위한 방법이 없데
        	// 일찍 중단 시키는 건데, 이게 클라에게 더 나은 response를 주는 거래.
            // terminate early can give better response by client.
            return true;
        }
		
		return false;
	}
	
	protected void disconnect() {
		try {
            if (httpIn != null) {
                httpIn.close();
            }
        } catch (Exception e) {}
        
        try {
            if (httpOut != null) {
                httpOut.close();
            }
        } catch (Exception e) {}
		HttpUtil.closeSocket(inSocket);
        
		if (httpSender != null) {
            httpSender.shutdown();
        }

	}
		
	/*
	 * 각 옵저버 새끼들이 request를 진행 하기 위해 각 옵저버를 지나가는건데, 
	 * 각 옵저버 안에서는 입력받은 httpmessage 메소드가 한정됬다고 보면됨
	 */
	private void notifyListenerRequestSend(HttpMessage httpMessage) {
		ProxyListener listener = null;
		List listenerList = parentServer.getListenerList();
		for (int i=0;i<listenerList.size();i++) {
			listener = (ProxyListener) listenerList.get(i);
			try {
			    listener.onHttpRequestSend(httpMessage);
			} catch (Exception e) {
			}
		}	
	}

	/*
	 * 옵저버를 통해서 가고, 각 옵저버 안에 있는 httpmessage를 진행하는겨,
	 * 입력 받은 msg같은 경우는 각 옵저버들 때문에 변경될 수 있어.
	 */
	private void notifyListenerResponseReceive(HttpMessage httpMessage) {
		ProxyListener listener = null;
		List listenerList = parentServer.getListenerList();
		for (int i=0;i<listenerList.size();i++) {
			listener = (ProxyListener) listenerList.get(i);
			try {
			    listener.onHttpResponseReceive(httpMessage);
			} catch (Exception e) {
			}
		}
	}
	
	private boolean isRecursive(HttpRequestHeader header) {
        boolean isRecursive = false;
        try {
            URI uri = header.getURI();
            if (uri.getHost().equals(proxyParam.getProxyIp())) {
                if (uri.getPort() == proxyParam.getProxyPort()) {
                    isRecursive = true;
                }
            }
        } catch (Exception e) {
        }
        return isRecursive;
    }
	    
    private static final Pattern remove_gzip1 = Pattern.compile("(gzip|deflate|compress|x-gzip|x-compress)[^,]*,?\\s*", Pattern.CASE_INSENSITIVE);
    private static final Pattern remove_gzip2 = Pattern.compile("[,]\\z", Pattern.CASE_INSENSITIVE);
    
    private void modifyHeader(HttpMessage msg) {
        String encoding = msg.getRequestHeader().getHeader(HttpHeader.ACCEPT_ENCODING);
        if (encoding == null) {
            return;
        }
        
        encoding = remove_gzip1.matcher(encoding).replaceAll("");
        encoding = remove_gzip2.matcher(encoding).replaceAll("");
        // gzip 엔코딩을 반환하는 걸 피하려고
        
        if (encoding.length() == 0) {
            encoding = null;
        }
        msg.getRequestHeader().setHeader(HttpHeader.ACCEPT_ENCODING,encoding);

    }
    
	protected HttpSender getHttpSender() {

	    if (httpSender == null) {
		    httpSender = new HttpSender(connectionParam, true);
		}

	    return httpSender;
	}

    static boolean isAnyProxyThreadRunning() {
        return !proxyThreadList.isEmpty();
    }
    
    protected boolean isProcessCache(HttpMessage msg) throws IOException {
        if (!parentServer.isEnableCacheProcessing()) {
            return false;
        }
        
        if (parentServer.getCacheProcessingList().isEmpty()) {
            return false;
        }
        
        CacheProcessingItem item = (CacheProcessingItem) parentServer.getCacheProcessingList().get(0);
        if (msg.equals(item.message)) {
            HttpMessage newMsg = item.message.cloneAll();
            msg.setResponseHeader(newMsg.getResponseHeader());
            msg.setResponseBody(newMsg.getResponseBody());

            httpOut.write(msg.getResponseHeader());
            httpOut.flush();

            if (msg.getResponseBody().length() > 0) {
                httpOut.write(msg.getResponseBody().getBytes());
                httpOut.flush();

            }
            
            return true;
            
        } else {

            try {
                RecordHistory history = Model.getSingleton().getDb().getTableHistory().getHistoryCache(item.reference, msg);
                if (history == null) {
                    return false;
                }
                
                msg.setResponseHeader(history.getHttpMessage().getResponseHeader());
                msg.setResponseBody(history.getHttpMessage().getResponseBody());

                httpOut.write(msg.getResponseHeader());
                httpOut.flush();

                if (msg.getResponseBody().length() > 0) {
                    httpOut.write(msg.getResponseBody().getBytes());                    
                    httpOut.flush();

                }
                
                return true;
                
            } catch (Exception e) {
                return true;
            }   
        }   
    }
}
