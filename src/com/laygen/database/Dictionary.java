package com.laygen.database;

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

public class Dictionary {

	private static Dictionary instance = new Dictionary();
	private static HashMap<String, HashMap<String, String>> entries = null;
	private static String lang;

	private Dictionary() {
		refreshDictionary();
	}

	public static Dictionary getInstance() {
		return instance;
	}

	public static HashMap<String, HashMap<String, String>> getDictionary() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		String filePath = "./../eclipse-workspace/HorticultureApp/WebContent/res/xml/string.xml";

		HashMap<String, HashMap<String, String>> dictionary = new HashMap<String, HashMap<String, String>>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			NodeList nodes = doc.getElementsByTagName("string");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element entry = (Element) n;
					String string = entry.getAttribute("id");
					NodeList langStrings = entry.getChildNodes();

					HashMap<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < langStrings.getLength(); j++) {
						Node m = langStrings.item(j);
						if (m.getNodeType() == Node.ELEMENT_NODE) {
							Element word = (Element) m;
							map.put(word.getTagName(), word.getTextContent());
						}
						dictionary.put(string, map);
					}
				}
			}

//			for (String key : dictionary.keySet()) {
//				System.out.print(String.format("%s : \n", key));
//				HashMap<String, String> map = dictionary.get(key);
//				for (String key2 : map.keySet()) {
//					System.out.print(String.format("\t%s : %s \n", key2, map.get(key2)));
//				}
//			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dictionary;
	}

	public static void refreshDictionary() {
		entries = getDictionary();
	}

	public String get(String string, String language) {
		while (entries == null) {
			refreshDictionary();
		}
		lang = language == null ? "ko" : language;
		return entries.get(string).get(lang);
	}

	public String get(String string) {
		return get(string, lang);
	}
	
	public static void setLanguage(String lang) {
		Dictionary.lang = lang;
	}

}
