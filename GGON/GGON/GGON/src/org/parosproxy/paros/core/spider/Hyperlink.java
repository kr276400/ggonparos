package org.parosproxy.paros.core.spider;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hyperlink extends org.parosproxy.paros.core.spider.Tag {

    private static final Pattern patternURL = Pattern.compile("\\W(http://[^\\x00-\\x1f\"'\\s<>#]+)");
    
	private String	href	= "";

	public static Hyperlink[] getHyperlinks(String doc) {

		Vector links = new Vector();
		Matcher matcher = patternURL.matcher(doc);
		while (matcher.find()) {
		    try {
		        String s = matcher.group(1);
		        Hyperlink hlink = new Hyperlink();
		        hlink.buildAttrs(s);
		        links.addElement(hlink);
		    } catch (Exception e) {}
		}

		Hyperlink[]	result	= new Hyperlink[links.size()];
		result = (Hyperlink[]) links.toArray(result);
		return result;
	}


	protected void buildAttrs(String href) {

		this.href	= href;
	}

    public String getLink() {
        return href;
    }
}