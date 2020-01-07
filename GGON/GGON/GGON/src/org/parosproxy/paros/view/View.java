package org.parosproxy.paros.view;


import java.util.Vector;

import javax.swing.JOptionPane;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ViewDelegate;

public class View implements ViewDelegate {
	
	private static View view = null;
	private SessionDialog sessionDialog = null;
	private OptionsDialog optionsDialog = null;
	
	private MainFrame mainFrame = null;
	private HttpPanel requestPanel = null;
	private HttpPanel responsePanel = null;
	private SiteMapPanel siteMapPanel  = null;
	private Vector popupList = new Vector();

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void init() {
		mainFrame = new MainFrame();

		siteMapPanel = new SiteMapPanel();

		getWorkbench().getTabbedWork().add(getRequestPanel());
		getWorkbench().getTabbedWork().add(getResponsePanel());

		getWorkbench().getTabbedSelect().add(siteMapPanel, "����Ʈ �κ�"); // ���� �����ӿ��� ��� ����Ʈ �κ� ǥ��
		
		getWorkbench().getTabbedWork().setAlternativeParent(mainFrame.getPaneDisplay());
		getWorkbench().getTabbedStatus().setAlternativeParent(mainFrame.getPaneDisplay());
		getWorkbench().getTabbedSelect().setAlternativeParent(mainFrame.getPaneDisplay());

	}

	public int showConfirmDialog(String msg) {
		return JOptionPane.showConfirmDialog(getMainFrame(), msg, Constant.PROGRAM_NAME, JOptionPane.OK_CANCEL_OPTION);
	}
	
	public int showYesNoCancelDialog(String msg) {
		return JOptionPane.showConfirmDialog(getMainFrame(), msg, Constant.PROGRAM_NAME, JOptionPane.YES_NO_CANCEL_OPTION);
	}
	
	public void showWarningDialog(String msg) {
		JOptionPane.showMessageDialog(getMainFrame(), msg, Constant.PROGRAM_NAME, JOptionPane.WARNING_MESSAGE);
	}

	public void showMessageDialog(String msg) {
		JOptionPane.showMessageDialog(getMainFrame(), msg, Constant.PROGRAM_NAME, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static View getSingleton() {
		if (view == null) {
			view = new View(); 
			view.init();
		}
		return view;
	}

    public SiteMapPanel getSiteTreePanel() {
        return siteMapPanel;
    }

    public HttpPanel getRequestPanel() {
        if (requestPanel == null) {
            requestPanel = new HttpPanel(false);
            requestPanel.setName("��û �κ�");
        }
        return requestPanel;
    }
    
    public HttpPanel getResponsePanel() {
        if (responsePanel == null) {
            responsePanel = new HttpPanel(false);
            responsePanel.setName("���� �κ�");
            responsePanel.setMessage("","",false);
        }
        return responsePanel;
    }
    
    public SessionDialog getSessionDialog(String title) {
        String[] ROOT = {};
        if (sessionDialog == null) {
            sessionDialog = new SessionDialog(getMainFrame(), true, title, "����");
            sessionDialog.addParamPanel(ROOT, new SessionGeneralPanel());

        }
        
        sessionDialog.setTitle(title);
        return sessionDialog;
    }
    
    public OptionsDialog getOptionsDialog(String title) {
        String[] ROOT = {};
        if (optionsDialog == null) {
            optionsDialog = new OptionsDialog(getMainFrame(), true, title);

        }
        
        optionsDialog.setTitle(title);
        return optionsDialog;
    }
    
    public WorkbenchPanel getWorkbench() {
        return mainFrame.getWorkbench();
    }
    
    public void setStatus(String msg) {
        if (msg == null || msg.equals("")) {
            msg = " ";
        }
        mainFrame.setStatus(msg);
    }
    
    public MainPopupMenu getPopupMenu() {
        MainPopupMenu popup = new MainPopupMenu(popupList);
        return popup;
    }
    
    public Vector getPopupList() {
        return popupList;
    }
    
    public WaitMessageDialog getWaitMessageDialog(String s) {
        WaitMessageDialog dialog = new WaitMessageDialog(getMainFrame(), true);
        dialog.setText(s);
        dialog.centreDialog();
        return dialog;
    }
    
    
}
