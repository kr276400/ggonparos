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
     * throw - ���� �߻���Ű�� ��
     * throw�� �޼ҵ� �ȿ��� ���� ������ ���ܸ� ������ ��
     * throw�� ������ ������ �߻���ų ���� ��� ������, ���� �޼ҵ��� ������ ó���� �Ŀ� ���� �޼ҵ忡 ���� ������ �������ν�, ���� �޼ҵ忡���� ������ �߻��� ���� ������ �� �ִ�.
     * throw�� ������ exception�� throw�� �� ����ϴ� keyword��.
     * ������ ���ܸ� �߻���Ų��. 
     * ���α׷����� �Ǵܿ� ���� ó���̴�.
     * 
     * 
     * throws, throw - ���ܸ� ������ ��
     * throws�� ���� �޼ҵ忡�� ���� �޼ҵ�� ���ܸ� ������.
     * throws�� �޼ҵ峪 �����ڸ� ������ �� �߻��ϴ� exception�� ������ �� ����ϴ� keyword�̴�.
     * ��, ���ܸ� �ڽ��� ó������ �ʰ�, �ڽ��� ȣ���ϴ� �޼ҵ忡�� å���� �����ϴ� ���̴�.
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
             * throw new�� ���� checkPair �Լ��� ������ �� ����� ���� ó�� �Լ��� �ٸ��� �ϱ� ���Ѱ���
             * ���� Exception ���� ó���� �ٸ��� �����ϱ� ���Ѱ�
             */
            keywords.put(paramName, value);
            /*
             * keyword�ȿ� paramName�� �Էµǰų� ���� value ���� ���� �־��شٴ� ����
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
        	 * ������ ������
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
		         * CommandLineArgument[] ���������� �迭�� ����ȯ ��Ų�ٰ� �����ϸ��.
		         */
		        for (int k=0; k<extArg.length && !found; k++)
		        if (args[i].compareToIgnoreCase(extArg[k].getName()) == 0) {
		            
		        	// ���࿡ ���� Ű���峪 �Ķ���Ͱ��� �ʼ��� �ƴ϶�� ������ ������ ��, üũ�Ѵ�.
		            if (remainingValueCount > 0) {
			            throw new Exception("Ű������ �Ķ���� ���� �����ϴ�. '" + lastArg.getName() + "'.");
		            }
		            // �� Ű������ �����Ѵ�.
		            lastArg = extArg[k];
		            lastArg.setEnabled(true);
		            found = true;
		            args[i] = null;
		            remainingValueCount = lastArg.getNumOfArguments();
		        }
		    }
		    // ���� ���� ���ڿ��� '-' �뽬�� ����� Ű������ Ȯ���ض�
		    if (args[i] != null && args[i].startsWith("-")) {
		        continue;
		    }
		    // ���� �� �̻� ����ߴ� param ���� �������� ���� ��� Ȯ���ϼ� (�����, param�� parameter�� ���� ����)
		    if (lastArg != null && remainingValueCount == 0) {
		        continue;
		    }   
		    // ����� Ű���忡�� ���� �͵��� ����� �� üũ��
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
		     * ����� matcher�� ���� ǥ���� Ŭ���� �ε� �� �ȿ� find, start, end ���� ���� �޼ҵ尡 ������.
		     * ���ϰ��̶� ��ġ�ϴ� �ؽ�Ʈ�鿡 ���ؼ� find start end �޼ҵ尡 ���� �ϴ°���
		     */
	    }
	    // ���� ������ Ű���峪 �Ķ���� ���� �ƴϰų� �������� ���� ������ ������ ��� üũ�Ѵٰ� 
	    if (lastArg != null && remainingValueCount > 0) {
            throw new Exception("Ű������ �Ķ���� ���� �����ϴ� '" + lastArg.getName() + "'.");
        }	    
	    // �˷����� ���� Ű������� ������ ��� üũ�غ���
	    for (int i=0; i<args.length; i++) {
	        if (args[i] != null) {
                throw new Exception("�˷����� ���� �ɼ�: " + args[i]);	            
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
         * �Էµ� keyword �� �����ͼ� keywords�� �����ؼ� ���ڴٰ�
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
        sb.append("��ɾ� ���:\r\n");
        sb.append("java -jar GGON.jar {-h|-help} {-newsession session_file_path} {options}\r\n");
        sb.append("�ɼ�:\r\n\r\n");
        /*
         * append ���ڿ��� �����ִ°� concat�̶��� �ٸ� sb�� ���� ����־ ����� ��� append�� ��� ���ڿ����� �� ��µ�
         */

        for (int i=0; i<commandList.size(); i++) {
	        CommandLineArgument[] extArg = (CommandLineArgument[]) commandList.get(i);
	        for (int j=0; j<extArg.length; j++) {
	            sb.append(extArg[j].getHelpMessage() + "\r\n");
	        }
        }
        
        return sb.toString();
        /*
         * ���ڿ��� ��ȯ���شٰ� �����ض� toString
         * (String) ����ȯ ���ڿ��� �ȵǴ� �κ��� ���� �����ϴµ�, toString�Լ��� ������ �Ŀ��� ���� ���̾ �� ��ȯ���شٰ� ��������
         */
    }
    
    public boolean isEnabled(String keyword) {
        
        Object obj = keywords.get(keyword);
        if (obj != null && obj instanceof String) {
        	/*
        	 * instanceof�� ������ Ÿ�� Ȯ���ϴ°���
        	 * ��, ���⼭ keyword ���� ���� obj��� ���� null ���� �ƴϸ鼭, obj�� ���� String���� ����ȯ�� ������ ��츦 ����
        	 */
            return true;
        }
        return false;
    }
    
}
