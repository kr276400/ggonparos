package org.parosproxy.paros.extension.trap;

import java.awt.EventQueue;

import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.Session;


public class ExtensionTrap extends ExtensionAdaptor implements SessionChangedListener {

    
	private TrapPanel trapPanel = null;  
	private ProxyListenerTrap proxyListener = null;
    		

	private OptionsTrapPanel optionsTrapPanel = null;
	private TrapParam trapParam = null;   

    public ExtensionTrap() {
        super();
 		initialize();
    }

    public ExtensionTrap(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionTrap");
			
	}

	TrapPanel getTrapPanel() {
		if (trapPanel == null) {
		    trapPanel = new TrapPanel();
		    trapPanel.setName("Æ®·¦ ºÎºÐ");
		}
		return trapPanel;
	}
	
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        ExtensionHookView pv = extensionHook.getHookView();
	        pv.addWorkPanel(getTrapPanel());
	        pv.addOptionPanel(getOptionsTrapPanel());
	    }

        extensionHook.addOptionsParamSet(getTrapParam());
        
        extensionHook.addProxyListener(getProxyListenerTrap());
        extensionHook.addSessionListener(this);
        

	}
	
	public void sessionChanged(final Session session)  {
	    if (EventQueue.isDispatchThread()) {
		    sessionChangedEventHandler(session);

	    } else {
	        
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	        		    sessionChangedEventHandler(session);
	                }
	            });
	        } catch (Exception e) {
	            
	        }
	    }
	    
	}
	
	private void sessionChangedEventHandler(Session session) {

	    getTrapPanel().setMessage("","", false);
	    
	    
	}
	
	
	private ProxyListenerTrap getProxyListenerTrap() {
        if (proxyListener == null) {
            proxyListener = new ProxyListenerTrap(getModel(), getTrapParam());
            proxyListener.setTrapPanel(getTrapPanel());

        }
        return proxyListener;
	}

	private OptionsTrapPanel getOptionsTrapPanel() {
		if (optionsTrapPanel == null) {
			optionsTrapPanel = new OptionsTrapPanel();
		}
		return optionsTrapPanel;
	}
 
	private TrapParam getTrapParam() {
		if (trapParam == null) {
			trapParam = new TrapParam();
		}
		return trapParam;
	}
  }