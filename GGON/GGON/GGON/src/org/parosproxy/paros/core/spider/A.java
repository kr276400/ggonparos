package org.parosproxy.paros.core.spider;

import java.util.Vector;

public class A extends org.parosproxy.paros.core.spider.Tag {

	// 'A' �±� �Ľ��ϴ� �κ�
	private static final ParserTag	parser	= new ParserTag("A", ParserTag.CLOSING_TAG_YES);

	// �±׿� 'HREF' �Ӽ� �κ� �Ľ��ϴ� �κ�
	private static final ParserAttr	parserAttrHref = new ParserAttr(Attr.HREF);

	private String	href	= "";

	public static A[] getAs(String doc) {

		Vector as = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			A a = new A();
			a.buildAttrs(attrs);
			a.build(content);
			as.addElement(a);
		}

		A[]	result	= new A[as.size()];
		result = (A[]) as.toArray(result);
		return result;
	}


	protected void buildAttrs(String attrs) {
		super.buildAttrs(attrs);

		href	= parserAttrHref.getValue(attrs);
	}

    public String getHref() {
        return href;
    }
}