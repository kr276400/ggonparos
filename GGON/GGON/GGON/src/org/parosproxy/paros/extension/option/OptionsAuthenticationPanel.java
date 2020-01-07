package org.parosproxy.paros.extension.option;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.network.ConnectionParam;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.parosproxy.paros.view.OptionsAuthenticationTableModel;

public class OptionsAuthenticationPanel extends AbstractParamPanel {

	private JTable tableAuth = null;
	private JScrollPane jScrollPane = null;
	private OptionsAuthenticationTableModel authModel = null;  
    public OptionsAuthenticationPanel() {
        super();
 		initialize();
    }

	private void initialize() {
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

        javax.swing.JLabel jLabel = new JLabel();

        this.setLayout(new GridBagLayout());
        this.setSize(409, 268);
        this.setName("인증(Authentication)");
        jLabel.setText("<html><body><p>호스트의 인증을 위해 밑에 보이는 테이블에 HTTP를 입력해주세요.현재 기본 인증은 되어있습니다. 몇몇 호스트에는 NTLM 서비스가 나타날 수 있습니다, 간단한 테스트이니 괜찮습니다.삭제-(호스트 이름-빈칸)</p></body></html>");
        jLabel.setPreferredSize(new java.awt.Dimension(494,80));
        jLabel.setMinimumSize(new java.awt.Dimension(494,16));
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.ipady = 65;
        gridBagConstraints1.insets = new java.awt.Insets(10,0,5,0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 1.0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.ipadx = 0;
        gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(jLabel, gridBagConstraints1);
        this.add(getJScrollPane(), gridBagConstraints2);
			
	}

    public void initParam(Object obj) {
	    OptionsParam optionsParam = (OptionsParam) obj;
	    ConnectionParam connectionParam = optionsParam.getConnectionParam();
	    getAuthModel().setListAuth(connectionParam.getListAuth());
    }

    public void validateParam(Object obj) throws Exception {

    }

    public void saveParam(Object obj) throws Exception {
	    OptionsParam optionsParam = (OptionsParam) obj;
	    ConnectionParam connectionParam = optionsParam.getConnectionParam();
	    connectionParam.setListAuth(getAuthModel().getListAuth());
    }

    private static int[] width = {280,55,100,100,70};
  
	private JTable getTableAuth() {
		if (tableAuth == null) {
			tableAuth = new JTable();
			tableAuth.setModel(getAuthModel());
			tableAuth.setRowHeight(18);
			tableAuth.setIntercellSpacing(new java.awt.Dimension(1,1));
	        for (int i = 0; i < 5; i++) {
	            TableColumn column = tableAuth.getColumnModel().getColumn(i);
	            column.setPreferredWidth(width[i]);
	        }
		}
		return tableAuth;
	}
  
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTableAuth());
			jScrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
		}
		return jScrollPane;
	}

	private OptionsAuthenticationTableModel getAuthModel() {
		if (authModel == null) {
			authModel = new OptionsAuthenticationTableModel();
		}
		return authModel;
	}
   }  
