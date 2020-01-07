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
                         * 이미지 처리 시에는 버퍼의 내용을 비워준는 거고, 
                         * 자바에서 대부분 flush같은 경우는 출력하는 함수인데,
                         * 네트워크에서는 버터의 내용을 보내주는거라고 생각하면 됨.
                         * 즉, 현재 버퍼에 저장되어있는 내용을 클라이언트에 전송하고 버퍼를 비운다라고 보면 될듯함.
                         */
	                    continuousCount++;
	                    if (continuousCount % 5 == 4) Thread.yield();
	                    // 모든 CPU 시간을 차지하는 같은 쓰레드들을 피하는거여

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
