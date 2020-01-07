package org.parosproxy.paros.extension.scanner;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.InputEvent;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.HttpPanel;

public class AlertPanel extends AbstractPanel {

    private ViewDelegate view = null;
	private JTree treeAlert = null;
	private TreePath rootTreePath = null;	
	private DefaultTreeModel treeModel = null;
	
	private JScrollPane paneScroll = null;

    public AlertPanel() {
        super();
 		initialize();
    }

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setSize(474, 251);
        this.setName("∞Ê∞Ì√¢");
        this.add(getPaneScroll(), getPaneScroll().getName());
			
	}
  
	JTree getTreeAlert() {
		if (treeAlert == null) {
			treeAlert = new JTree();
			treeAlert.setName("Alert");
			treeAlert.setShowsRootHandles(true);
			treeAlert.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			treeAlert.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
				    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
				        view.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				    }	
				}
			});
			treeAlert.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() { 
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
				    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeAlert.getLastSelectedPathComponent();
				    if (node.getUserObject() != null) {
				        Object obj = node.getUserObject();
				        if (obj instanceof Alert) {
				            Alert alert = (Alert) obj;
						    setMessage(alert.getMessage());

				        }
				    }
				}
			});
		}
		return treeAlert;
	}

	private JScrollPane getPaneScroll() {
		if (paneScroll == null) {
			paneScroll = new JScrollPane();
			paneScroll.setName("paneScroll");
			paneScroll.setViewportView(getTreeAlert());
		}
		return paneScroll;
	}
	
	void setView(ViewDelegate view) {
	    this.view = view;
	}

    private ViewDelegate getView() {
        return view;
    }

    
	public void expandRoot() {
        TreeNode root = (TreeNode) getTreeAlert().getModel().getRoot();
        if (rootTreePath == null || root != rootTreePath.getPathComponent(0)) {
            rootTreePath = new TreePath(root);
        }
	    
		if (EventQueue.isDispatchThread()) {
		    getTreeAlert().expandPath(rootTreePath);
		    return;
		}
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
				    getTreeAlert().expandPath(rootTreePath);
				}
			});
		} catch (Exception e) {
		}
	}
	
	private void setMessage(HttpMessage msg) {
	    HttpPanel requestPanel = getView().getRequestPanel();
	    HttpPanel responsePanel = getView().getResponsePanel();
	    requestPanel.setMessage("","", true);
	    if (!msg.getRequestHeader().isEmpty()) {
	        requestPanel.setMessage(msg.getRequestHeader().toString(), msg.getRequestBody().toString(), true);
	    }

	    responsePanel.setMessage("","", false);
	    if (!msg.getResponseHeader().isEmpty()) {
	        responsePanel.setMessage(msg.getResponseHeader().toString(), msg.getResponseBody().toString(), false);
	    }

	}
    }  
