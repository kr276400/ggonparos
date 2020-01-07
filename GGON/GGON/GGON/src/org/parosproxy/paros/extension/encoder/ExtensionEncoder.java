package org.parosproxy.paros.extension.encoder;

import javax.swing.JMenuItem;

import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookMenu;

public class ExtensionEncoder extends ExtensionAdaptor {

	private EncoderDialog encoderDialog = null;  
	private JMenuItem menuItemEncoder = null;
	private ExtensionHookMenu extensionMenu = null;

    public ExtensionEncoder() {
        super();
 		initialize();
   }   

	private void initialize() {
        this.setName("ExtensionEncoder");
			
	}
  
	private EncoderDialog getEncoderDialog() {
		if (encoderDialog == null) {
	        encoderDialog = new EncoderDialog(getView().getMainFrame(), false);
	        encoderDialog.setView(getView());
	        encoderDialog.setSize(480, 360);

		}
		return encoderDialog;
	}

	private JMenuItem getMenuItemEncoder() {
		if (menuItemEncoder == null) {
			menuItemEncoder = new JMenuItem();
			menuItemEncoder.setText("엔코더/해쉬");
			menuItemEncoder.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					getEncoderDialog().setVisible(true);
					
				}
			});

		}
		return menuItemEncoder;
	}
	
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuItemEncoder());
	    }
	}
  }
