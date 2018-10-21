package com.sunnada.kernel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sunnada.kernel.util.XmlUtil;

public class DreamConf {
	static Logger logger = Logger.getLogger(DreamConf.class);
	private final static String FILED_MAPS_DIR = "conf";
	private final static String DEPLOY_FILE = "appInfo.xml";
	private final static String CONF_FILE = "appConf.xml";
	
	public final static String DEFAULT_PASSWORD = "default_password";
	
	protected static Properties properties = new Properties();
	protected static Map<String,BackendDef> backendDefs = new HashMap<String,BackendDef>();
	protected static Map<String,ServerDef> serverDefs = new HashMap<String,ServerDef>();
	protected static Map<String, com.sunnada.kernel.ServiceDef> serviceDefs = new HashMap<String, com.sunnada.kernel.ServiceDef>();
	
	protected static List<ServerSupport> serverList = new ArrayList<ServerSupport>();
	
	protected static com.sunnada.kernel.DeployDef currentDeploy;
	
	
	static{
		properties.put(DEFAULT_PASSWORD, "000000");
		loadArtConf();
		loadDeployConf();
		
	}
	
	private static InputStream getMapsStream(String dir){
		InputStream  is = null;
		try{
		   Resource resource = new ClassPathResource(dir);
		   is = resource.getInputStream();
		}catch(Exception e){
			e.printStackTrace();
		}
		return is;
		
	}
	 /**
	   * 载入项目配置信息
	   */
	  public static void loadArtConf() {
	    try {
	      InputStream is = getMapsStream(FILED_MAPS_DIR+File.separator+CONF_FILE);
	      if (is == null) {
	        return;
	      }
	      logger.debug("载入" + CONF_FILE);
	      Document configDoc = XmlUtil.parse(is);
	      Element root = configDoc.getDocumentElement();
	      parseProperties(root);
	      parseBackends(root);
	      parseServers(root);
	      parseServices(root);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      throw new com.sunnada.kernel.exception.BaseException("fail to to load /conf/appConf.xml", e);
	    }
	  }
	  
	  public static void parseBackends(Element root) {
		    try {
		      NodeList backendNodes = root.getElementsByTagName("backend");
		      for (int i = 0; i < backendNodes.getLength(); i++) {
		        Element backendElement = (Element) backendNodes.item(i);
		        String s = XmlUtil.getAttribute(backendElement, "name");
		        if (s == null)
		          throw new Exception("<backend>的name属性不能为空");
		        BackendDef backendDef = (BackendDef) backendDefs.get(s);
		        if (backendDef == null) {
		          backendDef = new BackendDef();
		          backendDef.setName(s);
		          backendDefs.put(backendDef.getName(), backendDef);
		        }
		        s = XmlUtil.getAttribute(backendElement, "text");
		        if (s != null) {
		          backendDef.setText(s);
		        }
		        s = XmlUtil.getAttribute(backendElement, "class");
		        if (s == null)
			          throw new Exception("<backend>的class属性不能为空");
		        backendDef.setClassName(s);
//		        if (s != null) {
//		          if (backendDef.getClassName() != null)
//		            throw new Exception("重复定义backend的class");
//		          backendDef.setClassName(s);
//		        }
		        String sPort = XmlUtil.getAttribute(backendElement, "port");
		        if (sPort != null)
		          try {
		            backendDef.setPort(Integer.parseInt(sPort));
		          }
		          catch (Exception e) {
		            throw new Exception( (new StringBuffer(String.valueOf(backendDef.
		                getName()))).append("的端口设置错误:").append(sPort).toString());
		          }
		        NodeList propertyNodes = backendElement.getElementsByTagName("property");
		        if (propertyNodes.getLength() > 0) {
		          Properties properties = new Properties();
		          for (int j = 0; j < propertyNodes.getLength(); j++) {
		            Element propertyNode = (Element) propertyNodes.item(j);
		            String name = XmlUtil.getAttribute(propertyNode, "name");
		            String value = XmlUtil.getAttribute(propertyNode, "value");
		            properties.put(name, value);
		          }
		          backendDef.setProperties(properties);
		        }
//		        s = backendNode.attributeValue("require");
//				if (s != null) {
//					StringTokenizer st = new StringTokenizer(s, ", ");
//					while (st.hasMoreTokens())
//						backendDef.getRequires().add(st.nextToken());
//				}
				
		      }
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
	  public static void parseServices(Element root) {
		    try {
		      NodeList serviceNodes = root.getElementsByTagName("service");
		      for (int i = 0; i < serviceNodes.getLength(); i++) {
		        Element serviceNode = (Element) serviceNodes.item(i);
		        String sTemp = XmlUtil.getAttribute(serviceNode, "name");
		        if (sTemp == null)
		           throw new Exception("appConf中<service>的name属性不能为空");
		        com.sunnada.kernel.ServiceDef sd = (com.sunnada.kernel.ServiceDef) serviceDefs.get(sTemp);
		        if (sd == null) {
		          sd = new com.sunnada.kernel.ServiceDef();
		          sd.setName(sTemp);
		          serviceDefs.put(sd.getName(), sd);
		        }
		        sTemp = XmlUtil.getAttribute(serviceNode, "impl");
		        if (sTemp != null) {
		          if (sd.getSImpl() != null)
		            throw new Exception( (new StringBuffer("重复定义service ")).append(
		                sd.getName()).append("的impl").toString());
		          sd.setSImpl(sTemp);
		        }
		        
		        sTemp = XmlUtil.getAttribute(serviceNode, "class");
		        if (sTemp == null)
			           throw new Exception("appConf中<service>的class属性不能为空");
		        sd.setClassName(sTemp);
		        
//		        if (sTemp != null) {
//		          if (sd.getClassName() != null)
//		            throw new Exception( (new StringBuffer("重复定义class ")).append(sd.getName()).append("的impl").toString());
//		          sd.setClassName(sTemp);
//		        }
		        
		        sTemp = XmlUtil.getAttribute(serviceNode, "interceptor");
		        if (sTemp != null) {
		          if (sd.getSInterceptor() != null)
		            throw new Exception( (new StringBuffer("重复定义service ")).append(
		                sd.getName()).append("的interceptor").toString());
		          sd.setSInterceptor(sTemp);
		        }
		        sTemp = XmlUtil.getAttribute(serviceNode, "protocols");
		        if (sTemp != null) {
		          for (StringTokenizer st = new StringTokenizer(sTemp, ",");
		               st.hasMoreTokens(); sd.getProtocols().add(st.nextToken()));
		        }
		        sTemp = XmlUtil.getAttribute(serviceNode, "servers");
		        if (sTemp != null) {
		          ServerDef md;
		          for (StringTokenizer st = new StringTokenizer(sTemp, ",");
		               st.hasMoreTokens(); sd.getServers().add(md)) {
		            String serverName = st.nextToken();
		            md = (ServerDef) getServerDefs().get(serverName);
		            if (md == null)
		              throw new Exception( (new StringBuffer("service ")).append(sd.
		                  getName()).append("定义出错,没有定义server:").append(serverName).
		                                     toString());
		          }
		        }
		      }
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
	  public static void parseServers(Element root) {
		    try {
		      NodeList serverNodes = root.getElementsByTagName("server");
		      for (int i = 0; i < serverNodes.getLength(); i++) {
		        Element serverNode = (Element) serverNodes.item(i);
		        ServerDef server = new ServerDef();
		        String name = XmlUtil.getAttribute(serverNode, "name");
		        String text = XmlUtil.getAttribute(serverNode, "text");
		        String className = XmlUtil.getAttribute(serverNode, "class");
		        //server.ip = XmlUtil.getAttribute(serverNode, "ip");
//		        try {
//		          server.rmiPort = Integer.parseInt(XmlUtil.getAttribute(serverNode,
//		              "rmiPort"));
//		        }catch (NumberFormatException nfe) {
//		          throw new ArtException("解析server的rmiPort属性出错", nfe);
//		        }
		        if (name == null )//|| server.ip == null
		          throw new Exception("<server>的name属性不能为空");
		        if (className == null )//|| server.ip == null
			          throw new Exception("<server>的class属性不能为空");
		        if (serverDefs.containsKey(name))
		          throw new Exception( (new StringBuffer("重复定义server:")).append(name).toString());
		        
		        
		        server.setName(name);
		        server.setText(text);
		        server.setClassName(className);
		        
		        boolean current = "true".equals(XmlUtil.getAttribute(serverNode,"current"));
//		        server.joinCluster = !"false".equals(XmlUtil.getAttribute(serverNode,"joinCluster"));
		        if (current){		        	
		        	serverList.add((ServerSupport)(server.getServer().newInstance()));
		        }
		        
		        
		        
		        NodeList propertyNodes = serverNode.getElementsByTagName("property");
		        if (propertyNodes.getLength() > 0) {
		          Properties properties = new Properties();
		          for (int j = 0; j < propertyNodes.getLength(); j++) {
		            Element propertyNode = (Element) propertyNodes.item(j);
		            String pname = XmlUtil.getAttribute(propertyNode, "name");
		            String pvalue = XmlUtil.getAttribute(propertyNode, "value");
		            properties.put(pname, pvalue);
		          }
		          server.setProperties(properties);
		        }
		        
		        serverDefs.put(name, server);		        
		      }
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
	  private static void parseProperties(Element root) {
		    NodeList propNodes = root.getElementsByTagName("property");
		    for (int i = 0; i < propNodes.getLength(); i++) {
		      Element propNode = (Element) propNodes.item(i);
		      String name = XmlUtil.getAttribute(propNode, "name");
		      String value = XmlUtil.getAttribute(propNode, "value");
		      if (name == null || value == null)
		        throw new com.sunnada.kernel.exception.BaseException("<property>的name和value属性不能为空");
		      if (DEFAULT_PASSWORD.equals(name)){
		    	  throw new com.sunnada.kernel.exception.BaseException("<property>的name不能为系统默认的["+DEFAULT_PASSWORD+"]");
		      }
		      properties.put(name, value);
		    }

	  }
	  /**
	   * 载入项目发布信息
	   */
	  public static void loadDeployConf() {
	    try {
	      logger.debug("载入项目信息");
	      InputStream is = getMapsStream(FILED_MAPS_DIR+File.separator+DEPLOY_FILE);
	      if (is == null) {
	        return;
	      }	      
	      Document configDoc = XmlUtil.parse(is);
	      Element root = configDoc.getDocumentElement();
	      parseDeploy(root);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  public static void parseDeploy(Element root) {
		    try{
		      NodeList deployNodes = root.getElementsByTagName("deploy");
		      if(deployNodes.getLength() != 1){
		            throw new Exception("appInfo.xml中定义的部署多于一个");
		      }else{
		        Element deployNode = (Element)deployNodes.item(0);
		        currentDeploy = new com.sunnada.kernel.DeployDef();
		        currentDeploy.vendor = XmlUtil.getAttribute(deployNode, "vendor");
		        currentDeploy.application = XmlUtil.getAttribute(deployNode, "application");
		        currentDeploy.version = XmlUtil.getAttribute(deployNode, "version");
		        currentDeploy.copyright = XmlUtil.getAttribute(deployNode, "copyright");
		        System.out.println("*******************************");
		        System.out.println((new StringBuffer("   ")).append(currentDeploy.application).append(" ").append(currentDeploy.version).toString());
		        System.out.println("   ");
		        System.out.println((new StringBuffer("   ")).append(currentDeploy.vendor).toString());
		        System.out.println((new StringBuffer("   ")).append(currentDeploy.copyright).toString());
		        System.out.println("*******************************");
		        return;
		      }

		    }catch(Exception e){
		    	e.printStackTrace();
		        throw new com.sunnada.kernel.exception.BaseException("fail to to load conf/appInfo.xml", e);
		    }

	}
	public static Map<String,BackendDef> getBackendDefs() {
	    return backendDefs;
	}

	public static Properties getProperties() {
	    return properties;
	}

    public static String getPropertie(String name) {
	    return properties.getProperty(name);
	}

    public static Map<String,ServerDef> getServerDefs() {
	    return serverDefs;
	}


	public static Map<String, com.sunnada.kernel.ServiceDef> getServiceDefs() {
	    return serviceDefs;
	}
	public static com.sunnada.kernel.DeployDef getCurrentDeploy(){
	    return currentDeploy;
	}
}
