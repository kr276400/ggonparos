package org.parosproxy.paros.view;

import javax.swing.JPanel;

import java.awt.CardLayout;

public class WorkbenchPanel extends JPanel {
	private javax.swing.JSplitPane splitVert = null;
	private javax.swing.JSplitPane splitHoriz = null;
	private javax.swing.JPanel paneStatus = null;
	private javax.swing.JPanel paneSelect = null;
	private javax.swing.JPanel paneWork = null;
	private org.parosproxy.paros.view.TabbedPannel tabbedStatus = null;
	private org.parosproxy.paros.view.TabbedPannel tabbedWork = null;
	private org.parosproxy.paros.view.TabbedPannel tabbedSelect = null;

	public WorkbenchPanel() {
		super();
		initialize();
	}

	private  void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();

		this.setLayout(new java.awt.GridBagLayout());
		this.setSize(1000, 600);
		this.setPreferredSize(new java.awt.Dimension(600,400));
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.weighty = 1.0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		this.add(getSplitVert(), consGridBagConstraints1);
	}
 
	private javax.swing.JSplitPane getSplitVert() {
		if (splitVert == null) {
			splitVert = new javax.swing.JSplitPane();
			splitVert.setDividerLocation(480);
			splitVert.setDividerSize(4);
			splitVert.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			splitVert.setResizeWeight(0.5D);
			splitVert.setPreferredSize(new java.awt.Dimension(1000,600));
			splitVert.setLeftComponent(getSplitHoriz());
			splitVert.setRightComponent(getPaneStatus());
			splitVert.setContinuousLayout(false);
		}
		return splitVert;
	}
  
	private javax.swing.JSplitPane getSplitHoriz() {
		if (splitHoriz == null) {
			splitHoriz = new javax.swing.JSplitPane();
			splitHoriz.setLeftComponent(getPaneWork());
			splitHoriz.setRightComponent(getPaneSelect());
			//site랑 요청 위치 바꾸기
			splitHoriz.setDividerLocation(500);
			splitHoriz.setDividerSize(3);
			splitHoriz.setResizeWeight(0.3D);
			splitHoriz.setPreferredSize(new java.awt.Dimension(800,400));
			splitHoriz.setContinuousLayout(false);
			splitHoriz.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		return splitHoriz;
	}

	private javax.swing.JPanel getPaneStatus() {
		if (paneStatus == null) {
			paneStatus = new javax.swing.JPanel();
			paneStatus.setLayout(new CardLayout());
			paneStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			paneStatus.add(getTabbedStatus(), getTabbedStatus().getName());
		}
		return paneStatus;
	}

	private javax.swing.JPanel getPaneSelect() {
		if (paneSelect == null) {
			paneSelect = new javax.swing.JPanel();
			paneSelect.setLayout(new CardLayout());
			paneSelect.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			paneSelect.add(getTabbedSelect(), getTabbedSelect().getName());
		}
		return paneSelect;
	}
  
	private javax.swing.JPanel getPaneWork() {
		if (paneWork == null) {
			paneWork = new javax.swing.JPanel();
			paneWork.setLayout(new CardLayout());
			paneWork.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			paneWork.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			paneWork.add(getTabbedWork(), getTabbedWork().getName());
		}
		return paneWork;
	}

	public org.parosproxy.paros.view.TabbedPannel getTabbedStatus() {
		if (tabbedStatus == null) {
			tabbedStatus = new org.parosproxy.paros.view.TabbedPannel();
			tabbedStatus.setPreferredSize(new java.awt.Dimension(800,200));
			tabbedStatus.setTabPlacement(javax.swing.JTabbedPane.TOP);
			// 위에는 스파이더 OUTPUT 위치 
			tabbedStatus.setName("tabbedStatus");
			tabbedStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		return tabbedStatus;
	}
 
	public org.parosproxy.paros.view.TabbedPannel getTabbedWork() {
		if (tabbedWork == null) {
			tabbedWork = new org.parosproxy.paros.view.TabbedPannel();
			tabbedWork.setPreferredSize(new java.awt.Dimension(600,400));
			tabbedWork.setName("tabbedWork");
			tabbedWork.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		return tabbedWork;
	}

	public org.parosproxy.paros.view.TabbedPannel getTabbedSelect() {
		if (tabbedSelect == null) {
			tabbedSelect = new org.parosproxy.paros.view.TabbedPannel();
			tabbedSelect.setPreferredSize(new java.awt.Dimension(200,400));
			tabbedSelect.setName("tabbedSelect");
			tabbedSelect.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
		}
		
		return tabbedSelect;
	}

        }
