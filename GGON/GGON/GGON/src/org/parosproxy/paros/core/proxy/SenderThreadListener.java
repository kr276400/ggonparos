package org.parosproxy.paros.core.proxy;
 
import org.parosproxy.paros.network.HttpMessage;

public interface SenderThreadListener {

	public void onMessageReceive(HttpMessage msg, Exception ex);
	// �޼��� ������ �޴� �κ��ε�?
	
}
