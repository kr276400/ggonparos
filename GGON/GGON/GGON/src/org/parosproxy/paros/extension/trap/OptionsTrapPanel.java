package org.parosproxy.paros.extension.trap;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;

public class OptionsTrapPanel extends AbstractParamPanel {

	private TrapFilterPanel trapFilterPanel = null;
	private JLabel jLabel = null;

    public OptionsTrapPanel() {
        super();
		initialize();
    }

	private void initialize() {
        jLabel = new JLabel();
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.setName("트랩(Trap)");
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0D;
        jLabel.setText(" ");
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        this.add(getTrapFilterPanel(), gridBagConstraints1);
        this.add(jLabel, gridBagConstraints2);
			
	}

    public void initParam(Object obj) {
        
	    OptionsParam optionsParam = (OptionsParam) obj;
	    TrapParam trapParam = (TrapParam) optionsParam.getParamSet(TrapParam.class);
	    
	    if (trapParam.getInclusiveFilter().equals("")) {
	        getTrapFilterPanel().getChkEnableInclusiveFilter().setSelected(false);
	        getTrapFilterPanel().setInclusiveFilter(false);
	    } else {
	        getTrapFilterPanel().getChkEnableInclusiveFilter().setSelected(true);
	        getTrapFilterPanel().setInclusiveFilter(true);
	    }
        getTrapFilterPanel().getTxtInclusiveFilter().setText(trapParam.getInclusiveFilter());

	    if (trapParam.getExclusiveFilter().equals("")) {
	        getTrapFilterPanel().getChkEnableExclusiveFilter().setSelected(false);
	        getTrapFilterPanel().setExclusiveFilter(false);
	    } else {
	        getTrapFilterPanel().getChkEnableExclusiveFilter().setSelected(true);
	        getTrapFilterPanel().setExclusiveFilter(true);
	    }
        getTrapFilterPanel().getTxtExclusiveFilter().setText(trapParam.getExclusiveFilter());		    


    }

    public void validateParam(Object obj) throws Exception {

    }

    public void saveParam(Object obj) throws Exception {
        
        OptionsParam optionsParam = (OptionsParam) obj;
        TrapParam trapParam = (TrapParam) optionsParam.getParamSet(TrapParam.class);
	    
            try {
                String s = getTrapFilterPanel().getTxtInclusiveFilter().getText();
                trapParam.setInclusiveFilter(s); 
            } catch (Exception e) {
                getTrapFilterPanel().getTxtInclusiveFilter().requestFocus();
                e.printStackTrace();
                throw new Exception("인식 되지 않는 패턴입니다.");
                
            }

            try {
                String s = getTrapFilterPanel().getTxtExclusiveFilter().getText();
                trapParam.setExclusiveFilter(s);
            } catch (Exception e) {
                getTrapFilterPanel().getTxtExclusiveFilter().requestFocus();
                throw new Exception("인식 되지 않는 패턴입니다.");
                
            }

    }

	private TrapFilterPanel getTrapFilterPanel() {
		if (trapFilterPanel == null) {
			trapFilterPanel = new TrapFilterPanel();
		}
		return trapFilterPanel;
	}
 }
