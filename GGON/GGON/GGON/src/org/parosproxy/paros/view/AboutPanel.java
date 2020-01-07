package org.parosproxy.paros.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.parosproxy.paros.Constant;

public class AboutPanel extends JPanel {

    private static final String PRODUCT  = Constant.PROGRAM_NAME;

	private JLabel lblName = null;
	private JLabel lblDisclaimer = null;

    public AboutPanel() {
        super();
   
		initialize();
 }

    public AboutPanel(boolean arg0) {
        super(arg0);
   
		initialize();
 }

    public AboutPanel(LayoutManager arg0) {
        super(arg0);
   
		initialize();
 }

    public AboutPanel(LayoutManager arg0, boolean arg1) {
        super(arg0, arg1);
   
		initialize();
 }

	private  void initialize() {
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		lblDisclaimer = new JLabel();
		lblName = new JLabel();
		javax.swing.JLabel lblCopyright = new JLabel();

		javax.swing.JLabel lblOtherCopyright = new JLabel();

		javax.swing.JLabel lblVersion = new JLabel();

		javax.swing.JLabel lblProgramName = new JLabel();

		javax.swing.JLabel lblCopyrightDetail = new JLabel();

		javax.swing.JLabel lblLogo = new JLabel();

		this.setLayout(new GridBagLayout());
		this.setBackground(java.awt.Color.white);
		this.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
		lblLogo.setText("");
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/resource/p_gon_64.png")));
		lblLogo.setName("lblLogo");
		lblCopyrightDetail.setPreferredSize(new java.awt.Dimension(300,120));
		lblCopyrightDetail.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 11));
		lblCopyrightDetail.setName("lblCopyrightDetail");
		lblCopyrightDetail.setBackground(java.awt.Color.white);
		lblProgramName.setText(PRODUCT);
		lblProgramName.setFont(new java.awt.Font("Default", java.awt.Font.BOLD, 36));
		lblVersion.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 18));
		lblVersion.setName("lblVersion");
		lblVersion.setBackground(java.awt.Color.white);
		lblOtherCopyright.setPreferredSize(new java.awt.Dimension(300,80));
		lblOtherCopyright.setName("lblOtherCopyright");
		lblOtherCopyright.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
		lblOtherCopyright.setBackground(java.awt.Color.white);

		lblCopyright.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 11));
		lblCopyright.setName("lblCopyright");
		lblCopyright.setBackground(java.awt.Color.white);
		//lblName.setIcon(new ImageIcon(getClass().getResource("/resource/paros_name.gif")));
		lblName.setText("");
		lblName.setName("lblName");
		this.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 11));
		lblProgramName.setVisible(false);
		lblProgramName.setName("lblProgramName");
		lblDisclaimer.setText("<html><body><p>Disclaimer: You should only use this software to test the security of your own web application or those you are authorized to do so.  parosproxy.org takes no responsibility for any problems in relation to running Paros against any applications or machines.<p></body></html>");
		lblDisclaimer.setPreferredSize(new java.awt.Dimension(300,60));
		lblDisclaimer.setName("lblDisclaimer");
		lblDisclaimer.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
		lblDisclaimer.setBackground(java.awt.Color.white);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.ipadx = 0;
		gridBagConstraints1.ipady = 0;
		gridBagConstraints1.gridheight = 2;
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.ipadx = 0;
		gridBagConstraints2.ipady = 0;
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.ipadx = 0;
		gridBagConstraints3.ipady = 0;
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints4.gridx = 1;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.ipadx = 0;
		gridBagConstraints4.ipady = 0;
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints4.insets = new java.awt.Insets(0,5,0,5);
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.weightx = 1.0D;
		gridBagConstraints5.gridy = 3;
		gridBagConstraints5.ipadx = 0;
		gridBagConstraints5.ipady = 0;
		gridBagConstraints5.insets = new java.awt.Insets(2,5,2,5);
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridy = 5;
		gridBagConstraints6.ipadx = 0;
		gridBagConstraints6.ipady = 0;
		gridBagConstraints6.insets = new java.awt.Insets(2,5,2,5);
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.gridy = 6;
		gridBagConstraints7.ipadx = 0;
		gridBagConstraints7.ipady = 0;
		gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints7.gridwidth = 2;
		gridBagConstraints7.insets = new java.awt.Insets(2,5,2,5);
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 4;
		gridBagConstraints8.ipadx = 0;
		gridBagConstraints8.ipady = 0;
		gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.insets = new java.awt.Insets(2,5,2,5);
		gridBagConstraints8.weightx = 1.0D;
		gridBagConstraints8.gridwidth = 2;
		this.add(lblProgramName, gridBagConstraints3);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.insets = new java.awt.Insets(5,15,5,15);
			gridBagConstraints2.insets = new java.awt.Insets(3,5,5,10);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			this.add(lblVersion, gridBagConstraints4);
			this.add(lblLogo, gridBagConstraints1);
			this.add(lblName, gridBagConstraints2);
			this.add(lblCopyright, gridBagConstraints5);
			this.add(lblDisclaimer, gridBagConstraints8);
			this.add(lblCopyrightDetail, gridBagConstraints6);
			this.add(lblOtherCopyright, gridBagConstraints7);
	}
}  
