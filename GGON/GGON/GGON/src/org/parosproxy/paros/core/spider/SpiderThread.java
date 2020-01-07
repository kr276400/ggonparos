package org.parosproxy.paros.core.spider;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpStatusCode;

public class SpiderThread extends Thread {

    private static final String[] NEGLECT_SUFFIXES = {"gif", "jpg", "bmp", "mp3", "arj", "doc", "swf", "pdf", "mpg", "wmv", "zip", "exe", "cab", "iso", "avi"};
    
    private Spider parent = null;
    private boolean stop = false;
    private List queue = null;
    private boolean completed = false;
    private Collector collector = null;
    private boolean emptyQueue = false;

    boolean isEmptyQueue() {
        return emptyQueue;
    }
    
    SpiderThread(Spider parent) {
        this.parent = parent;
        queue = parent.getQueue();
        collector = new Collector(this);
        this.setDaemon(true);
        this.setPriority(Thread.NORM_PRIORITY-2);
    }

    boolean isStop() {
        return stop;
    }

    void setStop(boolean stop) {
        this.stop = stop;
    }
    
    public void run() {

        QueueItem item = null;

        while (!isStop() && !parent.isAllThreadEmptyQueue()) {

            try {
                synchronized(queue) {
                
                    do {
                        item = null;
                        if (queue.isEmpty()) {
                            try {
                                setEmptyQueue(true);
                                Thread.sleep(500);
                            } catch (InterruptedException ie) {}

                        } else {
                            item = (QueueItem) queue.remove(0);
                            setEmptyQueue(false);
                        }                        
                    } while (!stop && item != null && parent.isInVisitedLink(item.getMessage()));
                }
                
                if (item != null) {
                    parent.SpiderProgress(item);
                    crawl(item.getMessage(), item.getDepth());
                    item.getHistoryReference().delete();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e1) {}
                } else if (!stop) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {}
                    
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (queue.isEmpty()) {
            completed = true;
        }
        
        parent.checkIfAllThreadCompleted();
        
    }

    private void readMsgResponse(HttpMessage msg) throws HttpException, IOException, HttpMalformedHeaderException {
        
        msg.getRequestHeader().setHeader(HttpHeader.IF_MODIFIED_SINCE, null);
        msg.getRequestHeader().setHeader(HttpHeader.IF_NONE_MATCH, null);
        msg.getRequestHeader().setContentLength(msg.getRequestBody().length());
        parent.getHttpSender().sendAndReceive(msg);
        msg.getResponseHeader().setHeader(HttpHeader.TRANSFER_ENCODING, null);            
    }

    private void crawl(HttpMessage msg, int depth) {
        
        Html html = null;

        
        try {
            
            if (isNeglectCrawl(msg)) {
                parent.readURI(msg);
                return;
            }
            
            readMsgResponse(msg);

            if (!HttpStatusCode.isSuccess(msg.getResponseHeader().getStatusCode())) {
                return;
            }

            if (msg.getResponseHeader().getContentLength() > 200000) {
                msg.setResponseHeader(new HttpResponseHeader());
                msg.getResponseBody().setBody("");
            }

            parent.readURI(msg);

            if (isNeglectResponse(msg.getResponseHeader())) {
                return;
            }
            
            html = new Html(msg.getRequestHeader().getURI(), msg.getResponseBody().toString());
            collector.collect(html, depth);
            
        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            msg.setResponseHeader(new HttpResponseHeader());
            msg.getResponseBody().setBody("");
            parent.addVisitedLink(msg);
        }
        
    }
    
    private URI buildURI(URI base, String link) throws URIException {

        URI uri = null;

        uri = new URI(base, link, true);
        return uri;
    }
    
    
    void foundURI(HttpMessage msg, String referer, int currentDepth) throws URIException {
        msg.getRequestHeader().setHeader(HttpHeader.REFERER, referer);
        parent.foundURI(msg, currentDepth+1);        
    }

    private boolean isNeglectCrawl(HttpMessage msg) {
        boolean result = false;

        
        URI uri = msg.getRequestHeader().getURI();

        try {
            
            if (parent.getSpiderParam().isSkipURL(uri)) {
                return true;
            }
            
            if (uri.getPath() != null) {
                String path = uri.getPath().toLowerCase();
                for (int i=0; i<NEGLECT_SUFFIXES.length; i++) {
                    String suffix = "." + NEGLECT_SUFFIXES[i];
                    if (path.endsWith(suffix)) {
                        return true;
                    }
                }
            }
            
        } catch (Exception e) {}
        
        return result;
        
    }

    private boolean isNeglectResponse(HttpResponseHeader resHeader) {
        
        if (!HttpStatusCode.isSuccess(resHeader.getStatusCode())) {
            return true;
        }
        
        if (resHeader.isImage()) {
            return true;
        }

        if (resHeader.isText()) {
            return false;
        }

        if (resHeader.getContentLength() > 200000) {
            return true;
        }        
        
        return false;
    }

    public boolean isCompleted() {
        return completed;
    }

    private void setEmptyQueue(boolean emptyQueue) {
        this.emptyQueue = emptyQueue;
    }
    
    Spider getParent() {
        return parent;
    }
}
