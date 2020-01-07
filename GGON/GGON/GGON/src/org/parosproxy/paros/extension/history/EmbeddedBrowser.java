package org.parosproxy.paros.extension.history;

import org.jdesktop.jdic.browser.WebBrowser;
import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListener;
import org.parosproxy.paros.control.Control;

public class EmbeddedBrowser extends WebBrowser implements WebBrowserListener {

    PopupMenuEmbeddedBrowser menu = null;
    
	public EmbeddedBrowser() {
		super();
		initialize();
	}

    void setPopupMenuEmbeddedBrowser(PopupMenuEmbeddedBrowser menu) {
        this.menu = menu;
    }
    
    private void initialize() {
        addWebBrowserListener(this);
        
    }

    public void downloadStarted(WebBrowserEvent event) {

    }

    public void downloadCompleted(WebBrowserEvent event) {
        
    }

    public void downloadProgress(WebBrowserEvent event) {
    }

    public void downloadError(WebBrowserEvent event) {
        
    }

    public void documentCompleted(WebBrowserEvent event) {
        Control.getSingleton().getProxy().setEnableCacheProcessing(false);
    }

    public void titleChange(WebBrowserEvent event) {
        
    }

    public void statusTextChange(WebBrowserEvent event) {
        
    }

}
