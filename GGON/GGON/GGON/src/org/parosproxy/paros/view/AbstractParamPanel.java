package org.parosproxy.paros.view;

import javax.swing.JPanel;

abstract public class AbstractParamPanel extends JPanel {

	public AbstractParamPanel() {
		super();
		initialize();
	}

	private  void initialize() {
		this.setSize(1000, 600);
	}
	
	abstract public void initParam(Object obj);
	
	abstract public void validateParam(Object obj) throws Exception;
	
	abstract public void saveParam(Object obj) throws Exception;
	

}
