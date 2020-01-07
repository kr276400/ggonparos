package org.parosproxy.paros.extension;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.model.Session;

public interface Extension {
    
    public String getName();

    public void init();

    public void initModel(Model model);

    public void initView(ViewDelegate view);
    
    public Model getModel();

    public ViewDelegate getView();

    public void start();

    public void stop();

    public void destroy();

    public void initXML(Session session, OptionsParam options);
    
    public void hook(ExtensionHook pluginHook);
    
}
