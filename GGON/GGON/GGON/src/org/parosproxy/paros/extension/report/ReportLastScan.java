package org.parosproxy.paros.extension.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.db.Database;
import org.parosproxy.paros.db.RecordAlert;
import org.parosproxy.paros.db.RecordScan;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.model.Model;

public class ReportLastScan {

    
    public ReportLastScan() {
        
    }

    

    private String getAlertXML(Database db, RecordScan recordScan) throws SQLException {

        Connection conn = null;
        PreparedStatement psAlert = null;
        StringBuffer sb = new StringBuffer();
        
        try {
            conn = db.getDatabaseServer().getNewConnection();
            conn.setReadOnly(true);
            psAlert = conn.prepareStatement("SELECT ALERT.ALERTID FROM ALERT JOIN SCAN ON ALERT.SCANID = SCAN.SCANID WHERE SCAN.SCANID = ? ORDER BY PLUGINID");
            psAlert.setInt(1, recordScan.getScanId());
            psAlert.executeQuery();
            ResultSet rs = psAlert.getResultSet();

            RecordAlert recordAlert = null;
            Alert alert = null;
            Alert lastAlert = null;

            StringBuffer sbURLs = new StringBuffer(100);
            String s = null;
            
            while (rs.next()) {
                int alertId = rs.getInt(1);
                recordAlert = db.getTableAlert().read(alertId);
                alert = new Alert(recordAlert);

                if (lastAlert != null && alert.getPluginId() != lastAlert.getPluginId()) {
                    s = lastAlert.toPluginXML(sbURLs.toString());
                    sb.append(s);
                    sbURLs.setLength(0);
                }

                s = alert.getUrlParamXML();
                sbURLs.append(s);

                lastAlert = alert;

            }

            if (lastAlert != null) {
                sb.append(lastAlert.toPluginXML(sbURLs.toString()));
            }
    
        } catch (SQLException e) {
        } finally {
            if (conn != null) {
                conn.close();
            }
            
        }
        
        return sb.toString();
    }
    
    public File generate(String fileName, Model model) throws Exception {
        
	    StringBuffer sb = new StringBuffer(500);
	    RecordScan scan = null;
	        
	    scan = model.getDb().getTableScan().getLatestScan();
	    if (scan == null) {
	        return null;
	    }
	    sb.append("<?xml version=\"1.0\"?>");
	    sb.append("<report>\r\n");
	    sb.append("Report generated at " + ReportGenerator.getCurrentDateTimeString() + ".\r\n");
	    sb.append(getAlertXML(model.getDb(), scan));
	    sb.append("</report>");	
	    
	    if (!fileName.endsWith(".htm")) {
	        fileName = fileName + ".htm";		        
	    }
	    
	    File report = ReportGenerator.stringToHtml(sb.toString(), "xml" + File.separator + "reportLatestScan.xsl", fileName);
	    
	    
	    return report;
    }
    
	public void generate(ViewDelegate view, Model model) {		

	    RecordScan scan = null;
	    try{
	        
    		
    		String output = model.getSession().getSessionFolder() + File.separator + "LatestScannedReport.htm";
    		File report = generate(output, model);
    		if (report == null) {
    		    return;
    		}
    		
		    view.showMessageDialog("��ĳ�� ������ ����������ϴ�. ����, 'OK' Ŭ�� �Ŀ� ��Ÿ���� �ʴ´ٸ� �ڿ� ������ ����� ������ ������ �����ּ���: " + report.getAbsolutePath()); 
  
  			ReportGenerator.openBrowser(report.getAbsolutePath());
  			
    	} catch (Exception e){
    	    e.printStackTrace();
      		view.showWarningDialog("���� ������ ������ �߻��Ͽ����ϴ�."); 
    	}
	}
	

		
    
}
