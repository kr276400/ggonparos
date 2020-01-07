package org.parosproxy.paros.network;

public final class HttpStatusCode {
	
	// ����  �����ϴ� �ڵ�
	public static final int	CONTINUE						= 100;
	public static final int	SWITCHING_Protocols				= 101;

	// ���� �ڵ��
	public static final int	OK								= 200;
	public static final int	CREATED							= 201;
	public static final int	ACCEPTED						= 202;
	public static final int	NON_AUTHORITATIVE_INFORMATION	= 203;
	public static final int	NO_CONTENT						= 204;
	public static final int	RESET_CONTENT					= 205;
	public static final int	PARTIAL_CONTENT					= 206;

	// �� ����(���� ���� �ϴ°� ���ݾ�) �ϴ� �ڵ� �κ�
	public static final int	MULTIPLE_CHOICES				= 300;
	public static final int	MOVED_PERMANENTLY				= 301;
	public static final int	FOUND							= 302;
	public static final int	SEE_OTHER						= 303;
	public static final int	NOT_MODIFIED					= 304;
	public static final int	USE_PROXY						= 305;
	public static final int	TEMPORARY_REDIRECT				= 306;

	// Ŭ���̾�Ʈ ���� �κ�
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

	// ���� ���� �κ�
	public static final int	INTERNAL_SERVER_ERROR			= 500;
	public static final int	NOT_IMPLEMENTED					= 501;
	public static final int	BAD_GATEWAY						= 502;
	public static final int	SERVICE_UNAVAILABLE				= 503;
	public static final int	GATEWAY_TIEMOUT					= 504;
	public static final int	HTTP_VERSION_NOT_SUPPORTED		= 505;

	/*
	 * �ڵ� ���°� ���࿡, ���� ���� �ϴ� �κ��̸� üũ���� (100 <= status < 200) <- �̺κ�
	 * statusCode ���°Ÿ� �̿��ϴµ�,
	 * ���� ���� ���� ���� �ڵ�� true�� �����Ұ���
	 */
	public static boolean isInformatinal(int statusCode) {
		if (statusCode >= 100 && statusCode < 200)
			return true;
		else
			return false;
	}

	/*
	 * ���� ���� statusCode�� HTTP status �ڵ��̰�, �� http status �ڵ尡 �������̶��, üũ�ϴ� �κ�
	 * http status code -> (200 <= status < 300)
	 */
	public static boolean isSuccess(int statusCode) {
		if (statusCode >= 200 && statusCode < 300)
			return true;
		else
			return false;
	}

	/*
	 * http status �ڵ尡 �ٽ� ������ �κ��� �����, redirection ����� �ڵ���, üũ
	 * redirection -> (300 <= status < 400)
	 */
	public static boolean isRedirection(int statusCode) {
		if (statusCode >= 300 && statusCode < 400) {
			return true;
        }
        
		return false;
	}

	/*
	 * http �ڵ尡 Ŭ���̾�Ʈ ������� üũ 
	 * ����, Ŭ�� ���� �ڵ�� 400 <= status < 500 ��.
	 */
	public static boolean isClientError(int statusCode) {
		if (statusCode >= 400 && statusCode < 500)
			return true;
		else
			return false;
	}

	/*
	 * http �ڵ尡 ����������� üũ
	 * ����� �������� �� statuscode�� ���� ����.
	 * ���� ���� -> (500 <= status)
	 */
	public static boolean isServerError(int statusCode) {
		if (statusCode >= 500)
			return true;
		else
			return false;
	}

}
