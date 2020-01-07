package org.parosproxy.paros.extension.scanner;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.apache.commons.configuration.ConfigurationException;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.core.scanner.HostProcess;
import org.parosproxy.paros.core.scanner.Scanner;
import org.parosproxy.paros.core.scanner.ScannerListener;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.db.RecordAlert;
import org.parosproxy.paros.db.RecordScan;
import org.parosproxy.paros.db.TableAlert;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookMenu;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.extension.history.ManualRequestEditorDialog;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMalformedHeaderException;

public class ExtensionScanner extends ExtensionAdaptor implements ScannerListener, SessionChangedListener, CommandLineListener {
    
    private static final int ARG_SCAN_IDX = 0;
    
	private JMenuItem menuItemScanAll = null;
	private ExtensionHookMenu pluginMenu = null;
	private Scanner scanner = null;
	private SiteMap siteTree = null;
	private SiteNode startNode = null;	
	private AlertTreeModel treeAlert = null;
	
	private JMenu menuScanner = null;
	private JMenuItem menuItemPolicy = null;
	private ProgressDialog progressDialog = null;  
	private JMenuItem menuItemScan = null;
	private AlertPanel alertPanel = null;  
	private RecordScan recordScan = null;
	
	private ManualRequestEditorDialog manualRequestEditorDialog = null;
	private PopupMenuResend popupMenuResend = null;
	private OptionsScannerPanel optionsScannerPanel = null;
	private ScannerParam scannerParam = null;   
	private CommandLineArgument[] arguments = new CommandLineArgument[1];
	private long startTime = 0;

    private PopupMenuScanHistory popupMenuScanHistory = null;
    
    public ExtensionScanner() {
        super();
 		initialize();
    }

    public ExtensionScanner(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionScanner");
			
	}

	private JMenuItem getMenuItemScanAll() {
		if (menuItemScanAll == null) {
			menuItemScanAll = new JMenuItem();
			menuItemScanAll.setText("전체 스캔하기");
			menuItemScanAll.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {
				    menuItemScan.setEnabled(false);
				    menuItemScanAll.setEnabled(false);
				    getAlertPanel().setTabFocus();
				    startScan();
				    
				}
			});

		}
		return menuItemScanAll;
	}
		
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
            extensionHook.getHookMenu().addAnalyseMenuItem(getMenuItemScanAll());
            extensionHook.getHookMenu().addAnalyseMenuItem(getMenuItemScan());
            extensionHook.getHookMenu().addAnalyseMenuItem(getMenuItemPolicy());

            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuResend());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuScanHistory());

            extensionHook.getHookView().addStatusPanel(getAlertPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsScannerPanel());
	    }
        extensionHook.addSessionListener(this);
        extensionHook.addOptionsParamSet(getScannerParam());
        extensionHook.addCommandLine(getCommandLineArguments());

	}
	
	
	void startScan() {
        siteTree = getModel().getSession().getSiteTree();

	    if (startNode == null) {
	        startNode = (SiteNode) siteTree.getRoot();
	    }
	    
        startScan(startNode);
	}
	
	void startScan(SiteNode startNode) {

	    scanner = new Scanner(getScannerParam(), getModel().getOptionsParam().getConnectionParam());
	    scanner.addScannerListener(this);

	    if (getView() != null) {
	        getProgressDialog().setVisible(true);
		    getProgressDialog().setPluginScanner(this);
	        menuItemScanAll.setEnabled(false);
	        menuItemScan.setEnabled(false);
	        getMenuItemPolicy().setEnabled(false);
            getPopupMenuScanHistory().setEnabled(false);
            getAlertPanel().setTabFocus();

	    }
	    
	    try {
	        recordScan = getModel().getDb().getTableScan().insert(getModel().getSession().getSessionId(), getModel().getSession().getSessionName());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
        startTime = System.currentTimeMillis();
	    scanner.start(startNode);
        
	}
	
    public SiteNode getStartNode() {
        return startNode;
    }


    public void scannerComplete() {
	    try {
	        Thread.sleep(1000);
	    } catch (Exception e) {}

        final long scanTime = System.currentTimeMillis() - startTime;
        
	    if (getView() != null) {
	        getMenuItemScanAll().setEnabled(true);
	        getMenuItemScan().setEnabled(true);
	        getMenuItemPolicy().setEnabled(true);
            popupMenuScanHistory.setEnabled(true);

	    }

	    if (getView() != null && progressDialog != null) {
	        if (EventQueue.isDispatchThread()) {
	            progressDialog.dispose();
                progressDialog = null;
                getView().showMessageDialog("Scanning completed in " + scanTime/1000 + "s.  The result can be obtained from Report>Last Scan Result.");
	            return;
	        }
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	                    progressDialog.dispose();
	                    progressDialog = null;
                        getView().showMessageDialog("Scanning completed in " + scanTime/1000 + "s.  The result can be obtained from Report>Last Scan Result.");
	                }
	            });
	        } catch (Exception e) {
	        }
	    }

    }

	private JMenuItem getMenuItemPolicy() {
		if (menuItemPolicy == null) {
			menuItemPolicy = new JMenuItem();
			menuItemPolicy.setText("스캔 방식");
			menuItemPolicy.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					PolicyDialog dialog = new PolicyDialog(getView().getMainFrame());
				    dialog.initParam(getModel().getOptionsParam());
					int result = dialog.showDialog(false);
					if (result == JOptionPane.OK_OPTION) {
					    try {
			                getModel().getOptionsParam().getConfig().save();
			            } catch (ConfigurationException ce) {
			                ce.printStackTrace();
			                getView().showWarningDialog("수정된 스캔 정책을 저장하는 중에 오류가 발생했습니다..");
			                return;
			            }
					}					
				}
			});

		}
		return menuItemPolicy;
	}

    public void hostProgress(String hostAndPort, String msg, int percentage) {
        if (getView() != null) {
            getProgressDialog().updateHostProgress(hostAndPort, msg, percentage);
        }
    }

    public void hostComplete(String hostAndPort) {
        if (getView() != null) {
            getProgressDialog().removeHostProgress(hostAndPort);
        }
    }

    public void hostNewScan(String hostAndPort, HostProcess hostThread) {
        if (getView() != null) {
            getProgressDialog().addHostProgress(hostAndPort, hostThread);
        }
    }
    
    public void alertFound(Alert alert) {

        try {
            writeAlertToDB(alert);
            addAlertToDisplay(alert);
        } catch (Exception e) {
        }
    }

    private void addAlertToDisplay(Alert alert) {

        treeAlert.addPath(alert);
        if (getView() != null) {
            getAlertPanel().expandRoot();
        }
        
    }
 
	private ProgressDialog getProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getView().getMainFrame(), false);
			progressDialog.setSize(500, 460);
		}
		return progressDialog;
	}

    public Scanner getScanner() {
        return scanner;
    }

	private JMenuItem getMenuItemScan() {
		if (menuItemScan == null) {
			menuItemScan = new JMenuItem();
			menuItemScan.setText("스캔하기");
			menuItemScan.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    JTree siteTree = getView().getSiteTreePanel().getTreeSite();
		            SiteNode node = (SiteNode) siteTree.getLastSelectedPathComponent();
		            if (node == null) {
		                getView().showWarningDialog("'사이트' 패널에서 site/folder/URL을 선택해주세요.");
		                return;
		            }
				    menuItemScan.setEnabled(false);
				    menuItemScanAll.setEnabled(false);
	                startScan(node);
	                
				}
			});

		}
		return menuItemScan;
	}

	AlertPanel getAlertPanel() {
		if (alertPanel == null) {
			alertPanel = new AlertPanel();
			alertPanel.setView(getView());
			alertPanel.setSize(345, 122);
			alertPanel.getTreeAlert().setModel(getTreeModel());
		}
		
		return alertPanel;
	}
	
	private DefaultTreeModel getTreeModel() {
	    if (treeAlert == null) {
	        treeAlert = new AlertTreeModel();
	    }
	    return treeAlert;
	}
	
	private void writeAlertToDB(Alert alert) throws HttpMalformedHeaderException, SQLException {

	    TableAlert tableAlert = getModel().getDb().getTableAlert();
        HistoryReference ref = new HistoryReference(getModel().getSession(), HistoryReference.TYPE_SCANNER, alert.getMessage());
        RecordAlert recordAlert = tableAlert.write(
                recordScan.getScanId(), alert.getPluginId(), alert.getAlert(), alert.getRisk(), alert.getReliability(),
                alert.getDescription(), alert.getUri(), alert.getParam(), alert.getOtherInfo(), alert.getSolution(), alert.getReference(),
        		ref.getHistoryId()
                );
        
        alert.setAlertId(recordAlert.getAlertId());
        
	}
	
	public void sessionChanged(Session session) {
	    AlertTreeModel tree = (AlertTreeModel) getAlertPanel().getTreeAlert().getModel();

	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getRoot();
	    
        while (root.getChildCount() > 0) {
            tree.removeNodeFromParent((MutableTreeNode) root.getChildAt(0));
        }
	    
	    try {
            refreshAlert(session);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private void refreshAlert(Session session) throws SQLException {

	    TableAlert tableAlert = getModel().getDb().getTableAlert();
	    Vector v = tableAlert.getAlertListBySession(session.getSessionId());
	    
	    for (int i=0; i<v.size(); i++) {
	        int alertId = ((Integer) v.get(i)).intValue();
	        RecordAlert recAlert = tableAlert.read(alertId);
	        Alert alert = new Alert(recAlert);
	        addAlertToDisplay(alert);
	    }
	}
 
	ManualRequestEditorDialog getManualRequestEditorDialog() {
		if (manualRequestEditorDialog == null) {
			manualRequestEditorDialog = new ManualRequestEditorDialog(getView().getMainFrame(), false, false, this);
			manualRequestEditorDialog.setTitle("다시 전송하기");
			manualRequestEditorDialog.setSize(500, 600);
		}
		return manualRequestEditorDialog;
	}

	private PopupMenuResend getPopupMenuResend() {
		if (popupMenuResend == null) {
			popupMenuResend = new PopupMenuResend();
			popupMenuResend.setExtension(this);
		}
		return popupMenuResend;
	}

	private OptionsScannerPanel getOptionsScannerPanel() {
		if (optionsScannerPanel == null) {
			optionsScannerPanel = new OptionsScannerPanel();
		}
		return optionsScannerPanel;
	}

	private ScannerParam getScannerParam() {
		if (scannerParam == null) {
			scannerParam = new ScannerParam();
		}
		return scannerParam;
	}

    public void execute(CommandLineArgument[] args) {

        if (arguments[ARG_SCAN_IDX].isEnabled()) {
            System.out.println("스캐닝 시작합니다.");
            startScan();
        } else {
            return;
        }

        while (!getScanner().isStop()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("스캐닝을 완료했습니다.");

    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_SCAN_IDX] = new CommandLineArgument("-scan", 0, null, "", "-scan : Run vulnerability scan depending on previously saved policy.");
        return arguments;
    }

    private PopupMenuScanHistory getPopupMenuScanHistory() {
        if (popupMenuScanHistory == null) {
            popupMenuScanHistory = new PopupMenuScanHistory();
            popupMenuScanHistory.setExtension(this);
        }
        return popupMenuScanHistory;
    }
	
          }
