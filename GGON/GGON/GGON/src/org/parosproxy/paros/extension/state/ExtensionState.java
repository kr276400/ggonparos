package org.parosproxy.paros.extension.state;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpState;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.Session;

public class ExtensionState extends ExtensionAdaptor implements SessionChangedListener {

	private JCheckBoxMenuItem menuSessionTrackingEnable = null;

	private JMenuItem menuResetSessionState = null;

    public ExtensionState() {
        super();
 		initialize();
    }

    public ExtensionState(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionState");
			
	}
	
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    ExtensionHookView pv = extensionHook.getHookView();
	    extensionHook.getHookMenu().addEditMenuItem(extensionHook.getHookMenu().getMenuSeparator());
	    extensionHook.getHookMenu().addEditMenuItem(getMenuSessionTrackingEnable());
	    extensionHook.getHookMenu().addEditMenuItem(getMenuResetSessionState());
        
        
	}
  
	private JCheckBoxMenuItem getMenuSessionTrackingEnable() {
		if (menuSessionTrackingEnable == null) {
		    menuSessionTrackingEnable = new JCheckBoxMenuItem();
		    menuSessionTrackingEnable.setText("세션 트레킹 가능 (쿠키)");
			getMenuResetSessionState().setEnabled(menuSessionTrackingEnable.isSelected());

			menuSessionTrackingEnable.addItemListener(new java.awt.event.ItemListener() { 

				public void itemStateChanged(java.awt.event.ItemEvent e) {    

					getModel().getOptionsParam().getConnectionParam().setHttpStateEnabled(menuSessionTrackingEnable.isEnabled());
					getMenuResetSessionState().setEnabled(menuSessionTrackingEnable.isSelected());
			        resetSessionState();
				}
			});

		}
		return menuSessionTrackingEnable;
	}

	private JMenuItem getMenuResetSessionState() {
		if (menuResetSessionState == null) {
			menuResetSessionState = new JMenuItem();
			menuResetSessionState.setText("세션 상태 리셋");
			menuResetSessionState.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
				    if (getView().showConfirmDialog("세션 상태가 리셋됩니다.  계속 하시겠습니까?") == JOptionPane.OK_OPTION) {
				        resetSessionState();
				    }

				}
			});
		}
		return menuResetSessionState;
	}

    public void sessionChanged(Session session) {
        getModel().getOptionsParam().getConnectionParam().setHttpState(new HttpState());        
    }

    private void resetSessionState() {
        getModel().getOptionsParam().getConnectionParam().setHttpState(new HttpState());
    }


}