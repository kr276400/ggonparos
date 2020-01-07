package org.parosproxy.paros.core.spider;

import java.util.Vector;

public class Frame extends org.parosproxy.paros.core.spider.Tag {

	// A 태그 파싱
	private static final ParserTag	parser	= new ParserTag("FRAME", ParserTag.CLOSING_TAG_YES);

	// HREF 속성을 태그에서 파싱
	private static final ParserAttr	parserAttrSrc = new ParserAttr(Attr.SRC);

	private String	src	= "";

	public static Frame[] getFrames(String doc) {

		Vector frames = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Frame f = new Frame();
			f.buildAttrs(attrs);
			f.build(content);
			frames.add(f);
		}

		Frame[]	result	= new Frame[frames.size()];
		result = (Frame[]) frames.toArray(result);
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