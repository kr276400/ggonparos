package org.parosproxy.paros.core.spider;

import java.util.Vector;

public class Img extends org.parosproxy.paros.core.spider.Tag {

	// �±׿� �ִ� IMG �κ� �Ľ��ϴ� �κ�
	private static final ParserTag	parser	= new ParserTag("IMG", ParserTag.CLOSING_TAG_YES);

	// �±׾ȿ� SRC �κ� �Ľ��ϴ� �κ�
	private static final ParserAttr	parserAttrSrc = new ParserAttr(Attr.SRC);

	private String	src	= "";

	public static Img[] getImgs(String doc) {

		Vector imgs = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Img i = new Img();
			i.buildAttrs(attrs);
			i.build(content);
			imgs.add(i);
		}

		Img[]	result	= new Img[imgs.size()];
		result = (Img[]) imgs.toArray(result);
		return result;
	}


	protected void buildAttrs(String attrs) {
		super.buildAttrs(attrs);

		src	= parserAttrSrc.getValue(attrs);
	}

    public String getSrc() {
        return src;
    }
}