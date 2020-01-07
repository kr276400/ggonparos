package org.parosproxy.paros.network;

public final class HttpStatusCode {
	
	// 정보  제공하는 코드
	public static final int	CONTINUE						= 100;
	public static final int	SWITCHING_Protocols				= 101;

	// 성공 코드들
	public static final int	OK								= 200;
	public static final int	CREATED							= 201;
	public static final int	ACCEPTED						= 202;
	public static final int	NON_AUTHORITATIVE_INFORMATION	= 203;
	public static final int	NO_CONTENT						= 204;
	public static final int	RESET_CONTENT					= 205;
	public static final int	PARTIAL_CONTENT					= 206;

	// 재 방향(방향 수정 하는거 있잖어) 하는 코드 부분
	public static final int	MULTIPLE_CHOICES				= 300;
	public static final int	MOVED_PERMANENTLY				= 301;
	public static final int	FOUND							= 302;
	public static final int	SEE_OTHER						= 303;
	public static final int	NOT_MODIFIED					= 304;
	public static final int	USE_PROXY						= 305;
	public static final int	TEMPORARY_REDIRECT				= 306;

	// 클라이언트 에러 부분
	public static final int	BAD_REQUEST						= 400;
	public static final int	UNAUTHORIZED					= 401;
	public static final int	PAYMENT_REQUIRED				= 402;
	public static final int	FORBIDDEN						= 403;
	public static final int	NOT_FOUND						= 404;
	public static final int	METHOD_NOT_ALLOWED				= 405;
	public static final int	NOT_ACCEPTABLE					= 406;
	public static final int	PROXY_AUTHENTICATION_REQUIRED	= 407;
	public static final int	REQUEST_TIME_OUT				= 408;
	public static final int	CONFLICT						= 409;
	public static final int	GONE							= 410;
	public static final int	LENGTH_REQUIRED					= 411;
	public static final int	PRECONDITION_FAILED				= 412;
	public static final int	REQUEST_ENTITY_TOO_LARGE		= 413;
	public static final int	REQUEST_URI_TOO_LARGE			= 414;
	public static final int	UNSUPPORTED_MEDIA_TYPE			= 415;
	public static final int	REQUESTED_RANGE_NOT_SATISFIABLE	= 416;
	public static final int	EXPECTATION_FAILED				= 417;

	// 서버 에러 부분
	public static final int	INTERNAL_SERVER_ERROR			= 500;
	public static final int	NOT_IMPLEMENTED					= 501;
	public static final int	BAD_GATEWAY						= 502;
	public static final int	SERVICE_UNAVAILABLE				= 503;
	public static final int	GATEWAY_TIEMOUT					= 504;
	public static final int	HTTP_VERSION_NOT_SUPPORTED		= 505;

	/*
	 * 코드 상태가 만약에, 정보 제공 하는 부분이면 체크혀라 (100 <= status < 200) <- 이부분
	 * statusCode 얻어온거를 이용하는데,
	 * 만약 정보 제공 관련 코드면 true를 리턴할거임
	 */
	public static boolean isInformatinal(int statusCode) {
		if (statusCode >= 100 && statusCode < 200)
			return true;
		else
			return false;
	}

	/*
	 * 만약 얻어온 statusCode가 HTTP status 코드이고, 이 http status 코드가 성공적이라면, 체크하는 부분
	 * http status code -> (200 <= status < 300)
	 */
	public static boolean isSuccess(int statusCode) {
		if (statusCode >= 200 && statusCode < 300)
			return true;
		else
			return false;
	}

	/*
	 * http status 코드가 다시 수정할 부분이 생기는, redirection 재방향 코드라면, 체크
	 * redirection -> (300 <= status < 400)
	 */
	public static boolean isRedirection(int statusCode) {
		if (statusCode >= 300 && statusCode < 400) {
			return true;
        }
        
		return false;
	}

	/*
	 * http 코드가 클라이언트 에러라면 체크 
	 * 참고, 클라 에러 코드는 400 <= status < 500 임.
	 */
	public static boolean isClientError(int statusCode) {
		if (statusCode >= 400 && statusCode < 500)
			return true;
		else
			return false;
	}

	/*
	 * http 코드가 서버에러라면 체크
	 * 참고로 위에까지 다 statuscode는 얻어온 값임.
	 * 서버 에러 -> (500 <= status)
	 */
	public static boolean isServerError(int statusCode) {
		if (statusCode >= 500)
			return true;
		else
			return false;
	}

}
