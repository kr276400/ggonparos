package org.parosproxy.paros.core.proxy;
 
import org.parosproxy.paros.network.HttpMessage;

public interface SenderThreadListener {

	public void onMessageReceive(HttpMessage msg, Exception ex);
	// 메세지 보낸거 받는 부분인듯?
	
}
