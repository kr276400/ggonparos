package org.parosproxy.paros.core.spider;

import java.util.Vector;

import org.apache.commons.httpclient.URI;

public class Base extends org.parosproxy.paros.core.spider.Tag {

	// A �±� �Ľ� �ϴ� �κ�
	private static final ParserTag	parser	= new ParserTag("BASE", ParserTag.CLOSING_TAG_NO);

	// �±׿� �ִ� HREF �Ӽ� �κ� �Ľ��ϴ� �κ�
	private static final ParserAttr	parserAttrHref = new ParserAttr(Attr.HREF);

	private String	href	= "";

	public static Base[] getBases(String doc) {

		Vector bases = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Base base = new Base();
			base.buildAttrs(attrs);
			base.build(content);
			bases.addElement(base);
		}

		Base[]	result	= new Base[bases.size()];
		result = (Base[]) bases.toArray(result);
		return result;
	}


	protected void buildAttrs(String attrs) {
		super.buildAttrs(attrs);

		String tmp = parserAttrHref.getValue(attrs);
        try {
            URI uri = new URI(tmp, false);
            if (uri.isAbsoluteURI()) {
                href = tmp;
            }
        } catch (Exception e) {
        }
        
	}
 
    public String getHref() {
        return href;
    }
}