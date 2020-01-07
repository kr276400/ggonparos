package org.parosproxy.paros.extension.filter;

import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.network.HttpMessage;

abstract public class FilterAdaptor implements Filter {

    private boolean enabled = false;
    private ViewDelegate view = null;
    
    public void init() {
    }
    
    public void initView(ViewDelegate view) {
        this.view = view;
    }

    abstract public int getId();
    
    abstract public String getName();

    abstract public void onHttpRequestSend(HttpMessage httpMessage);

    abstract public void onHttpResponseReceive(HttpMessage httpMessage);
 
    public void destroy() {
        
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void timer() {
        
    }
    
    public boolean isPropertyExists() {
        return false;
    }
    
    public void editProperty() {
    }
    
    public ViewDelegate getView() {
        return view;
    }
}
