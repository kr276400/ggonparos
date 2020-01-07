package org.parosproxy.paros.core.scanner;

/*
 * 00000 - 09999 : 정보 수집
 * 10000 - 19999 : 기본 파일들
 * 20000 - 29999 :
 * 30000 - 39999 :
 */
public class Category {
    public static final int INFO_GATHER = 0;
    public static final int BROWSER = 1;
    public static final int SERVER = 2;
    public static final int MISC = 3;
    public static final int INJECTION = 4;
    
    private static String[] names = {
            "Information gathering",
            "Client browser",
            "Server security",
            "Miscellenous",
            "Injection"
                
    };
    
    
    public static String getName(int category) {
        String result = "확실하지 않습니다";
        if (category < names.length) {
            result = names[category];
        }
        return result;
    }
    
    public static int getCategory(String name) {
        for (int i=0; i<names.length; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return 0;
    }
    
    public static String[] getAllNames() {
        return names;
    }
    
    public static int length() {
        return names.length;
    }
}
