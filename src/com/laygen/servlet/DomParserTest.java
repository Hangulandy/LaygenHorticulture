package com.laygen.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomParserTest {
	
	public static void main(String[] args) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String filePath = "./WebContent/res/xml/string.xml";
		HashMap<String, HashMap<String, String>> dictionary = new HashMap<String, HashMap<String, String>>();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			NodeList nodes = doc.getElementsByTagName("string");
			System.out.println(nodes.getLength());
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element entry = (Element) n;
					String string = entry.getAttribute("id");
					NodeList langStrings = entry.getChildNodes();
					
					HashMap<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < langStrings.getLength(); j++) {
						Node m = langStrings.item(j);
						if (m.getNodeType()==Node.ELEMENT_NODE) {
							Element word = (Element) m;
							map.put(word.getTagName(), word.getTextContent());
						}
						dictionary.put(string, map);
					}
				}
			}
			
			for (String key : dictionary.keySet()) {
				System.out.print(String.format("%s : \n", key));
				HashMap<String, String> map = dictionary.get(key);
				for (String key2 : map.keySet()) {
					System.out.print(String.format("\t%s : %s \n", key2, map.get(key2)));
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
