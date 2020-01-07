package org.parosproxy.paros.core.scanner;

import java.util.Vector;

import org.parosproxy.paros.network.HttpMessage;

abstract public class AbstractAppParamPlugin extends AbstractAppPlugin {


    private Vector listVariant = new Vector();
    
    private Variant variant = null;
    private NameValuePair originalPair = null;
    
    public void scan() {
        listVariant.add(new VariantURLQuery());
        listVariant.add(new VariantFormQuery());
        
        for (int i=0; i<listVariant.size() && !isStop(); i++) {
            HttpMessage msg = getNewMsg();
            variant = (Variant) listVariant.get(i);
            variant.setMessage(msg);
            scanVariant();
        }
        
    }

    private void scanVariant() {
        for (int i=0; i<variant.getParamList().size() && !isStop(); i++) {
            originalPair = (NameValuePair) variant.getParamList().get(i);
            HttpMessage msg = getNewMsg();
            scan(msg, originalPair.getName(), originalPair.getValue());
        }
    }
    
    abstract public void scan(HttpMessage msg, String param, String value);

    protected String setParameter(HttpMessage msg, String param, String value) {
        return variant.setParameter(msg, originalPair, param, value);
    }
}
