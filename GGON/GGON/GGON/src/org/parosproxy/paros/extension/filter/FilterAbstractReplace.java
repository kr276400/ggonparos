package org.parosproxy.paros.extension.filter;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

abstract public class FilterAbstractReplace extends FilterAdaptor {
    
    private FilterReplaceDialog filterReplaceDialog = null;
	private Pattern pattern = null;
	private String txtReplace = "";
 
	private FilterReplaceDialog getFilterReplaceDialog() {
		if (filterReplaceDialog == null) {
			filterReplaceDialog = new FilterReplaceDialog(getView().getMainFrame(), true);
		}
		return filterReplaceDialog;
	}
	
	public boolean isPropertyExists() {
	    return true;
	}
	
	public void editProperty() {
	    FilterReplaceDialog dialog = getFilterReplaceDialog();
	    dialog.setView(getView());
	    int result = dialog.showDialog();
	    if (result == JOptionPane.CANCEL_OPTION) {
	        return;
	    }
	    
	    if (dialog.getTxtPattern().getText().equals("")) {
	        pattern = null;
	    } else {
	        pattern = Pattern.compile(dialog.getTxtPattern().getText(), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	    }
	    
	    txtReplace = dialog.getTxtReplaceWith().getText();
	    
	}
	
	protected Pattern getPattern() {
	    return pattern;
	}
	
	protected String getReplaceText() {
	    return txtReplace;
	}
	
	
}
