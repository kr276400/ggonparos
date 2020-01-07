package org.parosproxy.paros.extension.history;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.extension.Extension;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.view.HttpPanel;

import javax.swing.JCheckBox;

public class ManualRequestEditorDialog extends AbstractDialog {

	private HttpPanel requestPanel = null;
	private JPanel panelCommand = null;
	private JButton btnSend = null;
	private JLabel jLabel = null; 
	private JTabbedPane panelTab = null;
	private HttpPanel responsePanel = null;
	private Extension extension = null;
	private HttpSender httpSender = null;
	private boolean isSendEnabled = true;

	private JPanel jPanel = null;
	private JCheckBox chkFollowRedirect = null;
	private JCheckBox chkUseTrackingSessionState = null;

   public ManualRequestEditorDialog() throws HeadlessException {
       super();
		initialize();
       
   }

   public ManualRequestEditorDialog(Frame parent, boolean modal, boolean isSendEnabled, Extension extension) throws HeadlessException {
       super(parent, modal);
       this.isSendEnabled = isSendEnabled;
       this.extension = extension;
       initialize();

   }

	private void initialize() {
	    getRequestPanel().getPanelOption().add(getPanelCommand(), "");

	    this.addWindowListener(new java.awt.event.WindowAdapter() { 
	    	public void windowClosing(java.awt.event.WindowEvent e) {
	    	    getHttpSender().shutdown();
	    	    getResponsePanel().setMessage("","", false);
	    	}
	    });

	    this.setContentPane(getJPanel());
	    
	}
 
	public HttpPanel getRequestPanel() {
		if (requestPanel == null) {
			requestPanel = new HttpPanel(true);
		}
		return requestPanel;
	}
  
	private JPanel getPanelCommand() {
		if (panelCommand == null) {
			panelCommand = new JPanel();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			jLabel = new JLabel();
			panelCommand.setLayout(new GridBagLayout());
			jLabel.setText("");
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHEAST;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.insets = new java.awt.Insets(0,0,0,0);
			panelCommand.add(jLabel, gridBagConstraints2);
			panelCommand.add(getChkUseTrackingSessionState(), gridBagConstraints11);
			panelCommand.add(getChkFollowRedirect(), gridBagConstraints1);
			panelCommand.add(getBtnSend(), gridBagConstraints3);
		}
		return panelCommand;
	}
   
	private JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setText("보내기");
			btnSend.setEnabled(isSendEnabled);
			btnSend.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    btnSend.setEnabled(false);
			        HttpMessage msg = new HttpMessage();
			        getRequestPanel().getMessage(msg, true);
			        msg.getRequestHeader().setContentLength(msg.getRequestBody().length());
			        send(msg);
				    
				}
			});
		}
		return btnSend;
	}

	private JTabbedPane getPanelTab() {
		if (panelTab == null) {
			panelTab = new JTabbedPane();
			panelTab.setDoubleBuffered(true);
			panelTab.addTab("Request", null, getRequestPanel(), null);
			panelTab.addTab("Response", null, getResponsePanel(), null);
		}
		return panelTab;
	}
 
	public HttpPanel getResponsePanel() {
		if (responsePanel == null) {
			responsePanel = new HttpPanel(false);
		}
		return responsePanel;
	}

   public void setExtension(Extension extension) {
       this.extension = extension;
   }
   
   private Extension getExtention() {
       return extension;
   }
   
   public void setVisible(boolean show) {
       if (show) {
           try {
               if (httpSender != null) {
                   httpSender.shutdown();
                   httpSender = null;
               }
           } catch (Exception e) {}
           getPanelTab().setSelectedIndex(0);
       }

       boolean isSessionTrackingEnabled = Model.getSingleton().getOptionsParam().getConnectionParam().isHttpStateEnabled();
       getChkUseTrackingSessionState().setEnabled(isSessionTrackingEnabled);
       super.setVisible(show);       

   }
	
   private HttpSender getHttpSender() {
       if (httpSender == null) {
           httpSender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(), getChkUseTrackingSessionState().isSelected());
           
       }
       return httpSender;
   }
   
   public void setMessage(HttpMessage msg) {
       getPanelTab().setSelectedIndex(0);
       getRequestPanel().setMessage(msg, true);
       getResponsePanel().setMessage("", "", false);
       getBtnSend().setEnabled(true);
       
   }
 
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 0;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.weighty = 1.0;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.NORTHWEST;
			jPanel.add(getPanelTab(), gridBagConstraints31);
		}
		return jPanel;
	}
  
	private JCheckBox getChkFollowRedirect() {
		if (chkFollowRedirect == null) {
			chkFollowRedirect = new JCheckBox();
			chkFollowRedirect.setText("'Redirect' 따라가기");
			chkFollowRedirect.setSelected(true);
		}
		return chkFollowRedirect;
	}
  
	private JCheckBox getChkUseTrackingSessionState() {
		if (chkUseTrackingSessionState == null) {
			chkUseTrackingSessionState = new JCheckBox();
			chkUseTrackingSessionState.setText("현재 트레킹 세션 이용");
		}
		return chkUseTrackingSessionState;
	}
    
    private void send(final HttpMessage msg) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    getHttpSender().sendAndReceive(msg, getChkFollowRedirect().isSelected());
                    
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                        if (!msg.getResponseHeader().isEmpty()) {
                            getResponsePanel().setMessage(msg, false);
                        }
                        getPanelTab().setSelectedIndex(1);
                        }
                    });
                } catch (NullPointerException npe) {
                    getExtention().getView().showWarningDialog("Malformed 헤더 오류입니다.");                      
                } catch (HttpMalformedHeaderException mhe) {
                    getExtention().getView().showWarningDialog("Malformed 헤더 오류입니다.");                      
                } catch (IOException ioe) {
                    getExtention().getView().showWarningDialog("요청을 보내는 중에 IO 오류가 발생했습니다.");
                } catch (Exception e) {
                    
                } finally {
                    btnSend.setEnabled(true);
                }
            }
        });
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
    
    
        }
