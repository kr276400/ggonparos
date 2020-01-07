package org.parosproxy.paros.core.scanner;

import java.util.Vector;
import java.util.regex.Pattern;

import org.parosproxy.paros.network.HttpMessage;

abstract public class VariantAbstractQuery implements Variant {
    
	private static Pattern staticPatternParam = Pattern.compile("&", Pattern.CASE_INSENSITIVE);	

    private Vector listParam = new Vector();
    
    public VariantAbstractQuery() {
        
    }

    abstract protected void buildMessage(HttpMessage msg, String query);

    protected String getEncodedValue(HttpMessage msg, String value) {
       return value; 
    }
    
    protected void parse(String params) {
        
        if (params == null || params.equals("")) {
            return;
        }
        
		String[] keyValue = staticPatternParam.split(params);
		String key = null;
		String value = null;
		int pos = 0;
		for (int i=0; i<keyValue.length; i++) {
			key = null;
			value = null;
			pos = keyValue[i].indexOf('=');
			try {
				if (pos > 0) {
					key = keyValue[i].substring(0,pos);
					value = keyValue[i].substring(pos+1);
				} else {
				    key = keyValue[i];
				    value = null;
				    
				}
				listParam.add(new NameValuePair(key, value, i));

			} catch (Exception e) {
			}
		}
		
    }
    
    public Vector getParamList() {
        return listParam;
    }

    public String setParameter(HttpMessage msg, NameValuePair originalPair, String name, String value) {

        StringBuffer sb = new StringBuffer();
        NameValuePair pair = null;
        boolean isAppended = false;
        for (int i=0; i<getParamList().size(); i++) {
            pair = (NameValuePair) getParamList().get(i);
            if (i == originalPair.getPosition()) {
                String encodedValue = getEncodedValue(msg, value);
                isAppended = paramAppend(sb, name, encodedValue);

            } else {
                isAppended = paramAppend(sb, pair.getName(), pair.getValue());

            }

            if (isAppended && i<getParamList().size()-1) {
                sb.append('&');
            }
        }

        String query = sb.toString();
        buildMessage(msg, query);
        return query;
    }

    private boolean paramAppend(StringBuffer sb, String name, String value) {
        String param = name;
        boolean isEdited = false;
        if (name != null) {
            sb.append(name);
            isEdited = true;
        }
        if (value != null) {
            sb.append('=');
            sb.append(value);
            isEdited = true;
        }
        return isEdited;
    }

}
