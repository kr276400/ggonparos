package org.parosproxy.paros.extension;

import org.parosproxy.paros.view.HttpPanel;
import org.parosproxy.paros.view.MainFrame;
import org.parosproxy.paros.view.MainPopupMenu;
import org.parosproxy.paros.view.SiteMapPanel;
import org.parosproxy.paros.view.WaitMessageDialog;

public interface ViewDelegate {

    public MainFrame getMainFrame();
    
    public SiteMapPanel getSiteTreePanel();
        
    public int showConfirmDialog(String msg);

    public int showYesNoCancelDialog(String msg);
    
    public void showWarningDialog(String msg);
    
    public void showMessageDialog(String msg);
    
    public WaitMessageDialog getWaitMessageDialog(String msg);
    
    public MainPopupMenu getPopupMenu();
    
    public void setStatus(String msg);
    
    public HttpPanel getRequestPanel();
    
    public HttpPanel getResponsePanel();
    
}
