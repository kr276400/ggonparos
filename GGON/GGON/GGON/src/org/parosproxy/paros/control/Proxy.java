package org.parosproxy.paros.control;
 
import java.util.Vector;

import org.parosproxy.paros.core.proxy.CacheProcessingItem;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.core.proxy.ProxyServer;
import org.parosproxy.paros.core.proxy.ProxyServerSSL;
import org.parosproxy.paros.model.Model;

public class Proxy {
    
	private Model model = null;
	private ProxyServer proxyServer = null;
	private ProxyServerSSL proxyServerSSL = null;
	private boolean reverseProxy = false;
	private String reverseProxyHost = "";
    private Vector cacheProcessingList = new Vector();
    
	public Proxy(Model model) {
		
	    this.model = model;
	    
		proxyServer = new ProxyServer();
		proxyServerSSL = new ProxyServerSSL();
		// 상속에 의해 더해짐
	}
	
	public void startServer() {
		// 서버 시작을 하는 부분이여
		// 참고로 프록시 패턴은 다른 무언가랑 , 연결되는 인터페이스의 역할을 하는 클래스다

		// 여기에 setProxyParam을 입력함으로써 파라미터값을 다시 읽을수 있으면서 재시작을 할 수 있어요
		proxyServer.setProxyParam(model.getOptionsParam().getProxyParam());
		proxyServer.setConnectionParam(model.getOptionsParam().getConnectionParam());

		proxyServerSSL.setProxyParam(model.getOptionsParam().getProxyParam());
		proxyServerSSL.setConnectionParam(model.getOptionsParam().getConnectionParam());

		if (model.getOptionsParam().getProxyParam().isUseReverseProxy()) {

		    proxyServerSSL.startServer(model.getOptionsParam().getProxyParam().getReverseProxyIp(), model.getOptionsParam().getProxyParam().getReverseProxyHttpsPort(), false);

			proxyServer.startServer(model.getOptionsParam().getProxyParam().getReverseProxyIp(), model.getOptionsParam().getProxyParam().getReverseProxyHttpPort(), false);
		    
		} else {

			proxyServer.startServer(model.getOptionsParam().getProxyParam().getProxyIp(), model.getOptionsParam().getProxyParam().getProxyPort(), false);
		    
		}
	}

	public void stopServer() {
        if (model.getOptionsParam().getProxyParam().isUseReverseProxy()) {
            proxyServerSSL.stopServer();
            proxyServer.stopServer();

        } else {
            proxyServer.stopServer();
        }
	}
	
	public void setSerialize(boolean serialize) {
	    proxyServer.setSerialize(serialize);
	    proxyServerSSL.setSerialize(serialize);
	}
	
	public void addProxyListener(ProxyListener listener) {
	    proxyServer.addProxyListener(listener);
	    proxyServerSSL.addProxyListener(listener);

	}
	
	public void removeProxyListener(ProxyListener listener) {
	    proxyServer.removeProxyListener(listener);
	    proxyServerSSL.removeProxyListener(listener);
	}
	/*
	 * reverse proxy를 리턴한다
	 */
    public boolean isReverseProxy() {
        return reverseProxy;
    }
    /*
     * reverse proxy 값을 세팅한다.
     */
    public void setReverseProxy(boolean reverseProxy) {
        this.reverseProxy = reverseProxy;
    }
    /*
     * reverseproxyhost를 리턴함
     */
    public String getReverseProxyHost() {
        return reverseProxyHost;
    }
    /*
     * reverseprosyhost 를 reverseproxyhost로 세팅한다
     */
    public void setReverseProxyHost(String reverseProxyHost) {
        this.reverseProxyHost = reverseProxyHost;
    }

    /*
     * enabblecacheprocessing을 enablecacheprocessing으로 세팅함
     */
    public void setEnableCacheProcessing(boolean enableCacheProcessing) {
        if (proxyServer != null) {
            proxyServer.setEnableCacheProcessing(enableCacheProcessing);
        }
        
        if (proxyServerSSL != null) {
            proxyServerSSL.setEnableCacheProcessing(enableCacheProcessing);
        }

    }
    
    public void addCacheProcessingList(CacheProcessingItem item) {
        if (proxyServer != null) {
            proxyServer.addCacheProcessingList(item);
        }

        if (proxyServerSSL != null) {
            proxyServerSSL.addCacheProcessingList(item);
        }

        
    }
}
