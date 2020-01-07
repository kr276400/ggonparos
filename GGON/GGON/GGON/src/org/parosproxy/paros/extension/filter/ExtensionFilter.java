package org.parosproxy.paros.extension.filter;

import javax.swing.JMenuItem;

import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.network.HttpMessage;

public class ExtensionFilter extends ExtensionAdaptor implements ProxyListener, Runnable {

	private JMenuItem menuToolsFilter = null;
	private FilterFactory filterFactory = new FilterFactory();
	private boolean isStop = false;

    public ExtensionFilter() {
        super();
    }


    
    public void init() {
        Filter filter = null;
        for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
            filter = (Filter) filterFactory.getAllFilter().get(i);
            try {
                filter.init();
            } catch (Exception e) {}
        }
        
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void initView(ViewDelegate view) {
        super.initView(view);
        Filter filter = null;
        for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
            filter = (Filter) filterFactory.getAllFilter().get(i);
            try {
                filter.initView(view);
            } catch (Exception e) {}
        }
    }
  
	private JMenuItem getMenuToolsFilter() {
		if (menuToolsFilter == null) {
			menuToolsFilter = new JMenuItem();
			menuToolsFilter.setText("ÇÊÅÍ");
			menuToolsFilter.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					FilterDialog dialog = new FilterDialog(getView().getMainFrame());
				    dialog.setAllFilters(filterFactory.getAllFilter());
				    dialog.showDialog(false);
				}
			});

		}
		return menuToolsFilter;
	}

	
	public void hook(ExtensionHook extensionHook) {
	    if (getView() != null) {
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuToolsFilter());
	    }
	    extensionHook.addProxyListener(this);
	}

    public void onHttpRequestSend(HttpMessage httpMessage) {
        Filter filter = null;
        for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
            filter = (Filter) filterFactory.getAllFilter().get(i);
            try {
                if (filter.isEnabled()) {
                    filter.onHttpRequestSend(httpMessage);
                }
            } catch (Exception e) {}
        }
        
    }

    public void onHttpResponseReceive(HttpMessage httpMessage) {
        Filter filter = null;
        for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
            filter = (Filter) filterFactory.getAllFilter().get(i);
            try {
                if (filter.isEnabled()) {

                    filter.onHttpResponseReceive(httpMessage);
                }
            } catch (Exception e) {}
        }
        
    }

    public void destroy() {
        isStop = true;
        Filter filter = null;
        for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
            filter = (Filter) filterFactory.getAllFilter().get(i);
            try {
                filter.destroy();
            } catch (Exception e) {}
        }
        
        
    }

    public void run() {
        Filter filter = null;
        
        while (!isStop) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
            }
            for (int i=0; i<filterFactory.getAllFilter().size(); i++) {
                filter = (Filter) filterFactory.getAllFilter().get(i);
                try {
                    if (filter.isEnabled()) {
                        filter.timer();
                    }
                } catch (Exception e) {}
            }
        }
        
    }
    
  }
