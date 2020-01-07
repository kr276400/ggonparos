package org.parosproxy.paros.core.spider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Vector;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpBody;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

public class Collector {

    private SpiderThread parent = null;
    
    Collector(SpiderThread parent) {
        this.parent = parent;
    }

    private HttpMessage buildMsg(URI base, String link) throws URIException, HttpMalformedHeaderException {

        URI uri = null;        
        uri = new URI(base, link, true);
        HttpMessage msg = new HttpMessage(new HttpRequestHeader(HttpRequestHeader.GET, uri, HttpHeader.HTTP11));
        return msg;
    }

    private boolean isDuplicateInSameHtml(Vector list, HttpMessage msg) {
        
        if (list.contains(msg)) {
            return true;
        } else {
            list.add(msg);            
        }
        return false;
    }
    
	void collect(Html html, int currentDepth) {
	    Vector previousFoundList = new Vector();

	    URI uri = null;
	    A[] as = html.getAs();
	    Frame[] frames = html.getFrames();
	    Hyperlink[] hlinks = html.getHyperlinks();
	    Meta[] metas = html.getMetas();
	    
	    HttpMessage msg = null;
	    
	    for (int i=0; i<as.length; i++) {
	        String href = as[i].getHref().replaceAll("&amp;","&");
	        try {
	            msg = buildMsg(html.getURI(), href);
	            
	            if (!isDuplicateInSameHtml(previousFoundList, msg)) {
		            parent.foundURI(msg, html.getURI().toString(), currentDepth);

	            }
	        } catch (Exception e) {

	        }
	        
	    }
	    
	    for (int i=0; i<frames.length; i++) {
	        String src = frames[i].getSrc();
	        try {
	            msg = buildMsg(html.getURI(), src);
	            if (!isDuplicateInSameHtml(previousFoundList, msg)) {
	                parent.foundURI(msg, html.getURI().toString(), currentDepth);
	            }
	        } catch (Exception e) {

	        }
	    }

	    Vector formQueryList = getFormsQuery(html);
	    for (int i=0; i<formQueryList.size(); i++) {
	        msg = (HttpMessage) formQueryList.get(i);
	        try {
	            parent.foundURI(msg, html.getURI().toString(), currentDepth);
	        } catch (URIException e) {
	        }
	    }
	    
	    for (int i=0; i<hlinks.length; i++) {
	        String link = hlinks[i].getLink().replaceAll("&amp;","&");
	        try {
	            msg = buildMsg(html.getURI(), link);
	            if (!isDuplicateInSameHtml(previousFoundList, msg)) {
	                parent.foundURI(msg, html.getURI().toString(), currentDepth);
	            }
	        } catch (Exception e) {
	        }
	    }
	    
	    for (int i=0; i<metas.length; i++) {
	        String url = metas[i].getURL();
	        try {
	            msg = buildMsg(html.getURI(), url);
	            if (!isDuplicateInSameHtml(previousFoundList, msg)) {
	                parent.foundURI(msg, html.getURI().toString(), currentDepth);
	            }
	        } catch (Exception e) {

	        }
	    }

	}
	
	public Vector getFormsQuery(Html html) {
		Vector qryList = new Vector();
		Form[] forms = html.getForms();
		for (int i=0; i<forms.length; i++) {
			Form form = forms[i];
			Vector oneForm = getFormQuery(form, html.getURI());
			qryList.addAll(oneForm);
		}
		return qryList;
	}

	private Vector getFormQuery(Form form, URI baseURI) {
		Vector qryStrList	= new Vector();
		Vector qryList		= new Vector();
		String queryString = "";
		HttpRequestHeader reqHeader = null;
		HttpBody reqBody = null;
		
		if (form.getAction()==null) {
		    return qryList;
		}
		
		int combinationCount = 1;
		for (int i=0; i<form.getSelect().length; i++) {
			combinationCount *= (form.getSelect()[i].getOption().length > 2) ? 2: form.getSelect()[i].getOption().length;
		}

		if (combinationCount > 512) {
			return qryList;
		}

		try {
			for (int i=0; i<form.getInput().length; i++) {
				Input input = form.getInput()[i];
                if (input.getName()== null || input.getName().length() == 0) {
                    continue;
                }
				if (input.getType() != null) {
					if (
						input.getType().equalsIgnoreCase(Input.PASSWORD) ||
						input.getType().equalsIgnoreCase(Input.CHECKBOX) ||
						input.getType().equalsIgnoreCase(Input.RESET)) {
						continue;
					}
				}
				String value = input.getValue();
				if (input.getType().equalsIgnoreCase(Input.TEXT) && value.equals("")) {
				    value = "1";
				}
                queryString = buildPostQueryString(queryString, input.getName(), value);
			}

            for (int i=0; i<form.getTextArea().length; i++) {
                TextArea textarea = form.getTextArea()[i];
                if (textarea.getName()== null || textarea.getName().length() == 0) {
                    continue;
                }
                String value = textarea.getValue();
                queryString = buildPostQueryString(queryString, textarea.getName(), value);
            }
            
			qryStrList.addElement(queryString);
			for (int i=0; i<form.getSelect().length;i++) {
				Select select = form.getSelect()[i];
                if (select.getName()== null || select.getName().length() == 0) {
                    continue;
                }
				qryStrList = addSelectField(qryStrList, select);
			}

			for (int i=0; i<qryStrList.size(); i++) {
			    HttpMessage msg = null;
				URI uri = null;
				String qryStr = (String) qryStrList.elementAt(i);
				if (form.getMethod().equalsIgnoreCase(Form.GET)) {
				    String action = (form.getAction().indexOf(QUESTION) <0) ? form.getAction()+QUESTION+qryStr : form.getAction()+AMPERSAND+qryStr;				    
					uri = new URI(baseURI, action, true);
					reqHeader = new HttpRequestHeader(form.getMethod().trim().toUpperCase(), uri, HttpHeader.HTTP11);
					msg = new HttpMessage(reqHeader);
				} else if (form.getMethod().equalsIgnoreCase(Form.POST)) {
                    
                    if (!parent.getParent().getSpiderParam().isPostForm()) {
                        continue;
                    }
				    uri = new URI(baseURI, form.getAction(), true);
				    reqHeader = new HttpRequestHeader(form.getMethod().trim().toUpperCase(), uri, HttpHeader.HTTP11);
				    reqBody = new HttpBody(qryStr);
				    reqHeader.setContentLength(reqBody.length());
				    msg = new HttpMessage(reqHeader, reqBody);
				} else {
				    continue;
				}
				msg.getRequestHeader().setContentLength(msg.getRequestBody().length());
				qryList.add(msg);
			}
		} catch (Exception e) {
		}

		return qryList;

	}

	private Vector addSelectField(Vector qry, Select select) {
		Vector newQryList = new Vector();
		String queryString = null;
		if (select.getOption() == null) {
			return newQryList;
		}

		for (int i=0; i<select.getOption().length && i<2; i++) {
            
            if (i==0 && select.getOption().length > 1) {
                continue;
            }
            
			String value = select.getOption()[i].getValue();
            try {
				if (qry.isEmpty()) {
					queryString = buildPostQueryString("", select.getName(), value);
					newQryList.addElement(queryString);
				} else {
					for (int j=0; j<qry.size(); j++) {
						queryString = (String) qry.elementAt(j);
						queryString = buildPostQueryString(queryString, select.getName(), value);
						newQryList.addElement(queryString);
					}
				}
			} catch (Exception e) {
			}
		}
		return newQryList;
	}

	private static final String EQUAL		= "=";
	private static final String AMPERSAND	= "&";
	private static final String QUESTION	= "?";
	private String buildPostQueryString(String oldQuery, String newField, String newValue) {
		StringBuffer result = new StringBuffer(oldQuery);
		if (oldQuery.length() > 0) {
			result.append(AMPERSAND);
		}
		result.append(newField);
		result.append(EQUAL);
		try {
            result.append(URLEncoder.encode(newValue, "UTF8"));
        } catch (UnsupportedEncodingException e) {
        }
		return result.toString();
	}
	
}