package org.parosproxy.paros.core.spider;

import java.util.Vector;

class Input extends Tag {
	private String	value	= null;
	private String	type	= null;

	public static final String	TEXT		= "TEXT",
								HIDDEN		= "HIDDEN",
								SUBMIT		= "SUBMIT",
								CHECKBOX	= "CHECKBOX",
								RADIO		= "RADIO",
								RESET		= "RESET",
								PASSWORD	= "PASSWORD";

	private static final ParserTag parser = new ParserTag(Tag.INPUT, ParserTag.CLOSING_TAG_NO);
	private static final ParserAttr	parserAttrType = new ParserAttr(Attr.TYPE);

	static Input[] getInputs (String doc) {
		Vector inputs = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Input input = new Input();
			input.buildAttrs(attrs);
			input.build(content);
			inputs.addElement(input);
		}

		Input[]	result	= new Input[inputs.size()];
		result = (Input[]) inputs.toArray(result);
		return result;
	}

	protected void buildAttrs(String attrs) {
		super.buildAttrs(attrs);
		value	= parserAttrValue.getValue(attrs);
		type 	= parserAttrType.getValue(attrs);
	}

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
