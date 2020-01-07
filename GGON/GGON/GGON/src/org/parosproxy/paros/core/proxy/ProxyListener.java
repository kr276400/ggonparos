package org.parosproxy.paros.core.proxy;
 
import org.parosproxy.paros.network.HttpMessage;

public interface ProxyListener {

	public void onHttpRequestSend(HttpMessage msg);
	public void onHttpResponseReceive(HttpMessage msg);
	
}
