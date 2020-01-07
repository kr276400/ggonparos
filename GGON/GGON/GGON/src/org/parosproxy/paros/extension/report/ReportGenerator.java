package org.parosproxy.paros.extension.report;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.parosproxy.paros.Constant;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReportGenerator {

	private static final SimpleDateFormat staticDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	public static File stringToHtml (String inxml, String infilexsl, String outfilename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	Document doc = null;

 		File stylesheet 		= null;
 		File outfile			= null;
 		StringReader inReader	= new StringReader(inxml);
 		
        try {
            stylesheet = new File(infilexsl);
            outfile	= new File(outfilename);
 
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(inReader));
            
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = tFactory.newTransformer(stylesource);
 
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outfile);
            transformer.transform(source, result);

        } catch (TransformerConfigurationException tce) {
           System.out.println ("\n** 변경 과정에서 오류가 발생하였습니다,");
           System.out.println("   " + tce.getMessage() );

           Throwable x = tce;
           if (tce.getException() != null)
               x = tce.getException();
           x.printStackTrace();
      
        } catch (TransformerException te) {
           System.out.println ("\n** 변경하는 중에 오류가 발생하였습니다.");
           System.out.println("   " + te.getMessage() );

           Throwable x = te;
           if (te.getException() != null)
               x = te.getException();
           x.printStackTrace();
           
         } catch (SAXException sxe) {
           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();

        } catch (IOException ioe) {
           ioe.printStackTrace();
        } finally {

        }
                
		return outfile;

    }

    
    public static File fileToHtml (String infilexml, String infilexsl, String outfilename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	Document doc = null;

 		File stylesheet 	= null;
 		File datafile		= null;
 		File outfile		= null;
 		
        try {
            stylesheet = new File(infilexsl);
            datafile   = new File(infilexml);
            outfile	= new File(outfilename);
 
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(datafile);
 
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = tFactory.newTransformer(stylesource);
 
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outfile);
            transformer.transform(source, result);

        } catch (TransformerConfigurationException tce) {
           System.out.println ("\n** 변경 과정에서 오류가 발생하였습니다.");
           System.out.println("   " + tce.getMessage() );

           Throwable x = tce;
           if (tce.getException() != null)
               x = tce.getException();
           x.printStackTrace();
      
        } catch (TransformerException te) {
           System.out.println ("\n** 변경하는 중에 오류가 발생하였습니다.");
           System.out.println("   " + te.getMessage() );

           Throwable x = te;
           if (te.getException() != null)
               x = te.getException();
           x.printStackTrace();
           
         } catch (SAXException sxe) {

           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();

        } catch (IOException ioe) {
           ioe.printStackTrace();
        } finally {

        }
                
		return outfile;

    }
    
    public static void openBrowser(String uri) {

		try {
			if (Constant.isWindows()) {
				int pos = uri.lastIndexOf("\\");
				String path = uri.substring(0, pos);
				String file = uri.substring(pos+1);
				Runtime.getRuntime().exec("cmd /c start /D\"" + path + "\" " + file);
				return;
			}
			
			if (Constant.isLinux()) {
				Runtime.getRuntime().exec("mozilla " + uri);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String entityEncode(String text) {
		String result = text;
		
		if (result == null) {
			return result;
		}

		result = result.replaceAll("&", "&amp;");
		
		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");
		result = result.replaceAll("\"", "&quot;");
		result = result.replaceAll("'", "&apos;");
		
		return result;
	}

	public static String getCurrentDateTimeString() {
		Date dateTime = new Date(System.currentTimeMillis());
		return getDateTimeString(dateTime);
		
	}

	public static String getDateTimeString(Date dateTime) {
	    return staticDateFormat.format(dateTime);
	}
	
}
