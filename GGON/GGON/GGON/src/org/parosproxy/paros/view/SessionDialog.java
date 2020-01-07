package org.parosproxy.paros.view;

import java.awt.Frame;
import java.awt.HeadlessException;

import org.parosproxy.paros.model.Session;

public class SessionDialog extends AbstractParamDialog {

	private Session session = null;
	
    public SessionDialog() {
        super();
 		initialize();
   }

    public SessionDialog(Frame parent, boolean modal, String title)
            throws HeadlessException {
        super(parent, modal, title, "¼¼¼Ç");
        initialize();
    }
    
    public SessionDialog(Frame parent, boolean modal, String title, String rootName) {
        super(parent, modal, title, rootName);
    }

	private void initialize() {
        this.setSize(1000, 600);


	}
}  
