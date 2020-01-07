package org.parosproxy.paros.network;

public class HostAuthentication {

    private String	hostName = "";
    private int		port		= 80;
    private String	userName = "";
    private String	password = "";
    private String	realm	= "";
    
    public HostAuthentication() {
        
    }

    public HostAuthentication(String hostName, int port, String userName, String password, String realm) {
        super();
        setHostName(hostName);
        setPort(port);
        setUserName(userName);
        setPassword(password);
        setRealm(realm);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
