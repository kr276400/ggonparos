package org.parosproxy.paros.extension.option;

import java.io.File;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.network.SSLConnector;

public class OptionParamCertificate extends AbstractParam {

    private static final String ROOT = "certificate";

    private static final String USE_CLIENT_CERT = "certificate.use";
    private static final String CLIENT_CERT_LOCATION = "certificate.clientCertLocation";

    private int useClientCert = 0;
    private String clientCertLocation = "";

    public OptionParamCertificate() {
    }

    protected void parse() {
        

        int tempUseClientCert = 0;
        String tempClientCertLocation = "";
        
        
        tempUseClientCert = getConfig().getInt(USE_CLIENT_CERT, 0);
        tempClientCertLocation = getConfig().getString(CLIENT_CERT_LOCATION, "");

        setUseClientCert(false);
        setClientCertLocation("");
        
    }

    public String getClientCertLocation() {
        return clientCertLocation;
    }
    
    public void setClientCertLocation(String clientCertLocation) {
        if (clientCertLocation != null && !clientCertLocation.equals("")) {
            File file = new File(clientCertLocation);
            if (!file.exists()) {
                setUseClientCert(false);
                return;
            }
        } else {
            setUseClientCert(false);   
        }
        this.clientCertLocation = clientCertLocation;   
        getConfig().setProperty(CLIENT_CERT_LOCATION, clientCertLocation);
    }

    public boolean isUseClientCert() {
        return (useClientCert != 0);
    }

    public void setUseClientCert(boolean isUse) {
        if (isUse) {
            useClientCert = 1;
            getConfig().setProperty(USE_CLIENT_CERT, Integer.toString(useClientCert));
            return;
        }
        useClientCert = 0;
        getConfig().setProperty(USE_CLIENT_CERT, Integer.toString(useClientCert));
        
    }
    
    public void setCertificate(char[] passphrase) throws Exception {

        ProtocolSocketFactory sslFactory = Protocol.getProtocol("https").getSocketFactory();
        SSLConnector ssl = null;
        if (sslFactory instanceof SSLConnector) {
            ssl = (SSLConnector) sslFactory;
            ssl.setClientCert(new File(getClientCertLocation()), passphrase);
        }
    }

    public void setEnableCertificate(boolean enabled) {
        ProtocolSocketFactory sslFactory = Protocol.getProtocol("https").getSocketFactory();
        SSLConnector ssl = null;
        if (sslFactory instanceof SSLConnector) {
            ssl = (SSLConnector) sslFactory;
            ssl.setEnableClientCert(enabled);
        }
        
    }
    
}
