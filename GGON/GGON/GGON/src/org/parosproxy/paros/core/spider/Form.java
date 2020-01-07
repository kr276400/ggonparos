package org.parosproxy.paros.core.spider;

import java.util.Vector;


public class Form extends Tag {

	public static final String	POST	= "POST",
								GET		= "GET";

	private static final ParserTag	parser	= new ParserTag(Tag.FORM, ParserTag.CLOSING_TAG_YES);

	private static final ParserAttr	parserAttrAction = new ParserAttr(Attr.ACTION);
	private static final ParserAttr	parserAttrMethod = new ParserAttr(Attr.METHOD);

	private String	action	= "",
					method	= "";
	
	private Select[]	select	= null;
	private Input[]		input	= null;
    private TextArea[]  textArea = null;

	public static Form[] getForms(String doc) {

		Vector forms = new Vector();
		parser.parse(doc);
		while (parser.nextTag()) {
			String content	= parser.getContent();
			String attrs	= parser.getAttrs();
			Form form = new Form();
			form.buildAttrs(attrs);
			form.build(content);
			forms.addElement(form);
		}

		Form[]	result	= new Form[forms.size()];
		result = (Form[]) forms.toArray(result);
		return result;
	}

	protected void build(String content) {
		select	= Select.getSelects(content);
		input	= Input.getInputs(content);
        textArea = TextArea.getTextAreas(content);
	}

	protected void buildAttrs(String attrs) {
		super.buildAttrs(attrs);
		action	= parserAttrAction.getValue(attrs);
		method	= parserAttrMethod.getValue(attrs);
	}

    public String getAction() {
        return action;
    }

    public Input[] getInput() {
        return input;
    }
 
    public String getMethod() {
        return method;
    }

    public Select[] getSelect() {
        return select;
    }
    
    public TextArea[] getTextArea() {
        return textArea;
    }
}











