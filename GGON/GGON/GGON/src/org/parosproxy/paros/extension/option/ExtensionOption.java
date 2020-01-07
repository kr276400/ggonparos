package org.parosproxy.paros.extension.option;

import javax.swing.JCheckBoxMenuItem;

import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;

public class ExtensionOption extends ExtensionAdaptor {

	private JCheckBoxMenuItem menuViewImage = null;
	private OptionsConnectionPanel optionsConnectionPanel = null;
	private OptionsAuthenticationPanel optionsAuthenticationPanel = null;
	private OptionsCertificatePanel optionsCertificatePanel = null;
	private OptionsLocalProxyPanel optionsLocalProxyPanel = null;
	private OptionsViewPanel optionsViewPanel = null;

    public ExtensionOption() {
        super();
 		initialize();
    }

    public ExtensionOption(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionViewOption");
			
	}
	

	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        ExtensionHookView pv = extensionHook.getHookView();
	        extensionHook.getHookMenu().addViewMenuItem(getMenuViewImage());
	        
	        extensionHook.getHookView().addOptionPanel(getOptionsConnectionPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsLocalProxyPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsAuthenticationPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsCertificatePanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsViewPanel());
	    }
	}

	private JCheckBoxMenuItem getMenuViewImage() {
		if (menuViewImage == null) {
			menuViewImage = new JCheckBoxMenuItem();
			menuViewImage.setText("기록에 이미지 첨부 가능");
			menuViewImage.addItemListener(new java.awt.event.ItemListener() { 

				public void itemStateChanged(java.awt.event.ItemEvent e) {    

					getModel().getOptionsParam().getViewParam().setProcessImages(getMenuViewImage().getState() ? 1 : 0);
					
				}
			});

		}
		return menuViewImage;
	}
 
	private OptionsConnectionPanel getOptionsConnectionPanel() {
		if (optionsConnectionPanel == null) {
			optionsConnectionPanel = new OptionsConnectionPanel();
		}
		return optionsConnectionPanel;
	}
  
	private OptionsAuthenticationPanel getOptionsAuthenticationPanel() {
		if (optionsAuthenticationPanel == null) {
			optionsAuthenticationPanel = new OptionsAuthenticationPanel();
		}
		return optionsAuthenticationPanel;
	}
  
	private OptionsCertificatePanel getOptionsCertificatePanel() {
		if (optionsCertificatePanel == null) {
			optionsCertificatePanel = new OptionsCertificatePanel();
		}
		return optionsCertificatePanel;
	}
  
	private OptionsLocalProxyPanel getOptionsLocalProxyPanel() {
		if (optionsLocalProxyPanel == null) {
			optionsLocalProxyPanel = new OptionsLocalProxyPanel();
		}
		return optionsLocalProxyPanel;
	}
 
	private OptionsViewPanel getOptionsViewPanel() {
		if (optionsViewPanel == null) {
			optionsViewPanel = new OptionsViewPanel();
		}
		return optionsViewPanel;
	}
        }
