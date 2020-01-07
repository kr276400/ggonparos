package org.parosproxy.paros.view;

public class AboutWindow extends AbstractFrame {

	private AboutPanel aboutPanel = null;
    public AboutWindow() {
        super();
 		initialize();
    }

	private void initialize() {
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        this.setResizable(false);
        this.setContentPane(getAboutPanel());
        this.pack();
        centerFrame();
			
	}
  
	private AboutPanel getAboutPanel() {
		if (aboutPanel == null) {
			aboutPanel = new AboutPanel();
		}
		return aboutPanel;
	}
 }  
