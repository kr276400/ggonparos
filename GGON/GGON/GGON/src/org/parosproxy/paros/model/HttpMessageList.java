package org.parosproxy.paros.model;

import java.util.Vector;

import org.parosproxy.paros.network.HttpMessage;

public class HttpMessageList {

    private Vector list = new Vector();
    public synchronized boolean add(HttpMessage msg) {
        return list.add(msg);
    }
    
    public synchronized int size() {
        return list.size();
    }
    
    public synchronized HttpMessage get(int i) {
        
        HttpMessage msg = null;
        try {
            msg = (HttpMessage) list.get(i);
        } catch (Exception e) {};
        return msg;
    }
    
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }
    
    
}
