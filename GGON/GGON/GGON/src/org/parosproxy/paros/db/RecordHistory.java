package org.parosproxy.paros.db;

import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class RecordHistory {
    
    private int historyId = 0;
    private long sessionId = 0;
	private int historyType = HistoryReference.TYPE_MANUAL;
	private HttpMessage httpMessage = null;
	
	public RecordHistory() {
	    httpMessage = new HttpMessage();	
		
	}

	public RecordHistory(int historyId, int historyType, long sessionId, long timeSentMillis, int timeElapsedMillis, String reqHeader, String reqBody, String resHeader, String resBody, String tag) throws HttpMalformedHeaderException {
		setHistoryId(historyId);
		setHistoryType(historyType);
        setSessionId(sessionId);
		httpMessage = new HttpMessage(reqHeader, reqBody, resHeader, resBody);
		httpMessage.setTimeSentMillis(timeSentMillis);
		httpMessage.setTimeElapsedMillis(timeElapsedMillis);
        httpMessage.setTag(tag);
	}
	
	public int getHistoryId() {
		return historyId;
	}
    public int getHistoryType() {
        return historyType;
    }
	
	public HttpMessage getHttpMessage() {
		return httpMessage;
	}
	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}
    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

}
