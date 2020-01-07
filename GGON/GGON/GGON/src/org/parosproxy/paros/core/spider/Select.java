package org.parosproxy.paros.core.spider;

import java.util.Vector;

public class Select extends Tag {

	private static final ParserTag parser = new ParserTag(Tag.SELECT, ParserTag.CLOSING_TAG_YES);

	private Option[] option = null;

	static Select[] getSelects(String doc) {

		Vector selects = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Select select = new Select();
			select.build(content);
			select.buildAttrs(attrs);
			selects.addElement(select);
		}

		Select[]	result	= new Select[selects.size()];
		result = (Select[]) selects.toArray(result);
		return result;
	}

	protected void build(String content) {
		option = Option.getOptions(content);
	}
	
	public Option[] getOption() {
	    return option;
	}

}
