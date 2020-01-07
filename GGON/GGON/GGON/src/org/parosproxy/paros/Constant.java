package org.parosproxy.paros;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.model.FileCopier;

/*
 * final class랑 static class 차이는, 클래스의 확장의 가능성이나 상속의 가능성이다.
 * final은 확장 불가능한 클래스로 설계한다고 생각해라
 */
public final class Constant {
    public static final String PROGRAM_NAME     = "GGON";
    /*
     * config.xml이 version_tag랑 같아지도록 설정을 꼭 해야함
     * 다른 의미로 config.xml은 위에 더 항상 덧붙일 수 있을 거라는 뜻임.
     */
    //public static final String PROGRAM_VERSION = "3.2.13";
    public static final long VERSION_TAG = 30020013;
    public static final String PROGRAM_TITLE = PROGRAM_NAME;
    public static final String SYSTEM_GGON_USER_LOG = "GGON.user.log";
    public static final String FILE_CONFIG_DEFAULT = "xml/config.xml";
    public String FILE_CONFIG = "config.xml";
    public static final String FOLDER_PLUGIN = "plugin";
    public static final String FOLDER_FILTER = "filter";
    public static final String FOLDER_SESSION_DEFAULT = "session";
    public String FOLDER_SESSION = "session";
    public static final String DBNAME_TEMPLATE = "db" + System.getProperty("file.separator") + "parosdb";

    public static final String DBNAME_UNTITLED_DEFAULT = FOLDER_SESSION_DEFAULT + System.getProperty("file.separator") + "untitled";

    public String DBNAME_UNTITLED = FOLDER_SESSION + System.getProperty("file.separator") + "untitled";
    public String ACCEPTED_LICENSE_DEFAULT = "AcceptedLicense";
    public String ACCEPTED_LICENSE = ACCEPTED_LICENSE_DEFAULT;
    
    private static Constant instance = null;
    
    public static final int MAX_HOST_CONNECTION = 5;
    public static final String USER_AGENT = PROGRAM_NAME;

    private static String staticEyeCatcher = PROGRAM_NAME;
    private static boolean staticSP = false;
    private static Pattern patternWindows = Pattern.compile("window", Pattern.CASE_INSENSITIVE);
    private static Pattern patternLinux = Pattern.compile("linux", Pattern.CASE_INSENSITIVE);

    public static String getEyeCatcher() {
        return staticEyeCatcher;
        // 프로그램 이름 값 리턴함
    }
    
    public static void setEyeCatcher(String eyeCatcher) {
        staticEyeCatcher = eyeCatcher;
        // 프로그램 사용자가 지정하는 값이 프로그램 이름 값으로 지정되는 말임
    }
    
    public static void setSP(boolean isSP) {
        staticSP = isSP;
    }

    public static boolean isSP() {
        return staticSP;
    }

/*
 * 밑에 Constant 함수부분이 데이터형이 정의 되지 않은 이유는, 프로그램이 사용자 컴퓨터에 설치되거나, 설치된 프로그램의 업데이트를 할 시에
 * 실행되는 코드여서 반환값이 없어요
 * 위에는 함수들이고  밑에 있는 놈은 생성자라고 생각혀 그래서 이름이 같아도 둘이 다르게 실행되는거라 보면됨.
 */
    public Constant() {
        FileCopier copier = new FileCopier();
        File f = null;
        Log log = null;
        
        String userhome = System.getProperty("user.home");
        /*
         * getProperty()는 String으로 출력된다고 생각혀라, 예를 들어 위에 user랑 home은 리눅스에서 쓰이는 값들인데 저 값들이 string형으로 출력된다고 생각혀라.
         * 간단하게 사용자의 홈 디렉토리로 간다고 user.home 입력하면.
         */
        // log라고 저장된 파로스 자체의 내부 로그기록 남기는 디렉토리의 이름을 사용할 수 없으니, 사용자가 설치한 경로에 있는 파일 디렉토리 log값에 log기록들을 넣어준다고 생각하셈.
        System.setProperty(SYSTEM_GGON_USER_LOG, "log");
        /*
         * 밑에 부분은 그냥 간단하게 경로 지정하는 부분이라고 생각혀
         */
        if (userhome != null && !userhome.equals("")) {
            userhome += System.getProperty("file.separator")+"GGON";
            f = new File(userhome);
            userhome += System.getProperty("file.separator");
            FILE_CONFIG = userhome+FILE_CONFIG;
            FOLDER_SESSION = userhome+FOLDER_SESSION;
            DBNAME_UNTITLED = userhome+DBNAME_UNTITLED;
            ACCEPTED_LICENSE = userhome+ACCEPTED_LICENSE;
            try {
                
                System.setProperty(SYSTEM_GGON_USER_LOG, userhome);
                System.setProperty("log4j.configuration","xml/log4j.properties");
                
                if (!f.isDirectory()) {
                    f.mkdir();
                    log = LogFactory.getLog(Constant.class);
                    log.info("Created directory "+userhome);

                } else {
                    log = LogFactory.getLog(Constant.class);
                    
                }
                
                f=new File(FILE_CONFIG);
                if (!f.isFile()) {
                    log.info("디폴트값 복사중: "+FILE_CONFIG_DEFAULT+" 에서 "+FILE_CONFIG+ " 로 ");
                    copier.copy(new File(FILE_CONFIG_DEFAULT),f);

                } else {
                    try {
                        
                        XMLConfiguration config = new XMLConfiguration(FILE_CONFIG);
                        config.setAutoSave(false);
                        config.load();

                        long ver = config.getLong("Version");
                        if (VERSION_TAG > ver) {
                           // 만약 우리가 버전을 업데이트 하면, 사용자의 파로스 안에도 버전이 업데이트 되서 저장된다고
                            copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        
                        }
                    } catch (ConfigurationException e) {
                    	// 만약에 config 파일에 문제가 있을때, 전에 쓰던 config 파일을 덧붙인데.
                        copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        

                    } catch (NoSuchElementException e) {
                    	// 만약에 config 사용안하거나 퇴물인 파일일 경우 전의 config 파일로 덧붙여 준데.
                        copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        
                    }

                }
                
                f=new File(FOLDER_SESSION);
                if (!f.isDirectory()) {
                    log.info("폴더 생성중 "+FOLDER_SESSION);
                    f.mkdir();
                }
            } catch (Exception e) {
                System.err.println("홈 디렉토리를 초기화 할 수 없습니다! " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            }
            
        } else {

            System.setProperty("log4j.configuration","xml/log4j.properties");

            FILE_CONFIG = FILE_CONFIG_DEFAULT;
            FOLDER_SESSION = FOLDER_SESSION_DEFAULT;
            DBNAME_UNTITLED = DBNAME_UNTITLED_DEFAULT;
            ACCEPTED_LICENSE = ACCEPTED_LICENSE_DEFAULT;
            
        }
                

    }
    
    public static Constant getInstance() {
        if (instance==null) {
            instance=new Constant();
        }
        return instance;

    }
    
    public static boolean isWindows() {
        String os_name = System.getProperty("os.name");
        
        Matcher matcher = patternWindows.matcher(os_name);
        return matcher.find();
    }
    
    public static boolean isLinux() {
        String os_name = System.getProperty("os.name");
        Matcher matcher = patternLinux.matcher(os_name);
        return matcher.find();
    }
    
}
