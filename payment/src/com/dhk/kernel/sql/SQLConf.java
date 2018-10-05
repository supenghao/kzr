package com.dhk.kernel.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dhk.kernel.AppContext;
import com.dhk.kernel.DefaultAppContext;
import com.dhk.kernel.exception.BaseException;

public class SQLConf {
	public static AppContext appContext = new DefaultAppContext();
	static Logger logger = Logger.getLogger(SQLConf.class);
	
	private final static String SQL_FILED_DIR = "sql";
	private final static String SQL_FILE_SUFFIX = ".sql.xml";
	
	private static File root;
	
	private static Map<String, Long> fileUpdates = new HashMap<String, Long>();
	
	protected static Map<String,Map<String,SQLDef>> sqlDefs = new HashMap<String,Map<String,SQLDef>>();

	public static Map<String, Map<String, SQLDef>> getSqlDefs() {
		return sqlDefs;
	}
	public static Map<String,SQLDef> getSqlDefMap(String sqlFileName){
		return sqlDefs.get(sqlFileName);
	}
	public static SQLDef getSqlDef(String sqlFileName,String sqlName){
		return sqlDefs.get(sqlFileName).get(sqlName);
	}
	public static String getSql(String sqlFileName,String sqlName){
		return getSqlDef(sqlFileName,sqlName).getSql();
	}
	
	public static void reLoadSql(String fileName) throws Exception{
		Resource resource = new ClassPathResource(SQL_FILED_DIR+File.separator+fileName+SQL_FILE_SUFFIX);
		File file = resource.getFile();
		if (file.isFile()){
			Map<String,SQLDef> defs = parseXml(file);
			sqlDefs.remove(fileName);
		    sqlDefs.put(fileName, defs);
		    //logger.debug("reloadSql ok");
		}
		
		
	}

	/**
	 * 把SQL_FILED_DIR目录下的所有sql文件都装载进来
	 */
	public static void loadSql() throws Exception{
		//root = new File(appContext.getRealPath(SQL_FILED_DIR));
		Resource resource = new ClassPathResource(SQL_FILED_DIR);
		root = resource.getFile();
		if (!root.exists()) {			
		      return;
		}
		for (File f : root.listFiles()) {
			if (f.isDirectory())
		        continue;
			String name = f.getName();
			if (!name.endsWith(SQL_FILE_SUFFIX))
		        continue;
			Long update = fileUpdates.get(name);
		    if ((update != null) && (f.lastModified() == update.longValue()))
		        continue;
		     logger.debug("load sql file " + f.getName());
		     String fileName = f.getName();
		     int index = f.getName().indexOf(SQL_FILE_SUFFIX);
		     fileName = fileName.substring(0,index);
		     //System.out.println("fileName=========="+fileName);
		     fileUpdates.put(fileName, Long.valueOf(f.lastModified()));

		     Map<String,SQLDef> defs = parseXml(f);
		     sqlDefs.put(fileName, defs);
		}
	}
	
	private static Map<String,SQLDef> parseXml(File f) throws Exception{
		Map<String,SQLDef> defs = new HashMap<String,SQLDef>();
	      InputStream is = new FileInputStream(f);
	      if (is == null)
				throw new BaseException("未找到XML文件对应的输入流...");
	      
	      Document configDoc = null;
		  DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	      configDoc = docBuilder.parse(is);
	      Element root = configDoc.getDocumentElement();
	      
	      NodeList sqlsNodeList = root.getElementsByTagName("sql");
	      for (int i=0;i<sqlsNodeList.getLength();i++){
	    	  SQLDef def = new SQLDef();
	    	  Element childElemnet = (Element)sqlsNodeList.item(i);
	    	  String name = childElemnet.getAttribute("name");
	    	  String title = childElemnet.getAttribute("title");
	    	  String sqlValue = "";
	    	  //String sqlValue = childElemnet.getFirstChild().getNodeValue().trim();
	    	  NodeList nodes=childElemnet.getChildNodes();
	    	  for (int j=0;j<nodes.getLength();j++){
	    		  Node node1 = nodes.item(j);
	    		  if(node1.getNodeType()==Node.CDATA_SECTION_NODE){
	    			  CDATASection cdataNode = (CDATASection)node1;
	    			  sqlValue = cdataNode.getWholeText().trim();
	    			  break;
	    		  }
	    	  }
//	    	  System.out.println("sqlVal:ueaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:"+sqlValue);
//	    	  System.out.println("name:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:"+name);
//	    	  System.out.println("title:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:"+title);
	    	  def.setName(name);
	    	  def.setTitle(title);
	    	  def.setSql(sqlValue);
	    	  defs.put(name, def);	    	  
	      }
	      
	      return defs;	   
	}
	
	
}
