package org.parosproxy.paros.core.scanner;

import org.parosproxy.paros.common.AbstractParam;

public class ScannerParam extends AbstractParam {

	private static final String SCANNER = "scanner";

	private static final String HOST_PER_SCAN = "scanner.hostPerScan";
	private static final String THREAD_PER_HOST = "scanner.threadPerHost";
		
	private int hostPerScan = 2;
	private int threadPerHost = 1;

    public ScannerParam() {
    }

    protected void parse(){
        
		try {
			setThreadPerHost(getConfig().getInt(THREAD_PER_HOST, 1));
		} catch (Exception e) {}
		try {
			setHostPerScan(getConfig().getInt(HOST_PER_SCAN, 2));
		} catch (Exception e) {}

    }

    public int getThreadPerHost() {
        return threadPerHost;
    }
    
    public void setThreadPerHost(int threadPerHost) {
        this.threadPerHost = threadPerHost;
        getConfig().setProperty(THREAD_PER_HOST, Integer.toString(this.threadPerHost));

    }

    public int getHostPerScan() {
        return hostPerScan;
    }

    public void setHostPerScan(int hostPerScan) {
        this.hostPerScan = hostPerScan;
		getConfig().setProperty(HOST_PER_SCAN, Integer.toString(this.hostPerScan));

    }
	
}
