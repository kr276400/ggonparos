package org.parosproxy.paros.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.common.FileXML;
import org.parosproxy.paros.network.HttpMessage;
import org.xml.sax.SAXException;



public class Session extends FileXML {
	private static final String ROOT = "session";
	
	private static final String SESSION_DESC = "sessionDesc";
	private static final String SESSION_ID = "sessionId";
	private static final String SESSION_NAME = "sessionName";
	
	private static final String[] PATH_SESSION_DESC = {ROOT, SESSION_DESC};	
	private static final String[] PATH_SESSION_ID = {ROOT, SESSION_ID};
	private static final String[] PATH_SESSION_NAME = {ROOT, SESSION_NAME};

	private Model model = null;
	private String fileName = "";
	private String sessionDesc = "";
	
	// XML �ȿ� �Ķ���� ����
	private long sessionId = 0;
	private String sessionName = "";
	private SiteMap siteTree = null;
	
	/*
	 * ���� ���̵� �Է� �� ���� ������ ����  ����� �κ��̶�� �����ϸ� ��.
	 * ���� �ý��� �ð��� �Է� ���� ����ID�� ���ؼ��� ����ID���� �̿�ɰſ�
	 */
	public Session(Model model) {
		super(ROOT);

		// ���⿡�ٰ� �������� ���ǵ��� �߰��ϴ� �� ����
		setSessionId(System.currentTimeMillis());
		setSessionName("������� ���� ����");
		setSessionDesc("");
		
		// ����Ʈ�� ����ٰ� ����
		this.siteTree = SiteMap.createTree(model);
		
		this.model = model;
		
	}
	
	public void discard() {
	    try {
	        model.getDb().getTableHistory().deleteHistorySession(getSessionId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	// SessionDesc�� ����, ��ȯ��
    public String getSessionDesc() {
        return sessionDesc;
    }
	
    // ����ID�� ������
	public long getSessionId() {
		return sessionId;
	}
	// ���� �̸� ����
	public String getSessionName() {
		return sessionName;
	}
	//SiteTree�� ������
    public SiteMap getSiteTree() {
        return siteTree;
    }
	
    // ���ο� ���¸� �����Ѵٰ� ���� ��
    public boolean isNewState() {
        return fileName.equals("");
    }

    
    public void open(final File file, final SessionListener callback) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Exception thrownException = null;
                try {
                    open(file.getAbsolutePath());
                } catch (Exception e) {
                    thrownException = e;
                }
                if (callback != null) {
                    callback.sessionOpened(file, thrownException);
                }
            }
        });
        t.setPriority(Thread.NORM_PRIORITY-2);
        t.start();
    }

	public void open(String fileName) throws SQLException, SAXException, IOException, Exception {

		readAndParseFile(fileName);
		model.getDb().close(false);
		model.getDb().open(fileName);
		this.fileName = fileName;
		
		SiteNode newRoot = new SiteNode("Sites");
		siteTree.setRoot(newRoot);

		// history reference�� ���� �Ѵٰ� �����
		List list = model.getDb().getTableHistory().getHistoryList(getSessionId(), HistoryReference.TYPE_MANUAL);
		HistoryReference historyRef = null;
		
		for (int i=0; i<list.size(); i++) {
			int historyId = ((Integer) list.get(i)).intValue();

			try {
				historyRef = new HistoryReference(historyId);
				getSiteTree().addPath(historyRef);

				if (i % 100 == 99) Thread.yield();
			} catch (Exception e) {};
			
		}
		
		// siteTree�� �����Ѵٰ� ���� ��
		list = model.getDb().getTableHistory().getHistoryList(getSessionId(), HistoryReference.TYPE_SPIDER);
		
		for (int i=0; i<list.size(); i++) {
			int historyId = ((Integer) list.get(i)).intValue();

			try {
				historyRef = new HistoryReference(historyId);
				getSiteTree().addPath(historyRef);

				if (i % 100 == 99) Thread.yield();

			} catch (Exception e) {};
		}
		System.gc();
	}
	
	protected void parse() throws Exception {
	    
	    long tempSessionId = 0;
	    String tempSessionName = "";
	    String tempSessionDesc = "";
	    
	    // �������� temp�� üũ�Ϸ��� ��� �ϴ°ǵ�, temp�� �� �����̶�� �����ϸ� �ɵ�, ���� ������ �߻��ϸ� ���ܰ� �˾Ƽ� �ɰ� ����
		tempSessionId = Long.parseLong(getValue(SESSION_ID));
		tempSessionName = getValue(SESSION_NAME);
		tempSessionName = getValue(SESSION_NAME);

		// ���� ������ �������� ��� ������ ���õ� ��
		sessionId = tempSessionId;
		sessionName = tempSessionName;
		sessionDesc = tempSessionDesc;
		


	}

	/*
	 * �Է¹��� ���� �����̳� �ݹ��� ���ؼ�,
	 * ������ �����Ϸ��� �񵿱����� ���� �θ�����.
	 * �����, ��ǻ�Ϳ��� �񵿱����� ����, ������ �ӵ��� �ƴ϶�, ������ ��ȣ�� �������� �ӵ��� �ñ⸦ ���ߴ� ��� �����.
	 */
    public void save(final String fileName, final SessionListener callback) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Exception thrownException = null;
                try {
                    save(fileName);
                } catch (Exception e) {
                    thrownException = e;
                }
                if (callback != null) {
                    callback.sessionSaved(thrownException);
                }
            }
        });
        t.setPriority(Thread.NORM_PRIORITY-2);
        t.start();
    }
    
    // �Է¹��� ���� �̸��� ���ؼ�,
    // ���� �����Ϸ��� �񵿱����� �� �θ��� ��
	public void save(String fileName) throws Exception {
	    saveFile(fileName);
		if (isNewState()) {
		    model.moveSessionDb(fileName);
		} else {
		    if (!this.fileName.equals(fileName)) {
		    	// �� ���� �̸����� ������ �����ϴ� �κ�
		        model.copySessionDb(this.fileName, fileName);
		    }
		}
	    this.fileName = fileName;
		
		synchronized (siteTree) {
		    saveSiteTree((SiteNode) siteTree.getRoot());
		}
		
		model.getDb().getTableSession().update(getSessionId(), getSessionName());

	}
	
	/*
	 * �Է� ���� sessionDesc�� sessionDesc�� �����Ѵٰ� ���� ��
	 */
    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
		setValue(PATH_SESSION_DESC, sessionDesc);
    }
	
    /*
     * �Է¹��� ����id�� ����Ѵٰ� ������
     */
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
		setValue(PATH_SESSION_ID, Long.toString(sessionId));

	}
	/*
	 * �Է� ���� ���� ���� �������� ������
	 */
	public void setSessionName(String name) {
		this.sessionName = name;
		setValue(PATH_SESSION_NAME, name);
		
	}

    
    public String getFileName() {
        return fileName;
    }
    
    private void saveSiteTree(SiteNode node) {
        HttpMessage msg = null;

        if (!node.isRoot()) {
            if (node.getHistoryReference().getHistoryType() < 0) {
            	// ���⼭ ve�� ���� ��ٴ� ����
                saveNodeMsg(msg);
            }
        }
        for (int i=0; i<node.getChildCount(); i++) {
            try {
                saveSiteTree((SiteNode) node.getChildAt(i));
            } catch (Exception e) {}
        }        
    }
    
    private void saveNodeMsg(HttpMessage msg) {
    	// �� �͵� �� ����
    }
    
    public String getSessionFolder() {
        String result = "";
        if (fileName.equals("")) {
            result = Constant.getInstance().FOLDER_SESSION;
        } else {
            File file = new File(fileName);
            result = file.getParent();
        }
        return result;
    }

}
