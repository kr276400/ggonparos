package org.parosproxy.paros.extension.spider;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JTree;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.core.spider.Spider;
import org.parosproxy.paros.core.spider.SpiderListener;
import org.parosproxy.paros.core.spider.SpiderParam;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookMenu;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;

public class ExtensionSpider extends ExtensionAdaptor implements SpiderListener, SessionChangedListener, CommandLineListener {
    
    private static final int ARG_SPIDER_IDX = 0;
    private static final int ARG_URL_IDX = 1;
	private JMenuItem menuItemSpider = null; 
	private SpiderDialog dialog = null;  
	private Spider spider = null;
	private SiteMap siteTree = null;
	private SiteNode startNode = null;
		
	private PopupMenuSpider popupMenuSpider = null;  
	private SpiderPanel spiderPanel = null;
	private OptionsSpiderPanel optionsSpiderPanel = null;
	private SpiderParam spiderParam = null;   
	private CommandLineArgument[] arguments = new CommandLineArgument[2];

    public ExtensionSpider() {
        super();
 		initialize();
    }

    public ExtensionSpider(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionSpider");
			
	}
 
	JMenuItem getMenuItemSpider() {
		if (menuItemSpider == null) {
			menuItemSpider = new JMenuItem();
			menuItemSpider.setText("스파이더");
			menuItemSpider.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {
				    JTree siteTree = getView().getSiteTreePanel().getTreeSite();
		            SiteNode node = (SiteNode) siteTree.getLastSelectedPathComponent();
		            HttpMessage msg = null;
		            if (node == null) {
		                getView().showWarningDialog("먼저, 브라우저를 통해서 웹 사이트에 방문 하셔야합니다, 그리고 '사이트' 패널에 있는 URL/folder/node를 선택해주세요.");
		                return;
		            }
	                setStartNode(node);
	                if (node.isRoot()) {
	                    showDialog("전체 사이트를 크롤링합니다.");
	                } else {
                        try {
                            msg = node.getHistoryReference().getHttpMessage();
                        } catch (Exception e1) {
                            return;
                        }
                        String tmp = msg.getRequestHeader().getURI().toString();
                        showDialog(tmp);
	                }
	                
	                
				}
			});

		}
		return menuItemSpider;
	}
	
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        extensionHook.getHookMenu().addAnalyseMenuItem(getMenuItemSpider());
            extensionHook.getHookMenu().addAnalyseMenuItem(extensionHook.getHookMenu().getMenuSeparator());

            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpider());
	        extensionHook.getHookView().addStatusPanel(getSpiderPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsSpiderPanel());
	    }
        extensionHook.addSessionListener(this);
        extensionHook.addOptionsParamSet(getSpiderParam());

        extensionHook.addCommandLine(getCommandLineArguments());
	}
	
	public void startSpider() {
        siteTree = getModel().getSession().getSiteTree();

	    if (startNode == null) {
	        startNode = (SiteNode) siteTree.getRoot();
	    }
        startSpider(startNode);
	}
	
	private void startSpider(SiteNode startNode) {

	    if (spider == null) {
	        try {
                getModel().getDb().getTableHistory().deleteHistoryType(getModel().getSession().getSessionId(), HistoryReference.TYPE_SPIDER_SEED);
    	        getModel().getDb().getTableHistory().deleteHistoryType(getModel().getSession().getSessionId(), HistoryReference.TYPE_SPIDER_VISITED);

	        } catch (SQLException e) {
                e.printStackTrace();
            }
	        
	        spider = new Spider(getSpiderParam(), getModel().getOptionsParam().getConnectionParam(), getModel());
	        spider.addSpiderListener(this);

	        inOrderSeed(spider, startNode);

	    }
	    
	    getSpiderPanel().setTabFocus();

		try {
			spider.start();
		    
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
	}
	
	private void inOrderSeed(Spider spider, SiteNode node) {

	    try {
	        if (!node.isRoot()) {
	            HttpMessage msg = node.getHistoryReference().getHttpMessage();
	            if (msg != null) {
	                if (!msg.getResponseHeader().isImage()) {
	                    spider.addSeed(msg);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    if (!node.isLeaf()) {
	        for (int i=0; i<node.getChildCount(); i++) {
	            try {
	                inOrderSeed(spider, (SiteNode) node.getChildAt(i));
	            } catch (Exception e) {}
	        }
	    }
	}
	
	public void spiderComplete() {

        try {
            getModel().getDb().getTableHistory().deleteHistoryType(getModel().getSession().getSessionId(), HistoryReference.TYPE_SPIDER_SEED);
	        getModel().getDb().getTableHistory().deleteHistoryType(getModel().getSession().getSessionId(), HistoryReference.TYPE_SPIDER_VISITED);

        } catch (SQLException e) {
            e.printStackTrace();
        }

	    if (getView() != null) {
	        getMenuItemSpider().setEnabled(true);
	        getPopupMenuSpider().setEnabled(true);

	    }

	    try {
	        Thread.sleep(3000);
	    } catch (Exception e) {}
	    
	    if (getView() != null && dialog != null) {
	        if (EventQueue.isDispatchThread()) {
	            dialog.dispose();
	            return;
	        }
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	                    dialog.dispose();
	                }
	            });
	        } catch (Exception e) {
	        }
	    }
	}
	
	public void foundURI(HttpMessage msg, boolean isSkip) {
	    if (getView() != null) {
	        if (isSkip) {
	            getSpiderPanel().appendFoundButSkip(msg.getRequestHeader().getURI().toString() + "\n");
	        } else {
	            getSpiderPanel().appendFound(msg.getRequestHeader().getURI().toString() + "\n");
	        }
        }
	}
	
	public void readURI(HttpMessage msg) {

	    SiteMap siteTree = getModel().getSession().getSiteTree();

	    // 사이트맵에 적는 부분인데, 만약에 메시지가 존재하지 않을때
		HttpMessage existing = siteTree.pollPath(msg);

		HistoryReference historyRef = null;
        try {
            historyRef = new HistoryReference(getModel().getSession(), HistoryReference.TYPE_SPIDER, msg);
        } catch (Exception e) {}
        siteTree.addPath(historyRef, msg);

	}

    public Spider getSpider() {
	    return spider;
	}
	
	public void spiderProgress(final URI uri, final int percentageComplete, final int numberCrawled, final int numberToCrawl) {
	    String uriString= "";
	    
	    if (dialog != null) {
	        if (EventQueue.isDispatchThread()) {
	            dialog.getTxtNumCrawled().setText(Integer.toString(numberCrawled));
	            dialog.getTxtOutstandingCrawl().setText(Integer.toString(numberToCrawl));

	            dialog.getProgressBar().setValue(percentageComplete);
	            uriString = "";
	            if (uri != null) {
	                uriString = uri.toString();
	            }
	            dialog.getTxtDisplay().setText(uriString);
	            
	            return;
	        }
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	                    String uriString = "";
	    	            dialog.getTxtNumCrawled().setText(Integer.toString(numberCrawled));
	    	            dialog.getTxtOutstandingCrawl().setText(Integer.toString(numberToCrawl));

	    	            dialog.getProgressBar().setValue(percentageComplete);
	    	            if (uri != null) {
	    	                uriString = uri.toString();
	    	            }
	    	            dialog.getTxtDisplay().setText(uriString);
	    	            
	                }
	            });
	        } catch (Exception e) {
	        }
	        
	    }

	    
	}

    public SiteNode getStartNode() {
        return startNode;
    }

    public void setStartNode(SiteNode startNode) {
        this.startNode = startNode;
    }
    
    void showDialog(String msg) {
		dialog = new SpiderDialog(getView().getMainFrame(), false);
		dialog.setPlugin(ExtensionSpider.this);
		dialog.setVisible(true);
		dialog.getTxtDisplay().setText(msg);
		spider = null;

    }
   
	PopupMenuSpider getPopupMenuSpider() {
		if (popupMenuSpider == null) {
			popupMenuSpider = new PopupMenuSpider();


			popupMenuSpider.setExtension(this);
		}
		return popupMenuSpider;
	}
	
	void clear() {
	    spider = null;
	    System.gc();
	}
  
	private SpiderPanel getSpiderPanel() {
		if (spiderPanel == null) {
			spiderPanel = new SpiderPanel();
		}
		return spiderPanel;
	}

    public void sessionChanged(Session session) {
        getSpiderPanel().clear();
        
    }
 
	private OptionsSpiderPanel getOptionsSpiderPanel() {
		if (optionsSpiderPanel == null) {
			optionsSpiderPanel = new OptionsSpiderPanel();
		}
		return optionsSpiderPanel;
	}
 
	private SpiderParam getSpiderParam() {
		if (spiderParam == null) {
			spiderParam = new SpiderParam();
		}
		return spiderParam;
	}

    public void execute(CommandLineArgument[] args) {
        String uri = null;

        if (!arguments[ARG_URL_IDX].isEnabled() && (arguments[ARG_SPIDER_IDX].isEnabled())) {
            return;
        }
        
        spider = new Spider(getSpiderParam(), getModel().getOptionsParam().getConnectionParam(), getModel());
        spider.addSpiderListener(this);

        if (arguments[ARG_URL_IDX].isEnabled()) {
            Vector v = arguments[ARG_URL_IDX].getArguments();
            for (int i=0; i<v.size(); i++) {
                uri = (String) v.get(i);
                try {
                    System.out.println("씨드를 더하는 중입니다. " + uri);
                    spider.addSeed(new URI(uri, true));
                } catch (URIException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (arguments[ARG_SPIDER_IDX].isEnabled()) {
            System.out.println("스파이더를 시작합니다");
            spider.start();
        }

        while (!spider.isStop()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("스파이더 완료했습니다.");

    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_SPIDER_IDX] = new CommandLineArgument("-spider", 0, null, "", "-spider : run spider.  See other parameters");
        arguments[ARG_URL_IDX] = new CommandLineArgument("-seed", -1, "https{0,1}://\\S+", "Seed should be a URL", "-seed {URL1} {URL2} ... : Add seeds to the spider for crawling.");
        return arguments;
    }

  }  
