package org.parosproxy.paros.extension;

import java.util.List;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.parosproxy.paros.CommandLine;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.control.Proxy;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.view.AbstractParamDialog;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.parosproxy.paros.view.TabbedPannel;
import org.parosproxy.paros.view.View;

public class ExtensionLoader {

    private Vector extensionList = new Vector();
    private Vector hookList = new Vector();
    private Model model = null;

    private View view = null;

    public ExtensionLoader(Model model, View view) {
        
        this.model = model;
        this.view = view;
    }

    public void addExtension(Extension extension) {
        extensionList.add(extension);
    }
    
    public void destroyAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).destroy();
        }
        
    }
    
    public Extension getExtension(int i) {
        return (Extension) extensionList.get(i);
    }
    
    public Extension getExtension(String name) {
        if (name == null)
            return null;

        for (int i=0; i<extensionList.size(); i++) {
            Extension p = getExtension(i);
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        
        return null;
    }
    
    public int getExtensionCount() {
        return extensionList.size();
    }
    
    public void hookProxyListener(Proxy proxy) {
        for (int i=0; i<getExtensionCount(); i++) {
            ExtensionHook hook = (ExtensionHook) hookList.get(i);
            List listenerList = hook.getProxyListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    ProxyListener listener = (ProxyListener) listenerList.get(j);
                    if (listener != null) {
                        proxy.addProxyListener(listener);
                    }
                } catch (Exception e) {}
            }
        }
        
    }
    
    public void optionsChangedAllPlugin(OptionsParam options) {
        for (int i=0; i<getExtensionCount(); i++) {
            ExtensionHook hook = (ExtensionHook) hookList.get(i);
            List listenerList = hook.getOptionsChangedListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    OptionsChangedListener listener = (OptionsChangedListener) listenerList.get(j);
                    if (listener != null) {
                        listener.OptionsChanged(options);
                    }
                } catch (Exception e) {}
            }
            
        }
    }
    
    public void runCommandLine() {
        ExtensionHook hook = null;
        Extension ext = null;
        for (int i=0; i<getExtensionCount(); i++) {
            ext = getExtension(i);
            hook = (ExtensionHook) hookList.get(i);
            if (ext instanceof CommandLineListener) {
                CommandLineListener listener = (CommandLineListener) ext;
                listener.execute(hook.getCommandLineArgument());
            }
        }
    }
    
    public void sessionChangedAllPlugin(Session session) {
        for (int i=0; i<getExtensionCount(); i++) {
            ExtensionHook hook = (ExtensionHook) hookList.get(i);
            List listenerList = hook.getSessionListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    SessionChangedListener listener = (SessionChangedListener) listenerList.get(j);
                    if (listener != null) {
                        listener.sessionChanged(session);
                    }
                } catch (Exception e) {}
            }
            
        }
    }
    

    public void startAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).start();
        }
        
    }
    
    public void startLifeCycle() {
        initAllExtension();
        initModelAllExtension(model);
        initXMLAllExtension(model.getSession(), model.getOptionsParam());
        initViewAllExtension(view);
        
        hookAllExtension();
        startAllExtension();
        
    }
    
    public void stopAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).stop();
        }
        
    }
    
    private void addParamPanel(List panelList, AbstractParamDialog dialog) {
        AbstractParamPanel panel = null;
        String[] ROOT = {};
        for (int i=0; i<panelList.size(); i++) {
            try {
                panel = (AbstractParamPanel) panelList.get(i);
                dialog.addParamPanel(ROOT, panel);
            } catch (Exception e) {
                
            }
        }
        
    }
    
    private void addTabPanel(List panelList, TabbedPannel tab) {
        AbstractPanel panel = null;
        for (int i=0; i<panelList.size(); i++) {
            try {
                panel = (AbstractPanel) panelList.get(i);
                tab.add(panel, panel.getName());
            } catch (Exception e) {
                
            }
        }
    }


    
    
    private void hookAllExtension() {
        ExtensionHook extHook = null;
        for (int i=0; i<getExtensionCount(); i++) {
            extHook = new ExtensionHook(model, view);
            getExtension(i).hook(extHook);
            hookList.add(extHook);
            
            if (view != null) {
                hookView(view, extHook);
                hookMenu(view, extHook);

            }
            hookOptions(extHook);
        }
        
        if (view != null) {
            view.getMainFrame().getMainMenuBar().validate();
            view.getMainFrame().validate();
        }

    }

    public void hookCommandLineListener (CommandLine cmdLine) throws Exception {
        Vector allCommandLineList = new Vector();
        for (int i=0; i<hookList.size(); i++) {
            ExtensionHook hook = (ExtensionHook) hookList.get(i);
            CommandLineArgument[] arg = hook.getCommandLineArgument();
            if (arg.length > 0) {
                allCommandLineList.add(arg);
            }
        }
        
        cmdLine.parse(allCommandLineList);
    }
    
    private void hookMenu(View view, ExtensionHook hook) {

        if (view == null) {
            return;
        }
        
        if (hook.getHookMenu() == null) {
            return;
        }
        
        ExtensionHookMenu hookMenu = hook.getHookMenu();
        
        List list = null;
        JMenuItem item = null;
        JMenu menu = null;
        JMenu menuFile = view.getMainFrame().getMainMenuBar().getMenuFile();
        JMenu menuEdit = view.getMainFrame().getMainMenuBar().getMenuEdit();
        JMenu menuAnalyse = view.getMainFrame().getMainMenuBar().getMenuAnalyse();
        

        JMenuBar bar = view.getMainFrame().getMainMenuBar();
        list = hookMenu.getNewMenus();
        for (int i=0; i<list.size(); i++) {
            menu = (JMenu) list.get(i);
            bar.add(menu, bar.getMenuCount()-2);	
        }

        // 파일 메뉴 진행 하게 하는 부분
        list = hookMenu.getFile();
        int existingCount = 2;
        
        for (int i=0; i<list.size(); i++) {
            item = (JMenuItem) list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuFile.addSeparator();
                continue;
            }

            menuFile.add(item, menuFile.getItemCount()-existingCount);
        }

        
        list = hookMenu.getTools();
        existingCount = 2;
        
        for (int i=0; i<list.size(); i++) {
            item = (JMenuItem) list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                continue;
            }

        }

        list = hookMenu.getEdit();
        
        for (int i=0; i<list.size(); i++) {
            item = (JMenuItem) list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuEdit.addSeparator();
                continue;
            }
            menuEdit.add(item, menuEdit.getItemCount());
        }

        // 뷰 메뉴 진행하는 부분
        list = hookMenu.getView();
        
        for (int i=0; i<list.size(); i++) {
            item = (JMenuItem) list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                continue;
            }

        }

        // 분석 메뉴 부분 진행 하는 곳
        list = hookMenu.getAnalyse();
        
        for (int i=0; i<list.size(); i++) {
            item = (JMenuItem) list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuAnalyse.addSeparator();
                continue;
            }

            menuAnalyse.add(item, menuAnalyse.getItemCount());
        }
        
        // 포펍 매뉴 진행하는 부분
        
        list = hookMenu.getPopupMenus();
        
        for (int i=0; i<list.size(); i++) {
            item = (ExtensionPopupMenu) list.get(i);
            if (item == null) continue;

            view.getPopupList().add(item);
        }

    }
    
    private void hookOptions(ExtensionHook hook) {
        Vector list = hook.getOptionsParamSetList();
        for (int i=0; i<list.size(); i++) {
            try {
                AbstractParam paramSet = (AbstractParam) list.get(i);
                model.getOptionsParam().addParamSet(paramSet);
            } catch (Exception e) {
                
            }
        }
    }


    
    private void hookView(View view, ExtensionHook hook) {
        if (view == null) {
            return;
        }
        
        ExtensionHookView pv = hook.getHookView();
        if (pv == null) {
            return;
        }
        
        addTabPanel(pv.getSelectPanel(), view.getWorkbench().getTabbedSelect());
        addTabPanel(pv.getWorkPanel(), view.getWorkbench().getTabbedWork());
        addTabPanel(pv.getStatusPanel(), view.getWorkbench().getTabbedStatus());
 
        addParamPanel(pv.getSessionPanel(), view.getSessionDialog(""));
        addParamPanel(pv.getOptionsPanel(), view.getOptionsDialog(""));
        
        
    }
    
    private void initAllExtension() {

        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).init();
        }
    }
    

    
    private void initModelAllExtension(Model model) {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initModel(model);
        }
        
    }

    private void initViewAllExtension(View view) {

        if (view == null) {
            return;
        }
        
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initView(view);
        }

    }

    private void initXMLAllExtension(Session session, OptionsParam options) {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initXML(session, options);
        }        
    }
}
