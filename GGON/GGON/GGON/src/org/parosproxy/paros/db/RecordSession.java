package org.parosproxy.paros.db;

public class RecordSession {

    private long sessionId = 0;
    private String sessionName = "";
    private java.sql.Date lastAccess = null;

    public RecordSession(long sessionId, String sessionName, java.sql.Date lastAccess) {
        super();
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.lastAccess = lastAccess;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public java.sql.Date getLastAccess() {
        return lastAccess;
    }

    public long getSessionId() {
        return sessionId;
    }
}
