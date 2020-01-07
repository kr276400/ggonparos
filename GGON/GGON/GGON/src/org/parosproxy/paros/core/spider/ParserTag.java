package org.parosproxy.paros.core.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserTag {

	public static final int	CLOSING_TAG_YES			= 0,
							CLOSING_TAG_NO			= 1,
							CLOSING_TAG_OPTIONAL	= 2;

	private Pattern pattern = null;
	private Matcher matcher = null;
	private String	doc		= null,
			attrs	= null,
			content = null;
	private int closingTagType = CLOSING_TAG_YES;

	ParserTag(String tag, int closingTag) {
		String uTag = tag.toUpperCase();
		String tagPattern = null;
		this.closingTagType = closingTag;
		switch (closingTag) {
        
   		    case CLOSING_TAG_NO:
				tagPattern = "<" + uTag + "\\s+?([^>]+?)\\s*?>";
				break;
			case CLOSING_TAG_OPTIONAL:
				tagPattern = "<" + uTag + "\\s+?([^>]+?)\\s*?>([^<]*?)<";
				break;
			case CLOSING_TAG_YES:
			default:
				tagPattern = "<" + uTag + "\\s+?([^>]+?)\\s*?>(.*?)</" + uTag + "\\s*?>";
				break;

		}
		pattern = Pattern.compile(tagPattern, Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	}

	void parse(String doc) {
		this.doc = doc;
		matcher = pattern.matcher(doc);
	}

	boolean nextTag() {
		boolean isFound = false;
		attrs = "";
		content = "";
		if (matcher.find()) {
			attrs = matcher.group(1);
			try {
			    if (closingTagType == CLOSING_TAG_OPTIONAL || closingTagType == CLOSING_TAG_YES) {
			        content = matcher.group(2);
			    }
			} catch (Exception e) {
			}
			isFound = true;
		}
		return isFound;
	}

	String getAttrs() {
		return attrs;
	}

	String getContent() {
		return content;
	}

}
