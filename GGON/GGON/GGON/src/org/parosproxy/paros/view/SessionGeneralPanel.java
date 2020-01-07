package org.parosproxy.paros.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.parosproxy.paros.model.Session;

import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.CardLayout;

public class SessionGeneralPanel extends AbstractParamPanel {

	private JPanel panelSession = null;  
	private JTextField txtSessionName = null;
	private JTextArea txtDescription = null;
	private Session session = null;
	
    public SessionGeneralPanel() {
        super();
 		initialize();
   }

    private static final String[] ROOT = {};
    private static final String[] GENERAL = {"general"};
    private static final String[] MISCELLENOUS = {"Miscellenous"};

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName("일반");
        this.add(getPanelSession(), getPanelSession().getName());
	}

	private JPanel getPanelSession() {
		if (panelSession == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			panelSession = new JPanel();
			javax.swing.JLabel jLabel = new JLabel();

			javax.swing.JLabel jLabel1 = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();

			panelSession.setLayout(new GridBagLayout());
			jLabel.setText("세션 이름");
			jLabel1.setText("내용");
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.insets = new java.awt.Insets(2,0,2,0);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.insets = new java.awt.Insets(2,0,2,0);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new java.awt.Insets(2,0,2,0);
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			panelSession.setName("General");
			panelSession.setSize(700, 400);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			panelSession.add(jLabel, gridBagConstraints2);
			panelSession.add(getTxtSessionName(), gridBagConstraints9);
			panelSession.add(jLabel1, gridBagConstraints10);
			panelSession.add(getTxtDescription(), gridBagConstraints11);
		}
		return panelSession;
	}
 
	private JTextField getTxtSessionName() {
		if (txtSessionName == null) {
			txtSessionName = new JTextField();
		}
		return txtSessionName;
	}
  
	private JTextArea getTxtDescription() {
		if (txtDescription == null) {
			txtDescription = new JTextArea();
			txtDescription.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			txtDescription.setLineWrap(true);
			txtDescription.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
		}
		return txtDescription;
	}
	
	public void initParam(Object obj) {
	    Session session = (Session) obj;
	    getTxtSessionName().setText(session.getSessionName());
	    getTxtDescription().setText(session.getSessionDesc());
	}
	
	public void validateParam(Object obj) {
	}
	
	public void saveParam (Object obj) throws Exception {
	    Session session = (Session) obj;
	    session.setSessionName(getTxtSessionName().getText());
	    session.setSessionDesc(getTxtDescription().getText());
	    
	}
	
     }  
