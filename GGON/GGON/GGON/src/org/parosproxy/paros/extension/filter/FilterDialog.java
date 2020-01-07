package org.parosproxy.paros.extension.filter;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.util.List;

import org.parosproxy.paros.view.AbstractParamDialog;

public class FilterDialog extends AbstractParamDialog {

    private static final String[] ROOT = {};
	private AllFilterPanel allFilterPanel = null;
    public FilterDialog() {
        super();
        initialize();
        
    }
    
    public FilterDialog(Frame parent) throws HeadlessException {
        super(parent, true, "Filter", "Filters");
        initialize();
    }

    private void initialize() {
                this.setTitle("« ≈Õ");
                this.setSize(640, 480);
        addParamPanel(ROOT, getAllFilterPanel());
        getBtnCancel().setEnabled(false);
    }
  
	private AllFilterPanel getAllFilterPanel() {
		if (allFilterPanel == null) {
			allFilterPanel = new AllFilterPanel();
			allFilterPanel.setName("Filter");
		}
		return allFilterPanel;
	}
	
	void setAllFilters(List allFilters) {
	    getAllFilterPanel().getAllFilterTableModel().setTable(allFilters);
	}
   }  
