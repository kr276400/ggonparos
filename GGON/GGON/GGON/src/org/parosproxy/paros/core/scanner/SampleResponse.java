package org.parosproxy.paros.core.scanner;

import org.parosproxy.paros.network.HttpMessage;

class SampleResponse {

    static final int	ERROR_PAGE_RFC							= 0;
	static final int	ERROR_PAGE_NON_RFC						= 1;
	static final int	ERROR_PAGE_REDIRECT						= 2;
	static final int	ERROR_PAGE_STATIC						= 3;
	static final int	ERROR_PAGE_DYNAMIC_BUT_DETERMINISTIC	= 4;
	static final int	ERROR_PAGE_UNDETERMINISTIC				= 5;

	private HttpMessage message = null;
	private int	errorPageType = ERROR_PAGE_RFC;
	
	SampleResponse(HttpMessage message, int errorPageType) {
	    this.message = message;
	    this.errorPageType = errorPageType;
	    
	}

    public HttpMessage getMessage() {
        return message;
    }

    public void setMessage(HttpMessage message) {
        this.message = message;
    }

    public int getErrorPageType() {
        return errorPageType;
    }

    public void setErrorPageType(int errorPageType) {
        this.errorPageType = errorPageType;
    }
}
