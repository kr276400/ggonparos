package org.parosproxy.paros;

import java.awt.Frame;
import java.util.Locale;
import javax.swing.UIManager;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.network.SSLConnector;
import org.parosproxy.paros.view.View;

public class GGON {
    

    static {
	    ProtocolSocketFactory sslFactory = null;
	    try {
	        Protocol protocol = Protocol.getProtocol("https");
	        sslFactory = protocol.getSocketFactory();
	    } catch (Exception e) {}
	    if (sslFactory == null || !(sslFactory instanceof SSLConnector)) {
	        Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) new SSLConnector(), 443));
	    }
    }
    
    private static Log log = null;
    
    
	public static void main(String[] args) throws Exception {
	    GGON gon = new GGON();
	    gon.init(args);
        Constant.getInstance();
        String msg = Constant.PROGRAM_NAME + " " + "시작했습니다.";
        log = LogFactory.getLog(GGON.class);
	    log.info(msg);
        
	    try {
	    	gon.run();
	    } catch (Exception e) {
	        log.fatal(e.getStackTrace());
	        throw e;
	    }
		
	}

    private CommandLine cmdLine = null;

	private void init(String[] args) {

	    HttpSender.setUserAgent(Constant.USER_AGENT);
	    try {
	        cmdLine = new CommandLine(args);
	    } catch (Exception e) {
	        System.out.println(CommandLine.getHelpGeneral());
	        System.exit(1);
	    }

	    Locale.setDefault(Locale.ENGLISH);

		try {
  			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
		}
	}
	
	private void run() throws Exception {
	    Model.getSingleton().init();
	    Model.getSingleton().getOptionsParam().setGUI(cmdLine.isGUI());
		
		if (Model.getSingleton().getOptionsParam().isGUI()) {
		    runGUI();
	    } else {
	        runCommandLine();
	    }
	    
	}
	
	private void runCommandLine() {
	    int rc = 0;
	    String help = "";
	    
	    Control.initSingletonWithoutView();
	    Control control = Control.getSingleton();

	    try {
	        control.getExtensionLoader().hookCommandLineListener(cmdLine);
	        if (cmdLine.isEnabled(CommandLine.HELP) || cmdLine.isEnabled(CommandLine.HELP2)) {
	            help = cmdLine.getHelp();
	            System.out.println(help);
	        } else {
	        
	            control.runCommandLineNewSession(cmdLine.getArgument(CommandLine.NEW_SESSION));
		    
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {}
	        }
		    rc = 0;
	    } catch (Exception e) {
	        log.error(e.getMessage());
	        System.out.println(e.getMessage());
	        rc = 1;
	    } finally {
            control.shutdown(false);
	    }
	    System.exit(rc);
	}
	
	
	
	private void runGUI() throws ClassNotFoundException, Exception {

	    Control.initSingletonWithView();
	    Control control = Control.getSingleton();
	    View view = View.getSingleton();
	    view.getMainFrame().setExtendedState(Frame.MAXIMIZED_BOTH);		
	    view.getMainFrame().setVisible(true);
	    view.setStatus("");

	    control.getMenuFileControl().newSession(false);

	}
  
}