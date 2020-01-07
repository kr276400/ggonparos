package org.parosproxy.paros.core.scanner;

import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpMessage;

abstract public class AbstractDefaultFilePlugin extends AbstractHostPlugin {
    
	private static final Pattern patternItems = Pattern.compile(",");
	private static final String[]	SPECIAL_TAG_LIST = {"@cgibin"};
	
	private static final String[]	TAG_REPLACE_LIST = {
		"cgi-bin,cgi-local,htbin,cgi,cgis,cgi-win,bin,scripts"
	};
    

    private URI baseURI = null;    
    private Vector listURI = new Vector();
		
	protected void addTest(String directories, String files) {
		String[] 	dirList = null,
					fileList = null;
		String		dir = "",
					file = "";

		directories = directories.trim();
		files = files.trim();
		for (int i=0; i<SPECIAL_TAG_LIST.length; i++) {
			directories = directories.replaceAll(SPECIAL_TAG_LIST[i], TAG_REPLACE_LIST[i]);
		}

		try {
			dirList = patternItems.split(directories);
			fileList = patternItems.split(files);
			for (int i=0; i<dirList.length; i++) {
				dir = dirList[i].trim();
				if (!dir.startsWith("/")) {
					dir = "/" + dir;
				}

				for (int j=0; j<fileList.length; j++) {
					file = fileList[j].trim();
					try {
					    URI uri = createURI(baseURI, dir, file);
					    listURI.add(uri);
					} catch (URIException eu) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private URI createURI(URI base, String dir, String file) throws URIException {
		if (!dir.startsWith("/")) {
			dir = "/" + dir;
		}
		
		if (!file.startsWith("/") && !dir.endsWith("/")) {
			file = "/" + file;
		}
				
		String path = dir + file;
		URI uri = new URI(base, path, true);
		return uri;
	}

    public URI getBaseURI() {
        return baseURI;
    }

    public Vector getListURI() {
        return listURI;
    }

    public void init() {
        baseURI = getBaseMsg().getRequestHeader().getURI();
	}

    public void scan() {
        for (int i=0; i<getListURI().size() && !isStop(); i++) {
            URI uri = (URI) getListURI().get(i);
            HttpMessage msg = getNewMsg();
            try {
                msg.getRequestHeader().setURI(uri);
                msg.getRequestBody().setLength(0);
                sendAndReceive(msg);
                if (isFileExist(msg)) {
                    bingo(Alert.RISK_MEDIUM, Alert.SUSPICIOUS, uri.toString(), "", "", msg);
                }
            } catch (Exception e) {
            }
        }
    }
}
