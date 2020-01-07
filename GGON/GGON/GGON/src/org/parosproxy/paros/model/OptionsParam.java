
package org.parosproxy.paros.model;

import java.io.File;
import java.util.Vector;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.core.proxy.ProxyParam;
import org.parosproxy.paros.extension.option.OptionParamCertificate;
import org.parosproxy.paros.extension.option.OptionParamView;
import org.parosproxy.paros.network.ConnectionParam;

public class OptionsParam extends AbstractParam {
		
	private ProxyParam proxyParam = new ProxyParam();
	private ConnectionParam connectionParam = new ConnectionParam();
	private OptionParamView viewParam = new OptionParamView();
	private OptionParamCertificate certificateParam = new OptionParamCertificate();

	private Vector paramSetList = new Vector();
	private XMLConfiguration config = null;
	private boolean gui = true;
	private File userDirectory = null;

    public ConnectionParam getConnectionParam() {
        return connectionParam;
    }
 
	public ProxyParam getProxyParam() {
		return proxyParam;
	}

	public void setProxyParam(ProxyParam proxyParam) {
		this.proxyParam = proxyParam;
	}
	
	public OptionsParam() {
		
	}

    public void setConnectionParam(ConnectionParam connectionParam) {
        this.connectionParam = connectionParam;
    }

    public void setViewParam(OptionParamView viewParam) {
        this.viewParam = viewParam;
    }

    public OptionParamView getViewParam() {
        return viewParam;
    }

    public void setCertificateParam(OptionParamCertificate certificateParam) {
        this.certificateParam = certificateParam;
    }
 
    public OptionParamCertificate getCertificateParam() {
        return certificateParam;
    }

    public void addParamSet(AbstractParam paramSet) {
        paramSetList.add(paramSet);
	    paramSet.load(getConfig());
    }

    public AbstractParam getParamSet(Class className) {
       
        AbstractParam result = null;
        for (int i=0; i<paramSetList.size(); i++) {
            Object obj = paramSetList.get(i);
            if (obj.getClass().equals(className)) {
                try {
                    result = (AbstractParam) obj;
                    break;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    
    public FileConfiguration getConfig() {
	    if (config == null) {
	        try {
                config = new XMLConfiguration(Constant.getInstance().FILE_CONFIG);

            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
	    }
        return config;
    }
 
    protected void parse() {
		getConnectionParam().load(getConfig());
	    getProxyParam().load(getConfig());
		getCertificateParam().load(getConfig());
		getViewParam().load(getConfig());

    }
    
    public boolean isGUI() {
        return gui;
    }
    
    public void setGUI(boolean gui) {
        this.gui = gui;
    }

    public File getUserDirectory() {
        return userDirectory;
    }

    public void setUserDirectory(File currentDirectory) {
        this.userDirectory = currentDirectory;
    }
    
}