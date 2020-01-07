package org.parosproxy.paros.control;

import org.parosproxy.paros.extension.ExtensionLoader;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.View;


public abstract class AbstractControl {

    protected ExtensionLoader loader = null;
    protected Model model = null;
    protected View view = null;
    
    public AbstractControl(Model model, View view) {
        this.model = model;
        this.view = view;
    }
    
    public ExtensionLoader getExtensionLoader() {
        if (loader == null) {
            loader = new ExtensionLoader(model, view);
        }
        return loader;
    }
    
    protected void loadExtension() {
        addCommonExtension();
        addExtension();
        
        getExtensionLoader().startLifeCycle();

    }
    /*
     * �����Ϸ��� Ư�� �÷������� ���� Ŭ������ ���ؼ� ����������
     */
    protected abstract void addExtension();

    /*
     * ���� �÷����ε��� ��ü �����ϱ� ���ؼ� ���ؿ�, ������� Ư�� �÷������� �����ϱ� ���� ��������
     */
    protected void addCommonExtension() {

    }
    
    public void shutdown(boolean compact) {
	    getExtensionLoader().destroyAllExtension();
		model.getDb().close(compact);
		
    }
}
