package org.parosproxy.paros.model;

import java.io.File;

public interface SessionListener {

	//세션이 열려있다면, 메소드 다시 부르는 부분이여 둘다
    public void sessionOpened(File file, Exception e);

    public void sessionSaved(Exception e);
    
}
