package org.parosproxy.paros.core.scanner;

class NameValuePair {
    private String name = null;
    private String value = null;
    private int position = 0;
    
    NameValuePair() {
        
    }

    NameValuePair(String name, String value, int position) {
        super();
        this.name = name;
        this.value = value;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
