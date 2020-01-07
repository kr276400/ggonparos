package org.parosproxy.paros.core.spider;

import java.util.Vector;

public class Option extends Tag {

	private static final ParserTag parser = new ParserTag(Tag.OPTION, ParserTag.CLOSING_TAG_OPTIONAL);

	private String value = "";

	static Option[] getOptions (String doc) {
		Vector options = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Option option = new Option();
			option.buildAttrs(attrs);
			option.build(content);
			options.addElement(option);
		}

		Option[]	result	= new Option[options.size()];
		result = (Option[]) options.toArray(result);
		return result;
	}

	protected void buildAttrs(String attrs) {

		value = parserAttrValue.getValue(attrs);

	}

	protected void build(String content) {
		if (value.trim().length() == 0) {
			value = content;
		}
	}

    public String getValue() {
        return value;
    }
}