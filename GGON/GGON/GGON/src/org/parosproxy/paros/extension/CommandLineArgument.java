package org.parosproxy.paros.extension;

import java.util.Vector;
import java.util.regex.Pattern;

public class CommandLineArgument {

    private String name = "";
    private int numOfArguments = 0;
    private Vector arg = new Vector();
    private boolean enabled = false;
    private Pattern pattern = null;
    private String errorMessage = "";
    private String helpMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public Pattern getPattern() {
        return pattern;
    }
    public CommandLineArgument(String name, int numOfArguments) {
        this.name = name;
        this.numOfArguments = numOfArguments;
    }

    public CommandLineArgument(String name, int numOfArguments, String pattern, String errorMessage, String helpMessage) {
        this(name, numOfArguments);
        if (pattern != null && pattern.length() > 0) {
            this.pattern = Pattern.compile(pattern);            
        }
        if (errorMessage != null) {
            this.errorMessage = errorMessage;
        }
        
        if (helpMessage != null) {
            this.helpMessage = helpMessage;
        }
    }

    public String getName() {
        return name;
    }
    
    public int getNumOfArguments() {
        return numOfArguments;
    }
    
    public Vector getArguments() {
        return arg;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHelpMessage() {
        return helpMessage;
    }
}
