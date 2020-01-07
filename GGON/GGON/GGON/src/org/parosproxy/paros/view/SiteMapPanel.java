package org.parosproxy.paros.view;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.InputEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;

public class SiteMapPanel extends JPanel {

	private JScrollPane jScrollPane = null;
	private JTree treeSite = null;
	private TreePath rootTreePath = null;
	private View view = null;

	public SiteMapPanel() {
		super();
		initialize();
	}
	
	private View getView() {
	    if (view == null) {
	        view = View.getSingleton();
	    }
	    
	    return view;
	}

	private  void initialize() {
		this.setLayout(new CardLayout());
		this.setSize(300,200);
		this.add(getJScrollPane(), getJScrollPane().getName());
        expandRoot();
	}
 
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTreeSite());
			jScrollPane.setPreferredSize(new java.awt.Dimension(200,400));
			jScrollPane.setName("jScrollPane");
		}
		return jScrollPane;
	}
  
	public JTree getTreeSite() {
		if (treeSite == null) {
			treeSite = new JTree();
			treeSite.setShowsRootHandles(true);
			treeSite.setName("����Ʈ Ʈ��");
			treeSite.setToggleClickCount(1);
			treeSite.addMouseListener(new java.awt.event.MouseAdapter() { 

				public void mousePressed(java.awt.event.MouseEvent e) {    
	          		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
	            		View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
	            	}
				} 
			});

			treeSite.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() { 

				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {    

				    HttpMessage msg = null;
				    SiteNode node = (SiteNode) treeSite.getLastSelectedPathComponent();
				    if (node == null)
				        return;
				    if (!node.isRoot()) {
                        try {
                            msg = node.getHistoryReference().getHttpMessage();
                        } catch (Exception e1) {
                            return;
                            
                        }

                        HttpPanel reqPanel = getView().getRequestPanel();
				        HttpPanel resPanel = getView().getResponsePanel();
				        reqPanel.setMessage(msg, true);
			            resPanel.setMessage(msg, false);

				    }
	
				}
			});

		}
		return treeSite;
	}
	
	public void expandRoot() {
        TreeNode root = (TreeNode) treeSite.getModel().getRoot();
        if (rootTreePath == null || root != rootTreePath.getPathComponent(0)) {
            rootTreePath = new TreePath(root);
        }
	    
		if (EventQueue.isDispatchThread()) {
		    getTreeSite().expandPath(rootTreePath);
		    return;
		}
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
				    getTreeSite().expandPath(rootTreePath);
				}
			});
		} catch (Exception e) {
		}
	}
  }
