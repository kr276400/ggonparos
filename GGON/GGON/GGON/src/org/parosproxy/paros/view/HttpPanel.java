package org.parosproxy.paros.view;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.network.HttpMessage;

public class HttpPanel extends AbstractPanel {
    
    private static final String VIEW_RAW = "기본 방식";
    private static final String VIEW_TABULAR = "표 방식";
    private static final String VIEW_IMAGE = "이미지 보기";
    
	private javax.swing.JSplitPane splitVert = null;  //
	private javax.swing.JScrollPane scrollHeader = null;
	private javax.swing.JScrollPane scrollTableBody = null;
	private javax.swing.JTextArea txtHeader = null;
	private javax.swing.JTextArea txtBody = null;
	private JLabel lblIcon = null;
	private JPanel panelView = null;
	private JPanel jPanel = null;
	private JComboBox comboView = null;
	private JPanel panelOption = null;
	private JTable tableBody = null;
	private HttpPanelTabularModel httpPanelTabularModel = null;  
	private JScrollPane scrollTxtBody = null;
	private String currentView = VIEW_RAW;
	
	
	private JScrollPane scrollImage = null;

	public HttpPanel() {
		super();
		initialize();
	}
	
	public HttpPanel(boolean isEditable) {
		this();
		getTxtHeader().setEditable(isEditable);
		getTxtBody().setEditable(isEditable);
		getHttpPanelTabularModel().setEditable(isEditable);	
	}

	private  void initialize() {
		java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();

		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

		this.setLayout(new GridBagLayout());
		this.setSize(403, 296);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.ipadx = 0;
		gridBagConstraints1.ipady = 0;
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.weightx = 1.0D;
		this.add(getSplitVert(), gridBagConstraints1);
		this.add(getJPanel(), gridBagConstraints4);
	}

	private javax.swing.JSplitPane getSplitVert() {
		if (splitVert == null) {
			splitVert = new javax.swing.JSplitPane();
			splitVert.setDividerLocation(220);
			splitVert.setDividerSize(3);
			splitVert.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			splitVert.setPreferredSize(new java.awt.Dimension(400,400));
			splitVert.setResizeWeight(0.5D);
			splitVert.setTopComponent(getScrollHeader());
			splitVert.setContinuousLayout(false);
			splitVert.setBottomComponent(getPanelView());
		}
		return splitVert;
	}
 
	private javax.swing.JScrollPane getScrollHeader() {
		if (scrollHeader == null) {
			scrollHeader = new javax.swing.JScrollPane();
			scrollHeader.setViewportView(getTxtHeader());
		}
		return scrollHeader;
	}

	private javax.swing.JScrollPane getScrollTableBody() {
		if (scrollTableBody == null) {
			scrollTableBody = new javax.swing.JScrollPane();
			scrollTableBody.setName(VIEW_TABULAR);
			scrollTableBody.setViewportView(getTableBody());
		}
		return scrollTableBody;
	}

	public javax.swing.JTextArea getTxtHeader() {
		if (txtHeader == null) {
			txtHeader = new javax.swing.JTextArea();
			txtHeader.setLineWrap(true);
			txtHeader.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
			txtHeader.setName("");
			txtHeader.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
				    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
				        View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				    }				
				}
			});
		}
		return txtHeader;
	}
 
	public javax.swing.JTextArea getTxtBody() {
		if (txtBody == null) {
			txtBody = new javax.swing.JTextArea();
			txtBody.setLineWrap(true);
			txtBody.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
			txtBody.setName("");
			txtBody.setTabSize(4);
			txtBody.setVisible(true);
		    txtBody.addMouseListener(new java.awt.event.MouseAdapter() { 
		    	public void mousePressed(java.awt.event.MouseEvent e) {    
				    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {  // right mouse button
				        View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				    }			    	
				}
		    });
		}
		
	    if (currentView.equals(VIEW_TABULAR)) {
            String s = getHttpPanelTabularModel().getText();
            if (s != null && s.length() > 0) {
                txtBody.setText(s);
            }
		}
		
		return txtBody;
	}

	private JPanel getPanelView() {
		if (panelView == null) {
			panelView = new JPanel();
			panelView.setLayout(new CardLayout());
			panelView.setPreferredSize(new java.awt.Dimension(278,10));
			panelView.add(getScrollTxtBody(), getScrollTxtBody().getName());
			panelView.add(getScrollImage(), getScrollImage().getName());
			panelView.add(getScrollTableBody(), getScrollTableBody().getName());
			show(VIEW_RAW);
		}
		return panelView;
	}
  
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();

			javax.swing.JLabel jLabel = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints5.ipadx = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(2,0,2,0);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0D;
			jLabel.setText("      ");
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			jPanel.add(getComboView(), gridBagConstraints5);
			jPanel.add(jLabel, gridBagConstraints7);
			jPanel.add(getPanelOption(), gridBagConstraints6);
		}
		return jPanel;
	}
 
	private JComboBox getComboView() {
		if (comboView == null) {
			comboView = new JComboBox();
			comboView.setSelectedIndex(-1);
			comboView.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    String item = (String) comboView.getSelectedItem();
				    if (item == null || item.equals(currentView)) {
				        return;
				    }
				    
			        if (currentView.equals(VIEW_TABULAR)) {
                        String s = getHttpPanelTabularModel().getText();
				        if (s != null && s.length() > 0) {
                            txtBody.setText(s);
                        }
				    }
				    
				    if (item.equals(VIEW_TABULAR)) {
				        getHttpPanelTabularModel().setText(getTxtBody().getText());
				    }
				    
				    currentView = item;
				    show(item);
				}
			});

			comboView.addItem(VIEW_RAW);
			comboView.addItem(VIEW_TABULAR);

		}
		return comboView;
	}
   
	public JPanel getPanelOption() {
		if (panelOption == null) {
			panelOption = new JPanel();
			panelOption.setLayout(new CardLayout());
		}
		return panelOption;
	}
   
	private JTable getTableBody() {
		if (tableBody == null) {
			tableBody = new JTable();
			tableBody.setName("");
			tableBody.setModel(getHttpPanelTabularModel());

			tableBody.setGridColor(java.awt.Color.black);
			tableBody.setIntercellSpacing(new java.awt.Dimension(1,1));
			tableBody.setRowHeight(18);
		}
		return tableBody;
	}

	private HttpPanelTabularModel getHttpPanelTabularModel() {
		if (httpPanelTabularModel == null) {
			httpPanelTabularModel = new HttpPanelTabularModel();
		}
		return httpPanelTabularModel;
	}
  
	private JScrollPane getScrollTxtBody() {
		if (scrollTxtBody == null) {
			scrollTxtBody = new JScrollPane();
			scrollTxtBody.setName(VIEW_RAW);
			scrollTxtBody.setViewportView(getTxtBody());
			scrollTxtBody.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return scrollTxtBody;
	}
	
	private void show(String viewName) {
		CardLayout card = (CardLayout) getPanelView().getLayout();
		card.show(getPanelView(), viewName);
	    
	}
	
	public void setMessage(String header, String body, boolean enableViewSelect) {
	    getComboView().setEnabled(enableViewSelect);

        javax.swing.JTextArea txtBody = getTxtBody();
        
        this.validate();
        if (enableViewSelect) {
	        getHttpPanelTabularModel().setText(body);
	    } else {
		    getComboView().setSelectedItem(VIEW_RAW);
		    currentView = VIEW_RAW;

	        show(VIEW_RAW);
	        getHttpPanelTabularModel().setText("");
	    }

	    getTxtHeader().setText(header);
        getTxtHeader().setCaretPosition(0);

        txtBody.setText(body);
        txtBody.setCaretPosition(0);

	}
	
	public void setMessage(HttpMessage msg, boolean isRequest) {

	    javax.swing.JTextArea txtBody = getTxtBody();

	    getComboView().removeAllItems();
	    getComboView().setEnabled(false);
	    getComboView().addItem(VIEW_RAW);
	    
	    if (msg == null) {
		    getTxtHeader().setText("");
		    getTxtHeader().setCaretPosition(0);

	        txtBody.setText("");
	        txtBody.setCaretPosition(0);

	        getComboView().setSelectedItem(VIEW_RAW);
		    currentView = VIEW_RAW;

	        show(VIEW_RAW);
	        getHttpPanelTabularModel().setText("");
	        return;
	    }
	    
	    if (isRequest) {
	        setDisplayRequest(msg);
	        
	    } else {
	        setDisplayResponse(msg);
	    }
        this.validate();
	    
	}

	private void setDisplayRequest(HttpMessage msg) {

	    String header = replaceHeaderForJTextArea(msg.getRequestHeader().toString());
	    String body = msg.getRequestBody().toString();
	    
	    getHttpPanelTabularModel().setText(msg.getRequestBody().toString());

	    getTxtHeader().setText(header);
        getTxtHeader().setCaretPosition(0);

        txtBody.setText(body);
        txtBody.setCaretPosition(0);

        getComboView().addItem(VIEW_TABULAR);
	    getComboView().setEnabled(true);

	}
	
	private void setDisplayResponse(HttpMessage msg) {
	    
	    
	    if (msg.getResponseHeader().isEmpty()) {
		    getTxtHeader().setText("");
		    getTxtHeader().setCaretPosition(0);

	        txtBody.setText("");
	        txtBody.setCaretPosition(0);
	        
	        getLblIcon().setIcon(null);
	        return;
	    }
	    
	    String header = replaceHeaderForJTextArea(msg.getResponseHeader().toString());
	    String body = msg.getResponseBody().toString();

	    getTxtHeader().setText(header);
	    getTxtHeader().setCaretPosition(0);
        
        txtBody.setText(body);
        txtBody.setCaretPosition(0);

        getComboView().removeAllItems();
        getComboView().addItem(VIEW_RAW);

	    getComboView().setEnabled(true);

	    if (msg.getResponseHeader().isImage()) {
	        getComboView().addItem(VIEW_IMAGE);
	        getLblIcon().setIcon(getImageIcon(msg));

	    }
	    
	    if (msg.getResponseHeader().isImage()) {
		    getComboView().setSelectedItem(VIEW_IMAGE);	        
	    } else {
		    getComboView().setSelectedItem(VIEW_RAW);	        	        
	    }

	    
	}
	
	private String getHeaderFromJTextArea(JTextArea txtArea) {
		
		String msg = txtArea.getText();
		String result = msg.replaceAll("\\n", "\r\n");
		result = result.replaceAll("(\\r\\n)*\\z", "") + "\r\n\r\n";
		return result;
	}
	
	private String replaceHeaderForJTextArea(String msg) {
		return msg.replaceAll("\\r\\n", "\n");
	}
	
	public void getMessage(HttpMessage msg, boolean isRequest) {
	    try {
	        if (isRequest) {
	            if (getTxtHeader().getText().length() == 0) {
	                msg.getRequestHeader().clear();
	                msg.getRequestBody().setBody("");
	            } else {
	                msg.getRequestHeader().setMessage(getHeaderFromJTextArea(getTxtHeader()));
	                msg.getRequestBody().setBody(getTxtBody().getText());
	                msg.getRequestHeader().setContentLength(msg.getRequestBody().length());
	            }
	        } else {
	            if (getTxtHeader().getText().length() == 0) {
	                msg.getResponseHeader().clear();
	                msg.getResponseBody().setBody("");
	            } else {
	                msg.getResponseHeader().setMessage(getHeaderFromJTextArea(getTxtHeader()));
	                String txt = getTxtBody().getText();
	                msg.getResponseBody().setBody(txt);
	                msg.getResponseHeader().setContentLength(msg.getResponseBody().length());
	            }
	        }
	    } catch (Exception e) {
	    }

	}

	private JScrollPane getScrollImage() {
		if (scrollImage == null) {
			
			scrollImage = new JScrollPane();
			scrollImage.setName(VIEW_IMAGE);
			scrollImage.setViewportView(getLblIcon());
		}
		return scrollImage;
	}
	
	private JLabel getLblIcon() {
	    if (lblIcon == null) {
			lblIcon = new JLabel();
			lblIcon.setText("");

			lblIcon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			lblIcon.setBackground(java.awt.SystemColor.text);
	    }
	    return lblIcon;
	}
	private ImageIcon getImageIcon(HttpMessage msg) {
	    ImageIcon image = new ImageIcon(msg.getResponseBody().getBytes());
	    return image;
	}
	
  }  
