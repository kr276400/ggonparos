package org.parosproxy.paros.extension;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AbstractPanel extends JPanel {

	public AbstractPanel() {
		super();
		initialize();
	}

	private  void initialize() {
		this.setSize(300,200);
	}

	public void setTabFocus() {
	    Component c = this.getParent();
	    if (c instanceof JTabbedPane) {
		    JTabbedPane tab = (JTabbedPane) c;
		    tab.setSelectedComponent(this);
	    }    
	}
}
