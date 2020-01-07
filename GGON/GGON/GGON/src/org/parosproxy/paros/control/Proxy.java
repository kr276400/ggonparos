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
		// ��ӿ� ���� ������
	}
	
	public void startServer() {
		// ���� ������ �ϴ� �κ��̿�
		// ����� ���Ͻ� ������ �ٸ� ���𰡶� , ����Ǵ� �������̽��� ������ �ϴ� Ŭ������

		// ���⿡ setProxyParam�� �Է������ν� �Ķ���Ͱ��� �ٽ� ������ �����鼭 ������� �� �� �־��
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
	 * reverse proxy�� �����Ѵ�
	 */
    public boolean isReverseProxy() {
        return reverseProxy;
    }
    /*
     * reverse proxy ���� �����Ѵ�.
     */
    public void setReverseProxy(boolean reverseProxy) {
        this.reverseProxy = reverseProxy;
    }
    /*
     * reverseproxyhost�� ������
     */
    public String getReverseProxyHost() {
        return reverseProxyHost;
    }
    /*
     * reverseprosyhost �� reverseproxyhost�� �����Ѵ�
     */
    public void setReverseProxyHost(String reverseProxyHost) {
        this.reverseProxyHost = reverseProxyHost;
    }

    /*
     * enabblecacheprocessing�� enablecacheprocessing���� ������
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
