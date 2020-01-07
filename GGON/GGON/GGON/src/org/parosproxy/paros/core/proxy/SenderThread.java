
package org.parosproxy.paros.core.proxy;

import java.util.Vector;

import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

public class SenderThread implements Runnable {
    
    private HttpMessage msg = null;
    private HttpSender httpSender = null;
    private Vector listenerList = null;
    public SenderThread(HttpSender httpSender, HttpMessage msg, SenderThreadListener listener) {
        this.httpSender = httpSender;
        this.msg = msg;
        listenerList = new Vector(1);
        listenerList.add(listener);
    }   
    
    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        // �ٸ� �Ϲ� �������� �۾��� ���� �������� ������ �ϴ� ����. �����, ���� �����尡 �ƴ� �Ϲ� �����常.
    }
    
    public void run() {
        Exception ex = null;
        try {
            getHttpSender().sendAndReceive(getHttpMessage());
            
        } catch (Exception e) {
            ex = e;
        }
        notifyListener(getHttpMessage(), ex);
    }

    private void notifyListener(HttpMessage msg, Exception ex) {
        for (int i=0; i<listenerList.size(); i++) {
            SenderThreadListener listener = (SenderThreadListener) listenerList.get(i);
            listener.onMessageReceive(msg, ex);
        }
    }
    /*
     * �޼��� msg ������
     */
    public HttpMessage getHttpMessage() {
        return msg;
    }
    /*
     * sender ������
     */
    public HttpSender getHttpSender() {
        return httpSender;
    }
}
