package org.parosproxy.paros.view;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.control.MenuFileControl;
import org.parosproxy.paros.control.MenuToolsControl;

import javax.swing.JMenu;

public class MainMenuBar extends JMenuBar {
	private javax.swing.JMenu menuEdit = null;
	private javax.swing.JMenu menuTools = null;
	private javax.swing.JMenu menuView = null;
	private javax.swing.JMenuItem menuToolsOptions = null;
	private javax.swing.JMenu menuFile = null;
	private javax.swing.JMenuItem menuFileNewSession = null;
	private javax.swing.JMenuItem menuFileOpen = null;
	private javax.swing.JMenuItem menuFileSaveAs = null;
	private javax.swing.JMenuItem menuFileExit = null;
	private JMenuItem menuFileProperties = null;
	private JMenuItem menuFileSave = null;
	private JMenu menuHelp = null;
	private JMenuItem menuHelpAbout = null;
    private JMenu menuAnalyse = null;

	public MainMenuBar() {
		super();
		initialize();
	}

	private void initialize() {
        this.add(getMenuFile());
        this.add(getMenuEdit());
        this.add(getMenuAnalyse());
		
	}
 
	public javax.swing.JMenu getMenuEdit() {
		if (menuEdit == null) {
			menuEdit = new javax.swing.JMenu();
			menuEdit.setText("편집");
		}
		return menuEdit;
	}

	private javax.swing.JMenuItem getMenuToolsOptions() {
		if (menuToolsOptions == null) {
			menuToolsOptions = new javax.swing.JMenuItem();
			menuToolsOptions.setText("옵션");
			menuToolsOptions.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					getMenuToolsControl().options();
					
				}
			});

		}
		return menuToolsOptions;
	}
   
	public javax.swing.JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new javax.swing.JMenu();
			menuFile.setText("파일");
			menuFile.setMnemonic(java.awt.event.KeyEvent.VK_F);
			menuFile.add(getMenuFileNewSession());
			menuFile.add(getMenuFileOpen());
			menuFile.addSeparator();
			menuFile.add(getMenuFileSave());
			menuFile.add(getMenuFileSaveAs());
			menuFile.addSeparator();
			menuFile.add(getMenuFileProperties());
			menuFile.addSeparator();
			menuFile.addSeparator();
			menuFile.add(getMenuFileExit());
		}
		return menuFile;
	}
 
	private javax.swing.JMenuItem getMenuFileNewSession() {
		if (menuFileNewSession == null) {
			menuFileNewSession = new javax.swing.JMenuItem();
			menuFileNewSession.setText("새로 만들기");
			menuFileNewSession.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
					try {
                        getMenuFileControl().newSession(true);
                        getMenuFileSave().setEnabled(false);
                    } catch (Exception e1) {
                        View.getSingleton().showWarningDialog("새로 만드는 중, 오류가 발생했습니다.");
                        e1.printStackTrace();
                    }
				}
			});

		}
		return menuFileNewSession;
	}
  
	private javax.swing.JMenuItem getMenuFileOpen() {
		if (menuFileOpen == null) {
			menuFileOpen = new javax.swing.JMenuItem();
			menuFileOpen.setText("불러오기");
			menuFileOpen.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {
					getMenuFileControl().openSession();
			        getMenuFileSave().setEnabled(true);


				}
			});

		}
		return menuFileOpen;
	}

	private javax.swing.JMenuItem getMenuFileSaveAs() {
		if (menuFileSaveAs == null) {
			menuFileSaveAs = new javax.swing.JMenuItem();
			menuFileSaveAs.setText("다른 이름으로 저장하기");
			menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
				    getMenuFileControl().saveAsSession();


				}
			});

		}
		return menuFileSaveAs;
	}

	private javax.swing.JMenuItem getMenuFileExit() {
		if (menuFileExit == null) {
			menuFileExit = new javax.swing.JMenuItem();
			menuFileExit.setText("종료 하기");
			menuFileExit.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getMenuFileControl().exit();
				}
			});

		}
		return menuFileExit;
	}
 
	public MenuFileControl getMenuFileControl() {
		return Control.getSingleton().getMenuFileControl();
	}
  
	private MenuToolsControl getMenuToolsControl() {
		return Control.getSingleton().getMenuToolsControl();
	}
   
	private JMenuItem getMenuFileProperties() {
		if (menuFileProperties == null) {
			menuFileProperties = new JMenuItem();
			menuFileProperties.setText("세션 속성 보기");
			menuFileProperties.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
				    getMenuFileControl().properties();
				}
			});

		}
		return menuFileProperties;
	}
 
	public JMenuItem getMenuFileSave() {
		if (menuFileSave == null) {
			menuFileSave = new JMenuItem();
			menuFileSave.setText("저장하기");
			menuFileSave.setEnabled(false);
			menuFileSave.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					getMenuFileControl().saveSession();
					
				}
			});

		}
		return menuFileSave;
	}
 
    public JMenu getMenuAnalyse() {
        if (menuAnalyse == null) {
            menuAnalyse = new JMenu();
            menuAnalyse.setText("스캐너");
        }
        return menuAnalyse;
    }
   }
