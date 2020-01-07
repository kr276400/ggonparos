package org.parosproxy.paros.extension.history;
 
import java.awt.EventQueue;
import java.util.regex.Pattern;

import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.model.HistoryList;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpStatusCode;


public class ProxyListenerLog implements ProxyListener {
    
	private ViewDelegate view = null;
	private Model model = null;
	private HistoryList historyList = null;
	private Pattern pattern = null;
	private boolean isFirstAccess = true;
	
	public ProxyListenerLog(Model model, ViewDelegate view, HistoryList historyList) {
	    this.model = model;
	    this.view = view;
	    this.historyList = historyList;
	}

	public void setFilter(String filter) {
	    if (filter == null || filter.equals("")) {
	        pattern = null;
	    } else {
	        pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	    }
	}

	public void onHttpRequestSend(HttpMessage msg) {

	    HttpMessage existingMsg = model.getSession().getSiteTree().pollPath(msg);

	    if (existingMsg != null && !existingMsg.getResponseHeader().isEmpty()) {
	        if (HttpStatusCode.isSuccess(existingMsg.getResponseHeader().getStatusCode())) {
	            return;
	        }
	    }
        
	    if (msg.getRequestHeader().getHeader(HttpHeader.IF_MODIFIED_SINCE) != null) {
	        msg.getRequestHeader().setHeader(HttpHeader.IF_MODIFIED_SINCE, null);
	    }
	    
	    if (msg.getRequestHeader().getHeader(HttpHeader.IF_NONE_MATCH) != null) {
	        msg.getRequestHeader().setHeader(HttpHeader.IF_NONE_MATCH, null);
	    }

	}

	public void onHttpResponseReceive(final HttpMessage msg) {

        int type = HistoryReference.TYPE_MANUAL;
		if (isSkipImage(msg.getRequestHeader()) || isSkipImage(msg.getResponseHeader())) {
            if (msg.getResponseHeader().getStatusCode() == HttpStatusCode.OK) {
                type = HistoryReference.TYPE_HIDDEN;
            } else {
                return;
            }
		}
		final int finalType = type;
		Thread t = new Thread(new Runnable() {
		    public void run() {
		        addHistory(msg, finalType);
		    }
		});
		t.start();
				

	}
	    
    public boolean isSkipImage(HttpHeader header) {
		if (header.isImage() && !model.getOptionsParam().getViewParam().isProcessImages()) {
			return true;
		}
			
		return false;
				
	}
    
    private void addHistory(HttpMessage msg, int type) {
        
        HistoryReference historyRef = null;

        try {
            historyRef = new HistoryReference(model.getSession(), type, msg);
        } catch (Exception e) {
            return;
        }

        if (type != HistoryReference.TYPE_MANUAL && type != HistoryReference.TYPE_HIDDEN) {
            return;
        }

        synchronized(historyList) {
            if (type == HistoryReference.TYPE_MANUAL) {
                
                if (pattern == null) {
                    addHistoryInEventQueue(historyRef);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append(msg.getRequestHeader().toString());
                    sb.append(msg.getRequestBody().toString());
                    if (!msg.getResponseHeader().isEmpty()) {
                        sb.append(msg.getResponseHeader().toString());
                        sb.append(msg.getResponseBody().toString());
                        
                    }
                    if (pattern.matcher(sb.toString()).find()) {
                        addHistoryInEventQueue(historyRef);
                    }
                }
            }
        }

        final HistoryReference ref = historyRef;
        final HttpMessage finalMsg = msg;
        if (EventQueue.isDispatchThread()) {
            model.getSession().getSiteTree().addPath(ref, msg);
            if (isFirstAccess) {
                isFirstAccess = false;
                view.getSiteTreePanel().expandRoot();
            }
            
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        model.getSession().getSiteTree().addPath(ref, finalMsg);
                        if (isFirstAccess) {
                            isFirstAccess = false;
                            view.getSiteTreePanel().expandRoot();
                        }
                    }
                });
            } catch (Exception e) {
            }        
            
        }
    }
    
    private void addHistoryInEventQueue(final HistoryReference ref) {
        if (EventQueue.isDispatchThread()) {
            historyList.addElement(ref);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        historyList.addElement(ref);
                    }
                    
                });
            } catch (Exception e) {
            }
        }
    }
}
