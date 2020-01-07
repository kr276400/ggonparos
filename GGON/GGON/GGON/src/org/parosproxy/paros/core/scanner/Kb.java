package org.parosproxy.paros.core.scanner;

import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public class Kb {

    private TreeMap mapKb = new TreeMap();
    private TreeMap mapURI = new TreeMap();

	public synchronized Vector getList(String key) {
	    return getList(mapKb, key);
	    
	}

	public synchronized void add(String key, Object value) {
	    add(mapKb, key, value);
	}
	
	public synchronized Object get(String key) {
	    Vector v = getList(key);
	    if (v == null || v.size() == 0) {
	        return null;
	        
	    } else {
	        return v.get(0);
	    }
	}

	public String getString(String key) {
	    Object obj = get(key);
	    if (obj != null && obj instanceof String) {
		    return (String) obj;
	        
		}
	    return null;
	    
	}
		
	public boolean getBoolean(String key) {
	    Object obj = get(key);
	    if (obj != null && obj instanceof Boolean) {
	        return ((Boolean) obj).booleanValue();
	        
	    }
	    return false;
	    
	}


	public synchronized void add(URI uri, String key, Object value) {
	    uri = (URI) uri.clone();
	    String uriKey = uri.toString();
	    TreeMap map = null;
	    try {
            uri.setQuery(null);
        } catch (URIException e) {
            return;
        }
	    Object obj = mapURI.get(uriKey);
	    if (obj == null) {
	        map = new TreeMap();
	        mapURI.put(uriKey, map);
	    } else {
	        map = (TreeMap) obj;
	    }
	    
	    add(map, key, value);
	}
	
	public synchronized Vector getList(URI uri, String key) {
	    uri = (URI) uri.clone();
	    String uriKey = uri.toString();
	    TreeMap map = null;
	    try {
            uri.setQuery(null);
        } catch (URIException e) {
            return null;
        }

        Object obj = mapURI.get(uriKey);
	    if (obj != null && obj instanceof TreeMap) {
	        map = (TreeMap) obj;
	    } else {
	        return null;
	    }
	    return getList(map, key);
	}
	
	public synchronized Object get(URI uri, String key) {
	    Vector v = getList(uri, key);
	    if (v == null || v.size() == 0) {
	        return null;
	        
	    } else {
	        return v.get(0);
	    }
	    
	}
	public String getString(URI uri, String key) {
	    Object obj = get(uri, key);
	    if (obj != null && obj instanceof String) {
	        return (String) obj;
	    }
	    return null;
	}
	
	public boolean getBoolean(URI uri, String key) {
	    Object obj = get(uri, key);
	    if (obj != null && obj instanceof Boolean) {
	        return ((Boolean) obj).booleanValue();
	    }
	    return false;
	    
	}

	private void add(TreeMap map, String key, Object value) {
	    Vector v = getList(map, key);
	    if (v == null) {
	        v = new Vector();
	        synchronized (map) {
	            map.put(key, v);
	        }
	    }
	    if (!v.contains(value)) {
	        v.add(value);
	    }

	}

	private Vector getList(TreeMap map, String key) {
	    Vector result = null;
	    Object obj = null;
	    synchronized (map) {
	        obj = map.get(key);
	    }
	    
	    if (obj != null && obj instanceof Vector) {
	        return (Vector) obj;
	    }
	    return null;
	    
	}
	
}
