package org.parosproxy.paros.model;

import java.io.File;

public interface SessionListener {

	//������ �����ִٸ�, �޼ҵ� �ٽ� �θ��� �κ��̿� �Ѵ�
    public void sessionOpened(File file, Exception e);

    public void sessionSaved(Exception e);
    
}
