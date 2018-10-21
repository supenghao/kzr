package com.sunnada.kernel.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml操作
 * @author bianzk
 *
 */
public class XmlUtil {
        static Logger logger = Logger.getLogger(XmlUtil.class);
    public static Element getFirstElementByTagName(Element parentElement, String tagName){
        NodeList list = parentElement.getElementsByTagName(tagName);
        if(list.getLength() == 0)
            return null;
        else
            return (Element)list.item(0);
    }
    public static List getChildElementByTagName(Element parentElement, String tagName){
        NodeList list = parentElement.getChildNodes();
        ArrayList l = new ArrayList();
        for(int i = 0; i < list.getLength(); i++){
            Node n = list.item(i);
            if((n instanceof Element) && n.getNodeName().equals(tagName))
                l.add(n);
        }
        return l;
    }
    public static String getAttribute(Element element, String attr){
        String value = element.getAttribute(attr).trim();
        return value.equals("") ? null : value;
    }
    public static String getAttribute(Element element, String attr, String defValue){
        String value = element.getAttribute(attr).trim();
        return value.equals("") ? defValue : value;
    }
    public static String domToXml(Document document){
        try{
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty("encoding", "GBK");//MagicContext.getEncoding()
            DOMSource source = new DOMSource(document);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            return sw.getBuffer().toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new com.sunnada.kernel.exception.BaseException(e);
        }
    }
    public static void getPath() throws Exception{
                FileReader fr = null;
                  BufferedReader br = null;
                  try{
                          logger.debug("dlsdfasfalsfjaslfjasjflaslfasf");
//		File propertyDir = new File("webapp/WEB-INF/workspaces/conf/artDb.xml");
//		fr = new FileReader(propertyDir);
//		br = new BufferedReader(fr);
//		String line = null;
//		while ((line=br.readLine())!=null){
//			System.out.println("line===="+line);
//		}
                  }catch(Exception e){
                          e.printStackTrace();
                  }finally{
//	  		br.close();
//	  		fr.close();
                  }
        }

    public static Document parse(InputStream filename){
       Document configDoc = null;
       try{
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            configDoc = docBuilder.parse(filename);
            return configDoc;
       }catch(Exception e){
               throw new com.sunnada.kernel.exception.BaseException(e);
       }
    }
    public static Element getRootElement(InputStream filename){
            Element root = null;
            try{
                    root = parse(filename).getDocumentElement();
                    return root;
            }catch(Exception e){
                    throw new com.sunnada.kernel.exception.BaseException(e);
            }
    }

}

