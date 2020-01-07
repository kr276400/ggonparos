package org.parosproxy.paros.core.spider;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Meta extends org.parosproxy.paros.core.spider.Tag {

	// META 태그 파싱 부분
	private static final ParserTag	parser	= new ParserTag("META", ParserTag.CLOSING_TAG_OPTIONAL);

	// CONTENT 속성 태그에서 파싱하는 부분
	private static final ParserAttr	parserAttrContent = new ParserAttr(Attr.CONTENT);

	private static final Pattern urlPattern = Pattern.compile("url\\s*=\\s*([^;]+)", Pattern.CASE_INSENSITIVE);
	private String	url = "";

	public static Meta[] getMetas(String doc) {

		Vector metas = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Meta m = new Meta();
			m.buildAttrs(attrs);
			m.build(content);
			metas.addElement(m);
		}

		Meta[]	result	= new Meta[metas.size()];
		result = (Meta[]) metas.toArray(result);
		return result;
	}


	protected void buildAttrs(String attrs) {
	    super.buildAttrs(attrs);
	    
	    try {
	        String s = parserAttrContent.getValue(attrs);
	        Matcher matcher = urlPattern.matcher(s);
	        if (matcher.find()) {
	            url = matcher.group(1);
	        }
	    } catch (Exception e) {}
	}

    public String getURL() {
        return url;
    }
}