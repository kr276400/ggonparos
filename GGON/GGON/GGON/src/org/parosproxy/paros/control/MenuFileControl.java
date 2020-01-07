package org.parosproxy.paros.control;
 
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SessionListener;
import org.parosproxy.paros.view.SessionDialog;
import org.parosproxy.paros.view.View;
import org.parosproxy.paros.view.WaitMessageDialog;

public class MenuFileControl implements SessionListener {

    private static Log log = LogFactory.getLog(MenuFileControl.class);

    private View view = null;
    private Model model = null;
    private Control control = null;
    private WaitMessageDialog waitMessageDialog = null;
    
    public MenuFileControl(Model model, View view, Control control) {
        this.view = view;
        this.model = model;
        this.control = control;
    }
    
	public void exit() {
	    boolean isNewState = model.getSession().isNewState();
	    if (isNewState) {
			if (view.showConfirmDialog("���� ������ ������� �ʾҽ��ϴ�.  ����Ͻðڽ��ϱ�?") != JOptionPane.OK_OPTION) {
				return;
			}
			model.getSession().discard();
	    }

	    WaitMessageDialog dialog = view.getWaitMessageDialog("�����ͺ��̽��� ������ �����ϴ� ���Դϴ�...");

	    Thread t = new Thread(new Runnable() {
	        public void run() {
	            control.shutdown(false);
	    	    log.info(Constant.PROGRAM_TITLE + " ���ŵǾ����ϴ�.");
	    		System.exit(0);   
	        }
	    });
	    t.start();
	    dialog.setVisible(true);
	    // ����ڿ��� ���̾�α��� �������� �����ְԲ� �ϴ� �κ�
	    
	}
	
	public void newSession(boolean isPromptNewSession) throws ClassNotFoundException, Exception {
		if (isPromptNewSession) {
		    if (model.getSession().isNewState()) {
				if (view.showConfirmDialog("���� ������ ����Ǿ� ���� �ʽ��ϴ�.  �����ϰ� ���� ������ ����ðڽ��ϱ�?") != JOptionPane.OK_OPTION) {
					return;
				}
				model.getSession().discard();
		    } else if (view.showConfirmDialog("���� ������ ����˴ϴ�.  ���� ������ ����ðڽ��ϱ�?") != JOptionPane.OK_OPTION) {
				return;
			}
			model.createAndOpenUntitledDb();
		}
		
		Session session = new Session(model);
	    log.info("���ο� ���� ������ �����Ǿ����ϴ�");
	    model.setSession(session);

		view.getSiteTreePanel().getTreeSite().setModel(session.getSiteTree());

		// �ڵ忡 ���¸� ����ϰ�, ���ο� ������ �̸��� ���� ������� ���� �͵��� ���� ����Ѵ�
		control.getExtensionLoader().sessionChangedAllPlugin(session);
		// ���÷��� ������
		
		view.getMainFrame().setTitle(session.getSessionName() + " - " + Constant.PROGRAM_NAME);
		
	}
	
	public void openSession() {
		JFileChooser chooser = new JFileChooser(model.getOptionsParam().getUserDirectory());
		File file = null;
	    chooser.setFileFilter(new FileFilter() {
	           public boolean accept(File file) {
	                if (file.isDirectory()) {
	                    return true;
	                } else if (file.isFile() && file.getName().endsWith(".session")) {
	                    return true;
	                }
	                return false;
	            }
	           public String getDescription() {
	               return "GGON session";
	           }
	    });
	    int rc = chooser.showOpenDialog(view.getMainFrame());
	    if(rc == JFileChooser.APPROVE_OPTION) {
			try {
	    		file = chooser.getSelectedFile();
	    		if (file == null) {
	    			return;
	    		}
                model.getOptionsParam().setUserDirectory(chooser.getCurrentDirectory());
	    		Session session = model.getSession();
	    	    log.info("���� ������ ���� �� " + file.getAbsolutePath());
	    	    waitMessageDialog = view.getWaitMessageDialog("���� ������ �ҷ����� ���Դϴ�.  ��ø� ��ٷ��ֽʽÿ� ...");
	    		session.open(file, this);
	    		waitMessageDialog.setVisible(true);
			} catch (Exception e) {
			    e.printStackTrace();
			}
	    }
	}
	public void saveSession() {
	    Session session = model.getSession();

	    if (session.isNewState()) {
		    view.showWarningDialog("�ٸ� �̸����� �����ϱ⸦ ������ּ���...");
		    return;
	    }
	    
		try {
    	    waitMessageDialog = view.getWaitMessageDialog("���� ������ �����ϴ� ���Դϴ�.  ��ø� ��ٷ��ּ��� ...");		    
    		session.save(session.getFileName(), this);
    	    log.info("���������� �����ϴ� �� " + session.getFileName());
    	    waitMessageDialog.setVisible(true);
    	    // �� ���ٿ� �޽����� ���̰� �Ѵٰ� �߰��� �ƴϴ�
    	    
		} catch (Exception e) {
		    view.showWarningDialog("���� ���� �����ϴ� �߿� ������ ������ϴ�.");
    	    log.error("������ �ִ� ���������� �����ϴ� �� " + session.getFileName());
    	    log.error(e.getMessage());
    	    
		}
	    
	}
	
	public void saveAsSession() {
	    Session session = model.getSession();

	    JFileChooser chooser = new JFileChooser(model.getOptionsParam().getUserDirectory());
	    chooser.setFileFilter(new FileFilter() {
	           public boolean accept(File file) {
	                if (file.isDirectory()) {
	                    return true;
	                } else if (file.isFile() && file.getName().endsWith(".session")) {
	                    return true;
	                }
	                return false;
	            }
	           public String getDescription() {
	               return "GGON session";
	           }
	    });
		File file = null;
	    int rc = chooser.showSaveDialog(view.getMainFrame());
	    if(rc == JFileChooser.APPROVE_OPTION) {
    		file = chooser.getSelectedFile();
    		if (file == null) {
    			return;
    		}
            model.getOptionsParam().setUserDirectory(chooser.getCurrentDirectory());
    		String fileName = file.getAbsolutePath();
    		if (!fileName.endsWith(".session")) {
    		    fileName += ".session";
    		}
    		
    		try {
	    	    waitMessageDialog = view.getWaitMessageDialog("���ο� ���� ������ �����ϴ� ���Դϴ�.  ��ø� ��ٷ��ּ��� ...");
	    	    session.save(fileName, this);
        	    log.info("���� ���Ϸ� ���� " + session.getFileName());
        	    waitMessageDialog.setVisible(true);
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
	    }
	}
	
	public void properties() {
	    SessionDialog dialog = view.getSessionDialog("���� �Ӽ�");
	    dialog.initParam(model.getSession());
	    dialog.showDialog(false);
	}
	/*
	 * �ڹ� ������ �ƴҰ��
	 * paros.model.SessionListener#sessionOpened(java.lang.Exception) ���� ����
	 */
    public void sessionOpened(File file, Exception e) {
        if (e == null) {
            control.getExtensionLoader().sessionChangedAllPlugin(model.getSession());
            view.getMainFrame().setTitle(file.getName().replaceAll(".session\\z","") + " - " + Constant.PROGRAM_NAME);
        } else {
            view.showWarningDialog("���� ������ ���� ���߿� ������ �߻��߽��ϴ�");
            log.error("���� ������ ���� ���� ���� ���� " + file.getAbsolutePath());
            log.error(e.getMessage());
        }

        if (waitMessageDialog != null) {
            waitMessageDialog.setVisible(false);
            waitMessageDialog = null;
        }
    }

    /* 
     * �ڹ� ������ �ƴѰ��
     * Paros.model.SessionListener#sessionSaved(java.lang.Exception) ����
     */
    public void sessionSaved(Exception e) {
        if (e == null) {
            view.getMainFrame().getMainMenuBar().getMenuFileSave().setEnabled(true);
            File file = new File(model.getSession().getFileName());
            view.getMainFrame().setTitle(file.getName().replaceAll(".session\\z","") + " - " + Constant.PROGRAM_NAME);
        } else {
		    view.showWarningDialog("���� ������ �����ϴ� ���� ������ ������ϴ�.");
		    e.printStackTrace();
    	    log.error("���� ������ �����ϴ� ���� ���� ���� " + model.getSession().getFileName());
    	    log.error(e.getStackTrace());

        }
        
        if (waitMessageDialog != null) {
            waitMessageDialog.setVisible(false);
            waitMessageDialog = null;
        }

    }
}
