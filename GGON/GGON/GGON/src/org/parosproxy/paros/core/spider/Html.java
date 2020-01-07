package org.parosproxy.paros.core.spider;


import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public class Html {

    private String doc = "";
    private URI uri = null;

    public Html(URI uri, String doc) {
		this.uri = uri;
		this.doc = doc;
        
        Base[] bases = getBases();
        if (bases.length > 0) {
            if (bases[0].getHref().length() >0) {
                try {
                    uri = new URI(bases[0].getHref(), false);
                } catch (Exception e) {
                }
            }
        }
        
    }

    public URI getURI() {
		return uri;
    }

    public String toString() {
		return doc;
    }

	public Form[] getForms() {
		return Form.getForms(doc);
	}

	public A[] getAs() {
		return A.getAs(doc);
	}
	
	public Frame[] getFrames() {
		return Frame.getFrames(doc);
	}

	public Img[] getImgs() {
		return Img.getImgs(doc);
	}
	
	public Hyperlink[] getHyperlinks() {
	    return Hyperlink.getHyperlinks(doc);
	}
	
	public Meta[] getMetas() {
		return Meta.getMetas(doc);
	}
    
    private Base[] getBases() {
        return Base.getBases(doc);
    }

}

