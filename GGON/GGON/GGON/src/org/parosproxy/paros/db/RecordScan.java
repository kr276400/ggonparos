package org.parosproxy.paros.db;

public class RecordScan {

    private int scanId = 0;
    private String scanName = "";
    private java.sql.Date scanTime = null;

    public RecordScan(int scanId, String scanName, java.sql.Date scanTime) {
        super();
        setScanId(scanId);
        setScanName(scanName);
        setScanTime(scanTime);
    }
 
    public int getScanId() {
        return scanId;
    }

    public void setScanId(int scanId) {
        this.scanId = scanId;
    }

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public java.sql.Date getScanTime() {
        return scanTime;
    }

    public void setScanTime(java.sql.Date scanTime) {
        this.scanTime = scanTime;
    }
}
