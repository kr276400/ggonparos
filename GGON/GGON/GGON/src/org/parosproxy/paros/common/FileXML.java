package org.parosproxy.paros.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

abstract public class FileXML {
	// �߻� ���� Ŭ������ ����
	protected Document doc = null;
	protected DocumentBuilder docBuilder = null;
	protected DocumentBuilderFactory docBuilderFactory = null;
	
	public FileXML(String rootElementName) {

		docBuilderFactory = DocumentBuilderFactory.newInstance();
		String rootString = "<" + rootElementName + "></" + rootElementName + ">";
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			//newDocumentBuilder �Լ� ���� ���� ���Ͽ��� ���� �Ľ��� ��� ����.
			
			doc = docBuilder.parse(new InputSource(new StringReader(rootString)));
			/*
			 * InputSource���� ��쵵 ���Ͽ��� ���� �Ľ��� �ϴ� ��쿡 ���̴� �Լ��ε�,
			 * newDocumentBuilder�� �ٸ� ����, newDocumentBuilder���� ���� DocumentBuilderFactory���� newInstance��� �Լ��� ����
			 * ���ο� ��ü�� �����ϰ� �� �ȿ� �Ľ��� �ϴ� ����ε� ����
			 * InputSource���� ���� newFileReader���� ���ϰ�θ� ���� �̿��ؼ� �Ľ��� �ϴ� ����� �� �� �ִ�.
			 * ���� ��ζ�� �ϸ� "C://TEMP//test.xml"���� xml�� ������ �Ľ��ϴ� ��쿡 �ش�ȴٰ� ���� ��.
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Document getDocument() {
		//DocumentŬ���� �ȿ� getDocument��� �Լ��� ���� ���� ������ �������� doc��� ��ü �ȿ� ������ �����Ѵ�.
		return doc;
	}
	
	/*
	 * �±׸� ��Ī�ϴ� �⺻ ��ҿ��� �̱� ��Ҹ� �������� �κ�, ����� �ؿ� �κ��� ����..
	 */
	protected Element getElement(Element base, String childTag) {
	    Element[] elements = getElements(base, childTag);
	    if (elements == null) {
	        return null;
	    } else {
	        return elements[0];
		}
	}
	
	protected Element getElement(String tag) {
		Element parent = doc.getDocumentElement();
		return getElement(parent, tag);
	}
	
	protected Element getElement(String[] path) {
		
	    Element[] elements = getElements(path);
	    if (elements==null) {
	        return null;
	    } else {
	        return elements[0];
	    }
	    
	}
	/*
	 * �±� ���� ��Ī�ϴ� �⺻ ��ҿ��� ��� ��ҵ� �������� �κ���.
	 */
	protected Element[] getElements(Element base, String childTag) {
		NodeList nodeList = base.getElementsByTagName(childTag);
		if (nodeList.getLength() == 0) {
			return null;
		}
		Element[] elements = new Element[nodeList.getLength()];
		for (int i=0; i<nodeList.getLength(); i++) {
		    elements[i] = (Element) nodeList.item(i);
		}
		return elements;
	}
	
	protected Element[] getElements(String tagName) {
		Element parent = doc.getDocumentElement();
		return getElements(parent, tagName);
	}
	
	protected Element[] getElements(String[] path) {
		NodeList nodeList = null;
		// ���� ������ ��� ������ ���ϴ� �ſ�, ���� ��� ������ ���� Nodelist node = ... �̷������� ����
		// ��尡 �ܼ� ������ ���� Node node = ... �̷������� xml ���� �Ľ��Ҷ� ���̴� ��.
		// node ���� ���� �����Ͱ� ����Ǵ� ����? ���� �Ŷ� �����ϸ� �ǿ�
		Element element = doc.getDocumentElement();
		for (int i=0; i<path.length-1; i++) {
			nodeList = element.getElementsByTagName(path[i]);
			if (nodeList.getLength() > 0) {
				element = (Element) nodeList.item(i);
			} else {
				return null;
			}
		}
		nodeList = element.getElementsByTagName(path[path.length-1]);
		if (nodeList.getLength() == 0) {
			return null;
		}
		Element[] elements = new Element[nodeList.getLength()];
		for (int i=0; i<nodeList.getLength(); i++) {
		    elements[i] = (Element) nodeList.item(0);
		}
		return elements;	
	    
	}

	/*
	 * ���ݱ����� ��� �������� �ؽ�Ʈ ��忡 �ؽ�Ʈ ��, �Է°����� �ؽ�Ʈ �ȿ� ������ �κ��̶�� ���� ��
	 * ����ڰ� �Է��� �ؽ�Ʈ ������ Element element�ȿ� �ؽ�Ʈ ������ �������ٰ� ���� �ǰ�,
	 * element �Ʒ��� �ִ� �ؽ�Ʈ ���ȿ� �ִ� ������ �����Ѵٰ� ���� �ȴ�.
	 */
	private String getText(Element element) {
		try {
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					return node.getNodeValue();
					// ��� �ȿ��� ��� ������ �����°�.
				}
			}
		} catch (Exception e) {
		}
		return "";

	}
	/*
	 * �⺻ element �Ʒ����� �±׵��� ������ ������ �κ��̿�
	 */
	protected String getValue(Element base, String tag) {

	    Element element = null;
		String result = "";
		try {
			element = (Element) getElement(base, tag);
			result = getText(element);
		} catch (Exception e) {
		}
		return result;
	    
	}
	
	protected String getValue(String tag) {
		Element element = doc.getDocumentElement();
		return getValue(element, tag);		
	}

	protected List getValues(String tag) {
		NodeList nodeList = (NodeList) doc.getElementsByTagName(tag);
		ArrayList resultList = new ArrayList();
		Element element = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);
			resultList.add(getText(element));
		}
		return resultList;
	}
	
	abstract protected void parse() throws Exception;

	public void readAndParseFile(String fileName) throws SAXException, IOException, Exception {
		readFile(fileName);
		parse();
	}
	
	protected void readFile(String fileName) throws SAXException, IOException {
		// SAXException ���� ���� ������� ���� �����ΰ� �ȿ� �ҽ� �ڵ峪 ���������� �ö� ����� ������ ���� ���� ó��
		// xml Document �������
		DocumentBuilderFactory	factory 	= null;
		DocumentBuilder 		builder	= null;
		
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			builder	= factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		
		doc = builder.parse(fileName);
	}
	
	public void saveFile(String fileName) {
		File file = null;
		FileOutputStream outFile = null;
		
        try {
            file = new File(fileName); 
            outFile = new FileOutputStream(file);
            // ����� ���ؼ� transformer��� �߻� Ŭ������ ����� ��ü�� ����ϴ� ���̴�.
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outFile);
            
            transformer.transform(source, result);
           
        } catch (TransformerConfigurationException tce) {
        	// parser�� ���ؼ� ������ ���� 
           System.out.println ("\n** Transformer Factory error");
           System.out.println("   " + tce.getMessage() );

           // ���ܸ� �����ؼ� �̿��ϴµ�, 
           Throwable x = tce;
           if (tce.getException() != null)
               x = tce.getException();
           x.printStackTrace();
      
        } catch (TransformerException te) {
        	// parser�� ���� ������� ����
           System.out.println ("\n** Transformation error");
           System.out.println("   " + te.getMessage() );

           // ���ܸ� �����ؼ� �̿���, ���࿡..
           Throwable x = te;
           if (te.getException() != null)
               x = te.getException();
           x.printStackTrace();
           

        } catch (IOException ioe) {
           // ����� ���� - IO�� Input / Output �̶�� ���� ������ �ִ�.
           ioe.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
	
	public void setDocument(Document doc) throws Exception {
		this.doc = doc;
		parse();
	}
	
	protected void setValue(String tagName, String value) {
		Element element = null;
		try {
			// ���� ù ��° �±׸� �غ��Ѵ�
			element = getElement(tagName);
			if (element == null) {
				// ���࿡ ã�� ���ߴٸ�, ��Ʈ element�� ���Ѵ�
				element = doc.createElement(tagName);
				doc.getDocumentElement().appendChild(element);
			}
			for (int i=0; i<element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					node.setNodeValue(value);
					return;
				}
			}
			Node newNode = doc.createTextNode(value);
			element.appendChild(newNode);
		} catch (Exception e) {
		}
		
	}
	
	protected void setValue(String[] path, String value) {
		Element element = doc.getDocumentElement();
		NodeList nodeList = null;
		Element newElement = null;
		Node newNode = null;
		
		for (int i=0; i<path.length; i++) {
			nodeList = (NodeList) doc.getElementsByTagName(path[i]);
			if (nodeList.getLength() == 0) {
				// ���� ��ã������ ���ο� element�� �����
				newElement = doc.createElement(path[i]);
				element.appendChild(newElement);
				element = newElement;
			} else {
				// ���ο� element�� ����Ű�鼭 �����
				element = (Element) nodeList.item(0);
			}
		}

		// element�� ��ġ�� �κ�
		try {
			// �ؽ�Ʈ ��带 ã�� ���� ������.
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					node.setNodeValue(value);
					return;
				}
			}
			// ���� ��ã����, �ؽ�Ʈ ��带 �ϳ� ������
			newNode = doc.createTextNode(value);
			element.appendChild(newNode);
			// ���� �θ� �̹� Ʈ���� ���� ������ �Ǿ��ְų� �ϸ�, �ٷ� child�� ����� �Լ��ΰſ�..
		} catch (Exception e) {
		}
		
	}
	
	protected void removeElement(Element base, String tag) {
	    Element[] elements = getElements(base, tag);
	    if (elements== null) return;
	    for (int i=0; i<elements.length; i++) {
	        try {
	            base.removeChild(elements[i]);
	        } catch (Exception e) {}
	    }
	}

	protected void removeElement(String tag) {
		Element base = doc.getDocumentElement();
		removeElement(base, tag);
	}

	/*
	 * �Էµ� ���̽�, �ױ�, ������ element�� ���ؼ� �����ϴ� �κ��̿�
	 */
	protected Element addElement(Element base, String tag, String value) {
	    Element element = doc.createElement(tag);
	    base.appendChild(element);
	    for (int i=0; i<element.getChildNodes().getLength(); i++) {
	        Node node = element.getChildNodes().item(i);
	        if (node.getNodeType() == Node.TEXT_NODE) {
	            node.setNodeValue(value);
	            return element;
	        }
	    }
	    Node newNode = doc.createTextNode(value);
	    element.appendChild(newNode);
	    return element;
	}
	
	protected Element addElement(Element base, String tag) {
	    Element element = doc.createElement(tag);
		base.appendChild(element);
	    return element;
	}
	
	protected Element addElement(String tag) {
	    Element element = doc.createElement(tag);
		doc.getDocumentElement().appendChild(element);
	    return element;
	}
}
