package org.parosproxy.paros.extension.trap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.parosproxy.paros.common.AbstractParam;

public class TrapParam extends AbstractParam {

	private static final String TRAP = "trap";

	private static final String INCLUSIVE_FILTER = "trap.inclusiveFilter";
	private static final String EXCLUSIVE_FILTER = "trap.exclusiveFilter";	
	
	private String inclusiveFilter = "";
	private String exclusiveFilter = "";
	
	private Pattern patternInclusiveFilter = null;
	private Pattern patternExclusiveFilter = null;

    public TrapParam() {

    }

    protected void parse() {
        

		setInclusiveFilter(getConfig().getString(INCLUSIVE_FILTER, ""));
		setExclusiveFilter(getConfig().getString(EXCLUSIVE_FILTER, ""));

    }
	
	private void parseInclusiveFilter(String inclusiveFilter) {
		patternInclusiveFilter = null;

		if (inclusiveFilter == null || inclusiveFilter.equals("")) {
			return;
		}
		
		inclusiveFilter = inclusiveFilter.replaceAll("\\.", "\\\\.");
		inclusiveFilter = inclusiveFilter.replaceAll("\\*",".*?").replaceAll("(;+$)|(^;+)", "");
		inclusiveFilter = "(" + inclusiveFilter.replaceAll(";+", "|") + ")$";
		patternInclusiveFilter = Pattern.compile(inclusiveFilter, Pattern.CASE_INSENSITIVE);
	}

	private void parseExclusiveFilter(String exclusiveFilter) {
		patternExclusiveFilter = null;

		if (exclusiveFilter == null || exclusiveFilter.equals("")) {
			return;
		}
		
		exclusiveFilter = exclusiveFilter.replaceAll("\\.", "\\\\.");
		exclusiveFilter = exclusiveFilter.replaceAll("\\*",".*?").replaceAll("(;+$)|(^;+)", "");
		exclusiveFilter = "(" + exclusiveFilter.replaceAll(";+", "|") + ")$";

		patternExclusiveFilter = Pattern.compile(exclusiveFilter, Pattern.CASE_INSENSITIVE);
	}

    public String getExclusiveFilter() {
        return exclusiveFilter;
    }

    public void setExclusiveFilter(String exclusiveFilter) {
        this.exclusiveFilter = exclusiveFilter;
		parseExclusiveFilter(this.exclusiveFilter);
    	getConfig().setProperty(EXCLUSIVE_FILTER, this.exclusiveFilter);

    }

    public String getInclusiveFilter() {
        return inclusiveFilter;
    }

    public void setInclusiveFilter(String inclusiveFilter) {
        this.inclusiveFilter = inclusiveFilter;
		parseInclusiveFilter(this.inclusiveFilter);
    	getConfig().setProperty(INCLUSIVE_FILTER, this.inclusiveFilter);

    }
    
    public boolean isInclude(String s) {
        boolean result = true;
        Matcher matcher = null;
        if (patternInclusiveFilter != null) {
            try {
                
                matcher = patternInclusiveFilter.matcher(s);
                if (matcher.find()) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {}
        }
        return result;
    }
    
    public boolean isExclude(String s) {
        boolean result = false;
        Matcher matcher = null;
        if (patternExclusiveFilter != null) {
            try {
                matcher = patternExclusiveFilter.matcher(s);
                if (matcher.find()) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

}
