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
	// 추상 개념 클래스로 정의
	protected Document doc = null;
	protected DocumentBuilder docBuilder = null;
	protected DocumentBuilderFactory docBuilderFactory = null;
	
	public FileXML(String rootElementName) {

		docBuilderFactory = DocumentBuilderFactory.newInstance();
		String rootString = "<" + rootElementName + "></" + rootElementName + ">";
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			//newDocumentBuilder 함수 같은 경우는 파일에서 직접 파싱할 경우 쓰임.
			
			doc = docBuilder.parse(new InputSource(new StringReader(rootString)));
			/*
			 * InputSource같은 경우도 파일에서 직접 파싱을 하는 경우에 쓰이는 함수인데,
			 * newDocumentBuilder와 다른 점은, newDocumentBuilder같은 경우는 DocumentBuilderFactory에서 newInstance라는 함수를 통해
			 * 새로운 객체를 형성하고 그 안에 파싱을 하는 경우인데 반해
			 * InputSource같은 경우는 newFileReader같은 파일경로를 직접 이용해서 파싱을 하는 경우라고 볼 수 있다.
			 * 파일 경로라고 하면 "C://TEMP//test.xml"같은 xml의 파일을 파싱하는 경우에 해당된다고 보면 됨.
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Document getDocument() {
		//Document클래스 안에 getDocument라는 함수를 통해 얻어온 파일을 가져오면 doc라는 객체 안에 저장후 리턴한다.
		return doc;
	}
	
	/*
	 * 태그를 매칭하는 기본 요소에서 싱글 요소를 가져오는 부분, 참고로 밑에 부분임 ㅋㅋ..
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
	 * 태그 네임 매칭하는 기본 요소에서 모든 요소들 가져오는 부분임.
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
		// 복수 형태의 노드 값들을 말하는 거여, 복수 노드 선택할 때는 Nodelist node = ... 이런식으로 진행
		// 노드가 단수 형태일 경우는 Node node = ... 이런식으로 xml 파일 파싱할때 쓰이는 겨.
		// node 같은 경우는 데이터가 저장되는 공간? 같은 거라 생각하면 되여
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
	 * 지금까지의 요소 과정에서 텍스트 노드에 텍스트 즉, 입력값들을 텍스트 안에 얻어오는 부분이라고 보면 됨
	 * 사용자가 입력한 텍스트 값들은 Element element안에 텍스트 값으로 얻어와진다고 보면 되고,
	 * element 아래에 있는 텍스트 노드안에 있는 값들을 리턴한다고 보면 된다.
	 */
	private String getText(Element element) {
		try {
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					return node.getNodeValue();
					// 노드 안에서 노드 값들을 얻어오는겨.
				}
			}
		} catch (Exception e) {
		}
		return "";

	}
	/*
	 * 기본 element 아래에서 태그들의 값들을 얻어오는 부분이여
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
		// SAXException 같은 경우는 보장되지 않은 도메인값 안에 소스 코드나 문서파일이 올때 생기는 오류에 대한 예외 처리
		// xml Document 진행과정
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
            // 출력을 위해서 transformer라는 추상 클래스를 상속한 객체를 사용하는 것이다.
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outFile);
            
            transformer.transform(source, result);
           
        } catch (TransformerConfigurationException tce) {
        	// parser에 의해서 생성된 에러 
           System.out.println ("\n** Transformer Factory error");
           System.out.println("   " + tce.getMessage() );

           // 예외를 포함해서 이용하는데, 
           Throwable x = tce;
           if (tce.getException() != null)
               x = tce.getException();
           x.printStackTrace();
      
        } catch (TransformerException te) {
        	// parser에 의해 만들어진 오류
           System.out.println ("\n** Transformation error");
           System.out.println("   " + te.getMessage() );

           // 예외를 포함해서 이용함, 만약에..
           Throwable x = te;
           if (te.getException() != null)
               x = te.getException();
           x.printStackTrace();
           

        } catch (IOException ioe) {
           // 입출력 에러 - IO는 Input / Output 이라는 뜻을 가지고 있다.
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
			// 오직 첫 번째 태그만 준비한다
			element = getElement(tagName);
			if (element == null) {
				// 만약에 찾지 못했다면, 루트 element를 더한다
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
				// 만약 못찾았으면 새로운 element를 만든다
				newElement = doc.createElement(path[i]);
				element.appendChild(newElement);
				element = newElement;
			} else {
				// 새로운 element를 가리키면서 실행됨
				element = (Element) nodeList.item(0);
			}
		}

		// element가 위치한 부분
		try {
			// 텍스트 노드를 찾고 값을 세팅함.
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					node.setNodeValue(value);
					return;
				}
			}
			// 만약 못찾으면, 텍스트 노드를 하나 생성함
			newNode = doc.createTextNode(value);
			element.appendChild(newNode);
			// 만약 부모가 이미 트리나 전에 생성이 되어있거나 하면, 바로 child를 만드는 함수인거여..
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
	 * 입력된 베이스, 테그, 값들을 element에 더해서 리턴하는 부분이여
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
