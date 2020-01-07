package org.parosproxy.paros.extension.history;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.util.Vector;

import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpStatusCode;
import org.parosproxy.paros.view.HttpPanel;
import org.parosproxy.paros.view.View;

public class LogPanel extends AbstractPanel implements Runnable {
	private javax.swing.JScrollPane scrollLog = null;
	private javax.swing.JList listLog = null;
	
	private HttpPanel requestPanel = null;
	private HttpPanel responsePanel = null;
    private ExtensionHistory extension = null;

	public LogPanel() {
		super();
		initialize();
	}

	private  void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(600, 200);
		this.add(getScrollLog(), java.awt.BorderLayout.CENTER);
	}
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }

	private javax.swing.JScrollPane getScrollLog() {
		if (scrollLog == null) {
			scrollLog = new javax.swing.JScrollPane();
			scrollLog.setViewportView(getListLog());
			scrollLog.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollLog.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollLog.setPreferredSize(new java.awt.Dimension(800,200));
			scrollLog.setName("scrollLog");
		}
		return scrollLog;
	}
   
	public javax.swing.JList getListLog() {
		if (listLog == null) {
			listLog = new javax.swing.JList();
			listLog.setDoubleBuffered(true);
            listLog.setCellRenderer(getLogPanelCellRenderer());
			listLog.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			listLog.setName("ListLog");
			listLog.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
			listLog.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
				    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
				        View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				        return;
				    }	
				    
				    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && e.getClickCount() > 1) {  // double click
						requestPanel.setTabFocus();
						return;
				    }
				}
			});
			
			listLog.addListSelectionListener(new javax.swing.event.ListSelectionListener() { 

				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				    if (listLog.getSelectedValue() == null) {
				        return;
				    }
                    
					final HistoryReference historyRef = (HistoryReference) listLog.getSelectedValue();

                    readAndDisplay(historyRef);

				}


			});

		}
		return listLog;
	}

    private Vector displayQueue = new Vector();
    private Thread thread = null;
    private LogPanelCellRenderer logPanelCellRenderer = null;  
    private void readAndDisplay(final HistoryReference historyRef) {

        synchronized(displayQueue) {
            if (!ExtensionHistory.isEnableForNativePlatform() || !extension.getBrowserDialog().isVisible()) {
                if (displayQueue.size()>0) {

                    displayQueue.clear();
                }
            }
            displayQueue.add(historyRef);

        }
        
        if (thread != null && thread.isAlive()) {
            return;
        }
        
        thread = new Thread(this);

        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }
    
    
    public void setDisplayPanel(HttpPanel requestPanel, HttpPanel responsePanel) {
        this.requestPanel = requestPanel;
        this.responsePanel = responsePanel;

    }
    
    private void displayMessage(HttpMessage msg) {
        
        if (msg.getRequestHeader().isEmpty()) {
            requestPanel.setMessage(null, true);
        } else {
            requestPanel.setMessage(msg, true);
        }
        
        if (msg.getResponseHeader().isEmpty()) {
            responsePanel.setMessage(null, false);
        } else {
            responsePanel.setMessage(msg, false);
        }
    }

    public void run() {
        HistoryReference ref = null;
        int count = 0;
        
        do {
            synchronized(displayQueue) {
                count = displayQueue.size();
                if (count == 0) {
                    break;
                }
                
                ref = (HistoryReference) displayQueue.get(0);
                displayQueue.remove(0);
            }
            
            try {
                final HistoryReference finalRef = ref;
                final HttpMessage msg = ref.getHttpMessage();
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        displayMessage(msg);
                        checkAndShowBrowser(finalRef, msg);
                        listLog.requestFocus();

                    }
                });
                
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        } while (true);
        
        
    }
    
    private void checkAndShowBrowser(HistoryReference ref, HttpMessage msg) {
        if (!ExtensionHistory.isEnableForNativePlatform() || !extension.getBrowserDialog().isVisible()) {
            return;
        }
        
        extension.browserDisplay(ref, msg);
    }

    private LogPanelCellRenderer getLogPanelCellRenderer() {
        if (logPanelCellRenderer == null) {
            logPanelCellRenderer = new LogPanelCellRenderer();
            logPanelCellRenderer.setSize(new java.awt.Dimension(328,21));
            logPanelCellRenderer.setBackground(java.awt.Color.white);
            logPanelCellRenderer.setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.PLAIN, 12));
        }
        return logPanelCellRenderer;
    }

    
}

