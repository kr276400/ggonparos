package org.parosproxy.paros.core.spider;


class Tag {

	public static final String FORM 	= "FORM";
	public static final String INPUT	= "INPUT";
	public static final String SELECT 	= "SELECT";
	public static final String OPTION 	= "OPTION";
	public static final String A		= "A";
    public static final String TEXTAREA = "TEXTAREA";

	protected static final ParserAttr parserAttrName	= new ParserAttr(Attr.NAME);
	protected static final ParserAttr parserAttrValue	= new ParserAttr(Attr.VALUE);

	private String	name = "";

	Tag() {
	}

	Tag(String name) {
		this.name	= name;
	}

	protected void build(String content) {
	}

	protected void buildAttrs(String attrs) {
		name = parserAttrName.getValue(attrs);
	}

	public String getName() {
	    return name;	    
	}
}