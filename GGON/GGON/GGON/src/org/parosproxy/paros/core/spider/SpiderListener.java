package org.parosproxy.paros.core.spider;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.network.HttpMessage;


public interface SpiderListener {

    public void spiderProgress(URI uri, int percentageComplete, int numberCrawled, int numberToCrawl );
    public void foundURI(HttpMessage msg, boolean skip);
    public void readURI(HttpMessage msg);
    public void spiderComplete();
    
}
