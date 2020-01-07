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
     * 제어하려고 특정 플러그인을 하위 클래스에 더해서 시행했지요
     */
    protected abstract void addExtension();

    /*
     * 보통 플러그인들을 전체 제어하기 위해서 더해요, 딴놈들이 특정 플러그인을 제어하기 전에 더해져요
     */
    protected void addCommonExtension() {

    }
    
    public void shutdown(boolean compact) {
	    getExtensionLoader().destroyAllExtension();
		model.getDb().close(compact);
		
    }
}
