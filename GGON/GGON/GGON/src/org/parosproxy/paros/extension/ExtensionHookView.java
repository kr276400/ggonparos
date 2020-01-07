package org.parosproxy.paros.extension;

import java.util.List;
import java.util.Vector;

import org.parosproxy.paros.view.AbstractParamPanel;

public class ExtensionHookView {

    private Vector workPanelList = new Vector();
    private Vector statusPanelList = new Vector();
    private Vector selectPanelList = new Vector();
    private Vector sessionPanelList = new Vector();
    private Vector optionPanelList = new Vector();
    
    public ExtensionHookView() {
    }
    
    public void addWorkPanel(AbstractPanel panel) {
        workPanelList.add(panel);
    }
    
    public void addSelectPanel(AbstractPanel panel) {
        selectPanelList.add(panel);
    }
    
    public void addStatusPanel(AbstractPanel panel) {
        statusPanelList.add(panel);
    }
    
    public void addSessionPanel(AbstractPanel panel) {
        sessionPanelList.add(panel);
    }
    
    public void addOptionPanel(AbstractParamPanel panel) {
        optionPanelList.add(panel);
    }
    
    List getWorkPanel() {
        return workPanelList;
    }
        
    List getSelectPanel() {
        return selectPanelList;
    }
    
    List getStatusPanel() {
        return statusPanelList;
    }

    List getSessionPanel() {
        return sessionPanelList;
    }
    
    List getOptionsPanel() {
        return optionPanelList;
    }
    
}
