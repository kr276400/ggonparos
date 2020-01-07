package org.parosproxy.paros.extension.history;

import java.awt.Component;
import java.sql.SQLException;

import javax.swing.JList;
import javax.swing.JTree;

import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

public class PopupMenuResend extends ExtensionPopupMenu {

    private ExtensionHistory extension = null;
    private JTree treeSite = null;
    private HttpSender httpSender = null;

    public PopupMenuResend() {
        super();
 		initialize();
    }

    public PopupMenuResend(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("다시 보내기");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
        	    
        	    ManualRequestEditorDialog dialog = extension.getResendDialog();
        	    
        	    JList listLog = extension.getLogPanel().getListLog();
        	    HistoryReference ref = (HistoryReference) listLog.getSelectedValue();
        	    HttpMessage msg = null;
        	    try {
                    msg = ref.getHttpMessage().cloneRequest();
                    dialog.setMessage(msg);
                    dialog.setVisible(true);
                } catch (HttpMalformedHeaderException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
  
        	}
        });
		
	}
	
    public boolean isEnableForComponent(Component invoker) {
        if (invoker.getName() != null && invoker.getName().equals("ListLog")) {
            try {
                JList list = (JList) invoker;
                if (list.getSelectedIndex() >= 0) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
            } catch (Exception e) {}
            return true;
        }
        return false;
    }
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
    

	
}
