package org.parosproxy.paros.model;

import java.sql.SQLException;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.db.TableHistory;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/*
 * 이 클래스는 데이터베이스 안에 저장되어있는 http message를 이용하려고 추상적인 거로 이용하는 건데.
 * getHttpMessage()라는 http메세지 얻어오는 함수가 이용되려고 불러지면, 그 때 데이터베이스에서 전체 http 메세지를 읽을거여
 */
public class HistoryReference {

	/*
	 * 임시적으로 타입은 기록에서 다시 거져오지 않을 거여, 삭제되어있을겨
	 */
   public static final int TYPE_TEMPORARY = 0;
   public static final int TYPE_MANUAL = 1;
   public static final int TYPE_SPIDER = 2;
   public static final int TYPE_SCANNER = 3;
   public static final int TYPE_SPIDER_SEED = 4;
   public static final int TYPE_SPIDER_VISITED = 5;
   public static final int TYPE_HIDDEN = 6;
   
   // 여기서 ve는 저장되지 않은 메세지라는 뜻이여.
   public static final int TYPE_SPIDER_UNSAVE = -TYPE_SPIDER;

   private static java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("##0.###");
	private static TableHistory staticTableHistory = null;

	private int historyId = 0;
	private int historyType = TYPE_MANUAL;
	private SiteNode siteNode = null;
    private String display = null;
    private long sessionId = 0;
	
    public long getSessionId() {
        return sessionId;
    }

    public HistoryReference(int historyId) throws HttpMalformedHeaderException, SQLException {
		RecordHistory history = null;		
		history = staticTableHistory.read(historyId);
		HttpMessage msg = history.getHttpMessage();
		build(history.getSessionId(), history.getHistoryId(), history.getHistoryType(), msg);

	}
	
	public HistoryReference(Session session, int historyType, HttpMessage msg) throws HttpMalformedHeaderException, SQLException {
		RecordHistory history = null;		
		history = staticTableHistory.write(session.getSessionId(), historyType, msg);		
		build(session.getSessionId(), history.getHistoryId(), history.getHistoryType(), msg);

		
	}

	private void build(long sessionId, int historyId, int historyType, HttpMessage msg) {
	    this.sessionId = sessionId;
	    this.historyId = historyId;
		this.historyType = historyType;
		if (historyType == TYPE_MANUAL) {
		    this.display = getDisplay(msg);
		}
	}
	
	public static void setTableHistory(TableHistory tableHistory) {
		staticTableHistory = tableHistory;
	}
	public int getHistoryId() {
		return historyId;
	}

	public HttpMessage getHttpMessage() throws HttpMalformedHeaderException, SQLException {
		// 완성된? 완료한 메세지 불러올겨
		RecordHistory history = staticTableHistory.read(historyId);
		return history.getHttpMessage();
	}
	
	public String toString() {

        if (display != null) {
            return display;
        }
        
	    HttpMessage msg = null;
	    try {
	        msg = getHttpMessage();
            display = getDisplay(msg);	        
	    } catch (HttpMalformedHeaderException e1) {
	        display = "";
	    } catch (SQLException e) {
	        display = "";
	    }
        return display;
	}
	
   public int getHistoryType() {
       return historyType;
   }
   
   /*
    * 데이터베이스에서 지금 까지 reference한 기록들 삭제하는 부분이여.
    */
   public void delete() {
       if (historyId > 0) {
           try {
               staticTableHistory.delete(historyId);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }
   
   public SiteNode getSiteNode() {
       return siteNode;
   }
   /*
    * 세팅할 사이트 노드.
    */
   public void setSiteNode(SiteNode siteNode) {
       this.siteNode = siteNode;
   }
   
   private String getDisplay(HttpMessage msg) {
       StringBuffer sb = new StringBuffer(Integer.toString(historyId) + " ");
       sb.append(msg.getRequestHeader().getPrimeHeader());
       if (!msg.getResponseHeader().isEmpty()) {
           sb.append(" \t=> " + msg.getResponseHeader().getPrimeHeader());
           String diffTimeString = "\t [" + decimalFormat.format((double) (msg.getTimeElapsedMillis()/1000.0)) + " s]";
           // 위에도 쓰여있지만 decimalFormat은 decimal 패턴으로 format 메소드를 이용해서 포맷하는 건데,
           // 반환되는 값은 string으로 이루어져있고, 지정할 수 있는 패턴은 0이랑 #을 사용해서 지정해
           // 예를 들어서 74.12345678일때, 0.###방식으로 구하면 74.12
           // 000.## 이라면, 074.12
           // 00.# 이라면, 74.1 이렇게
           sb.append(diffTimeString);
       }
       
       return sb.toString();
       
   }
   
   public void setTag(String tag) {
       try {
           staticTableHistory.updateTag(historyId, tag);
       } catch (SQLException e) {
           e.printStackTrace();
       }
       
   }
}
