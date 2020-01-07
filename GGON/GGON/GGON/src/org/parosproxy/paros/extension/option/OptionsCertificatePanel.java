package org.parosproxy.paros.extension.option;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.parosproxy.paros.view.View;

public class OptionsCertificatePanel extends AbstractParamPanel {

	private JPanel panelCertificate = null;  
	private JCheckBox chkUseClientCertificate = null;
	
    public OptionsCertificatePanel() {
        super();
 		initialize();
   }


    private static final String[] ROOT = {};
    
	private JPanel panelLocation = null;
	private JLabel lblLocation = null;
	private JTextField txtLocation = null;
	private JButton btnLocation = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel = null;

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName("����(Certificate)");
        this.add(getPanelCertificate(), getPanelCertificate().getName());

	}
  
	private JPanel getPanelCertificate() {
		if (panelCertificate == null) {
			jLabel = new JLabel();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			jLabel1 = new JLabel();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			panelCertificate = new JPanel();
			panelCertificate.setLayout(new GridBagLayout());
			panelCertificate.setSize(114, 132);
			panelCertificate.setName("Certificate");
			panelCertificate.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.ipadx = 113;
			gridBagConstraints2.ipady = 15;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.gridheight = 2;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			jLabel1.setText("");
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 5;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.weighty = 1.0D;
			jLabel.setText("<html><body><p>(PKCS#12)����� ������ ����ּ���.  ���� ������ �������� ���, ���� �Ŀ� ��ȣ ������ �ؾ��� �̴ϴ�.  The PKCS#12 ������ �������� ���ؼ� �޾� ���ž� �մϴ�  </p><p>�ɼǿ� ���� ������ ������� �ʽ��ϴ� �׸��� �ķν��� �� �����Ҷ�, ����ڴ� ������ �ؾ��մϴ�.</p></body></html>");
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.insets = new java.awt.Insets(5,0,5,0);
			panelCertificate.add(jLabel, gridBagConstraints11);
			panelCertificate.add(getChkUseClientCertificate(), gridBagConstraints1);
			panelCertificate.add(getPanelLocation(), gridBagConstraints2);
			panelCertificate.add(jLabel1, gridBagConstraints6);
		}
		return panelCertificate;
	}

	private JCheckBox getChkUseClientCertificate() {
		if (chkUseClientCertificate == null) {
			chkUseClientCertificate = new JCheckBox();
			chkUseClientCertificate.setText("����� ���� �̿�");
			chkUseClientCertificate.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			chkUseClientCertificate.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
			chkUseClientCertificate.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {
				    getPanelLocation().setEnabled(chkUseClientCertificate.isSelected());
				    getTxtLocation().setEnabled(chkUseClientCertificate.isSelected());
				    getBtnLocation().setEnabled(chkUseClientCertificate.isSelected());
				    if (chkUseClientCertificate.isSelected()) {
				        getBtnLocation().doClick();
				    }
				}
			});
		}
		return chkUseClientCertificate;
	}
	
	public void initParam(Object obj) {
	    OptionsParam options = (OptionsParam) obj;
	    getChkUseClientCertificate().setSelected(options.getCertificateParam().isUseClientCert());
	    getBtnLocation().setEnabled(getChkUseClientCertificate().isSelected());
	    getTxtLocation().setText(options.getCertificateParam().getClientCertLocation());

	}
	
	public void validateParam(Object obj) {
	}
	
	public void saveParam (Object obj) throws Exception {
	    OptionsParam options = (OptionsParam) obj;
	    OptionParamCertificate certParam = options.getCertificateParam();
        certParam.setUseClientCert(getChkUseClientCertificate().isSelected());
        certParam.setEnableCertificate(certParam.isUseClientCert());
	    
	}
 
	private JPanel getPanelLocation() {
		if (panelLocation == null) {
			lblLocation = new JLabel();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			panelLocation = new JPanel();
			panelLocation.setLayout(new GridBagLayout());
			lblLocation.setText("��ġ:");
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.gridheight = 0;
			gridBagConstraints5.gridwidth = 0;
			panelLocation.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "����� ����", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			panelLocation.add(lblLocation, gridBagConstraints3);
			panelLocation.add(getTxtLocation(), gridBagConstraints4);
			panelLocation.add(getBtnLocation(), gridBagConstraints5);
		}
		return panelLocation;
	}
 
	private JTextField getTxtLocation() {
		if (txtLocation == null) {
			txtLocation = new JTextField();
			txtLocation.setEditable(false);
		}
		return txtLocation;
	}
   
	private JButton getBtnLocation() {
		if (btnLocation == null) {
			btnLocation = new JButton();
			btnLocation.setText("...");
			btnLocation.setPreferredSize(new java.awt.Dimension(25,25));
			btnLocation.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser chooser = null;
                    if (getTxtLocation().getText()!= null && getTxtLocation().getText().length() >0) {
                        File file = new File(getTxtLocation().getText());                        
                        chooser = new JFileChooser(file);
                    } else  {
                        chooser = new JFileChooser();
                    }
		            OptionParamCertificate certParam = Model.getSingleton().getOptionsParam().getCertificateParam();
				    int rc = chooser.showOpenDialog(View.getSingleton().getMainFrame());
				    if(rc == JFileChooser.APPROVE_OPTION) {
				        try {
				            File keyFile = chooser.getSelectedFile();
				            if (keyFile == null) {
				                return;
				            }
				            
				            JPasswordField pwd = new JPasswordField("");
				            String label = "PKCS#12 ������ ���� ��ȣ�� �Է����ּ���:"; 
				            Object[] objArray = {label, pwd};
				            JOptionPane pane = new JOptionPane(objArray, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				            JDialog dialog = pane.createDialog(View.getSingleton().getMainFrame(), "��ȣ");
				            dialog.setVisible(true);
				            int result = ((Integer) pane.getValue()).intValue();
				            if (result != JOptionPane.OK_OPTION) {
				                return;
				            }
				            char[] passPhrase = pwd.getPassword();
				            if (passPhrase == null) {
				                return;
				            }

				            certParam.setUseClientCert(true);
				            certParam.setClientCertLocation(keyFile.getAbsolutePath());
				            certParam.setCertificate(passPhrase);
				            for (int i=0; i<passPhrase.length; i++) {
				                passPhrase[i] = ' ';
				            }
				            getTxtLocation().setText(keyFile.getAbsolutePath());
				        } catch (Exception ex) {
				            View.getSingleton().showWarningDialog("PKCS#12 ������ �о���� �߿� ������ �߻��Ͽ����ϴ�.  �ùٸ� ��ȣ �̿� ��Ź�帳�ϴ� \r\n ����, Mozilla/Netscape���� ������� ������ �̿����ּ��� .");
				            ex.printStackTrace();
				            certParam.setUseClientCert(false);
				            certParam.setClientCertLocation("");
				            
				        }
				        
				    }
				}
			});
		}
		return btnLocation;
	}
	

        }  
