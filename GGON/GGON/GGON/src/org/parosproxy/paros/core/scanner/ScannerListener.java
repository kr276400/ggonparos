package org.parosproxy.paros.core.scanner;

public interface ScannerListener {

    
    public void scannerComplete();
    
    public void hostNewScan(String hostAndPort, HostProcess hostThread);
    
    public void hostProgress(String hostAndPort, String msg, int percentage);
    
    public void hostComplete(String hostAndPort);

    public void alertFound(Alert alert);
    
}
