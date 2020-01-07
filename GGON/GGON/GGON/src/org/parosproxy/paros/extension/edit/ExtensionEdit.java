package org.parosproxy.paros.extension.edit;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;

import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;
import org.parosproxy.paros.view.FindDialog;

public class ExtensionEdit extends ExtensionAdaptor {

    private FindDialog findDialog = null;
    private JMenuItem menuFind = null;
    private PopupFindMenu popupFindMenu = null;

    public ExtensionEdit() {
        super();
 		initialize();
    }

    public ExtensionEdit(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionEdit");
			
	}
	

	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);

	    if (getView() != null) {	        
	        extensionHook.getHookMenu().addEditMenuItem(getMenuFind());
	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuFind());
	        
	    }

	}
    
    private void showFindDialog(JFrame frame, JTextComponent lastInvoker) {
        if (findDialog == null || findDialog.getParent() != frame) {
            findDialog = new FindDialog(frame, false);            
        }
        
        findDialog.setLastInvoker(lastInvoker);
        findDialog.setVisible(true);
    }

    private JMenuItem getMenuFind() {
        if (menuFind == null) {
            menuFind = new JMenuItem();
            menuFind.setText("찾기");
            menuFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.Event.CTRL_MASK, false));

            menuFind.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showFindDialog(getView().getMainFrame(), null);
                }
            });
        }
        return menuFind;
    }

    private PopupFindMenu getPopupMenuFind() {
        if (popupFindMenu== null) {
            popupFindMenu = new PopupFindMenu();
            popupFindMenu.setText("찾기");
            popupFindMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showFindDialog(popupFindMenu.getParentFrame(), popupFindMenu.getLastInvoker());
                    
                }
            });
        }
        return popupFindMenu;
    }
	

         }
