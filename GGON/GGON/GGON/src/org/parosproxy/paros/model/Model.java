package org.parosproxy.paros.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.db.Database;
import org.xml.sax.SAXException;

public class Model {

    private static Model model = null;

    private static final String DBNAME_TEMPLATE = Constant.DBNAME_TEMPLATE;
    private String DBNAME_UNTITLED = Constant.getInstance().DBNAME_UNTITLED;
	private static int DBNAME_COPY = 1;
	
    private Session session = null;
	private OptionsParam optionsParam = null;
	private Database db = null;
	private String currentDBNameUntitled = "";

	
	public Model() {
	    /*
	     * model이 확인을 했는데 또 확인하지 않도록, 여기서 확실하게 잘 잡고 넘어가야함.
	     * init이나 각 getter안에서 실행되어야함
	     */
	    session = new Session(this);
	    optionsParam = new OptionsParam();
	}
	
	public OptionsParam getOptionsParam() {
	    if (optionsParam == null) {
	        optionsParam = new OptionsParam();
	    }
		return optionsParam;
	}
	
	public void setOptionsParam(OptionsParam param) {
		optionsParam = param;
	}
	
	public Session getSession() {
	    if (session == null) {
	        session = new Session(this);
	    }
		return session;
	}
	public void setSession(Session paramSession) {
		session = paramSession;
	}
	
	public void init() throws SAXException, IOException, Exception {
	    db = Database.getSingleton();
	    
	    createAndOpenUntitledDb();
		HistoryReference.setTableHistory(getDb().getTableHistory());
        getOptionsParam().load(Constant.getInstance().FILE_CONFIG);
	}

	
	public static Model getSingleton() {
	    if (model == null) {
	        model = new Model();
	    }
	    return model;
	}
	/*
	 * db 리턴하는 부분
	 */
    public Database getDb() {
        return db;
    }
    
    public void moveSessionDb(String destFile) throws Exception {
    	/*
    	 * 복사된 세션을 항상 이용해야한다, 옮겨진 파일은 리눅스 운영체제 안에서는 실행 안됨.
    	 * 그리고, 윈도우에 서로 다르게 남겨진 파일들은 실행이 안되게끔 만듬
    	 */

        copySessionDb(currentDBNameUntitled, destFile);

    }
    
    public void copySessionDb(String currentFile, String destFile) throws Exception {
        
        getDb().close(false);
        // 특정 경로에 있는 파일들이랑 연관되어있는 copy 세션임
        FileCopier copier = new FileCopier();        
        
        File fileIn1 = new File(currentFile + ".data");
        File fileOut1 = new File(destFile + ".data");
        copier.copy(fileIn1, fileOut1);
        
        File fileIn2 = new File(currentFile + ".script");
        File fileOut2 = new File(destFile + ".script");
        copier.copy(fileIn2, fileOut2);
        
        File fileIn3 = new File(currentFile + ".properties");
        File fileOut3 = new File(destFile + ".properties");
        copier.copy(fileIn3, fileOut3);

        File fileIn4 = new File(currentFile + ".backup");
        if (fileIn4.exists()) {
            File fileOut4 = new File(destFile + ".backup");
            copier.copy(fileIn4, fileOut4);
        }

        getDb().open(destFile);
        
    }

    
    public void createAndOpenUntitledDb() throws ClassNotFoundException, Exception {

        getDb().close(false);
        // 세션 디렉토리 안에 이름 없는 db 세션 모두 삭제함
        File dir = new File(getSession().getSessionFolder());
        File[] listFile = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir1, String fileName) {
                if (fileName.startsWith("untitled")) {
                    return true;
                }
                return false;
            }
        });
        for (int i=0; i<listFile.length;i++) {
            listFile[i].delete();
        }
        // 새로운 db 템플릿을 만들고 복사함
        currentDBNameUntitled = DBNAME_UNTITLED + DBNAME_COPY;
        FileCopier copier = new FileCopier();
	    File fileIn = new File(DBNAME_TEMPLATE + ".data");
	    File fileOut = new File(currentDBNameUntitled + ".data");
	    fileOut.delete();	    
	    
	    copier.copy(fileIn, fileOut);

	    fileIn = new File(DBNAME_TEMPLATE + ".properties");
	    fileOut = new File(currentDBNameUntitled  + ".properties"); 
	    fileOut.delete();

	    copier.copy(fileIn, fileOut);
	    
	    fileIn = new File(DBNAME_TEMPLATE + ".script");
	    fileOut = new File(currentDBNameUntitled + ".script"); 
	    fileOut.delete();
	    copier.copy(fileIn, fileOut);
	    
	    fileIn = new File(currentDBNameUntitled + ".backup");
	    if (fileIn.exists()) {
	        fileIn.delete();
	    }
	    
	    getDb().open(currentDBNameUntitled);
	    DBNAME_COPY++;
    }

}
