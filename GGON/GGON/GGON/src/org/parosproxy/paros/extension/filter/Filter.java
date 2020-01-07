package org.parosproxy.paros.extension.filter;

import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.network.HttpMessage;

public interface Filter {

    public void init();
    
    public void initView(ViewDelegate view);

    public int getId();
    
    public String getName();

    public void onHttpRequestSend(HttpMessage httpMessage);

    public void onHttpResponseReceive(HttpMessage httpMessage);

    public void destroy();

    public boolean isEnabled();

    public void setEnabled(boolean enabled);
    
    public void timer();
    
    public boolean isPropertyExists();
    
    public void editProperty();
    
}
