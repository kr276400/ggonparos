package org.parosproxy.paros.extension;

import java.util.Vector;

import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.model.Model;

public class ExtensionHook {

    private ExtensionHookMenu hookMenu = new ExtensionHookMenu();
    private ExtensionHookView hookView = new ExtensionHookView();
    private Model model = null;    
    private Vector optionsListenerList = new Vector();

    private Vector proxyListenerList = new Vector();
    private Vector sessionListenerList = new Vector();
    private Vector optionsParamSetList = new Vector();
    
    private ViewDelegate view = null;
    private CommandLineArgument arg[] = new CommandLineArgument[0];
    
    public ExtensionHook(Model model, ViewDelegate view) {
        this.view = view;
        this.model = model;
    }
    
    public void addOptionsChangedListener(OptionsChangedListener listener) {
        optionsListenerList.add(listener);
    }    

    public void addOptionsParamSet(AbstractParam paramSet) {
        optionsParamSetList.add(paramSet);
    }    

    public void addProxyListener(ProxyListener listener) {
        proxyListenerList.add(listener);
    }
    public void addSessionListener(SessionChangedListener listener) {
        sessionListenerList.add(listener);
    }
    
    public void addCommandLine(CommandLineArgument arg[]) {
        this.arg = arg;
    }

    public ExtensionHookMenu getHookMenu() {
        return hookMenu;
    }

    public ExtensionHookView getHookView() {
        return hookView;
    }

    public Model getModel() {
        return model;
    }

    public Vector getOptionsChangedListenerList() {
        return optionsListenerList;
    }

    public Vector getOptionsParamSetList() {
        return optionsParamSetList;
    }

    public Vector getProxyListenerList() {
        return proxyListenerList;
    }

    public Vector getSessionListenerList() {
        return sessionListenerList;
    }

    public ViewDelegate getView() {
        return view;
    }
    
    public CommandLineArgument[] getCommandLineArgument() {
        return arg;
    }
}
