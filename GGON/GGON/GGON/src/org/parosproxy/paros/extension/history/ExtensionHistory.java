package org.parosproxy.paros.extension.history;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.control.Proxy;
import org.parosproxy.paros.core.proxy.CacheProcessingItem;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.HistoryList;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class ExtensionHistory extends ExtensionAdaptor implements SessionChangedListener {

    private static final int FILTER_NONE = 0;
    private static final int FILTER_REQUEST = 1;
    private static final int FILTER_RESPONSE = 2;
    
	private LogPanel logPanel = null; 
	private ProxyListenerLog proxyListener = null;
	private HistoryList historyList = null;
    private String filter = "";
    
    private EmbeddedBrowser browser = null;
    private static BrowserDialog browserDialog = null;
    	
	private HistoryFilterDialog filterDialog = null;
	private JCheckBoxMenuItem menuFilterHistoryByRequest = null;  
	private JCheckBoxMenuItem menuFilterHistoryByResponse = null;
	private int stateFilter = FILTER_NONE;

	
	private PopupMenuDeleteHistory popupMenuDeleteHistory = null;
	private PopupMenuPurgeHistory popupMenuPurgeHistory = null;
	private PopupMenuResend popupMenuResend = null;
	private ManualRequestEditorDialog resendDialog = null;

    private PopupMenuEmbeddedBrowser popupMenuEmbeddedBrowser = null;
    private PopupMenuEmbeddedBrowser popupMenuEmbeddedBrowser2 = null;
    private PopupMenuTag popupMenuTag = null;

    public ExtensionHistory() {
        super();
 		initialize();
    }

    public ExtensionHistory(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionHistory");
			
	}
 
	LogPanel getLogPanel() {
		if (logPanel == null) {
			logPanel = new LogPanel();
			logPanel.setName("기록");
            logPanel.setExtension(this);
		}
		return logPanel;
	}
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
        extensionHook.addSessionListener(this);
        extensionHook.addProxyListener(getProxyListenerLog());

	    if (getView() != null) {
		    ExtensionHookView pv = extensionHook.getHookView();
		    pv.addStatusPanel(getLogPanel());

		    getLogPanel().setDisplayPanel(getView().getRequestPanel(), getView().getResponsePanel());
		    

            extensionHook.getHookMenu().addViewMenuItem(extensionHook.getHookMenu().getMenuSeparator());

	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuResend());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuTag());

            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuEmbeddedBrowser());

	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuDeleteHistory());
	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuPurgeHistory());

            if (isEnableForNativePlatform()) {
                getBrowserDialog();
            }
	    }

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
	    

	    getHistoryList().clear();
	    getLogPanel().getListLog().setModel(getHistoryList());
		getView().getRequestPanel().setMessage("","", true);
		getView().getResponsePanel().setMessage("","", false);

		try {
		    List list = getModel().getDb().getTableHistory().getHistoryList(session.getSessionId(), HistoryReference.TYPE_MANUAL);

		    buildHistory(getHistoryList(), list);
		} catch (SQLException e) {}
	    
	}
	
	
	private ProxyListenerLog getProxyListenerLog() {
        if (proxyListener == null) {
            proxyListener = new ProxyListenerLog(getModel(), getView(), getHistoryList());
        }
        return proxyListener;
	}
	
	public HistoryList getHistoryList() {
	    if (historyList == null) {
	        historyList = new HistoryList();
	    }
	    return historyList;
	}
	
	private void searchHistory(String filter, boolean isRequest) {
	    Session session = getModel().getSession();
        
	    synchronized (historyList) {
	        try {
	            List list = getModel().getDb().getTableHistory().getHistoryList(session.getSessionId(), HistoryReference.TYPE_MANUAL, filter, isRequest);
	            
	            buildHistory(getHistoryList(), list);
	        } catch (SQLException e) {}
	    }

	    
	}
	
	private void buildHistory(HistoryList historyList, List dbList) {

	    HistoryReference historyRef = null;
	    synchronized (historyList) {
	        historyList.clear();
	        
	        for (int i=0; i<dbList.size(); i++) {
	            int historyId = ((Integer) dbList.get(i)).intValue();

	            try {
	                    historyRef = new HistoryReference(historyId);
	                    historyList.addElement(historyRef);
	            } catch (Exception e) {};
	        }
	    }

   }

	private HistoryFilterDialog getFilterDialog() {
		if (filterDialog == null) {
			filterDialog = new HistoryFilterDialog(getView().getMainFrame(), true);
		}
		return filterDialog;
	}
  
	private int showFilterDialog(boolean isRequest) {
		HistoryFilterDialog dialog = getFilterDialog();
		dialog.setModal(true);
		int exit = dialog.showDialog();
		int result = 0;		
		if (exit == JOptionPane.OK_OPTION) {
		    filter = dialog.getPattern();
		    getProxyListenerLog().setFilter(filter);
		    searchHistory(filter, isRequest);
		    result = 1;		
		    
		} else if (exit == JOptionPane.NO_OPTION) {
		    filter = "";
		    getProxyListenerLog().setFilter(filter);
		    searchHistory(filter, isRequest);
		    result = -1;	
		}
		
		return result;
	}

	private PopupMenuDeleteHistory getPopupMenuDeleteHistory() {
		if (popupMenuDeleteHistory == null) {
			popupMenuDeleteHistory = new PopupMenuDeleteHistory();
			popupMenuDeleteHistory.setExtension(this);
		}
		return popupMenuDeleteHistory;
	}
   
	private PopupMenuPurgeHistory getPopupMenuPurgeHistory() {
		if (popupMenuPurgeHistory == null) {
			popupMenuPurgeHistory = new PopupMenuPurgeHistory();
			popupMenuPurgeHistory.setExtension(this);

		}
		return popupMenuPurgeHistory;
	}
 
	private PopupMenuResend getPopupMenuResend() {
		if (popupMenuResend == null) {
			popupMenuResend = new PopupMenuResend();
			popupMenuResend.setExtension(this);
		}
		return popupMenuResend;
	}

	ManualRequestEditorDialog getResendDialog() {
		if (resendDialog == null) {
			resendDialog = new ManualRequestEditorDialog(getView().getMainFrame(), false, false, this);
			resendDialog.setSize(500, 600);
			resendDialog.setTitle("다시 보내기");
		}
		return resendDialog;
	}

    private PopupMenuEmbeddedBrowser getPopupMenuEmbeddedBrowser() {
        if (popupMenuEmbeddedBrowser == null) {
            popupMenuEmbeddedBrowser = new PopupMenuEmbeddedBrowser();
            popupMenuEmbeddedBrowser.setExtension(this);

        }
        return popupMenuEmbeddedBrowser;
    }

    private PopupMenuEmbeddedBrowser getPopupMenuEmbeddedBrowser2() {
        if (popupMenuEmbeddedBrowser2 == null) {
            popupMenuEmbeddedBrowser2 = new PopupMenuEmbeddedBrowser();
            popupMenuEmbeddedBrowser2.setExtension(this);

        }
        return popupMenuEmbeddedBrowser2;
    }

    private PopupMenuTag getPopupMenuTag() {
        if (popupMenuTag == null) {
            popupMenuTag = new PopupMenuTag();
            popupMenuTag.setExtension(this);

        }
        return popupMenuTag;
    }
    
    boolean browserDisplay(HistoryReference ref, HttpMessage msg) {
        
        boolean isShow = false;
        String contentType = msg.getResponseHeader().getHeader(HttpHeader.CONTENT_TYPE);
        if (contentType != null) {
            if (contentType.indexOf("text/html") >= 0 || contentType.indexOf("text/plain") >= 0) {
                isShow = true;
            } else if (msg.getResponseHeader().isImage()) {
                isShow = true;
            } else if (contentType.indexOf("application/pdf") >= 0)  {
                isShow = true;
            }
        }

        if (!isShow) {
            return isShow;
        }
        
        try {
            if (!getBrowserDialog().isVisible()) {
                getBrowserDialog().setVisible(true);
            }
            
            browser = getBrowserDialog().getEmbeddedBrowser();
            browser.stop();
            browser.setVisible(true);
            CacheProcessingItem item = new CacheProcessingItem(ref, msg);
            Proxy proxy = Control.getSingleton().getProxy();
            proxy.setEnableCacheProcessing(true);
            proxy.addCacheProcessingList(item);
            
            getBrowserDialog().setURLTitle(msg.getRequestHeader().getURI().toString());
            if (msg.getRequestHeader().getMethod().equalsIgnoreCase(HttpRequestHeader.POST)) {
                browser.setURL(new java.net.URL(msg.getRequestHeader().getURI().toString()), msg.getRequestBody().toString());
            } else {
                browser.setURL(new java.net.URL(msg.getRequestHeader().getURI().toString()));
            }
        } catch (Exception e) {
            
        }
        
        return isShow;
    }
 
    BrowserDialog getBrowserDialog() {
        if (browserDialog == null) {
            browserDialog = new BrowserDialog(getView().getMainFrame(), false);
        }
        return browserDialog;
    }
    
    private static Pattern patternWindows = Pattern.compile("window", Pattern.CASE_INSENSITIVE);

    static boolean isEnableForNativePlatform() {
      String os_name = System.getProperty("os.name");
      Matcher matcher = patternWindows.matcher(os_name);
      if (matcher.find()) {
          return true;
      }
      
      return false;

  }  
         }
