package org.parosproxy.paros.db;

public class RecordAlert {
    
    private int alertId = 0;
    private int scanId = 0;
    private int pluginId = 0;
    private String alert = "";
    private int risk = 0;
    private int reliability = 0;
    private String description = "";
    private String uri = "";
    private String param = "";
    private String otherInfo = "";
    private String solution = "";
    private String reference = "";
    private int historyId = 0;
    
	public RecordAlert() {
		
	}

	public RecordAlert(int alertId, int scanId, int pluginId, String alert, int risk, int reliability, String description, String uri, String query, String otherInfo, String solution, String reference, int historyId) {
	    setAlertId(alertId);
	    setScanId(scanId);
	    setPluginId(pluginId);
	    setAlert(alert);
	    setRisk(risk);
	    setReliability(reliability);
	    setDescription(description);
	    setUri(uri);
	    setParam(query);
	    setOtherInfo(otherInfo);
	    setSolution(solution);
	    setReference(reference);
	    setHistoryId(historyId);
	    
	}

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public int getPluginId() {
        return pluginId;
    }

    public void setPluginId(int pluginId) {
        this.pluginId = pluginId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String query) {
        this.param = query;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getReliability() {
        return reliability;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public int getRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public int getScanId() {
        return scanId;
    }

    public void setScanId(int scanId) {
        this.scanId = scanId;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }
}
