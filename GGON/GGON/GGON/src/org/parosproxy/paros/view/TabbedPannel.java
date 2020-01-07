package org.parosproxy.paros.view;

import javax.swing.JTabbedPane;

public class TabbedPannel extends JTabbedPane {

    private java.awt.Container originalParent = null;
    private java.awt.Container alternativeParent = null;
    private java.awt.Component backupChild = null;

	public TabbedPannel() {
		super();
		initialize();
	}

	private  void initialize() {
		this.setSize(225, 145);
		this.addMouseListener(new java.awt.event.MouseAdapter() { 

			public void mouseClicked(java.awt.event.MouseEvent e) {    

			    if (e.getClickCount() >= 2) {
			        alternateParent();
			    }

			}
		});

	}
	
	public void setAlternativeParent(java.awt.Container alternativeParent) {
	    this.alternativeParent = alternativeParent;
	}
	
	private boolean isAlternative = true;
	
	private void alternateParent() {
	    if (alternativeParent == null) return;

	    if (isAlternative) {
	        
	        originalParent = this.getParent();
	        originalParent.remove(this);
	        backupChild = alternativeParent.getComponent(0);
	        alternativeParent.remove(backupChild);
	        alternativeParent.add(this, "");
	    } else {
	        alternativeParent.remove(this);
	        alternativeParent.add(backupChild, "");
	        originalParent.add(this, "");
	    }
        originalParent.validate();
        alternativeParent.validate();
        this.validate();
	    isAlternative = !isAlternative;
	}
	

} 
