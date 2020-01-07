package org.parosproxy.paros.core.spider;

import java.util.Vector;

class TextArea extends Tag {
	private String	value	= null;
	private String	type	= null;
    
	private static final ParserTag parser = new ParserTag(Tag.TEXTAREA, ParserTag.CLOSING_TAG_YES);
	
	static TextArea[] getTextAreas(String doc) {
		Vector textareas  = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			TextArea textarea = new TextArea();
            textarea.buildAttrs(attrs);
            textarea.build(content);
			textareas.addElement(textarea);
		}

		TextArea[]	result	= new TextArea[textareas.size()];
		result = (TextArea[]) textareas.toArray(result);
		return result;
	}
    
    protected void build(String content) {
        value = content;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
}
