package utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileUtils {
	public static InputStream openFile(String fname) {
	    InputStream is;
	    
	    is = FileUtils.class.getResourceAsStream(fname);

	    return is;
	}
	
	public static Document openXML(String fname) {
		InputStream in;
		Document doc;
		DocumentBuilder builder;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Error Configuring Parser: " + e.getMessage());
			return null;
		}
		
		try {
			in = FileUtils.class.getResourceAsStream(fname);
			doc = builder.parse(in);
		} catch (IOException e) {
			System.err.println("Error Reading File: " + e.getMessage());
			System.err.println(e.getStackTrace());
			return null;
		} catch (SAXException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
			return null;
		}
		
		return doc;
	}
}
