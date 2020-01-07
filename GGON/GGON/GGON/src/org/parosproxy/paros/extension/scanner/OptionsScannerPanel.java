package org.parosproxy.paros.extension.scanner;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;

public class OptionsScannerPanel extends AbstractParamPanel {

	private JPanel panelSpider = null;  
    public OptionsScannerPanel() {
        super();
 		initialize();
   }
    
	private JSlider sliderHostPerScan = null;
	private JSlider sliderThreadsPerHost = null;

	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName("스캐너(Scanner)");
        this.setSize(314, 245);
        this.add(getPanelSpider(), getPanelSpider().getName());
	}
 
	private JPanel getPanelSpider() {
		if (panelSpider == null) {
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();

			javax.swing.JLabel jLabel2 = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			panelSpider = new JPanel();
			javax.swing.JLabel jLabel1 = new JLabel();

			javax.swing.JLabel jLabel = new JLabel();

			panelSpider.setLayout(new GridBagLayout());
			panelSpider.setSize(114, 132);
			panelSpider.setName("");
			jLabel.setText("동시 스캔할 호스트의 개수:");
			jLabel1.setText("호스트별로 동시 스캔을 할 쓰레드:");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.ipady = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 10;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.weighty = 1.0D;
			jLabel2.setText("");
			panelSpider.add(jLabel, gridBagConstraints1);
			panelSpider.add(getSliderHostPerScan(), gridBagConstraints2);
			panelSpider.add(jLabel1, gridBagConstraints3);
			panelSpider.add(getSliderThreadsPerHost(), gridBagConstraints4);
			panelSpider.add(jLabel2, gridBagConstraints6);
		}
		return panelSpider;
	}
	public void initParam(Object obj) {
	    OptionsParam options = (OptionsParam) obj;
	    ScannerParam param = (ScannerParam) options.getParamSet(ScannerParam.class);
	    getSliderHostPerScan().setValue(param.getHostPerScan());
	    getSliderThreadsPerHost().setValue(param.getThreadPerHost());
	}
	
	public void validateParam(Object obj) {
	}
	
	public void saveParam (Object obj) throws Exception {
	    OptionsParam options = (OptionsParam) obj;
	    ScannerParam param = (ScannerParam) options.getParamSet(ScannerParam.class);
	    param.setHostPerScan(getSliderHostPerScan().getValue());
	    param.setThreadPerHost(getSliderThreadsPerHost().getValue());
	}
   
	private JSlider getSliderHostPerScan() {
		if (sliderHostPerScan == null) {
			sliderHostPerScan = new JSlider();
			sliderHostPerScan.setMaximum(5);
			sliderHostPerScan.setMinimum(1);
			sliderHostPerScan.setMinorTickSpacing(1);
			sliderHostPerScan.setPaintTicks(true);
			sliderHostPerScan.setPaintLabels(true);
			sliderHostPerScan.setName("");
			sliderHostPerScan.setMajorTickSpacing(1);
			sliderHostPerScan.setSnapToTicks(true);
			sliderHostPerScan.setPaintTrack(true);
		}
		return sliderHostPerScan;
	}

	private JSlider getSliderThreadsPerHost() {
		if (sliderThreadsPerHost == null) {
			sliderThreadsPerHost = new JSlider();
			sliderThreadsPerHost.setMaximum(Constant.MAX_HOST_CONNECTION);
			sliderThreadsPerHost.setMinimum(1);
			sliderThreadsPerHost.setValue(1);
			sliderThreadsPerHost.setPaintTicks(true);
			sliderThreadsPerHost.setPaintLabels(true);
			sliderThreadsPerHost.setMinorTickSpacing(1);
			sliderThreadsPerHost.setMajorTickSpacing(1);
			sliderThreadsPerHost.setSnapToTicks(true);
			sliderThreadsPerHost.setPaintTrack(true);
		}
		return sliderThreadsPerHost;
	}
}  
