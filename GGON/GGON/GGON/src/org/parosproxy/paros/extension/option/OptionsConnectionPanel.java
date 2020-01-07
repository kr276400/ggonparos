package org.parosproxy.paros.extension.option;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.network.ConnectionParam;
import org.parosproxy.paros.view.AbstractParamPanel;

public class OptionsConnectionPanel extends AbstractParamPanel {

    private static final String[] ROOT = {};
	private JCheckBox chkUseProxyChain = null;
	private JPanel jPanel = null;
	private JPanel panelProxyAuth = null;
	private JScrollPane jScrollPane = null;
	private OptionsParam optionsParam = null;
	private JPanel panelProxyChain = null;
	private JTextField txtProxyChainName = null;
	private JTextField txtProxyChainPort = null;
	private JTextArea txtProxyChainSkipName = null;
	private JTextField txtProxyChainRealm = null;
	private JTextField txtProxyChainUserName = null;
	private JTextField txtProxyChainPassword = null;
	private JCheckBox chkProxyChainAuth = null;
    public OptionsConnectionPanel() {
        super();
 		initialize();
   }

	private JCheckBox getChkUseProxyChain() {
		if (chkUseProxyChain == null) {
			chkUseProxyChain = new JCheckBox();
			chkUseProxyChain.setText("�ܺ� ���Ͻ� ���� �̿�");
			chkUseProxyChain.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
					setProxyChainEnabled(chkUseProxyChain.isSelected());
				}
			});

		}
		return chkUseProxyChain;
	}
  
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints71 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints61 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints51 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints41 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints15 = new GridBagConstraints();

			javax.swing.JLabel jLabel7 = new JLabel();

			javax.swing.JLabel jLabel6 = new JLabel();

			javax.swing.JLabel jLabel5 = new JLabel();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jLabel5.setText("�ּ�/������ �̸�:");
			jLabel6.setText("��Ʈ (�� 8080):");
			jLabel7.setText("<html><p>�Ʒ��� �ִ� ������ �̸��̳� IP �ּҴ� �Ѱ��ּ��� (������ ���ڱ�ȣ�� '*'��, �̸��� ';'�� ������ ���ϴ�.):</p></html>");
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.weightx = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 0.5D;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 0.5D;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.ipadx = 50;
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.gridy = 2;
			gridBagConstraints41.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.weightx = 0.5D;
			gridBagConstraints51.gridx = 1;
			gridBagConstraints51.gridy = 2;
			gridBagConstraints51.weightx = 0.5D;
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.ipadx = 50;
			gridBagConstraints61.gridx = 0;
			gridBagConstraints61.gridy = 3;
			gridBagConstraints61.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.weightx = 1.0D;
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.NORTHEAST;
			gridBagConstraints71.gridx = 0;
			gridBagConstraints71.gridy = 4;
			gridBagConstraints71.weightx = 1.0D;
			gridBagConstraints71.weighty = 0.2D;
			gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints71.anchor = java.awt.GridBagConstraints.NORTHEAST;
			gridBagConstraints71.gridwidth = 2;
			gridBagConstraints71.ipady = 20;
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "���Ͻ� ü�� ���", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11), java.awt.Color.black));
			jPanel.add(getChkUseProxyChain(), gridBagConstraints15);
			jPanel.add(jLabel5, gridBagConstraints2);
			jPanel.add(getTxtProxyChainName(), gridBagConstraints3);
			jPanel.add(jLabel6, gridBagConstraints41);
			jPanel.add(getTxtProxyChainPort(), gridBagConstraints51);
			jPanel.add(jLabel7, gridBagConstraints61);
			jPanel.add(getJScrollPane(), gridBagConstraints71);
		}
		return jPanel;
	}
 
	private JPanel getPanelProxyAuth() {
		if (panelProxyAuth == null) {
			java.awt.GridBagConstraints gridBagConstraints72 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints62 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints52 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints42 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints31 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints16 = new GridBagConstraints();

			javax.swing.JLabel jLabel11 = new JLabel();

			javax.swing.JLabel jLabel10 = new JLabel();

			javax.swing.JLabel jLabel9 = new JLabel();

			panelProxyAuth = new JPanel();
			panelProxyAuth.setLayout(new GridBagLayout());
			jLabel9.setText("����:");
			jLabel10.setText("����� �̸�:");
			jLabel11.setText("������ ��й�ȣ:");
			panelProxyAuth.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "���Ͻ� ����", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11), java.awt.Color.black));
			panelProxyAuth.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridwidth = 2;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.weightx = 0.5D;
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.gridy = 1;
			gridBagConstraints31.weightx = 0.5D;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.ipadx = 50;
			gridBagConstraints42.gridx = 0;
			gridBagConstraints42.gridy = 2;
			gridBagConstraints42.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints42.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints42.weightx = 0.5D;
			gridBagConstraints42.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints52.gridx = 1;
			gridBagConstraints52.gridy = 2;
			gridBagConstraints52.weightx = 0.5D;
			gridBagConstraints52.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints52.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints52.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints52.ipadx = 50;
			gridBagConstraints62.gridx = 0;
			gridBagConstraints62.gridy = 3;
			gridBagConstraints62.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints62.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints62.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints62.weightx = 0.5D;
			gridBagConstraints72.gridx = 1;
			gridBagConstraints72.gridy = 3;
			gridBagConstraints72.weightx = 0.5D;
			gridBagConstraints72.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints72.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints72.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints72.ipadx = 50;
			panelProxyAuth.add(getChkProxyChainAuth(), gridBagConstraints16);
			panelProxyAuth.add(jLabel9, gridBagConstraints21);
			panelProxyAuth.add(getTxtProxyChainRealm(), gridBagConstraints31);
			panelProxyAuth.add(jLabel10, gridBagConstraints42);
			panelProxyAuth.add(getTxtProxyChainUserName(), gridBagConstraints52);
			panelProxyAuth.add(jLabel11, gridBagConstraints62);
			panelProxyAuth.add(getTxtProxyChainPassword(), gridBagConstraints72);
		}
		return panelProxyAuth;
	}
  
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtProxyChainSkipName());
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane;
	}
    
	private JPanel getPanelProxyChain() {
		if (panelProxyChain == null) {
			panelProxyChain = new JPanel();
			java.awt.GridBagConstraints gridBagConstraints82 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints92 = new GridBagConstraints();

			javax.swing.JLabel jLabel8 = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints102 = new GridBagConstraints();

			panelProxyChain.setLayout(new GridBagLayout());
			gridBagConstraints82.gridx = 0;
			gridBagConstraints82.gridy = 0;
			gridBagConstraints82.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints82.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints82.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints82.weightx = 1.0D;
			gridBagConstraints92.gridx = 0;
			gridBagConstraints92.gridy = 1;
			gridBagConstraints92.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints92.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints92.fill = java.awt.GridBagConstraints.HORIZONTAL;
			panelProxyChain.setName("Proxy Chain");
			jLabel8.setText("");
			gridBagConstraints102.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints102.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints102.gridx = 0;
			gridBagConstraints102.gridy = 2;
			gridBagConstraints102.weightx = 1.0D;
			gridBagConstraints102.weighty = 1.0D;
			panelProxyChain.add(getJPanel(), gridBagConstraints82);
			panelProxyChain.add(getPanelProxyAuth(), gridBagConstraints92);
			panelProxyChain.add(jLabel8, gridBagConstraints102);
		}
		return panelProxyChain;
	}
  
	private JTextField getTxtProxyChainName() {
		if (txtProxyChainName == null) {
			txtProxyChainName = new JTextField();
		}
		return txtProxyChainName;
	}
   
	private JTextField getTxtProxyChainPort() {
		if (txtProxyChainPort == null) {
			txtProxyChainPort = new JTextField();
		}
		return txtProxyChainPort;
	}
  
	private JTextArea getTxtProxyChainSkipName() {
		if (txtProxyChainSkipName == null) {
			txtProxyChainSkipName = new JTextArea();
			txtProxyChainSkipName.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			txtProxyChainSkipName.setMinimumSize(new java.awt.Dimension(0,32));
			txtProxyChainSkipName.setRows(2);
		}
		return txtProxyChainSkipName;
	}

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName("����(Connection)");
        this.add(getPanelProxyChain(), getPanelProxyChain().getName());


	}
	
	public void initParam(Object obj) {
	    
	    OptionsParam optionsParam = (OptionsParam) obj;
	    ConnectionParam connectionParam = optionsParam.getConnectionParam();
	    
	    if (connectionParam.getProxyChainName().equals("")) {
	        chkUseProxyChain.setSelected(false);
	        setProxyChainEnabled(false);
	    } else {
	        chkUseProxyChain.setSelected(true);
	        setProxyChainEnabled(true);
		    txtProxyChainName.setText(connectionParam.getProxyChainName());
		    txtProxyChainPort.setText(Integer.toString(connectionParam.getProxyChainPort()));
		    txtProxyChainSkipName.setText(connectionParam.getProxyChainSkipName());
		    
		    if (connectionParam.getProxyChainUserName().equals("")) {
		        chkProxyChainAuth.setSelected(false);
		        setProxyChainAuthEnabled(false);
		    } else {
		        chkProxyChainAuth.setSelected(true);
		        setProxyChainAuthEnabled(true);
		        txtProxyChainRealm.setText(connectionParam.getProxyChainRealm());
		        txtProxyChainUserName.setText(connectionParam.getProxyChainUserName());
		        txtProxyChainPassword.setText(connectionParam.getProxyChainPassword());
		    }
		    
	    }
	}
	
	private void setProxyChainEnabled(boolean isEnabled) {
	    txtProxyChainName.setEnabled(isEnabled);
	    txtProxyChainPort.setEnabled(isEnabled);
	    txtProxyChainSkipName.setEnabled(isEnabled);
	    chkProxyChainAuth.setEnabled(isEnabled);
	    Color color = Color.WHITE;
	    if (!isEnabled) {
	        txtProxyChainName.setText("");
	        txtProxyChainPort.setText("");
	        txtProxyChainSkipName.setText("");
	        chkProxyChainAuth.setSelected(false);
	        setProxyChainAuthEnabled(false);
	        color = panelProxyChain.getBackground();
	    }
	    txtProxyChainName.setBackground(color);
	    txtProxyChainPort.setBackground(color);
	    txtProxyChainSkipName.setBackground(color);
	    
	}
	
	private void setProxyChainAuthEnabled(boolean isEnabled) {

	    txtProxyChainRealm.setEnabled(isEnabled);
	    txtProxyChainUserName.setEnabled(isEnabled);
	    txtProxyChainPassword.setEnabled(isEnabled);
	    
	    Color color = Color.WHITE;
	    if (!isEnabled) {
	        txtProxyChainRealm.setText("");
	        txtProxyChainUserName.setText("");
	        txtProxyChainPassword.setText("");
	        color = panelProxyChain.getBackground();
	    }
	    txtProxyChainRealm.setBackground(color);
	    txtProxyChainUserName.setBackground(color);
	    txtProxyChainPassword.setBackground(color);
	    
	}
	
	public void validateParam(Object obj) throws Exception {
	    int proxyChainPort = 8080;

	    if (chkUseProxyChain.isSelected()) {
            try {
                proxyChainPort = Integer.parseInt(txtProxyChainPort.getText());
            } catch (NumberFormatException nfe) {
                txtProxyChainPort.requestFocus();
                throw new Exception("�������� ���� ���Ͻ� ü�� ��Ʈ ��ȣ�Դϴ�..");
            }

        }
	    
	}

	public void saveParam(Object obj) throws Exception {
		
	    OptionsParam optionsParam = (OptionsParam) obj;
	    ConnectionParam connectionParam = optionsParam.getConnectionParam();
	    int proxyChainPort = 8080;

	    if (chkUseProxyChain.isSelected()) {
            try {
                proxyChainPort = Integer.parseInt(txtProxyChainPort.getText());
            } catch (NumberFormatException nfe) {
                txtProxyChainPort.requestFocus();
                throw new Exception("�������� ���� ���Ͻ� ü�� ��Ʈ ��ȣ �Դϴ�.");
            }
 
        }
	    connectionParam.setProxyChainName(txtProxyChainName.getText());
	    connectionParam.setProxyChainPort(proxyChainPort);
	    connectionParam.setProxyChainSkipName(txtProxyChainSkipName.getText());

	    connectionParam.setProxyChainRealm(txtProxyChainRealm.getText());
	    connectionParam.setProxyChainUserName(txtProxyChainUserName.getText());
	    connectionParam.setProxyChainPassword(txtProxyChainPassword.getText());
	    
	    
	}

	private JTextField getTxtProxyChainRealm() {
		if (txtProxyChainRealm == null) {
			txtProxyChainRealm = new JTextField();
		}
		return txtProxyChainRealm;
	}
   
	private JTextField getTxtProxyChainUserName() {
		if (txtProxyChainUserName == null) {
			txtProxyChainUserName = new JTextField();
		}
		return txtProxyChainUserName;
	}
  
	private JTextField getTxtProxyChainPassword() {
		if (txtProxyChainPassword == null) {
			txtProxyChainPassword = new JTextField();
		}
		return txtProxyChainPassword;
	}
 
	private JCheckBox getChkProxyChainAuth() {
		if (chkProxyChainAuth == null) {
			chkProxyChainAuth = new JCheckBox();
			chkProxyChainAuth.setText("�ܺ� ���Ͻ� ������ ������ �ʿ��մϴ�.");
			chkProxyChainAuth.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					setProxyChainAuthEnabled(chkProxyChainAuth.isSelected());
				}
			});

		}
		return chkProxyChainAuth;
	}
      }  
