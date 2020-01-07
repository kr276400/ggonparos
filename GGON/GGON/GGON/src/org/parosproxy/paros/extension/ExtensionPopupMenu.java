package org.parosproxy.paros.extension;

import java.awt.Component;

import javax.swing.JMenuItem;

public class ExtensionPopupMenu extends JMenuItem {

    public ExtensionPopupMenu() {
        super();
    }
    
    public ExtensionPopupMenu(String label) {
        super(label);
    }
    
    public boolean isEnableForComponent(Component invoker) {
        return true;
    }
}
