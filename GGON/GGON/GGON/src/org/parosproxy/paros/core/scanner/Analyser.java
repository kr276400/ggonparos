package org.parosproxy.paros.core.scanner;

import java.io.IOException;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.network.HttpStatusCode;

public class Analyser {
	
	private static final String p_REMOVE_HEADER = "(?m)(?i)(?s)<HEAD>.*?</HEAD>";
	private static final Pattern patternNotFound = Pattern.compile("(\\bnot\\b(found|exist))|(\\b404\\berror\\b)|(\\berror\\b404\\b)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);

    private static Random	staticRandomGenerator = 	new Random();
	private static final String[] staticSuffixList = { ".cfm", ".jsp", ".php", ".asp", ".aspx", ".dll", ".exe", ".pl"};

	private HttpSender	httpSender = null;
	private TreeMap		mapVisited = new TreeMap();
	private boolean		isStop = false;
	
	public Analyser() {
        
    }
    
    public Analyser(HttpSender httpSender) {
        this.httpSender = httpSender;
    }
	
    public boolean isStop() {
        return isStop;
    }
    
    public void stop() {
        isStop = true;
    }
    
    public void start(SiteNode node) {
        inOrderAnalyse(node);
    }
    
	private void addAnalysedHost(URI uri, HttpMessage msg, int errorIndicator) {
        mapVisited.put(uri.toString(), new SampleResponse(msg, errorIndicator));
	}

	private void analyse(SiteNode node) throws Exception {

		// ���࿡ ���� �м��� �Ǿ�������, �����ϰ�,  ȣ��Ʈ �κ����� �ű�ٴ°�
		if (node.getHistoryReference() == null) {
		    return;
		}
		
		HttpMessage baseMsg = (HttpMessage) node.getHistoryReference().getHttpMessage();
		URI baseUri = (URI) baseMsg.getRequestHeader().getURI().clone();

		baseUri.setQuery(null);

		if (mapVisited.get(baseUri.toString()) != null) {
			return;
		}

		String path = getRandomPathSuffix(node, baseUri);
		HttpMessage msg = baseMsg.cloneRequest();
		
		URI uri = (URI) baseUri.clone();
		uri.setPath(path);
		msg.getRequestHeader().setURI(uri);
        
		sendAndReceive(msg);
		
		
		if (msg.getResponseHeader().getStatusCode() == HttpStatusCode.NOT_FOUND) {
			addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_RFC);
			return;
		}

		if (HttpStatusCode.isRedirection(msg.getResponseHeader().getStatusCode())) {
			addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_REDIRECT);
			return;
		}
		
		if (msg.getResponseHeader().getStatusCode() != HttpStatusCode.OK) {
			addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_NON_RFC);
			return;
		}
	
		HttpMessage msg2 = baseMsg.cloneRequest();
		URI uri2 = msg2.getRequestHeader().getURI();
		String path2 = getRandomPathSuffix(node, uri2);
		uri2 = (URI) baseUri.clone();
		uri2.setPath(path2);
		msg2.getRequestHeader().setURI(uri2);
		sendAndReceive(msg2);

		// HTML HEAD ����� �κ��ε�, ���� ��ȯ�� �Ǵ� Ȯ���� �ӽ� �Ⱓ�� ������ ����ٴ� �� ������.. ��Ȯ����..
		String resBody1 = msg.getResponseBody().toString().replaceAll(p_REMOVE_HEADER, "");
		String resBody2 = msg2.getResponseBody().toString().replaceAll(p_REMOVE_HEADER, "");

		// �������� STATIC�̸� üũ�ϰ� ������ ����϶�µ�
		if (resBody1.equals(resBody2)) {
		    msg.getResponseBody().setBody(resBody1);
			addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_STATIC);
			return;
		}

		// �������� �����̾ üũ�ϴµ� �Է°��� ���� ��°��� �����ɰž�
		resBody1 = resBody1.replaceAll(getPathRegex(uri), "").replaceAll("\\s[012]\\d:[0-5]\\d:[0-5]\\d\\s","");
		resBody2 = resBody2.replaceAll(getPathRegex(uri2), "").replaceAll("\\s[012]\\d:[0-5]\\d:[0-5]\\d\\s","");
		if (resBody1.equals(resBody2)) {
		    msg.getResponseBody().setBody(resBody1);
			addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_DYNAMIC_BUT_DETERMINISTIC);
			return;
		}

		addAnalysedHost(baseUri, msg, SampleResponse.ERROR_PAGE_UNDETERMINISTIC);
	
	}

	private String getChildSuffix(SiteNode node, boolean performRecursiveCheck) {

		String resultSuffix = "";
		String suffix = null;
		SiteNode child = null;
        HistoryReference ref = null;
		HttpMessage msg = null;
		try {

			for (int i=0; i<staticSuffixList.length; i++) {
				suffix = staticSuffixList[i];
				for (int j=0; j<node.getChildCount(); j++) {
					child = (SiteNode) node.getChildAt(j);
                    ref = child.getHistoryReference();
					try {
					    msg = ref.getHttpMessage();
                        if (msg.getRequestHeader().getURI().getPath().endsWith(suffix)) {
					        return suffix;
					    }
					} catch (Exception e) {
					}
				}
			}
			
			if (performRecursiveCheck) {
				for (int j=0; j<node.getChildCount(); j++) {
					resultSuffix = getChildSuffix((SiteNode) node.getChildAt(j), performRecursiveCheck);
					if (!resultSuffix.equals("")) {
						return resultSuffix;
					}
				}
			}
														
		} catch (Exception e) {
		}
		
		return resultSuffix;
	}
	
	private String getPathRegex(URI uri) throws URIException {
	    URI newUri = (URI) uri.clone();
	    String query = newUri.getQuery();
	    StringBuffer sb = new StringBuffer(100);

	    newUri.setQuery(null);
	    
		sb.append(newUri.toString().replaceAll("\\.", "\\."));
		if (query != null) {
			String queryPattern = "(\\?" + query + ")?";
			sb.append(queryPattern);
		}
		
		return sb.toString();
	}

	private String getRandomPathSuffix(SiteNode node, URI uri) throws URIException {
		String resultSuffix = getChildSuffix(node, true);
		
		String path = "";
		path = (uri.getPath() == null) ? "" : uri.getPath();
		path = path + (path.endsWith("/") ? "" : "/") + Long.toString(Math.abs(staticRandomGenerator.nextLong()));
		path = path + resultSuffix;

		return path;

	}
	
	// ��� �м��ϴ� �κ��̿�, �ʿ��ϸ�
	private void inOrderAnalyse(SiteNode node) {
	    
		SiteNode tmp = null;
		
		if (isStop) {
		    return;
		}
		
		if (node == null) {
			return;
		}

		// ��Ʈ�� �ƴϰ�, ������ �ƴϸ� �м��϶�� ��. ������  ������ �����ϸ� �̹� �˰� �ִ� �׷��� �������� ������ �м� ����
		try {
			if (!node.isRoot()) {
				if (!node.isLeaf() || node.isLeaf() && ((SiteNode) node.getParent()).isRoot()) {
					analyse(node);
				}
			}
		} catch (Exception e) {

		}
				
		for (int i=0; i<node.getChildCount() && !isStop(); i++) {
			try {
				tmp = (SiteNode) node.getChildAt(i);
				inOrderAnalyse(tmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
    public boolean isFileExist(HttpMessage msg) {
        
        if (msg.getResponseHeader().isEmpty()) {
            return false;
        }
        
		// RFC
        if (msg.getResponseHeader().getStatusCode() == HttpStatusCode.NOT_FOUND) {
            return false;
        }

        URI uri = (URI) msg.getRequestHeader().getURI().clone();
        try {
            uri.setQuery(null);
            String path = uri.getPath();
            path = path.replaceAll("/[^/]*$","");
            uri.setPath(path);
        } catch (Exception e1) {}
    
        String sUri = uri.toString();        
        
        // �����ϸ� ����� ���� ��ġ�� ���� ������ �������µ�, ���� ���ϸ� ȣ��Ʈ�� ����Ұž�
		SampleResponse sample = (SampleResponse) mapVisited.get(sUri);
		if (sample == null) {
		    try {
                uri.setPath(null);
            } catch (URIException e2) {}
		    String sHostOnly = uri.toString();
			sample = (SampleResponse) mapVisited.get(sHostOnly);
		}
		
		// ����� �м���ٸ� üũ�ϴ� �κ��̾�
		if (sample == null) {
			if (msg.getResponseHeader().getStatusCode() == HttpStatusCode.OK) {
				// Ȯ���ؾߵ� �м��� ����� ������, ������ �����Ѵٰ� �����ϰ� �����ع����°�
				return true;
			} else {
				return false;
			}
		}
		
		// ������ �ٽ� �������ų� �ߴ��� Ȯ���ϰ�, ���࿡ ���� ��ġ�� �ٽ� ��������, ������ ����
		if (HttpStatusCode.isRedirection(msg.getResponseHeader().getStatusCode())) {
			try {
				if (sample.getMessage().getResponseHeader().getStatusCode() == msg.getResponseHeader().getStatusCode()) {
					String location = msg.getResponseHeader().getHeader(HttpHeader.LOCATION);
					if (location != null && location.equals(sample.getMessage().getResponseHeader().getHeader(HttpHeader.LOCATION))) {
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		
		// ���� �ڵ� �ƴ�
		if (msg.getResponseHeader().getStatusCode() != HttpStatusCode.OK) {
			return false;
		}

		// OK���� ���丸 ���⿡ ����ΰ�, �˾Ƴ��� �� ����, ã�� ���� �������� ������ ���ɼ��� ������ üũ�غ��� �κ��̿�
		Matcher matcher = patternNotFound.matcher(msg.getResponseBody().toString());
		if (matcher.find()) {
			return false;
		}
		
		// ����ƽ ����
		String body = msg.getResponseBody().toString().replaceAll(p_REMOVE_HEADER, "");
		if (sample.getErrorPageType() == SampleResponse.ERROR_PAGE_STATIC) {
			if (sample.getMessage().getResponseBody().toString().equals(body)) {
				return false;
			}
			return true;
		}

		uri = msg.getRequestHeader().getURI();
		try {
			if (sample.getErrorPageType() == SampleResponse.ERROR_PAGE_DYNAMIC_BUT_DETERMINISTIC) {
				body = msg.getResponseBody().toString().replaceAll(getPathRegex(uri), "").replaceAll("\\s[012]\\d:[0-5]\\d:[0-5]\\d\\s","");
				if (sample.getMessage().getResponseBody().equals(body)) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}



        return true;
    }
	
	private void sendAndReceive(HttpMessage msg) throws HttpException, IOException {
	    httpSender.sendAndReceive(msg, true);
	}
	

	
}

