package org.parosproxy.paros;

import java.util.Hashtable;
import java.util.Vector;

import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.network.HttpSender;

public class CommandLine {

    static final String SESSION = "-session";
    static final String NEW_SESSION = "-newsession";
    static final String HELP = "-help";
    static final String HELP2 = "-h";
    
    static final String NO_USER_AGENT = "-nouseragent";
    static final String SP = "-sp";
    
    private boolean GUI = true;
    private String[] args = null;
    private Hashtable keywords = new Hashtable();
    private Vector commandList = null;
    
    public CommandLine(String[] args) throws Exception {
        this.args = args;
        parseFirst(this.args);
    }
    /* 
     * throw - 예외 발생시키는 것
     * throw는 메소드 안에서 상위 블럭으로 예외를 던지는 것
     * throw는 억지로 에러를 발생시킬 때도 사용 되지만, 현재 메소드의 에러를 처리한 후에 상위 메소드에 에러 정보를 보냄으로써, 상위 메소드에서도 에러가 발생한 것을 감지할 수 있다.
     * throw는 실제로 exception을 throw할 때 사용하는 keyword임.
     * 강제로 예외를 발생시킨다. 
     * 프로그래머의 판단에 따른 처리이다.
     * 
     * 
     * throws, throw - 예외를 던지는 것
     * throws는 현재 메소드에서 상위 메소드로 예외를 던진다.
     * throws는 메소드나 생성자를 수행할 때 발생하는 exception을 선언할 때 사용하는 keyword이다.
     * 즉, 예외를 자신이 처리하지 않고, 자신을 호출하는 메소드에게 책임을 전가하는 것이다.
     * 
     * 
     */
    private boolean checkPair(String[] args, String paramName, int i) throws Exception {
        String key = args[i];
        String value = null;
        if (key == null) return false;
        if (key.equalsIgnoreCase(paramName)) {
            value = args[i+1];
            if (value == null) throw new Exception();
            /*
             * throw new는 위에 checkPair 함수를 실행할 때 생기는 예외 처리 함수와 다르게 하기 위한거임
             * 위에 Exception 예외 처리랑 다르게 정의하기 위한거
             */
            keywords.put(paramName, value);
            /*
             * keyword안에 paramName과 입력되거나 들어가는 value 값을 집어 넣어준다는 뜻임
             */
            args[i] = null;
            args[i+1] = null;
            return true;
        }
        return false;
    }

    private boolean checkSwitch(String[] args, String paramName, int i) throws Exception {
        String key = args[i];
        if (key == null) return false;
        if (key.equalsIgnoreCase(paramName)) {
        	/*
        	 * 같으면 무시함
        	 */
            keywords.put(paramName, "");
            args[i] = null;
            return true;
        }
        return false;
    }
    
	private void parseFirst(String[] args) throws Exception {

	    for (int i=0; i<args.length; i++) {
	        
	        if (parseSwitchs(args, i)) continue;
	        if (parseKeywords(args, i)) continue;
	        
        }	        
    }

	public void parse(Vector commandList) throws Exception {
	    this.commandList = commandList;
	    CommandLineArgument lastArg = null;
	    boolean found = false;
	    int remainingValueCount = 0;
	    
	    for (int i=0; i<args.length; i++) {
	        if (args[i] == null) continue;
	        found = false;
	        
		    for (int j=0; j<commandList.size() && !found; j++) {
		        CommandLineArgument[] extArg = (CommandLineArgument[]) commandList.get(j);
		        /*
		         * CommandLineArgument[] 데이터형의 배열로 형변환 시킨다고 생각하면됨.
		         */
		        for (int k=0; k<extArg.length && !found; k++)
		        if (args[i].compareToIgnoreCase(extArg[k].getName()) == 0) {
		            
		        	// 만약에 전의 키워드나 파라미터값이 필수가 아니라는 조건이 만족할 시, 체크한다.
		            if (remainingValueCount > 0) {
			            throw new Exception("키워드의 파라미터 값이 없습니다. '" + lastArg.getName() + "'.");
		            }
		            // 이 키워드대로 진행한다.
		            lastArg = extArg[k];
		            lastArg.setEnabled(true);
		            found = true;
		            args[i] = null;
		            remainingValueCount = lastArg.getNumOfArguments();
		        }
		    }
		    // 만약 현재 문자열이 '-' 대쉬로 진행된 키워드라면 확인해라
		    if (args[i] != null && args[i].startsWith("-")) {
		        continue;
		    }
		    // 만약 더 이상 기대했던 param 값이 존재하지 않을 경우 확인하셈 (참고로, param은 parameter의 축약된 값임)
		    if (lastArg != null && remainingValueCount == 0) {
		        continue;
		    }   
		    // 성사된 키워드에서 남은 것들을 사용할 때 체크함
		    if (!found && lastArg != null) {		            
		        if (lastArg.getPattern() == null || lastArg.getPattern().matcher(args[i]).find()) {
		            lastArg.getArguments().add(args[i]);
		            if (remainingValueCount > 0) {
		                remainingValueCount--;
		            }
		            args[i] = null;
		        } else {
		            throw new Exception(lastArg.getErrorMessage());
		        }
		    }
		    /*
		     * 참고로 matcher는 정규 표현식 클래스 인데 그 안에 find, start, end 등의 많은 메소드가 존재함.
		     * 패턴값이랑 일치하는 텍스트들에 대해서 find start end 메소드가 일을 하는거임
		     */
	    }
	    // 만약 마지막 키워드나 파라미터 값이 아니거나 존재하지 않은 조건이 성립할 경우 체크한다고 
	    if (lastArg != null && remainingValueCount > 0) {
            throw new Exception("키워드의 파라미터 값이 없습니다 '" + lastArg.getName() + "'.");
        }	    
	    // 알려지지 않은 키워드들이 존재할 경우 체크해보셈
	    for (int i=0; i<args.length; i++) {
	        if (args[i] != null) {
                throw new Exception("알려진게 없는 옵션: " + args[i]);	            
	        }
	    }
	}
	
	private boolean parseSwitchs(String[] args, int i) throws Exception {

	    boolean result = false;
	    
        if (checkSwitch(args, NO_USER_AGENT, i)) {
            HttpSender.setUserAgent("");
            Constant.setEyeCatcher("");
            result = true;

        } else if (checkSwitch(args, SP, i)) {
            Constant.setSP(true);
            result = true;
        } else if (checkSwitch(args, HELP, i)) {
            result = true;
            setGUI(false);
        } else if (checkSwitch(args, HELP2, i)) {
            result = true;
            setGUI(false);
        }
        return result;
	}
	
	private boolean parseKeywords(String[] args, int i) throws Exception {
	    boolean result = false;
	    if (checkPair(args, NEW_SESSION, i)) {
            setGUI(false);
            result = true;
	    } else if (checkPair(args, SESSION, i)) {
	        setGUI(false);
	        result = true;
	    }
	    return result;
	}

    public boolean isGUI() {
        return GUI;
    }
 
    public void setGUI(boolean GUI) {
        this.GUI = GUI;
    }
    
    public String getArgument(String keyword) {
        return (String) keywords.get(keyword);
        /*
         * 입력된 keyword 값 가져와서 keywords에 대입해서 쓰겠다고
         */
    }

	static String getHelpGeneral() {
		String CRLF = "\r\n";
		String help =
			"GUI usage:" + CRLF +
			"\tjavaw GGON.jar" + CRLF +
			"\tjava -jar GGON.jar" + CRLF +
			"see java -jar GGON.jar {-h|-help} for detail.\r\n\r\n";
		return help;
	}
	
    public String getHelp() {
        StringBuffer sb = new StringBuffer(getHelpGeneral());        
        sb.append("명령어 사용:\r\n");
        sb.append("java -jar GGON.jar {-h|-help} {-newsession session_file_path} {options}\r\n");
        sb.append("옵션:\r\n\r\n");
        /*
         * append 문자열을 더해주는거 concat이랑은 다름 sb로 값을 집어넣어서 출력할 경우 append된 모든 문자열들이 다 출력됨
         */

        for (int i=0; i<commandList.size(); i++) {
	        CommandLineArgument[] extArg = (CommandLineArgument[]) commandList.get(i);
	        for (int j=0; j<extArg.length; j++) {
	            sb.append(extArg[j].getHelpMessage() + "\r\n");
	        }
        }
        
        return sb.toString();
        /*
         * 문자열로 전환해준다고 생각해라 toString
         * (String) 형변환 문자열은 안되는 부분이 많이 존재하는데, toString함수는 강력한 파워를 지닌 놈이어서 다 변환해준다고 생각혀라
         */
    }
    
    public boolean isEnabled(String keyword) {
        
        Object obj = keywords.get(keyword);
        if (obj != null && obj instanceof String) {
        	/*
        	 * instanceof는 데이터 타입 확인하는거임
        	 * 즉, 여기서 keyword 값을 받은 obj라는 놈이 null 값이 아니면서, obj의 값이 String으로 형변환이 가능한 경우를 뜻함
        	 */
            return true;
        }
        return false;
    }
    
}
