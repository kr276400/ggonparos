package org.parosproxy.paros.extension.report;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookMenu;
import org.parosproxy.paros.model.SiteMap;

public class ExtensionReport extends ExtensionAdaptor implements CommandLineListener {

    private static final int ARG_LAST_SCAN_REPORT_IDX = 0;
    
	private ExtensionHookMenu pluginMenu = null;
	private SiteMap siteTree = null;
	
	private JMenu menuReport = null;
	private JMenuItem menuItemLastScanReport = null;
	private CommandLineArgument[] arguments = new CommandLineArgument[1];

    public ExtensionReport() {
        super();
 		initialize();
    }

    public ExtensionReport(String name) {
        super(name);
    }

	private void initialize() {
        this.setName("ExtensionReport");
			
	}
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        extensionHook.getHookMenu().addNewMenu(getMenuReport());
	    }
        extensionHook.addCommandLine(getCommandLineArguments());

	}
  
	private JMenu getMenuReport() {
		if (menuReport == null) {
			menuReport = new JMenu();
			menuReport.setText("보고서");
			menuReport.add(getMenuItemLastScanReport());
			menuReport.addSeparator();
		}
		return menuReport;
	}

	private JMenuItem getMenuItemLastScanReport() {
		if (menuItemLastScanReport == null) {
			menuItemLastScanReport = new JMenuItem();
			menuItemLastScanReport.setText("최근 스캔 보고서");
			menuItemLastScanReport.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    ReportLastScan report = new ReportLastScan();
				    report.generate(getView(), getModel());
	                
				}
			});

		}
		return menuItemLastScanReport;
	}

    public void execute(CommandLineArgument[] args) {

        if (arguments[ARG_LAST_SCAN_REPORT_IDX].isEnabled()) {
		    CommandLineArgument arg = arguments[ARG_LAST_SCAN_REPORT_IDX];
            ReportLastScan report = new ReportLastScan();
            String fileName = (String) arg.getArguments().get(0);
            try {
                report.generate(fileName, getModel());
                System.out.println("최근 스캔 보고서를 만듭니다, 파일 이름: " + fileName);
            } catch (Exception e) {
                
            }
        } else {
            return;
        }

    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_LAST_SCAN_REPORT_IDX] = new CommandLineArgument("-last_scan_report", 1, null, "", "-last_scan_report [file_path]: Generate 'Last Scan Report' into the file_path provided.");
        return arguments;
    }
      }
