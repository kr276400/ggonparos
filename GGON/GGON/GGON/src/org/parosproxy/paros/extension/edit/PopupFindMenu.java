package org.parosproxy.paros.extension.edit;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.ExtensionPopupMenu;

public class PopupFindMenu extends ExtensionPopupMenu {


    private JTextComponent lastInvoker = null;
    private JFrame parentFrame = null;
  
    public JTextComponent getLastInvoker() {
        return lastInvoker;
    }
   
	public PopupFindMenu() {
		super();
		initialize();
	}

	private void initialize() {
        this.setText("Ã£±â");


			
	}
    public boolean isEnableForComponent(Component invoker) {
        if (invoker instanceof JTextComponent) {
            setLastInvoker((JTextComponent) invoker);
            Container c = getLastInvoker().getParent();
            while (!(c instanceof JFrame)) {
                c = c.getParent();
            }
            setParentFrame((JFrame) c);
            return true;
        } else {
            setLastInvoker(null);
            return false;
        }

    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void setLastInvoker(JTextComponent lastInvoker) {
        this.lastInvoker = lastInvoker;
    }
    
}
