package org.parosproxy.paros.extension.filter;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.Constant;

public class FilterFactory {

    private static Log log = LogFactory.getLog(FilterFactory.class);

    private static TreeMap mapAllFilter = new TreeMap();
    private Vector listAllFilter = new Vector();

    public List getAllFilter() {
        return listAllFilter;
    }
}
