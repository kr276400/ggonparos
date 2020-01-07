package org.parosproxy.paros.core.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class StreamForwarder extends Thread {

    private Socket inSocket = null;
    private OutputStream out = null;
    private InputStream in = null;
    private boolean isStop = false;
    
    public StreamForwarder(Socket inSocket, InputStream in, OutputStream out) {
        this.inSocket = inSocket;
        this.out = out;
        this.in = in;
        this.setDaemon(true);

        
    }
    
    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
    
    public void run() {
        byte[] buffer = new byte[2048];
        int len = -1;
        int continuousCount = 0;
        
        try {
            inSocket.setSoTimeout(150);
            
            do {
                try {
                    len = in.read(buffer);
                    
                    if (len > 0) {
                        out.write(buffer, 0, len);
                        out.flush();
                        /*
                         * �̹��� ó�� �ÿ��� ������ ������ ����ش� �Ű�, 
                         * �ڹٿ��� ��κ� flush���� ���� ����ϴ� �Լ��ε�,
                         * ��Ʈ��ũ������ ������ ������ �����ִ°Ŷ�� �����ϸ� ��.
                         * ��, ���� ���ۿ� ����Ǿ��ִ� ������ Ŭ���̾�Ʈ�� �����ϰ� ���۸� ���ٶ�� ���� �ɵ���.
                         */
	                    continuousCount++;
	                    if (continuousCount % 5 == 4) Thread.yield();
	                    // ��� CPU �ð��� �����ϴ� ���� ��������� ���ϴ°ſ�

                    }
                } catch (SocketTimeoutException ex) {
                    len = 0;
		            continuousCount = 0;
                }
                
            } while (!isStop && len >= 0);
                
        } catch (IOException e) {
            
        }
    }
}
