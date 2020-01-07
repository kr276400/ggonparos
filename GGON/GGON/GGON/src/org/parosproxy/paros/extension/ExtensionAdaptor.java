package org.parosproxy.paros.extension;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.model.Session;

public class ExtensionAdaptor implements Extension {

    private String name = this.getClass().getName();
    private Model model = null;
    private ViewDelegate view = null;
    private ExtensionHook extensionHook = null;
    
    public ExtensionAdaptor() {
    }

    public ExtensionAdaptor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void init() {
      
    }
    public void initModel(Model model) {
        this.model = model;
    }
    
    public void initView(ViewDelegate view) {
        this.view = view;
    }
    
    public void initXML(Session session, OptionsParam options) {
    }

    public ExtensionHookView getExtensionView() {
        return null;
    }
    public ExtensionHookMenu getExtensionMenu() {
        return null;
    }
    
    public void start() {
        
    }
    
    public void stop() {
        
    }

    public void destroy() {
    }
    
    public ViewDelegate getView() {
        return view;
    }
    
    public Model getModel() {
        return model;
    }

    public void hook(ExtensionHook extensionHook) {
        this.extensionHook = extensionHook;
    }

    
}
