package org.parosproxy.paros.extension.option;

import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;

public class OptionsViewPanel extends AbstractParamPanel {

	private JPanel panelMisc = null;  
	private JCheckBox chkProcessImages = null;
	
    public OptionsViewPanel() {
        super();
 		initialize();
   }

    private static final String[] ROOT = {};
    private static final String[] GENERAL = {"General"};
    private static final String[] MISCELLENOUS = {"Miscellenous"};

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName("뷰(View)");
        this.add(getPanelMisc(), getPanelMisc().getName());

	}

	private JPanel getPanelMisc() {
		if (panelMisc == null) {
			panelMisc = new JPanel();
			java.awt.GridLayout gridLayout2 = new GridLayout();

			panelMisc.setLayout(gridLayout2);
			panelMisc.setSize(114, 132);
			panelMisc.setName("Miscellenous");
			gridLayout2.setRows(1);
			panelMisc.add(getChkProcessImages(), null);
		}
		return panelMisc;
	}

	private JCheckBox getChkProcessImages() {
		if (chkProcessImages == null) {
			chkProcessImages = new JCheckBox();
			chkProcessImages.setText("요청/응답 이미지 진행하기");
			chkProcessImages.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			chkProcessImages.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
		}
		return chkProcessImages;
	}
	
	public void initParam(Object obj) {
	    OptionsParam options = (OptionsParam) obj;
	    getChkProcessImages().setSelected(options.getViewParam().getProcessImages() > 0);
	}
	
	public void validateParam(Object obj) {
	}
	
	public void saveParam (Object obj) throws Exception {
	    OptionsParam options = (OptionsParam) obj;
	    options.getViewParam().setProcessImages((getChkProcessImages().isSelected()) ? 1 : 0);
	    
	}
	
     }  
