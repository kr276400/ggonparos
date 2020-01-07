package org.parosproxy.paros.view;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.parosproxy.paros.extension.ExtensionHookMenu;
import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.extension.edit.PopupFindMenu;

public class MainPopupMenu extends JPopupMenu {

    private List itemList = null;
	private PopupDeleteMenu popupDeleteMenu = null;
	private PopupPurgeMenu popupPurgeMenu = null;

    public MainPopupMenu() {
        super();
 		initialize();
   }

    public MainPopupMenu(String arg0) {
        super(arg0);
    }
    
    public MainPopupMenu(List itemList) {
        this();
        this.itemList = itemList;
    }

	private void initialize() {

        this.add(getPopupDeleteMenu());
        this.add(getPopupPurgeMenu());
	}
	
	public synchronized void show(Component invoker, int x, int y) {
	    
	    boolean isFirst = true;
	    ExtensionPopupMenu menu = null;
	    
	    for (int i=0; i<getComponentCount(); i++) {
	        try {
	            if (getComponent(i) != null && getComponent(i) instanceof ExtensionPopupMenu) {
	                menu = (ExtensionPopupMenu) getComponent(i);
	                menu.setVisible(menu.isEnableForComponent(invoker) && menu.isEnabled());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    for (int i=0; i<itemList.size(); i++) {
	        menu = (ExtensionPopupMenu) itemList.get(i);
	        try {
	            if (menu == ExtensionHookMenu.POPUP_MENU_SEPARATOR) {
	                this.addSeparator();
	                continue;
	            }
	            
	            if (menu.isEnableForComponent(invoker)) {		
	                this.add(menu);
	                isFirst = false;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    super.show(invoker, x, y);
	}

	private JMenuItem getPopupDeleteMenu() {
		if (popupDeleteMenu == null) {
			popupDeleteMenu = new PopupDeleteMenu();
		}
		return popupDeleteMenu;
	}

	private PopupPurgeMenu getPopupPurgeMenu() {
		if (popupPurgeMenu == null) {
			popupPurgeMenu = new PopupPurgeMenu();
		}
		return popupPurgeMenu;
	}
    }

