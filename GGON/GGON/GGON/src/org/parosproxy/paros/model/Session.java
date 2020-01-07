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
	
	// XML 안에 파라미터 값들
	private long sessionId = 0;
	private String sessionName = "";
	private SiteMap siteTree = null;
	
	/*
	 * 세션 아이디 입력 값 현재 세션을 위한  만드는 부분이라고 생각하면 됨.
	 * 현재 시스템 시간이 입력 받은 세션ID를 통해서나 세션ID으로 이용될거여
	 */
	public Session(Model model) {
		super(ROOT);

		// 여기에다가 변동심한 세션들을 추가하는 것 같음
		setSessionId(System.currentTimeMillis());
		setSessionName("저장되지 않은 세션");
		setSessionDesc("");
		
		// 디폴트를 만든다고 보셈
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
	
	// SessionDesc를 리턴, 반환함
    public String getSessionDesc() {
        return sessionDesc;
    }
	
    // 세션ID를 리턴함
	public long getSessionId() {
		return sessionId;
	}
	// 세션 이름 리턴
	public String getSessionName() {
		return sessionName;
	}
	//SiteTree를 리턴함
    public SiteMap getSiteTree() {
        return siteTree;
    }
	
    // 새로운 상태를 리턴한다고 보면 됨
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

		// history reference를 업뎃 한다고 보면됨
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
		
		// siteTree를 업뎃한다고 보면 됨
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
	    
	    // 변동심한 temp를 체크하려고 사용 하는건데, temp는 빈 공간이라고 생각하면 될듯, 만약 에러가 발생하면 예외가 알아서 될거 같음
		tempSessionId = Long.parseLong(getValue(SESSION_ID));
		tempSessionName = getValue(SESSION_NAME);
		tempSessionName = getValue(SESSION_NAME);

		// 여기 지나면 변동심한 멤버 변수가 세팅될 듯
		sessionId = tempSessionId;
		sessionName = tempSessionName;
		sessionDesc = tempSessionDesc;
		


	}

	/*
	 * 입력받은 파일 네임이나 콜백을 통해서,
	 * 세션을 저장하려고 비동기적인 놈을 부를거임.
	 * 참고로, 컴퓨터에서 비동기적인 뜻은, 고정된 속도가 아니라, 약정된 신호를 기준으로 속도나 시기를 맞추는 통신 방법임.
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
    
    // 입력받은 파일 이름을 통해서,
    // 세션 저장하려고 비동기적인 놈 부르는 곳
	public void save(String fileName) throws Exception {
	    saveFile(fileName);
		if (isNewState()) {
		    model.moveSessionDb(fileName);
		} else {
		    if (!this.fileName.equals(fileName)) {
		    	// 새 파일 이름으로 파일을 복사하는 부분
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
	 * 입력 받은 sessionDesc로 sessionDesc를 세팅한다고 보면 됨
	 */
    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
		setValue(PATH_SESSION_DESC, sessionDesc);
    }
	
    /*
     * 입력받은 세션id를 등록한다고 생각혀
     */
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
		setValue(PATH_SESSION_ID, Long.toString(sessionId));

	}
	/*
	 * 입력 받은 네임 값을 네임으로 지정함
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
            	// 여기서 ve는 저장 됬다는 말임
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
    	// 암 것도 필 없음
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
