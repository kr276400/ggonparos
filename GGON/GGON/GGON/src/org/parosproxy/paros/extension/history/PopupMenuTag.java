package org.parosproxy.paros.extension.history;

import java.awt.Component;
import java.sql.SQLException;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

public class PopupMenuTag extends ExtensionPopupMenu {

    private ExtensionHistory extension = null;

    public PopupMenuTag() {
        super();
 		initialize();
    }

    public PopupMenuTag(String label) {
        super(label);
    }

	private void initialize() {
        this.setText("태그");

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
        	    
        	    JList listLog = extension.getLogPanel().getListLog();
        	    HistoryReference ref = (HistoryReference) listLog.getSelectedValue();
        	    HttpMessage msg = null;
        	    try {
                    msg = ref.getHttpMessage();
                    JTextField[] inputs = new JTextField[1];
                    inputs[0] = new JTextField(msg.getTag());
                    String tag = JOptionPane.showInputDialog(extension.getView().getMainFrame(), "옆에 나오는 메세지를 태그해주세요:", msg.getTag());
                    if (tag != null) {
                        ref.setTag(tag);
                        extension.getHistoryList().notifyItemChanged(ref);
                    }
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
