package org.parosproxy.paros.view;

import java.awt.Frame;
import java.awt.HeadlessException;

import org.parosproxy.paros.model.OptionsParam;

public class OptionsDialog extends AbstractParamDialog {

    private static final String[] ROOT = {};
	private OptionsParam optionParam = null;
    public OptionsDialog() {
        super();
 		initialize();
   }
    public OptionsDialog(Frame parent, boolean modal, String title)
            throws HeadlessException {
        super(parent, modal, title, "¿É¼Ç");
        initialize();
    }

	private void initialize() {

        this.setSize(900, 600);
	}
	
}  
