package org.parosproxy.paros.core.scanner;

import org.apache.commons.configuration.Configuration;
import org.parosproxy.paros.network.HttpMessage;

public interface Plugin extends Runnable {

    public int getId();

    public String getName();

    public String getCodeName(); // �÷����� �ڵ� ���� ������

    public String getDescription(); // �⺻ �� ����
    
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
