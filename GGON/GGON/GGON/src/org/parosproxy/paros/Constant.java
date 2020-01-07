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
 * final class�� static class ���̴�, Ŭ������ Ȯ���� ���ɼ��̳� ����� ���ɼ��̴�.
 * final�� Ȯ�� �Ұ����� Ŭ������ �����Ѵٰ� �����ض�
 */
public final class Constant {
    public static final String PROGRAM_NAME     = "GGON";
    /*
     * config.xml�� version_tag�� ���������� ������ �� �ؾ���
     * �ٸ� �ǹ̷� config.xml�� ���� �� �׻� ������ �� ���� �Ŷ�� ����.
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
        // ���α׷� �̸� �� ������
    }
    
    public static void setEyeCatcher(String eyeCatcher) {
        staticEyeCatcher = eyeCatcher;
        // ���α׷� ����ڰ� �����ϴ� ���� ���α׷� �̸� ������ �����Ǵ� ����
    }
    
    public static void setSP(boolean isSP) {
        staticSP = isSP;
    }

    public static boolean isSP() {
        return staticSP;
    }

/*
 * �ؿ� Constant �Լ��κ��� ���������� ���� ���� ���� ������, ���α׷��� ����� ��ǻ�Ϳ� ��ġ�ǰų�, ��ġ�� ���α׷��� ������Ʈ�� �� �ÿ�
 * ����Ǵ� �ڵ忩�� ��ȯ���� �����
 * ������ �Լ����̰�  �ؿ� �ִ� ���� �����ڶ�� ������ �׷��� �̸��� ���Ƶ� ���� �ٸ��� ����Ǵ°Ŷ� �����.
 */
    public Constant() {
        FileCopier copier = new FileCopier();
        File f = null;
        Log log = null;
        
        String userhome = System.getProperty("user.home");
        /*
         * getProperty()�� String���� ��µȴٰ� ��������, ���� ��� ���� user�� home�� ���������� ���̴� �����ε� �� ������ string������ ��µȴٰ� ��������.
         * �����ϰ� ������� Ȩ ���丮�� ���ٰ� user.home �Է��ϸ�.
         */
        // log��� ����� �ķν� ��ü�� ���� �αױ�� ����� ���丮�� �̸��� ����� �� ������, ����ڰ� ��ġ�� ��ο� �ִ� ���� ���丮 log���� log��ϵ��� �־��شٰ� �����ϼ�.
        System.setProperty(SYSTEM_GGON_USER_LOG, "log");
        /*
         * �ؿ� �κ��� �׳� �����ϰ� ��� �����ϴ� �κ��̶�� ������
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
                    log.info("����Ʈ�� ������: "+FILE_CONFIG_DEFAULT+" ���� "+FILE_CONFIG+ " �� ");
                    copier.copy(new File(FILE_CONFIG_DEFAULT),f);

                } else {
                    try {
                        
                        XMLConfiguration config = new XMLConfiguration(FILE_CONFIG);
                        config.setAutoSave(false);
                        config.load();

                        long ver = config.getLong("Version");
                        if (VERSION_TAG > ver) {
                           // ���� �츮�� ������ ������Ʈ �ϸ�, ������� �ķν� �ȿ��� ������ ������Ʈ �Ǽ� ����ȴٰ�
                            copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        
                        }
                    } catch (ConfigurationException e) {
                    	// ���࿡ config ���Ͽ� ������ ������, ���� ���� config ������ �����ε�.
                        copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        

                    } catch (NoSuchElementException e) {
                    	// ���࿡ config �����ϰų� ���� ������ ��� ���� config ���Ϸ� ���ٿ� �ص�.
                        copier.copy(new File(FILE_CONFIG_DEFAULT),f);                        
                    }

                }
                
                f=new File(FOLDER_SESSION);
                if (!f.isDirectory()) {
                    log.info("���� ������ "+FOLDER_SESSION);
                    f.mkdir();
                }
            } catch (Exception e) {
                System.err.println("Ȩ ���丮�� �ʱ�ȭ �� �� �����ϴ�! " + e.getMessage());
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
