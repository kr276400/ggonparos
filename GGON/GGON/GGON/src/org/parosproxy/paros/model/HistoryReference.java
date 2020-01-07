package org.parosproxy.paros.model;

import java.sql.SQLException;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.db.TableHistory;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/*
 * �� Ŭ������ �����ͺ��̽� �ȿ� ����Ǿ��ִ� http message�� �̿��Ϸ��� �߻����� �ŷ� �̿��ϴ� �ǵ�.
 * getHttpMessage()��� http�޼��� ������ �Լ��� �̿�Ƿ��� �ҷ�����, �� �� �����ͺ��̽����� ��ü http �޼����� �����ſ�
 */
public class HistoryReference {

	/*
	 * �ӽ������� Ÿ���� ��Ͽ��� �ٽ� �������� ���� �ſ�, �����Ǿ�������
	 */
   public static final int TYPE_TEMPORARY = 0;
   public static final int TYPE_MANUAL = 1;
   public static final int TYPE_SPIDER = 2;
   public static final int TYPE_SCANNER = 3;
   public static final int TYPE_SPIDER_SEED = 4;
   public static final int TYPE_SPIDER_VISITED = 5;
   public static final int TYPE_HIDDEN = 6;
   
   // ���⼭ ve�� ������� ���� �޼������ ���̿�.
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
		// �ϼ���? �Ϸ��� �޼��� �ҷ��ð�
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
    * �����ͺ��̽����� ���� ���� reference�� ��ϵ� �����ϴ� �κ��̿�.
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
    * ������ ����Ʈ ���.
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
           // ������ ���������� decimalFormat�� decimal �������� format �޼ҵ带 �̿��ؼ� �����ϴ� �ǵ�,
           // ��ȯ�Ǵ� ���� string���� �̷�����ְ�, ������ �� �ִ� ������ 0�̶� #�� ����ؼ� ������
           // ���� �� 74.12345678�϶�, 0.###������� ���ϸ� 74.12
           // 000.## �̶��, 074.12
           // 00.# �̶��, 74.1 �̷���
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
