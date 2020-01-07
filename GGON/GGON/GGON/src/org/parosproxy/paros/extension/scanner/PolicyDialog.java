package org.parosproxy.paros.extension.scanner;

import java.awt.Frame;
import java.awt.HeadlessException;

import org.parosproxy.paros.core.scanner.Category;
import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.view.AbstractParamDialog;


public class PolicyDialog extends AbstractParamDialog {

    private static final String[] ROOT = {};
	private PolicyCategoryPanel policyCategoryPanel = null;  
	private PolicyAllCategoryPanel policyAllCategoryPanel = null;
    public PolicyDialog() {
        super();
        initialize();
        
    }
    
    public PolicyDialog(Frame parent) throws HeadlessException {
        super(parent, true, "스캔 방식", "플러그인 카테고리");
        initialize();
    }

    private void initialize() {
                this.setTitle("스캔 방식");
                this.setSize(1000, 600);
                // 스캔 방식 패널 기본 크기 지정하는 부분
        addParamPanel(null, getPolicyAllCategoryPanel());
        for (int i=0; i<Category.getAllNames().length; i++) {
            addParamPanel(ROOT, Category.getName(i), getPolicyCategoryPanel());
        }
        getBtnCancel().setEnabled(false);
    }

	private PolicyCategoryPanel getPolicyCategoryPanel() {
		if (policyCategoryPanel == null) {
			policyCategoryPanel = new PolicyCategoryPanel();
		}
		return policyCategoryPanel;
	}
	
	private void categorySelected(String name) {
	    int category = Category.getCategory(name);
	    getPolicyCategoryPanel().getCategoryTableModel().setTable(category, PluginFactory.getAllPlugin());
	}
	
	protected void showParamPanel(String name) {
	    super.showParamPanel(name);
	    if (!name.equals(this.getRootNode().toString())) {
	        categorySelected(name);
	    }
	}
  
	private PolicyAllCategoryPanel getPolicyAllCategoryPanel() {
		if (policyAllCategoryPanel == null) {
			policyAllCategoryPanel = new PolicyAllCategoryPanel();
			policyAllCategoryPanel.setName("플러그인 카테고리");
		}
		return policyAllCategoryPanel;
	}
  }
