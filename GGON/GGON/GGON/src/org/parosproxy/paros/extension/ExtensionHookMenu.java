package org.parosproxy.paros.extension;

import java.util.List;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ExtensionHookMenu {
    
    public static final JMenuItem MENU_SEPARATOR = new JMenuItem();
    public static final ExtensionPopupMenu POPUP_MENU_SEPARATOR = new ExtensionPopupMenu();
    
    private Vector newMenuList = new Vector();
    private Vector fileMenuItemList = new Vector();
    private Vector editMenuItemList = new Vector();
    private Vector viewMenuItemList = new Vector();
    private Vector analyseMenuItemList = new Vector();
    private Vector toolsMenuItemList = new Vector();
    private Vector popupMenuList = new Vector();
    
    List getNewMenus() {
        return newMenuList;
    }

    List getFile() {
        return fileMenuItemList;
    }

    List getEdit() {
        return editMenuItemList;
    }

    List getView() {
        return viewMenuItemList;
    }

    List getAnalyse() {
        return analyseMenuItemList;
    }
    
    List getTools() {
        return toolsMenuItemList;
    }

    List getPopupMenus() {
        return popupMenuList;
    }

    public void addFileMenuItem(JMenuItem menuItem) {
        getFile().add(menuItem);
    }

    public void addEditMenuItem(JMenuItem menuItem) {
        getEdit().add(menuItem);
    }

    public void addViewMenuItem(JMenuItem menuItem) {
        getView().add(menuItem);
    }

    public void addAnalyseMenuItem(JMenuItem menuItem) {
        getAnalyse().add(menuItem);
    }

    public void addToolsMenuItem(JMenuItem menuItem) {
        getTools().add(menuItem);
    }
    


    public void addNewMenu(JMenu menu) {
        getNewMenus().add(menu);
    }

    public void addPopupMenuItem(ExtensionPopupMenu menuItem) {
        getPopupMenus().add(menuItem);        
    }
    
    public JMenuItem getMenuSeparator() {
        return MENU_SEPARATOR;
    }

    public ExtensionPopupMenu getPopupMenuSeparator() {
        return POPUP_MENU_SEPARATOR;
    }
}
