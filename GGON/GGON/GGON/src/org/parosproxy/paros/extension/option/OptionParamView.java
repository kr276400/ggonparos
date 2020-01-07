package org.parosproxy.paros.extension.option;

import org.parosproxy.paros.common.AbstractParam;

public class OptionParamView extends AbstractParam {

	private static final String ROOT = "view";

	private static final String PROCESS_IMAGES = "view.processImages";
	
	private int processImages = 0;

    public OptionParamView() {
    }

    protected void parse() {
        
	    processImages = getConfig().getInt(PROCESS_IMAGES, 0);
		
    }

	public int getProcessImages() {
		return processImages;
	}

	public void setProcessImages(int processImages) {
		this.processImages = processImages;
		getConfig().setProperty(PROCESS_IMAGES, Integer.toString(processImages));
	}
	
	public boolean isProcessImages() {
		return !(processImages == 0);
	}

	
}
