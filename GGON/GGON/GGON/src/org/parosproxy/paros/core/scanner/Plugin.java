package org.parosproxy.paros.core.scanner;

import org.apache.commons.configuration.Configuration;
import org.parosproxy.paros.network.HttpMessage;

public interface Plugin extends Runnable {

    public int getId();

    public String getName();

    public String getCodeName(); // 플러그인 코드 네임 얻어오기

    public String getDescription(); // 기본 값 지정
    
    public void init(HttpMessage msg, HostProcess parent);
    
    public void scan();

    public String[] getDependency();

    public void setEnabled(boolean enabled);

    public boolean isEnabled();

    public int getCategory();

    public String getSolution();

    public String getReference();

    public void notifyPluginCompleted(HostProcess parent);

    public boolean isVisible();

    public void setConfig(Configuration config);
    
    public Configuration getConfig();
    
    public void createParamIfNotExist();
}
