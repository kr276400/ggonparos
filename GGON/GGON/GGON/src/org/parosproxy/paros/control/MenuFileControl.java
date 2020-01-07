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
			if (view.showConfirmDialog("현재 세션이 저장되지 않았습니다.  취소하시겠습니까?") != JOptionPane.OK_OPTION) {
				return;
			}
			model.getSession().discard();
	    }

	    WaitMessageDialog dialog = view.getWaitMessageDialog("데이터베이스와 서버를 종료하는 중입니다...");

	    Thread t = new Thread(new Runnable() {
	        public void run() {
	            control.shutdown(false);
	    	    log.info(Constant.PROGRAM_TITLE + " 제거되었습니다.");
	    		System.exit(0);   
	        }
	    });
	    t.start();
	    dialog.setVisible(true);
	    // 사용자에게 다이얼로그의 프레임을 보여주게끔 하는 부분
	    
	}
	
	public void newSession(boolean isPromptNewSession) throws ClassNotFoundException, Exception {
		if (isPromptNewSession) {
		    if (model.getSession().isNewState()) {
				if (view.showConfirmDialog("현재 세션이 저장되어 있지 않습니다.  삭제하고 새로 세션을 만드시겠습니까?") != JOptionPane.OK_OPTION) {
					return;
				}
				model.getSession().discard();
		    } else if (view.showConfirmDialog("현재 세션이 종료됩니다.  새로 세션을 만드시겠습니까?") != JOptionPane.OK_OPTION) {
				return;
			}
			model.createAndOpenUntitledDb();
		}
		
		Session session = new Session(model);
	    log.info("새로운 세션 파일이 생성되었습니다");
	    model.setSession(session);

		view.getSiteTreePanel().getTreeSite().setModel(session.getSiteTree());

		// 코드에 상태를 얘기하고, 새로운 세션은 이름도 없고 사용한적 없는 것들을 먼저 사용한다
		control.getExtensionLoader().sessionChangedAllPlugin(session);
		// 디스플레이 재조명
		
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
	    	    log.info("세션 파일을 여는 중 " + file.getAbsolutePath());
	    	    waitMessageDialog = view.getWaitMessageDialog("세션 파일을 불러오는 중입니다.  잠시만 기다려주십시오 ...");
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
		    view.showWarningDialog("다른 이름으로 저장하기를 사용해주세요...");
		    return;
	    }
	    
		try {
    	    waitMessageDialog = view.getWaitMessageDialog("세션 파일을 저장하는 중입니다.  잠시만 기다려주세요 ...");		    
    		session.save(session.getFileName(), this);
    	    log.info("세션파일을 저장하는 중 " + session.getFileName());
    	    waitMessageDialog.setVisible(true);
    	    // 맨 윗줄에 메시지를 보이게 한다고 중간꺼 아니다
    	    
		} catch (Exception e) {
		    view.showWarningDialog("세션 파일 저장하는 중에 오류가 생겼습니다.");
    	    log.error("문제가 있는 세션파일을 저장하는 중 " + session.getFileName());
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
	    	    waitMessageDialog = view.getWaitMessageDialog("새로운 세션 파일을 저장하는 중입니다.  잠시만 기다려주세요 ...");
	    	    session.save(fileName, this);
        	    log.info("세션 파일로 저장 " + session.getFileName());
        	    waitMessageDialog.setVisible(true);
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}
	    }
	}
	
	public void properties() {
	    SessionDialog dialog = view.getSessionDialog("세션 속성");
	    dialog.initParam(model.getSession());
	    dialog.showDialog(false);
	}
	/*
	 * 자바 문서가 아닐경우
	 * paros.model.SessionListener#sessionOpened(java.lang.Exception) 파일 봐라
	 */
    public void sessionOpened(File file, Exception e) {
        if (e == null) {
            control.getExtensionLoader().sessionChangedAllPlugin(model.getSession());
            view.getMainFrame().setTitle(file.getName().replaceAll(".session\\z","") + " - " + Constant.PROGRAM_NAME);
        } else {
            view.showWarningDialog("세션 파일을 여는 도중에 문제가 발생했습니다");
            log.error("세션 파일을 여는 도중 생긴 오류 " + file.getAbsolutePath());
            log.error(e.getMessage());
        }

        if (waitMessageDialog != null) {
            waitMessageDialog.setVisible(false);
            waitMessageDialog = null;
        }
    }

    /* 
     * 자바 문서가 아닌경우
     * Paros.model.SessionListener#sessionSaved(java.lang.Exception) 봐라
     */
    public void sessionSaved(Exception e) {
        if (e == null) {
            view.getMainFrame().getMainMenuBar().getMenuFileSave().setEnabled(true);
            File file = new File(model.getSession().getFileName());
            view.getMainFrame().setTitle(file.getName().replaceAll(".session\\z","") + " - " + Constant.PROGRAM_NAME);
        } else {
		    view.showWarningDialog("세션 파일을 저장하는 도중 문제가 생겼습니다.");
		    e.printStackTrace();
    	    log.error("세션 파일을 저장하는 도중 생긴 오류 " + model.getSession().getFileName());
    	    log.error(e.getStackTrace());

        }
        
        if (waitMessageDialog != null) {
            waitMessageDialog.setVisible(false);
            waitMessageDialog = null;
        }

    }
}
